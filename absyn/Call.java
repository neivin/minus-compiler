package absyn;

public class Call extends Absyn{
  public String id;
  public ArgList arglist;

  /* Arguments empty */
  public Call (int pos, String id){
    this.pos = pos;
    this.id = id;
    this.arglist = null;
  }

  public Call (int pos, String id, ArgList arglist){
    this.pos = pos;
    this.id = id;
    this.arglist = arglist;
  }

}
