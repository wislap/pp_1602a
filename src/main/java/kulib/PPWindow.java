package kulib;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.function.Supplier;

public class PPWindow extends Application {

    private static Supplier<Map> mapSupplier;  // 外部提供 map 的方法

    private final Label currentPPLabel = new Label("当前 PP：");
    private final Label fullPPLabel = new Label("满分 PP：");
    private final Label statusLabel = new Label("状态：等待中...");

    public static void setMapSupplier(Supplier<Map> supplier) {
        mapSupplier = supplier;
    }

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10, currentPPLabel, fullPPLabel, statusLabel);
        root.setStyle("-fx-padding: 20; -fx-font-size: 16; -fx-alignment: center;");

        Scene scene = new Scene(root, 300, 150);
        stage.setTitle("PP 显示窗口");
        stage.setScene(scene);
        stage.show();

        startUpdater();
    }

    private void startUpdater() {
        Timeline timeline = new Timeline(
                // 每0.25秒刷新一次
                new KeyFrame(Duration.millis(250), e -> updateDisplay())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateDisplay() {
        try {
            Map currentMap = mapSupplier.get();  // 从外部获取最新 map
            if (currentMap == null) {
                statusLabel.setText("状态：无数据");
                return;
            }
            currentPPLabel.setText("当前 PP：" + String.format("%.2f", currentMap.get_c_pp()));
            fullPPLabel.setText("满分 PP：" + String.format("%.2f", currentMap.get_f_pp()));
            statusLabel.setText("状态：更新成功");
        } catch (Exception e) {
            statusLabel.setText("状态：获取失败：" + e.getMessage());
        }
    }
}
