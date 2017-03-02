package absyn;

public class VarDec extends Dec {
	public Type type;
	public String num = "";

	/* General variable declarations*/
	public VarDec(int pos, Type type, String id){
		this.pos = pos;
		this.type = type;
		this.id = id;
	}

	/* Array variable declarations*/
	public VarDec(int pos, Type type, String id, String num){
		this.pos = pos;
		this.type = type;
		this.id = id;
		this.num = num;
	}
}
