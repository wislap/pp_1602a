package kulib;
import java.util.Map;

public class Info {
    private Current_pp pp;
    private Map<String, Object> hits;
    private Map<String, Double> menuPP;

    // 普通 PP（打图时）
    private float current_pp;
    private float fc_pp;

    // 选图用的 ACC PP
    private float pp_95;
    private float pp_98;
    private float pp_100;

    private int s300;
    private int s100;
    private int s50;
    private int smiss;
    private int sliderBreaks;
    private int states;

    public Info(Current_pp pp, Map<String, Object> hits, Map<String, Double> menuPP,int states) {
        this.pp = pp;
        this.hits = hits;
        this.menuPP = menuPP;
        this.states=states;

        if (pp == null) {
            System.err.println("当前 pp 为空，跳过设置");
            return;
        }

        // 设置 pp
        this.current_pp = pp.getCurrent();
        this.fc_pp = pp.getFc();

        // 处理ACC类别PP
        if (menuPP != null) {
            this.pp_95 = menuPP.get("95") != null ? menuPP.get("95").floatValue() : 0f;
            this.pp_98 = menuPP.get("98") != null ? menuPP.get("98").floatValue() : 0f;
            this.pp_100 = menuPP.get("100") != null ? menuPP.get("100").floatValue() : 0f;
        } else {
            System.err.println("ACC类别PP数据为空");
        }

        // 设置命中统计
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

    public void setStates(int states) { this.states = states; }
    public int get_states() {return states;}

    public float getPp95() { return pp_95; }
    public float getPp98() { return pp_98; }
    public float getPp100() { return pp_100; }
}
