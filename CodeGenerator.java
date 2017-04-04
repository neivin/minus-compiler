import absyn.*;


public class CodeGenerator {
  public static final int IN_ADDR = 4;
  public static final int OUT_ADDR = 7;

  public static final int GP = 6;
  public static final int AC = 0;
  public static final int PC = 7;
  public static final int FP = 5;
  public static int emitLoc = 0;
  public static int highEmitLoc = 0;

  private DecList program;
  private SymbolTable symTable;

  private String filename;

  public CodeGenerator(DecList program, String filename){
    this.program = program;
    this.filename = filename + CM.EXT_TM;
    symTable = new SymbolTable(false);
  }

  public void generate(){
    genCode(this.program);
  }

  public static void genCode(DecList tree)
  {
    int globalOffset = 0;
    
    // Clear file contents
    try{
      PrintWriter pw = new PrintWriter(this.filename);
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
    emitRMAbs("LDA", FP, 0, GP, "Copy GP to FP");
    emitRM("ST", 0, 0, 0, "Clear location 0");
    int savedLoc = emitSkip(1);
    
    /* Generate input function */
    emitComment("Jump around i/o functions");
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
    Jujpm around I/O functions*/
    emitBackup(savedLoc);
    emitRMAbs("LDA", PC, savedLoc2, "");
    emitRestore();

    /* Recursive code generation */
    while (tree != null){
			if (tree.head != null){
				cGen(tree.head, globalOffset);
			}
			tree = tree.tail;
		}
    /* Generate Finale */
    emitRM("ST", FP, globalOffset, FP, "Push ofp");
    emitRM("LDA", FP, globalOffset, FP, "Push frame");
    emitRM("LDA", 0, 1, PC, "Load ac with ret ptr");
    /* retrieve main function address from symbol table */
    emitRMAbs("LDA", PC, funAddr, "Jump to main");
    emitRM("LD", FP, 0, FP, "Pop frame");
    emitOp("HALT", 0, 0, 0, "");

    // Exit global scope
    symTable.exitScope();

  }

  /* ==== List Structures ====*/
  // Variable Declaration List
  public static void cGen (VarDecList tree){
    while (tree != null){
      if (tree.head != null){
        cGen(tree.head);
      }
      tree = tree.tail;
    }
  }

  // Expression List
  public static void cGen (ExpList tree){
    while (tree != null){
      if (tree.head != null){
        cGen(tree.head);
      }
      tree = tree.tail;
    }
  }

  /* ==== Abstract Classes ==== */
// Variable
  public static void cGen (Var tree){
    if (tree instanceof SimpleVar){
      cGen((SimpleVar) tree);
    }
    else if (tree instanceof IndexVar){
      cGen((IndexVar) tree);
    }
  }

// Declaration
  public static void cGen (Dec tree){
    if(tree instanceof FunctionDec){
      cGen((FunctionDec) tree);
    }
    else if (tree instanceof VarDec){
      cGen((VarDec) tree);
    }
  }


// Variable Declaration
  public static void cGen(VarDec tree){
    if (tree instanceof SimpleDec){
      cGen((SimpleDec) tree);
    }
    else if (tree instanceof ArrayDec){
      cGen((ArrayDec) tree);
    }
  }

// General Expression
  public static void cGen (Exp tree){
    if (tree instanceof NilExp){
      cGen((NilExp) tree);
    }
    else if (tree instanceof VarExp){
      cGen((VarExp) tree);
    }
    else if (tree instanceof CallExp){
      cGen((CallExp) tree);
    }
    else if (tree instanceof OpExp){
      cGen((OpExp) tree);
    }
    else if (tree instanceof AssignExp){
      cGen((AssignExp) tree);
    }
    else if (tree instanceof IfExp){
      cGen((IfExp) tree);
    }
    else if (tree instanceof WhileExp){
      cGen((WhileExp) tree);
    }
    else if (tree instanceof ReturnExp){
      cGen((ReturnExp) tree);
    }
    else if (tree instanceof CompoundExp){
      cGen((CompoundExp) tree);
    }
    else if (tree instanceof IntExp){
      cGen((IntExp) tree);
    }
  }

  /* ==== Concrete Classes ==== */
// Var subclasses
// SimpleVar
  public static void cGen(SimpleVar tree){
  }

// IndexVar
  public static void cGen(IndexVar tree){
  }

// Dec subclasses : FunctionDec, VarDec (SimpleDec, ArrayDec)
//FunctionDec
  public static void cGen(FunctionDec tree){
    emitComment("-> fundecl");
    emitComment(" processing function: " + tree.func);
    emitComment(" jump around functions body here");

    /* Store value for backpatching */
    int savedLoc = emitSkip(1);
    /* Copy return address from AC to local memory */
    emitRM("ST", 0, -1, FP, "store return");

    cGen(tree.params);
    cGen(tree.body);

    /* Generate jump around function body */
    int savedLoc2 = emitSkip(0);
    emitBackup(savedLoc);
    emitRMAbs("LDA", PC, savedLoc2, "Jump around function body");
    emitComment("<- fundecl");
  }

// SimpleDec
  public static void cGen(SimpleDec tree){
    emitComment("processing local var: " + tree.name);
  }

// ArrayDec
  public static void cGen(ArrayDec tree){
    emitComment("Processing local var: " + tree.name);
  }

// Exp subclasses
// NilExp
  public static void cGen(NilExp tree){

  }

// VarExp
  public static void cGen(VarExp tree){
  }

// IntExp
  public static void cGen(IntExp tree){
  // Check if null for function declarations/calls of the form int x[]
    if (tree != null){

    }
  }

// CallExp
  public static void cGen(CallExp tree){
  // Args
    emitComment("-> call");
    emitComment("call of function: " + tree.fun);
    cGen(tree.args);
    emitRM("ST", FP, /* Some offset */, FP, "push ofp");
    emitRM("LDA", FP, /* Some offset */, FP, "Push frame");
    emitRM("LDA", 0, 1, PC, "Load ac with ret ptr");
    emitRMAbs("LDA", PC, funAddr, "jump to fun loc")
    emitMR("LD", FP, 0, FP, "Pop frame");
    emitComment("<- call");
  }

// OpExp
  public static void cGen(OpExp tree){
    emitComment("-> op");
    cGen(tree.left);
    cGen(tree.right);
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
  public static void cGen(AssignExp tree){

  // Var = left
    cGen(tree.lhs);

  // Exp = right;
    cGen(tree.rhs);
  }

  // IfExp
  // Expression should be printed on the same line
  public static void cGen(IfExp tree){
    emitComment("-> if");
    cGen(tree.test); // Test Exp
    cGen(tree.thenpart); // Then Exp
    cGen(tree.elsepart); // Else Exp (NilExp)
    emitComment("<- if");
  }

  // WhileExp
  // Expression should be printed on the same line
  public static void cGen(WhileExp tree){
    emitComment("-> While");
    emitCOmment("While: jump after body comes back here");
    int savedLoc3 = emitSkip(1);
    cGen(tree.test); // While condition Exp
    int savedLoc = emitSkip(1);
    cGen(tree.body); // Loop body Exp
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

  // CompoundExp
  public static void cGen(CompoundExp tree){
    emitComment("-> compound statement");
    cGen(tree.decs); // VarDecList
    cGen(tree.exps); //ExpList
    emitComment("<- compound statement");
  }

  /* ==== Code Emiting Routines ==== */
  /* Returns current label value for backpatching then increases the label value*/
  int emitSkip(int distance){
    int i = emitLoc;
    emitLoc += distance;
    if(highEmitLoc < emitLoc){
      highEmitLoc = emitLoc;
    }
    return i;
  }

  void emitBackup(int loc){
    if(loc > highEmitLoc){
      emitComment("Bug in emmitBackup");
    }
    emitLoc = loc;
  }

  void emitRestore(){
    emitLoc = highEmitLoc;
  }

  void emitRM(String op, int r, int offset, int r1, String comment){
    Sting code = emitLoc + ": " + op + "  " + r + "," + offset + "(" + r1 + ")";
    writeCode(code);
    ++emitLoc;
    /* if(TraceCode)  writeCode("\t" + comment); */
    writeCode("\n");
    if(highEmitLoc < emitLoc){
      highEmitLoc = emitLoc;
    }
  }

  void emitRMAbs(String op, int r, int a, String comment){
    String code = emitLoc + ":  " + op + "  " + r + "," + a-(emitLoc+1) + "(" + PC + ")";
    writeCode(code);
    ++emitLoc;
    /* if (TraceCode) write("\n" + comment); */
    writeCode("\n");
    if(highEmitLoc < emitLoc){
      highEmitLoc = emitLoc;
    }
  }

  void emitOp(String op, int destination, int r, int r1, String comment){
    String code = emitloc + ": " + op + " " + destination + "," + r + "," + r1;
    writeCode(code);
    ++emitLoc;
    /* if (TraceCode) write("\n" + comment); */
    writeCode("\n");
  }

  void emitComment(String comment){
    comment = "* " + comment + "\n";
    writeCode(comment);
  }

  public static void writeCode(String content){
    PrintWriter outputStream = null;
    try{
      outputStream = new PrintWriter(new FileOutputStream(this.filename, true));
    }catch(FileNotFoundException e){
      e.printStackTrace();
    }
    outputStream.printf(content);
    outputStream.close();
  }

}
