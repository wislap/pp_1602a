package kulib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class Api {
    public static String raw_json;
    private static long lastSendTime = 0;
    private static final long MIN_INTERVAL_MS = 800; // 向1602的最小发送间隔

    public static String get_json() {
        int attempt = 0;
        int retryDelay = 2000;

        while (true) {
            try {
                URL url = new URL(PPWindow.getJsonIp()+"/json");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                in.close();
                conn.disconnect();

                raw_json = content.toString();
                return raw_json;

            } catch (IOException e) {
                attempt++;
                System.err.println("请求失败（第 " + attempt + " 次）： " + e.getMessage());
            }

            try {
                Thread.sleep(retryDelay);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                System.err.println("重试等待被中断。");
                return null;
            }
        }
    }

    public Info api_main() throws Exception {

        Dis_1602a dis = new Dis_1602a();
        Json_reader current_play = new Json_reader();

        String json = get_json();

        try {
            current_play.reader(json);
        } catch (Exception e) {
            System.err.println("JSON 解析失败：" + e.getMessage());
            return null;
        }

        if (current_play.gameplay == null || current_play.gameplay.getpp() == null) {
            System.out.println("pp 数据为 null,跳过");
            return null;
        }

        try {
            Info this_map = new Info(
                current_play.gameplay.getpp(),
                current_play.gameplay.getHits(),
                current_play.gameplay.getMenuPP(),
                current_play.gameplay.getStates()
            );

            //单独线程发送给1602a防止卡死窗口
            long now = System.currentTimeMillis();
            if (now - lastSendTime >= MIN_INTERVAL_MS) {
                new Thread(() -> dis.display_pp(this_map)).start();
                lastSendTime = now;
            }

            return this_map;

        } catch (Exception e) {
            System.err.println("构建或显示时发生异常：" + e.getMessage());
        }
        return null;
    }
}
