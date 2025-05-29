package kulib;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    private final Label currentPPLabel = new Label("当前 PP：");
    private final Label fullPPLabel = new Label("FC PP：");
    private final Label hitsLabel = new Label("");
    private final Label missLabel = new Label("");

    private final Label statusLabel = new Label("状态：等待中...");

    // Getter
    public static String getDeviceUrl() {
        return deviceUrl;
    }

    public static void setMapSupplier(Supplier<Beatmap> supplier) {
        mapSupplier = supplier;
    }

    @Override
    public void start(Stage stage) {
        Button settingsButton = new Button("设置");
        settingsButton.setOnAction(e -> openSettingsWindow());

        VBox root = new VBox(10, currentPPLabel, fullPPLabel, hitsLabel, missLabel, statusLabel, settingsButton);
        root.setStyle("-fx-padding: 20; -fx-font-size: 16; -fx-alignment: center;");

        Scene scene = new Scene(root, 300, 200);
        stage.setTitle("PP 显示窗口");
        stage.setScene(scene);
        stage.show();

        // 设置关闭事件
        stage.setOnCloseRequest(event -> {
            Dis_1602a.display_costom("Disconnected");
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

    private void startUpdater() {
        Timeline timeline = new Timeline(
                // 默认每0.25秒刷新一次
                new KeyFrame(Duration.millis(refreshIntervalMillis), e -> updateDisplay())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateDisplay() {
        try {
            Beatmap currentMap = mapSupplier.get();  // 从外部获取最新 map
            if (currentMap == null) {
                statusLabel.setText("状态：无数据");
                return;
            }
            currentPPLabel.setText("当前 PP：" + String.format("%.2f", currentMap.get_c_pp()));
            fullPPLabel.setText("FC PP：" + String.format("%.2f", currentMap.get_f_pp()));
            hitsLabel.setText("300:" + currentMap.get_s300() + 
            "  100:" + currentMap.get_s100() + 
            "  50:" + currentMap.get_s50());

            missLabel.setText("Miss:" + currentMap.get_smiss() + "  SB:" + currentMap.get_sb());
            statusLabel.setText("状态：更新成功");
        } catch (Exception e) {
            statusLabel.setText("状态：获取失败：" + e.getMessage());
        }
    }
}
