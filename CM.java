import java.io.*;
import java.util.*;


public class CM{
  public static final String EXT_AST = ".ast";
  public static final String EXT_SYM = ".sym";
  public static final String EXT_TM = ".tm";

  public static void main(String [] argv){
    
    boolean showAST = false;
    boolean showScopes = false;
    boolean genCode = false;

    String file = "";

    if (argv.length > 4 ){
    	System.err.println("Error: Invalid number of arguments");
    	System.err.println("The correct usage is:");
    	System.err.println("\tjava CM <c-minus-file>.cm");
    	System.exit(0);
    }

    for (String arg : argv){
      System.out.println(arg);
    	if (arg.equals("-a")){
    		showAST = true;
    	}
      else if (arg.equals("-s")){
        showScopes = true;
      }
      else if (arg.equals("-c"))
        genCode = true;
    	else{
    		file = arg;
    	}
    }

    try {
      parser p = new parser(new Lexer(new FileReader(file)));
      
      p.showAST = showAST;
      p.showScopes = showScopes;
      p.genCode = genCode;


      File f = new File(file);
      String name = f.getName();
      int dotIndex=name.lastIndexOf('.');
      if(dotIndex>=0) { // to prevent exception if there is no dot
        name=name.substring(0,dotIndex);
      }

      p.filename = name;

      Object result = p.parse().value;
    } catch (Exception e) {
      /* do cleanup here -- possibly rethrow e */
      e.printStackTrace();
    }
  }
}
