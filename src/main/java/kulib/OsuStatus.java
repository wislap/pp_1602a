package kulib;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)

public class OsuStatus {
    public Gameplay gameplay;
    public Menu menu;

    public Menu getMenu() { return menu; }
    public void setMenu(Menu menu) { this.menu = menu; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Menu {
        @JsonDeserialize(using = PpMapDeserializer.class) //使用自定义反序列器解析
        public Map<String, Double> pp;  // key是ACC，value是对应PP

        @JsonIgnoreProperties(ignoreUnknown = true)
        public Map<String, Object> hits;
        private int state;

        public int getState() { return state; }
        public void setState(int state) { this.state = state; }
    }
}
