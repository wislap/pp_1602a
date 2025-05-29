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

    public void display_pp(Info currentMap){
        int states = currentMap.get_states();
        if(states==2 || states ==7)
            display_costom(String.format("CUR:%12.1fFC:%13.1f", currentMap.get_c_pp(), currentMap.get_f_pp()));
        else{
            int pp95 = Math.round(currentMap.getPp95());
            int pp98 = Math.round(currentMap.getPp98());

            // 构造 95 和 98 的合并字符串，共16字符
            String part1 = String.format("95:%-4d 98:%-4d", pp95, pp98);
            // → "95:1234 98:5678"（总共16字符，数字部分不足4位自动空格补齐）

            int pp100 = Math.round(currentMap.getPp100());

            display_costom(part1 + " 100:" + pp100);
        }
    }
}
