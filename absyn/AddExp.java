pacakge absyn;

public class AddExp extends Absyn {
  public static final int PLUS = 0;
  public static final int MINUS = 1;

  public AddExp additiveExp;
  public int addop;
  public Term term;

  public AddExp(int pos, Term term){
    this.pos = pos;
    this.term = term;
    this.additiveExp = null;
    this.addop = -1;
  }

  public AddExp(int pos, AddExp additiveExp, int addop, Term term){
    this.pos = pos;
    this.additiveExp = additiveExp;
    this.addop = addop;
    this.term = term;
  }
}
