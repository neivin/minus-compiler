package absyn;

public class Factor extends Absyn {
  public int num = -1;
  public Var var;
  public Call call;
  public Exp exp;

  /* Expression */
  public Factor(int pos, Exp exp){
    this.pos = pos;
    this.exp = exp;

    this.var = null;
    this.call = null;
    this.num = -1;
  }

  /* Var */
  public Factor(int pos, Var var){
    this.pos = pos;
    this.var = var;

    this.exp = null;
    this.call = null;
    this.num = -1;
  }

  /* Function call */
  public Factor(int pos, Call call){
    this.pos = pos;
    this.call = call;

    this.var = null;
    this.exp = null;
    this.num = -1;
  }

  /* Number */
  public Factor(int pos, int num){
    this.pos = pos;
    this.num = num;

    this.var = null;
    this.call = null;
    this.exp = null;
  }
}
