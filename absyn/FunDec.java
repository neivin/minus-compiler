package absyn;

public class FunDec extends Dec {
	public ParamList params;
	public CompStmt compound;

	public FunDec (int pos, int type, String id, ParamList params, CompStmt compound){
		this.pos = pos;
		this.type = type;
		this.id = id;
		this.params = params;
		this.compound = compound;
	}
}