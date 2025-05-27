package kulib;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Gameplay {
    public current_pp pp;

    public current_pp getpp(){
        return pp;
    }

    public void setpp(current_pp pp){
        this.pp=pp;
    }
}
