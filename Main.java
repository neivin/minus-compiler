class Main{
  static public void main(String argv[]){
    try {
      parser p = new parser(new Lexer(new FileReader(argv[0])));
      Object result = p.parse().value;
    } catch (Exception e) {
      /* do cleanup here -- possibly rethrow e */
      e.printStackTrace();
    }
  }
}
