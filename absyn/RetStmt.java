package absyn;

public class RetStmt extends Stmt {
	public Exp expr;

	public RetStmt(){
		this.expr = null;
	}

	public RetStmt(int pos, Exp expr){
		this.pos = pos;
		this.expr = expr;
	}
}