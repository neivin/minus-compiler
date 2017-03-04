package absyn;

public class SimpleDec extends VarDec{

	/* General variable declarations*/
	public SimpleDec(int pos, Type type, String name){
		this.pos = pos;
		this.type = type;
		this.name = name;
	}
}