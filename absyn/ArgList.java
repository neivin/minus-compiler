package absyn;

public class ArgList {
  public Exp head;
  public ArgList tail;

  public ArgList (Exp head){
    this.head = head;
    this.tail = null;
  }

  public ArgList(Exp head, ArgList tail){
    this.head = head;
    this.tail = tail;
  }
}
