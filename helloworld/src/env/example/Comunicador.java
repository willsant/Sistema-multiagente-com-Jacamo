package example;

import cartago.*;

public class  Comunicador extends Artifact {
    int count;

    public void init(int value){
        this.count = value;
        defineObsProperty("count", this.count);
        log("Comunicador! ");
    }

    @OPERATION
    public void comunicando(){
        log("Comunicando valor! ");
    }
}
