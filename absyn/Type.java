package absyn;

public class Type extends Absyn{
    public final static int INT  = 0;
    public final static int VOID = 1;
 
    public int type;
 
    public Type(int pos, int type){
        this.pos = pos;
        this.type = type;
    }
	
}