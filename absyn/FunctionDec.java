package absyn;

public class FunctionDec extends Dec {
	public Type result;
	public String func;
	public VarDecList params;
	public CompoundExp body;

	public FunctionDec (int pos, Type result, String func, VarDecList params, CompoundExp body){
		this.pos = pos;
		this.result = result;
		this.func = func;
		this.params = params;
		this.body = body;
	}
}