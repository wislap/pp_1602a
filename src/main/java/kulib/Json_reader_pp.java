package kulib;

import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Json_reader_pp {
    Gameplay gameplay=new Gameplay();
    public void reader(String raw_json) throws Exception{
        if(raw_json==null) return;
        try {
            ObjectMapper mapper = new ObjectMapper();

            // 读取 JSON 文件
            OsuStatus status = mapper.readValue(raw_json, OsuStatus.class);

            // 获取 PP 对象
            Current_pp pp = status.gameplay.pp;
            gameplay.setpp(pp);

            // 获取 hits Map
            if (status.gameplay != null && status.gameplay.hits != null) {
                Map<String, Object> hits = status.gameplay.hits;
                // 这里可以传给 gameplay 或其他类
                gameplay.setHits(hits);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
