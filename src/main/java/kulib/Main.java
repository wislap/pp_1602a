package kulib;

public class Main {
    public static void main(String[] args) {
        api main_prosess = new api();
    while (true) {
        try {
            main_prosess.api_main();
            // 你的主逻辑
        } catch (Exception e) {
            System.err.println("异常捕获：程序继续运行 -> " + e.getMessage());
        }

        try {
            Thread.sleep(3000); // 等待几秒再试
        } catch (InterruptedException ignored) {}
    }
}

}