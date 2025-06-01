package kulib;

import javafx.application.Application;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Main {
    public static void main(String[] args) throws UnsupportedEncodingException{
        // 设置控制台为GBK编码
        System.setOut(new PrintStream(System.out, true, "GBK"));
        System.setErr(new PrintStream(System.err, true, "GBK"));
        
        Api api = new Api();    // 创建API对象

        // 将获取Map的函数提供给窗口
        PPWindow.setDataSupplier(api::fetchGameData);
        // 添加关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(api::shutdown));

        // 启动 JavaFX 窗口
        Application.launch(PPWindow.class);
    }
}
//233
