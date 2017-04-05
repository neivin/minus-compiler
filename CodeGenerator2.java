import absyn.*;
import symbol.*;
import java.io.*;
import java.util.ArrayList;

public class CodeGenerator {
  public static final int IN_ADDR = 4;
  public static final int OUT_ADDR = 7;
  public static final String FILENAME = "./test.tm";

  public static final int GP = 6;
  public static final int AC = 0;
  public static final int PC = 7;
  public static final int FP = 5;
  public static int emitLoc = 0;
  public static int highEmitLoc = 0;
  public static int globalOffset = 0;

  private DecList program;
  private static SymbolTable symTable;

  public CodeGenerator(DecList program){
    this.program = program;
    symTable = new SymbolTable(false);
  }

  public void generate(){
    genCode(this.program);
  }

  public static void genCode(DecList tree)
  {
    try{
      PrintWriter pw = new PrintWriter(FILENAME);
      pw.close();
    }catch(FileNotFoundException e){
      e.printStackTrace();
    }

    // Initialize Symbol Table
    // Enter new global scope
    symTable.enterNewScope();

    // Add input function
    Symb inputFunction = new FunctionSymbol(Type.INT, "input", new ArrayList<Symb>(), IN_ADDR);
    symTable.addSymbol("input",  inputFunction);

    // Output function
    ArrayList<Symb> parameters = new ArrayList<Symb>();
    parameters.add(new VarSymbol(Type.INT, "value"));

    Symb outputFunction = new FunctionSymbol(Type.VOID, "output", parameters, OUT_ADDR);
    symTable.addSymbol("output",  outputFunction);


    /* Generate standard prelude */
    emitRM("LD", GP, 0, AC, "Load GP with max address");
    emitRM("LDA", FP, 0, GP, "Copy GP to FP");
    emitRM("ST", 0, 0, 0, "Clear location 0");
    int savedLoc = emitSkip(1);

    /* Generate input function */
    emitComment("Jump around I/O functions here");
    emitComment("Code for input routine");
    emitRM("ST", 0, -1, FP, "Store return");
    emitOp("IN", 0, 0, 0, "");
    emitRM("LD", PC, -1, FP, "Return caller");

    /* Generate output function */
    emitComment("Code for output routine");
    emitRM("ST", 0, -1, FP, "Store return");
    emitRM("LD", 0, -2, FP, "Load output value");
    emitOp("OUT", 0, 0, 0, "");
    emitRM("LD", 7, -1, FP, "Return caller");
    int savedLoc2 = emitSkip(0);

    /* Set emitLoc to previously stored value
    Jump around I/O functions*/
    emitBackup(savedLoc);
    emitRMAbs("LDA", PC, savedLoc2, "Jump around I/O functions");
    emitRestore();
    emitComment("End of standard prelude");
    /* Recursive code generation */
    while (tree != null){
			if (tree.head != null){
				cGen(tree.head);
			}
			tree = tree.tail;
		}
    /* Generate Finale */
    emitRM("ST", FP, globalOffset, FP, "Push old frame pointer");
    emitRM("LDA", FP, globalOffset, FP, "Push frame");
    emitRM("LDA", 0, 1, PC, "Load ac with ret ptr");
    /* retrieve main function address from symbol table */
    //emitRMAbs("LDA", PC, symTable.getFunction("main").address, "Jump to main");
    emitRM("LD", FP, 0, FP, "Pop frame");
    emitOp("HALT", 0, 0, 0, "");

    // Exit global scope
    symTable.exitScope();

  }

  /* ==== List Structures ====*/
  // Variable Declaration List
  /*public static int cGen (VarDecList tree, int offset){
    while (tree != null){
      if (tree.head != null){
        offset = cGen(tree.head);
      }
      tree = tree.tail;
    }
    return offset;
  }

  */// Expression List
  public static void cGen (ExpList tree, offset){
    while (tree != null){
      if (tree.head != null){
        cGen(tree.head, offset);
      }
      tree = tree.tail;
    }
  }

  /* ==== Abstract Classes ==== */
// Variable
  /*public static void cGen (Var tree){
    if (tree instanceof SimpleVar){
      cGen((SimpleVar) tree);
    }
    else if (tree instanceof IndexVar){
      cGen((IndexVar) tree);
    }
  }*/

// Declaration
  public static void cGen (Dec tree){
    if(tree instanceof FunctionDec){
      cGen((FunctionDec) tree);
    }
    else if (tree instanceof VarDec){
      VarDec var = (VarDec)tree;
      if(var instanceof SimpleDec){
        SimpleDec sVar = (SimpleDec)var;
        emitComment("Allocating global var: " + sVar.name);
        emitComment("<- vardecl");
        globalOffset--;
      }
      else if(var instanceof ArrayDec){
        ArrayDec aVar = (ArrayDec)var;
        emitComment("Allocating global var: " + aVar.name);
        emitComment("<- vardecl");
        globalOffset = globalOffset - aVar.size.value;
      }
    }
  }


// Variable Declaration
  /*public static int cGen(VarDec tree, int offset){
    if (tree instanceof SimpleDec){
      offset = cGen((SimpleDec) tree);
      return offset;
    }
    else if (tree instanceof ArrayDec){
      offset = cGen((ArrayDec) tree);
      return offset;
    }
  }

*/// General Expression
  public static void cGen (Exp tree, int offset){
    if (tree instanceof NilExp){
      cGen((NilExp) tree);
    }
    else if (tree instanceof VarExp){
      cGen((VarExp) tree, offset);
    }
    else if (tree instanceof CallExp){
      cGen((CallExp) tree, offset);
    }
    else if (tree instanceof OpExp){
      cGen((OpExp) tree, offset);
    }
    else if (tree instanceof AssignExp){
      cGen((AssignExp) tree, offset);
    }
    else if (tree instanceof IfExp){
      cGen((IfExp) tree, offset);
    }
    else if (tree instanceof WhileExp){
      cGen((WhileExp) tree, offset);
    }
    else if (tree instanceof ReturnExp){
      cGen((ReturnExp) tree);
    }
    else if (tree instanceof CompoundExp){
      cGen((CompoundExp) tree, offset);
    }
    else if (tree instanceof IntExp){
      cGen((IntExp) tree, offset);
    }
  }

  /* ==== Concrete Classes ==== */
// Var subclasses
// SimpleVar
  /*public static int cGen(SimpleVar tree, int offset, boolean isAddress){
    if(isAddress == true){
      emitCommet("-> id");
      emitComment("Looking up id: " + tree.name);
      emitRM("LDA", AC, symTable.getSymbol(tree.name).offset, FP, "Load ID address");
      emitComment("<- id");
      emitRM("ST", AC, offset, FP, "op: push left");
    }
    else{
      emitComment("-> id");
      emitComment("Looking up id: " + tree.name);
      emitRM("LD", AC, symTable.getSymbol(tree.name).offset, FP, "Load IF value");
      emmitComment("<- id");
      emitRM("ST", 0, offset)

    }
    //return offset--;
  }

// IndexVar
  public static void cGen(IndexVar tree){
  }

// Dec subclasses : FunctionDec, VarDec (SimpleDec, ArrayDec)
//FunctionDec
  */public static void cGen(FunctionDec tree){
    int offset = -2;
    FunctionSymbol fun = (FunctionSymbol)symTable.getFunction(tree.func);
    emitComment("-> fundecl");
    emitComment(" processing function: " + tree.func);
    emitComment(" jump around functions body here");


    int savedLoc = emitSkip(1);
    emitRM("ST", 0, -1, FP, "store return");

    //offset -= fun.paramCount();
    cGen(tree.body/*, offset*/);
    int savedLoc2 = emitSkip(0);
    emitBackup(savedLoc);
    emitRMAbs("LDA", PC, savedLoc2, "Jump around function body");
    emitRestore();
    emitComment("<- fundecl");
  }

// SimpleDec
  /*public static int cGen(SimpleDec tree, int offset){
    emitComment("processing local var: " + tree.name);
    symTable.getSymbol(tree.name).offset = offset;
    return offset--;
  }

// ArrayDec
  public static int cGen(ArrayDec tree, int offset){
    emitComment("Processing local var: " + tree.name);
    symTable.getSymbol(tree.name).offset = offset-(tree.size-1);
    return offset-=tree.size;
  }

// Exp subclasses
// NilExp
  public static void cGen(NilExp tree){

  }

// VarExp
  public static void cGen(VarExp tree, int offset){
    //cGen(tree.variable, offset, false);
  }

// IntExp
  public static void cGen(IntExp tree){
  // Check if null for function declarations/calls of the form int x[]
    if (tree != null){

    }
  }

// CallExp
  public static void cGen(CallExp tree, int offset){
  // Args
    emitComment("-> call");
    emitComment("call of function: " + tree.fun);
    cGen(tree.args, offset);
    emitRM("ST", FP, offset, FP, "push ofp");
    emitRM("LDA", FP, offset, FP, "Push frame");
    emitRM("LDA", 0, 1, PC, "Load ac with ret ptr");
    emitRMAbs("LDA", PC, symTable.getFunction(tree.fun).address, "jump to fun loc")
    emitMR("LD", FP, 0, FP, "Pop frame");
    emitComment("<- call");
  }

// OpExp
  public static void cGen(OpExp tree, int offset){
    emitComment("-> op");
    cGen(tree.left, offset);
    cGen(tree.right, offset);
  // Operator
    switch(tree.op){
      case OpExp.PLUS:
      emitOp("ADD", AC, 1, AC, "");
      break;
      case OpExp.MINUS:
      emitOp("SUB", AC, 1, AC, "");
      break;
      case OpExp.MUL:
      emitOp("MUL", AC, 1, AC, "");
      break;
      case OpExp.DIV:
      emitOp("DIV", AC, 1, AC, "");
      break;
      case OpExp.EQ:
      emitOp("=", AC, 1, AC, "");
      break;
      case OpExp.EQUALEQUAL:

      break;
      case OpExp.NE:

      break;
      case OpExp.LT:

      break;
      case OpExp.GT:

      break;
      case OpExp.LE:

      break;
      case OpExp.GE:

      break;
    }
    emitComment("<- op");
  }

// AssignExp
  public static void cGen(AssignExp tree, int offset){


    offset = cGen(tree.lhs, offset, true);

    offset = cGen(tree.rhs, offset, false);
    return offset;
  }

  // IfExp
  // Expression should be printed on the same line
  public static void cGen(IfExp tree, int offset){
    emitComment("-> if");
    cGen(tree.test); // Test Exp
    cGen(tree.thenpart, offset); // Then Exp
    cGen(tree.elsepart, offset); // Else Exp (NilExp)
    emitComment("<- if");
  }

  // WhileExp
  // Expression should be printed on the same line
  public static void cGen(WhileExp tree, int offset){
    emitComment("-> While");
    emitCOmment("While: jump after body comes back here");
    int savedLoc3 = emitSkip(1);
    cGen(tree.test); // While condition Exp
    int savedLoc = emitSkip(1);
    cGen(tree.body, offset); // Loop body Exp
    int savedLoc2 = emitSkip(0);
    emitBackup(savedLoc);
    emitRMAbs("LDA", PC, savedLoc3, "While: absolute jmp to test");
    emitRMAbs("JEQ", 0, savedLoc2, "While: jmp to end");
    emitComment("<- While");
  }

  // ReturnExp
  public static void cGen(ReturnExp tree){

    if(tree.exp != null){
      cGen(tree.exp); // Return Exp
    }
  }

  */// CompoundExp
  public static void cGen(CompoundExp tree, int offset){
    emitComment("-> compound statement");
    //offset = cGen(tree.decs, offset); // VarDecList
    cGen(tree.exps, offset); //ExpList
    emitComment("<- compound statement");
  }

  /* ==== Code Emiting Routines ==== */
  /* Returns current label value for backpatching then increases the label value*/
  public static int emitSkip(int distance){
    int i = emitLoc;
    emitLoc += distance;
    if(highEmitLoc < emitLoc){
      highEmitLoc = emitLoc;
    }
    return i;
  }

  public static void emitBackup(int loc){
    if(loc > highEmitLoc){
      emitComment("Bug in emmitBackup");
    }
    emitLoc = loc;
  }

  public static void emitRestore(){
    emitLoc = highEmitLoc;
  }

  public static void emitRM(String op, int r, int offset, int r1, String comment){
    String code = emitLoc + ":  " + op + "  " + r + "," + offset + "(" + r1 + ")";
    writeCode(code);
    ++emitLoc;
    writeCode("\t" + comment);
    writeCode("\n");
    if(highEmitLoc < emitLoc){
      highEmitLoc = emitLoc;
    }
  }

  public static void emitRMAbs(String op, int r, int a, String comment){
    String code = emitLoc + ":  " + op + "  " + r + "," + (a-(emitLoc+1)) + "(" + PC + ")";
    writeCode(code);
    ++emitLoc;
    writeCode("\t" + comment);
    writeCode("\n");
    if(highEmitLoc < emitLoc){
      highEmitLoc = emitLoc;
    }
  }

  public static void emitOp(String op, int destination, int r, int r1, String comment){
    String code = emitLoc + ":  " + op + " " + destination + "," + r + "," + r1;
    writeCode(code);
    ++emitLoc;
    writeCode("\t" + comment);
    writeCode("\n");
  }

  public static void emitComment(String comment){
    comment = "* " + comment + "\n";
    writeCode(comment);
  }

  public static void writeCode(String content){
    PrintWriter outputStream = null;
    try{
      outputStream = new PrintWriter(new FileOutputStream(FILENAME, true));
    }catch(FileNotFoundException e){
      e.printStackTrace();
    }
    outputStream.printf(content);
    outputStream.close();
  }

}
