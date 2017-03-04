package absyn;

public class IndexVar extends Var{
	public Exp index;

	public IndexVar (int pos, String name, Exp index){
		this.pos = pos;
		this.name = name;
		this.index = index;
	}

}


