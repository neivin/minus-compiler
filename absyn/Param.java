package absyn;

public class Param extends Absyn{
	static final int ARRAYPARAM = 1;
	static final int INTPARAM = 0;
	
	public int type;
	public String id;
	public int param_type;

	public Param (int pos, int type, String id, int param_type ){
		this.pos = pos;
		this.type = type;
		this.id = id;
		this.param_type = param_type;
	}

}