package absyn;

public class ParamList {
	public Param head;
	public ParamList tail;

	public ParamList (Param head, ParamList tail){
		this.head = head;
		this.tail = tail;
	}

}
