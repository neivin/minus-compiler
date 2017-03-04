package absyn;

public class FunDec extends Dec {
	public Type result;
	public String func;
	public VarDecList params;
	public CompoundExp body;

	public FunDec (int pos, Type result, String name, ParamList params, VarDecList body){
		this.pos = pos;
		this.result = result;
		this.name = name;
		this.params = params;
		this.body = body;
	}
}