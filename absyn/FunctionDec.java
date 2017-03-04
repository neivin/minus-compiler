package absyn;

public class FunctionDec extends Dec {
	public Type result;
	public String func;
	public VarDecList params;
	public CompoundExp body;

	public FunctionDec (int pos, Type result, String name, VarDecList params, CompoundExp body){
		this.pos = pos;
		this.result = result;
		this.name = name;
		this.params = params;
		this.body = body;
	}
}