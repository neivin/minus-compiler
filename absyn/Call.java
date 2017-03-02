package absyn;

public class Call extends Absyn{
  public String id;
  public ArgList arglist;

  public Call (int pos, String id, ArgList arglist){
    this.pos = pos;
    this.id = id;
    this.arglist = arglist;
  }

}
