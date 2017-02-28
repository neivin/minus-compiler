package absyn;

public class VarDec extends Dec {
	public int num = -1;


	/* Constructor for general variable declarations*/
	public VarDec(int pos, int type, String id){
		this.pos = pos;
		this.type = type;
		this.id = id;
	}


	/* Constructor for array variable declarations*/
	public VarDec(int pos, int type, String id, int num){
		this.pos = pos;
		this.type = type;
		this.id = id;
		this.num = num;
	}	
}