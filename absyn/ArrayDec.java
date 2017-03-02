package absyn;

public class ArrayDec extends VarDec {
	public String num = "";

	/* Array variable declarations*/
	public VarDec(int pos, Type type, String id, String num){
		this.pos = pos;
		this.type = type;
		this.id = id;
		this.num = num;
	}
}