package kulib;

public class Map {
    private Current_pp pp;

    private float current_pp;
    private float fc_pp;
    private int s300;
    private int s100;
    private int s50;
    private int smiss;

    public Map(Current_pp pp) {
        if (pp == null) {
            System.err.println("当前 pp 为空，跳过设置");
        return;
        }
        this.pp = pp;
    }

    public float get_c_pp() {return current_pp;}
    public float get_f_pp() {return fc_pp;}
    public int get_geki() {return s300;}
    public int get_katsu() {return s100;}
    public int get_meh() {return s50;}
    public int get_miss() {return smiss;}

    public void set_c_pp() {
        this.current_pp =pp.getCurrent();}
    public void set_f_pp() {this.fc_pp =pp.getFc();}
    public void set_geki() {
        s300 =this.s300;}
    public void set_katsu() {
        s100 =this.s100;}
    public void set_meh() {
        s50 =this.s50;}
    public void set_miss() {
        smiss =this.smiss;}
}
