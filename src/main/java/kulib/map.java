package kulib;

public class map {
    private current_pp pp;

    private float c_pp;
    private float f_pp;
    private int geki;
    private int katsu;
    private int meh;
    private int miss;

    public map(current_pp pp) {
        if (pp == null) {
            System.err.println("当前 pp 为空，跳过设置");
        return;
        }
        this.pp = pp;
    }

    public float get_c_pp() {return c_pp;}
    public float get_f_pp() {return f_pp;}
    public int get_geki() {return geki;}
    public int get_katsu() {return katsu;}
    public int get_meh() {return meh;}
    public int get_miss() {return miss;}

    public void set_c_pp() {
        this.c_pp=pp.getCurrent();}
    public void set_f_pp() {this.f_pp=pp.getFc();}
    public void set_geki() {geki=this.geki;}
    public void set_katsu() {katsu=this.katsu;}
    public void set_meh() {meh=this.meh;}
    public void set_miss() {miss=this.miss;}
}
