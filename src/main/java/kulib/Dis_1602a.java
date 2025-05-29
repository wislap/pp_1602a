package kulib;
import java.net.*;

public class Dis_1602a {
    public static String URLstream="http://192.168.8.125";

    public static void display_costom(String customString){
        try{
            URLstream = PPWindow.getDeviceUrl();

            // 创建URL对象
            URL url = new URL(URLstream + "/?inputbox=" + customString);
                
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // 设置请求方法
            connection.setRequestMethod("GET");

            //超时时间1S
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);

            //关闭连接
            connection.getInputStream().close();
            connection.disconnect();
        }
        catch (Exception e) {
            //e.printStackTrace();
            System.err.println("1602发送失败: " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    public void display_pp(Beatmap currentMap){
        display_costom("current pp:"+String.format("%-5.1f",currentMap.get_c_pp())+"fc_pp:"+currentMap.get_f_pp());
    }
}
