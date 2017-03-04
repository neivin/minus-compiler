package absyn;

public class VarDecList {
	public VarDec head;
	public VarDecList tail;

	public DecList (VarDec head, VarDecList tail){
		this.head = head;
		this.tail = tail;
	}
}