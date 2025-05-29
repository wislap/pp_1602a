package kulib;

import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Json_reader {
    Gameplay gameplay=new Gameplay();

    public void reader(String raw_json) throws Exception{
        if(raw_json==null) return;
        try {
            ObjectMapper mapper = new ObjectMapper();

            // 读取 JSON 文件,将内容付给status
            OsuStatus status = mapper.readValue(raw_json, OsuStatus.class);

            // 获取PP对象并发送给gameplay
            Current_pp pp = status.gameplay.pp;
            gameplay.setpp(pp);

            // 获取hits Map并发送给gameplay
            if (status.gameplay != null && status.gameplay.hits != null) {
                Map<String, Object> hits = status.gameplay.hits;
                gameplay.setHits(hits);
            }

            // 读取ACC PP
            if (status.menu != null) {
                gameplay.setStates(status.menu.getState());
                gameplay.setMenuPP(status.menu.pp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
