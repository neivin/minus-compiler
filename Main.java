import java.io.*;
import java.util.*;

class Main{
  public static void main(String [] argv){
    
    boolean astMode = false;
    boolean typeMode = false;
    String file = "";

    if (argv.length > 2 ){
    	System.err.println("Error: Invalid number of arguments");
    	System.err.println("The correct usage is:");
    	System.err.println("\tjava Main <c-minus-file>.cm");
    	System.exit(0);
    }

    for (String arg : argv){
    	if (arg.equals("-a")){
    		astMode = true;
    	}
      else if (arg.equals("-s")){
        typeMode = true;
      }
    	else{
    		file = arg;
    	}
    }

    try {
      parser p = new parser(new Lexer(new FileReader(file)));
      p.astMode = astMode;
      p.typeMode = typeMode;
      Object result = p.parse().value;
    } catch (Exception e) {
      /* do cleanup here -- possibly rethrow e */
      e.printStackTrace();
    }
  }
}
