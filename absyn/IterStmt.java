package absyn;

public class IterStmt extends Stmt {
	public Exp test;
	public Stmt statement;

	public IterStmt (int pos, Exp test, Stmt statement){
		this.pos = pos;
		this.test = test;
		this.statement = statement;
	}
}