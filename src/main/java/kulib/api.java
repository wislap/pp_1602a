package kulib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.TimeUnit;

public class api {
    public static String raw_json;
   public static String get_json() {
    int attempt = 0;
    int retryDelay = 2000;

    while (true) {
        try {
            URL url = new URL("http://127.0.0.1:24050/json");
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

    void api_main() throws Exception {

    dis_1602a dis = new dis_1602a();
    json_reader_pp current_play = new json_reader_pp();

    String json = get_json();

    if (json == null || json.isEmpty()) {
        System.out.println("未获取到 JSON，跳过本轮处理");
        return;
    }

    try {
        current_play.reader(json);
    } catch (Exception e) {
        System.err.println("JSON 解析失败：" + e.getMessage());
        return;
    }

    if (current_play.gameplay == null || current_play.gameplay.getpp() == null) {
        System.out.println("pp 数据为 null，跳过");
        return;
    }

    try {
        map this_map = new map(current_play.gameplay.getpp());
        this_map.set_c_pp();
        this_map.set_f_pp();

        dis.display_pp(this_map.get_c_pp(), this_map.get_f_pp());
    } catch (Exception e) {
        System.err.println("构建或显示时发生异常：" + e.getMessage());
    }
}
}
