package kulib;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Gameplay {
    public Current_pp pp;

    public Current_pp getpp(){
        return pp;
    }

    public void setpp(Current_pp pp){
        this.pp=pp;
    }
}
