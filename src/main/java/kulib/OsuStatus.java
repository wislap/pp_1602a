package kulib;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class OsuStatus {
    public Gameplay gameplay;

    public Menu menu;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Menu {
        public Map<String, Object> hits;
    }
}
