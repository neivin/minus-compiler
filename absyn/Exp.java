package absyn;

public class Exp extends Absyn {
	public Var identifier;
	public Exp expr;
	public SimpleExp simpleExp;

	public Exp (int pos, SimpleExp simpleExp){
		this.pos = pos;
		this.simpleExp = simpleExp;

		this.expr = null;
		this identifier = null;
	}

	public Exp (int pos, Var identifier, Exp expr){
		this.pos = pos;
		this.identifier = identifier;
		this.expr = expr;
		
		this.simpleExp = null;
	}

}