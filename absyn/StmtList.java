package absyn;

public class StmtList extends Absyn{
	public Stmt head;
	public StmtList tail;

	public StmtList (){
		this.pos = -1;
		this.head = null;
		this.tail = null;
	}

	public StmtList (int pos, Stmt head, StmtList tail){
		this.pos = pos;
		this.head = head;
		this.tail = tail;
	}
}