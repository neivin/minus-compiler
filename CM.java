import java.io.*;
import java.util.*;


public class CM{
  public static final String EXT_AST = ".ast";
  public static final String EXT_SYM = ".sym";
  public static final String EXT_TM = ".tm";


  public static void displayUsage(){
    System.out.println("Usage: java CM [-a] [-s] [-c] [file.cm]");
    System.out.println("       -a: show abstract syntax tree");
    System.out.println("       -s: show symbol tables and scopes");
    System.out.println("       -c: generate assembly code");
  }

  public static void main(String [] args){
    
    boolean showAST = false;
    boolean showScopes = false;
    boolean genCode = false;

    String file = "";

    if (args.length > 4 || args.length < 1){
      System.err.println("Error: Invalid number of arguments");
    	displayUsage();
    	System.exit(0);
    }

    for (String arg : args){
    	if (arg.equals("-a")){
    		showAST = true;
    	}
      else if (arg.equals("-s")){
        showScopes = true;
      }
      else if (arg.equals("-c"))
        genCode = true;
    	else if (arg.endsWith(".cm")){
    		file = arg;
    	}
      else {
        System.err.println("Error: Invalid argument encountered. See correct usage.");
        displayUsage();
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
