package kulib;
import java.net.*;

public class Dis_1602a {
    public static String URLstream="http://192.168.8.125";

    public static void send_pp(String dis_url){
        try{
            // 创建URL对象
            URL url = new URL(dis_url);
                
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // 设置请求方法
            connection.setRequestMethod("GET");

            //超时时间1S
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);
            
            // 获取响应代码
            int responseCode = connection.getResponseCode();
            //System.out.println("Response Code: " + responseCode);

            //关闭连接
            connection.getInputStream().close();
            
            
        }
        catch (Exception e) {
            //e.printStackTrace();
            System.err.println("忽略错误: " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    public void display_pp(Map currentMap){
        send_pp("http://192.168.8.125/?inputbox=current pp:"+String.format("%-5.1f",currentMap.get_c_pp())+"fc_pp:"+currentMap.get_f_pp());
    }
}
