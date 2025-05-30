package kulib;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JsonReader {
    private Gameplay gameplay = new Gameplay();

    public void parse(String rawJson) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        OsuStatus status = mapper.readValue(rawJson, OsuStatus.class);

        gameplay.setpp(status.gameplay.pp);
        gameplay.setHits(status.gameplay.hits);

        if (status.menu != null) {
            gameplay.setStates(status.menu.getState());
            gameplay.setMenuPP(status.menu.pp);
        }
    }

    public Gameplay getGameplay() {
        return gameplay;
    }
}
