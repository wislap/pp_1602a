package kulib;
import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

public class json_reader_pp {
    Gameplay gameplay=new Gameplay();
    public void reader(String raw_json) throws Exception{
        try {
            ObjectMapper mapper = new ObjectMapper();

            // 读取 JSON 文件
            OsuStatus status = mapper.readValue(raw_json, OsuStatus.class);

            // 获取 PP 对象
            current_pp pp = status.gameplay.pp;

            // System.out.println("Current PP: " + pp.current);
            // System.out.println("FC PP: " + pp.fc);
            // System.out.println("Max This Play PP: " + pp.maxThisPlay);

            gameplay.setpp(pp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
