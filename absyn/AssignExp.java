package absyn;

public class AssignExp extends Exp {
	public Var lhs;
	public Exp rhs;

	public AssignExp (int pos, Var lhs, Exp rhs){
		this.pos = pos;
		this.lhs = lhs;
		this.rhs = rhs;
	}

}
