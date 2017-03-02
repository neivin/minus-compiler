package absyn;

public class Param extends Absyn{
	static final int INTPARAM = 0;
	static final int ARRAYPARAM = 1;

	public int decl;
	public String id;
	public Type type;

	public Param (int pos, Type type, String id, int decl ){
		this.pos = pos;
		this.type = type;
		this.id = id;
		this.decl = decl;
	}

}