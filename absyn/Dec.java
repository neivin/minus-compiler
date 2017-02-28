package absyn;

abstract public class Dec extends Absyn {
	static final int INTEGER = 0;
	static final int VOID = 1;

	public int type;
	public String id;
}