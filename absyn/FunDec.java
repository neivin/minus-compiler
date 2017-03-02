package absyn;

public class FunDec extends Dec {
	public Type type;
	public String id;
	public ParamList params;
	public CompStmt compound;

	public FunDec (int pos, Type type, String id, ParamList params, CompStmt compound){
		this.pos = pos;
		this.type = type;
		this.id = id;
		this.params = params;
		this.compound = compound;
	}
}