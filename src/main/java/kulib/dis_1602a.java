package kulib;
import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

import javax.print.DocFlavor.STRING;
public class dis_1602a {
    public static String URLstream="http://192.168.8.125";

    public static void send_pp(String dis_url){
        try{
            // 创建URL对象
            URL url = new URL(dis_url);
                
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // 设置请求方法
            connection.setRequestMethod("GET");
            
            // 获取响应代码
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void display_pp(float c_pp,float f_pp){
        send_pp("http://192.168.8.125/?inputbox=current pp:"+String.format("%-5.1f",c_pp)+"fc_pp:"+f_pp);
    }
}
