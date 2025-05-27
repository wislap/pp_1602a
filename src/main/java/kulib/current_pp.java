package kulib;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class current_pp {
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

    // 可选：方便打印查看
    @Override
    public String toString() {
        return "PP{" +
                "current=" + current +
                ", fc=" + fc +
                ", maxThisPlay=" + maxThisPlay +
                '}';
    }
}
