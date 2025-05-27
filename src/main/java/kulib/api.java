package kulib;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.TimeUnit;

public class api {
    public static String raw_json;
    public static String get_json() throws Exception {
        URL url = new URL("http://127.0.0.1:24050/json");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // 读取响应
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        conn.disconnect();

        // 输出原始 JSON 字符串
        raw_json=content.toString();
        //System.out.println(raw_json);

        return raw_json;

        // 你可以用 Gson 或 Jackson 进一步解析这个 JSON
    }
    public static void main(String[] args) throws Exception {
        System.out.println("hello!");
        dis_1602a dis=new dis_1602a();
        //get_json();
        //System.out.println(get_json());
        json_reader_pp current_play=new json_reader_pp();
        while(true){
            current_play.reader(get_json());
            map this_map=new map(current_play.gameplay.getpp());
            this_map.set_c_pp();
            this_map.set_f_pp();
            //System.out.println(this_map.get_c_pp());
            //System.out.println(this_map.get_f_pp());
            dis.display_pp(this_map.get_c_pp(), this_map.get_f_pp());
            TimeUnit.MILLISECONDS.sleep(50);
        }
    }
}
