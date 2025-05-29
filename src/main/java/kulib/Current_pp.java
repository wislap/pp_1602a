package kulib;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Current_pp {
    public float current;
    public float fc;
    public float maxThisPlay;

    public float getCurrent() {
        return current;
    }
    
    public float getFc() {
        return fc;
    }

    public float getMaxThisPlay() {
        return maxThisPlay;
    }
}
