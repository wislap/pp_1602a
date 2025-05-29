package kulib;

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
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.function.Supplier;

public class PPWindow extends Application {
    // 设置默认值
    private static int refreshIntervalMillis = 250; // 默认250毫秒刷新间隔
    private static String deviceUrl = "http://192.168.8.125";

    private static Supplier<Beatmap> mapSupplier;  // 外部提供 map 的方法

    // 获取1602a的IP
    public static String getDeviceUrl() {
        return deviceUrl;
    }

    public static void setMapSupplier(Supplier<Beatmap> supplier) {
        mapSupplier = supplier;
    }

    // 默认元素
    private final Label currentPPLabel = new Label("");
    private final Label fullPPLabel = new Label("");
    private final Label hitsLabel = new Label("");
    private final Label missLabel = new Label("");

    private final Label statusLabel = new Label("状态：等待中...");

    // 错误提示
    private final Label errorHint = new Label("请检查TOSU和OSU是否正常启动");

    private VBox mainContent;    // 主内容区
    private VBox errorContent;   // 错误内容区

    @Override
    public void start(Stage stage) {
        // 主内容
        mainContent = new VBox(
            10, 
            currentPPLabel, 
            fullPPLabel, 
            hitsLabel, 
            missLabel
        );
        mainContent.setStyle("-fx-alignment: center;");

        // 错误提示
        errorContent = new VBox(
            10, 
            statusLabel, 
            errorHint
        );
        //errorContent.setStyle("-fx-alignment: center;");
        errorContent.setStyle("-fx-alignment: center; -fx-border-color: red; -fx-border-width: 2;");
        errorHint.setStyle("-fx-text-fill: red;");

        // StackPane 覆盖显示两个内容区域
        StackPane contentPane = new StackPane(mainContent, errorContent);

        // 初始化只显示 mainContent
        mainContent.setVisible(true);
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

            // 关闭设置窗口
            settingsStage.close();
        });

        VBox settingsLayout = new VBox(
                10,
                new Label("1602a地址:"), urlField,
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
    private Beatmap currentMap;  // 保存异步获取的 Beatmap
    private long lastFailTime = 0; // 上次失败调用时间戳（毫秒）
    private boolean isUpdating = false; // 正在更新标志位
    private void updateBeatmap(){
        long now = System.currentTimeMillis();
        if (isUpdating || now - lastFailTime < 2000) { // 若获取失败进入2000ms冷却时间
            return;
        }
        isUpdating = true; // 标记为正在更新

        Task<Beatmap> task = new Task<>() {
            @Override
            protected Beatmap call() throws Exception {
                return mapSupplier.get();
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
            ex.printStackTrace();
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
                mainContent.setVisible(false);
                mainContent.setManaged(false);
                return;
            }

            // 如果 currentMap 有效，隐藏错误提示
            errorContent.setVisible(false);
            errorContent.setManaged(false);
            mainContent.setVisible(true);
            errorContent.setVisible(false);
            
            // 显示信息
            currentPPLabel.setText("当前 PP:" + String.format("%.2f", currentMap.get_c_pp()));
            fullPPLabel.setText("FC PP:" + String.format("%.2f", currentMap.get_f_pp()));
            hitsLabel.setText("300:" + currentMap.get_s300() + 
            "  100:" + currentMap.get_s100() + 
            "  50:" + currentMap.get_s50());
            missLabel.setText("Miss:" + currentMap.get_smiss() + "  SB:" + currentMap.get_sb());
            statusLabel.setText("");
    }
}
