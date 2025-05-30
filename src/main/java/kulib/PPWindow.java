package kulib;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.function.Supplier;

public class PPWindow extends Application {
    // 设置默认值
    private static int refreshIntervalMillis = 250; // 默认250毫秒刷新间隔
    private static String deviceUrl = "http://192.168.8.125"; // 1602a的IP默认值
    private static String jsonIp = "http://127.0.0.1:24050"; // 获取Json的IP默认值

    private static Supplier<Info> dataSupplier;  // 外部提供 map 的方法

    // 1602a的IP Getter
    public static String getDeviceUrl() {
        return deviceUrl;
    }

    // Json的IP Getter
    public static String getJsonIp() {
        return jsonIp;
    }

    public static void setDataSupplier(Supplier<Info> supplier) {
        dataSupplier = supplier;
    }

    // 默认元素
    private final Label currentPPLabel = new Label("");
    private final Label fullPPLabel = new Label("");
    private final Label hitsLabel = new Label("");
    private final Label missLabel = new Label("");

    private final Label statusLabel = new Label("状态：等待中...");

    //选图区元素
    private final Label pp95Label = new Label();
    private final Label pp98Label = new Label();
    private final Label pp100Label = new Label();

    // 错误提示
    private final Label errorHint = new Label("请检查TOSU和OSU是否正常启动");

    private VBox selectContent;  // 选图内容区
    private VBox playContent;    // 打图内容区
    private VBox errorContent;   // 错误内容区

    @Override
    public void start(Stage stage) {
        // 选图内容
        selectContent = new VBox(
            10,
            new Label("ACC PP"),
            pp95Label,
            pp98Label,
            pp100Label
        );
        selectContent.setStyle("-fx-alignment: center;");

        // 打图内容
        playContent = new VBox(
            10, 
            currentPPLabel, 
            fullPPLabel, 
            hitsLabel, 
            missLabel
        );
        playContent.setStyle("-fx-alignment: center;");

        // 错误提示
        errorContent = new VBox(
            10, 
            statusLabel, 
            errorHint
        );
        //errorContent.setStyle("-fx-alignment: center;");
        errorContent.setStyle("-fx-alignment: center; -fx-border-color: red; -fx-border-width: 2;");
        errorHint.setStyle("-fx-text-fill: red;");

        // StackPane 覆盖内容区域
        StackPane contentPane = new StackPane(selectContent, playContent, errorContent);

        // 初始化只显示 mainContent
        playContent.setVisible(true);
        errorContent.setVisible(false);

        Button settingsButton = new Button("设置");
        settingsButton.setOnAction(e -> openSettingsWindow());
        // 按钮用布局Vbox
        VBox buttonBox = new VBox(settingsButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        // root视图
        BorderPane root = new BorderPane();
        root.setStyle("-fx-padding: 20; -fx-font-size: 16; -fx-alignment: center;");
        // 内容居中显示
        root.setCenter(contentPane);
        settingsButton.setStyle("-fx-alignment: center;");
        root.setBottom(buttonBox);

        Scene scene = new Scene(root, 330, 220);
        stage.setTitle("PP 显示窗口");
        stage.setScene(scene);
        stage.show();

        // 设置关闭事件
        stage.setOnCloseRequest(event -> {
            Dis_1602a.display_costom("Disconnected");
            Platform.exit();
            System.exit(0);
        });

        startUpdater();
    }

    private void openSettingsWindow() {
        Stage settingsStage = new Stage();
        settingsStage.setTitle("设置");

        TextField urlField = new TextField(deviceUrl);
        TextField jsonIpField = new TextField(jsonIp); // 新增的 JSON IP 输入框
        TextField intervalField = new TextField(String.valueOf(refreshIntervalMillis));

        Button saveButton = new Button("保存");
        saveButton.setOnAction(e -> {
            // 从输入框读取文本，赋值给deviceUrl
            deviceUrl = urlField.getText().trim();
            System.out.println("新地址已保存：" + deviceUrl);

            // 保存刷新间隔，转换数字，防止非法输入
            try {
                int interval = Integer.parseInt(intervalField.getText().trim());
                if (interval <= 0) throw new NumberFormatException();
                refreshIntervalMillis = interval;
            } catch (NumberFormatException ex) {
                // 弹窗提示错误
                Alert alert = new Alert(Alert.AlertType.ERROR, "刷新间隔必须是正整数（毫秒）！");
                alert.showAndWait();
                return; // 不关闭窗口，不保存
            }

            // 保存 JSON IP
            jsonIp = jsonIpField.getText().trim();
            System.out.println("JSON IP 已保存：" + jsonIp);

            // 关闭设置窗口
            settingsStage.close();
        });

        VBox settingsLayout = new VBox(
                10,
                new Label("1602a地址:"), urlField,
                new Label("JSON数据地址:"), jsonIpField,
                new Label("刷新间隔（毫秒）:"), intervalField,
                saveButton
        );
        settingsLayout.setStyle("-fx-padding: 60; -fx-alignment: center;");

        settingsStage.setScene(new Scene(settingsLayout, 300, 200));
        settingsStage.show();
    }

    //设置定时刷新窗体
    private void startUpdater() {
        Timeline timeline = new Timeline(
                // 默认每0.25秒刷新一次
                new KeyFrame(Duration.millis(refreshIntervalMillis), e -> updateDisplay())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    // 从外部获取最新 map,异步获取防止卡死UI
    private Info currentMap;  // 保存异步获取的Info
    private long lastFailTime = 0; // 上次失败调用时间戳（毫秒）
    private boolean isUpdating = false; // 正在更新标志位
    private void updateBeatmap(){
        long now = System.currentTimeMillis();
        if (isUpdating || now - lastFailTime < 2000) { // 若获取失败进入2000ms冷却时间
            return;
        }
        isUpdating = true; // 标记为正在更新

        Task<Info> task = new Task<>() {
            @Override
            protected Info call() {
                return dataSupplier.get(); // 使用新的 dataSupplier
            }
        };

        task.setOnSucceeded(e -> {
            currentMap = task.getValue(); // 保存获取的 beatmap
            isUpdating = false; // 标记为已完成
            updateDisplay(); // 立即刷新 UI
        });

        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            statusLabel.setText("状态：获取失败：" + (ex != null ? ex.getMessage() : "未知错误"));
            lastFailTime = System.currentTimeMillis();  // 设置冷却时间起点
            isUpdating = false; // 标记为已完成
            if (ex != null) {
                ex.printStackTrace();
            }
        });

        new Thread(task).start();
    }

    // 更新窗体显示数据
    private void updateDisplay() {
            updateBeatmap();
            if (currentMap == null) {
                statusLabel.setText("状态：无数据\n");
                errorContent.setVisible(true);
                errorContent.setManaged(true);
                playContent.setVisible(false);
                playContent.setManaged(false);
                return;
            }

            // 如果 currentMap 有效，隐藏错误提示
            errorContent.setVisible(false);
            errorContent.setManaged(false);
            playContent.setVisible(true);
            errorContent.setVisible(false);

            int state = currentMap.get_states();
            //System.out.println(state);
            if (state == 2 || state == 7) {
                // 显示打图内容
                playContent.setVisible(true);
                playContent.setManaged(true);
                selectContent.setVisible(false);
                selectContent.setManaged(false);

                currentPPLabel.setText("当前 PP:" + String.format("%.2f", currentMap.get_c_pp()));
                fullPPLabel.setText("FC PP:" + String.format("%.2f", currentMap.get_f_pp()));
                hitsLabel.setText("300:" + currentMap.get_s300() + 
                "  100:" + currentMap.get_s100() + 
                "  50:" + currentMap.get_s50());
                missLabel.setText("Miss:" + currentMap.get_smiss() + "  SB:" + currentMap.get_sb());
                statusLabel.setText("");
            }
            else{
                // 显示选图内容
                playContent.setVisible(false);
                playContent.setManaged(false);
                selectContent.setVisible(true);
                selectContent.setManaged(true);

                // 更新 ACC PP 显示
                pp95Label.setText("95% PP: " + String.format("%.2f", currentMap.getPp95()));
                pp98Label.setText("98% PP: " + String.format("%.2f", currentMap.getPp98()));
                pp100Label.setText("100% PP: " + String.format("%.2f", currentMap.getPp100()));
                // 提前返回，不执行打图内容
            }
    }
}
