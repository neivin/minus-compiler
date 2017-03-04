package absyn;

public class SimpleDec extends VarDec{

	/* General variable declarations*/
	public VarDec(int pos, Type type, String name){
		this.pos = pos;
		this.type = type;
		this.name = name;
	}
}