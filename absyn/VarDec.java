package absyn;

public class VarDec extends Dec {
	public int num = -1;

	/* General variable declarations*/
	public VarDec(int pos, int type, String id){
		this.pos = pos;
		this.type = type;
		this.id = id;
	}

	/* Array variable declarations*/
	public VarDec(int pos, int type, String id, int num){
		this.pos = pos;
		this.type = type;
		this.id = id;
		this.num = num;
	}
}
