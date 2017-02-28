package absyn;

public ExpStmt extends Stmt{
	public Exp expr;

	public Exp(){
		this.expr = null;
	}

	public Exp(Exp expr){
		this.expr = expr;
	}
}