package absyn;

public class Term extends Absyn{
  public static final MUL = 0;
  public static final DIV = 1;

  public Term term;
  public int mulop;
  public Factor factor;

  public Term (int pos, Factor factor) {
    this.pos = pos;
    this.factor = factor;
    this.term = null;
    this.mulop = -1;
  }

  public Term (int pos, Term term, int mulop, Factor factor) {
    this.pos = pos;
    this.mulop = mulop;
    this.factor = factor;
    this.term = term;
  }

}
