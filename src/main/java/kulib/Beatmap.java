package kulib;
import java.util.Map;

public class Beatmap {
    private Current_pp pp;
    private Map<String, Object> hits;

    private float current_pp;
    private float fc_pp;
    private int s300;
    private int s100;
    private int s50;
    private int smiss;
    private int sliderBreaks;

    public Beatmap(Current_pp pp, Map<String, Object> hits) {
        if (pp == null) {
            System.err.println("当前 pp 为空，跳过设置");
            return;
        }
        this.pp = pp;

        // 设置 pp
        this.current_pp = pp.getCurrent();
        this.fc_pp = pp.getFc();

        // 设置命中统计（注意根据 JSON 键设定）
        
        if (hits != null) {
            this.s300 = (int) hits.getOrDefault("300", 0);
            this.s100 = (int) hits.getOrDefault("100", 0);
            this.s50 = (int) hits.getOrDefault("50", 0);
            this.smiss = (int) hits.getOrDefault("0", 0);  // miss 是 key "0"
            this.sliderBreaks = (int) hits.getOrDefault("sliderBreaks", 0);

        }
        else {
            System.err.println("hits 数据为空");
            return;
        }
    }

    public float get_c_pp() {return current_pp;}
    public float get_f_pp() {return fc_pp;}
    public int get_s300() {return s300;}
    public int get_s100() {return s100;}
    public int get_s50() {return s50;}
    public int get_smiss() {return smiss;}
    public int get_sb() {return sliderBreaks;}
}
