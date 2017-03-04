package absyn;

public class ArrayDec extends VarDec {
	public IntExp size;

	/* Array variable declarations*/
	public VarDec(int pos, Type type, String name, IntExp size){
		this.pos = pos;
		this.type = type;
		this.name = name;
		this.size = size;
	}
}