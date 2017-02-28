package absyn;

public ExpStmt extends Stmt{
	public Exp expr;

	public Exp(){
		this.expr = null;
	}

	public Exp(int pos, Exp expr){
		this.pos = pos;
		this.expr = expr;
	}
}