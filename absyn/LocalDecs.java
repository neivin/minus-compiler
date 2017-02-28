package absyn;

public class LocalDecs extends Absyn{
	public VarDec head;
	public LocalDecs tail;
	
	public LocalDecs (){
		this.pos = -1;
		this.head = null;
		this.tail = null;
	}

	public LocalDecs (int pos, VarDec head, LocalDecs tail){
		this.pos = pos;
		this.head = head;
		this.tail = tail;
	}
}