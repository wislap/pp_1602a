package kulib;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        Api api = new Api();

        // 将获取Map的函数提供给窗口
        PPWindow.setMapSupplier(() -> {
            try {
                return api.api_main(); // 每次调用重新抓取并构造Map
            } catch (Exception e) {
                return null;
            }
        });

        // 启动 JavaFX 窗口
        Application.launch(PPWindow.class);
    }
}
