package absyn;

public class SimpleExp extends Absyn {
  public static final int LESS = 0;
  public static final int GREATER = 1;
  public static final int LEQUIV = 2;
  public static final int GEQUIV = 3;
  public static final int EQUIV = 4;
  public static final int NEQUIV = 5;

  public AddExp exp1;
  public AddExp exp2;
  public int relop;

  public SimpleExp (int pos, AddExp exp){
    this.pos = pos;
    this.relop = -1;
    this.exp1 = exp;
    this.exp2 = null;
  }

  public SimpleExp (int pos, AddExp exp1, int relop, AddExp exp2){
    this.pos = pos;
    this.relop = relop;
    this.exp1 = exp1;
    this.exp2 = exp2;
  }
}
