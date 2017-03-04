package absyn;

public class IfExp extends Exp{
	public Exp test;
  	public Exp thenpart;
  	public Exp elsepart;

  	public IfExp( int pos, Exp test, Exp thenpart, Exp elsepart ) {
    	this.pos = pos;
    	this.test = test;
    	this.thenpart = thenpart;
    	this.elsepart = elsepart;
  	}
}