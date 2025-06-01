package kulib;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)

public class OsuStatus {
    public Gameplay gameplay;
    public Menu menu;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Menu {
        @JsonDeserialize(using = PpMapDeserializer.class) //使用自定义反序列器解析
        public Map<String, Double> pp;  // key是ACC，value是对应PP

        @JsonIgnoreProperties(ignoreUnknown = true)
        private int state;

        public int getState() { return state; }
    }
}
//233
