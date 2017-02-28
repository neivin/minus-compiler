package absyn;

public SelStmt extends Stmt{
	public Exp test;
  	public Stmt thenpart;
  	public Stmt elsepart;
  	public SelStmt( int pos, Exp test, Stmt thenpart, Stmt elsepart ) {
    	this.pos = pos;
    	this.test = test;
    	this.thenpart = thenpart;
    	this.elsepart = elsepart;
  	}
}