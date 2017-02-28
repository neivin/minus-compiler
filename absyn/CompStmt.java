package absyn;

public class CompStmt extends Absyn{
	public LocalDecs localDecs;
	public StmtList stmtList;

	public ComptStmt (int pos, LocalDecs localDecs, StmtList stmtList){
		this.pos = pos;
		this.localDecs = localDecs;
		this.stmtList = stmtList;
	}

}