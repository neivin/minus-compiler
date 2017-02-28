package absyn;

public class Var extends Absyn {
	public String id;
	public Exp expr;

	public Var (int pos, String id){
		this.pos = pos;
		this.id = id;
		this.expr = null;
	}

	public Var (int pos, String id, Exp expr){
		this.pos = pos;
		this.id = id;
		this.expr = expr;
	}
}