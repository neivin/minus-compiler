package absyn;

public class CallExp extends Exp{
  public String func;
  public ExpList args;

  public CallExp (int pos, String func, ExpList args){
    this.pos = pos;
    this.func = func;
    this.args = args;
  }

  public int argsCount(){
  	int count = 0;
  	ExpList argList = this.args;

  	while(argList != null){
  		argList = argList.tail;
  		count++;
  	}

  	return count;
  }
  
}
