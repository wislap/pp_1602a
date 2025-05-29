package kulib;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Gameplay {
    public Current_pp pp;
    public Map<String, Object> hits;
    private int missCount;

    public Current_pp getpp(){
        return pp;
    }
    public void setpp(Current_pp pp){
        this.pp=pp;
    }

    public Map<String, Object> getHits() {
        return hits;
    }
    public void setHits(Map<String, Object> hits) {
        this.hits = hits;
    }

    public int getMissCount() {
        return missCount;
    }
    public void setMissCount(int missCount) {
        this.missCount = missCount;
    }
}
