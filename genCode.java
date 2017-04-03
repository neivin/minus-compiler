package gencode;
static final int GP = 6;
static final int AC = 0;
static final int PC = 7;
static final int FP = 5;

public static void genCode()
{
  String content;
  try{
    PrintWriter pw = new PrintWriter(FILENAME);
    pw.close();
  }catch(FileNotFoundException e){
    e.printStackTrace();
  }

  /* Generate standard prelude */
  emitRM("LD", GP, 0, AC, "Load GP with max address");
  emitRMAbs("LDA", FP, 0, GP, "Copy GP to FP");
  emitRM("ST", 0, 0, 0, "Clear location 0");
  int savedLoc = emitSkip(1);
  /* Generate input function */
  emitComment("Jump around i/o functions");
  emitComment("Code for input routine");
  emitRM("ST", 0, -1, FP, "Store return");
  /*("IN", 0, 0, "Input");*/
  emitRM("LD", PC, -1, FP, "Return caller");

  /* Generate output function */
  emitComment("Code for output routine");
  emitRM("ST", 0, -1, FP, "Store return");
  emitRM("LD", 0, -2, FP, "Load output value");
  /*("OUT", 0, 0, "Output");*/
  emitRM("LD", 7, -1, FP, "Return caller");
  int savedLoc2 = emitSkip(0);
  emitBackup(savedLoc);
  emitRMAbs("LDA", PC, savedLoc2, "");
  emitRestore();
  /* Jump around I/O functions */

  /* Recursive code generation */

  /* Generate Finale */
}
/* ==== Abstract Classes ==== */
// Variable
public static void genCode (Var tree){
  if (tree instanceof SimpleVar){
    genCode((SimpleVar) tree);
  }
  else if (tree instanceof IndexVar){
    genCode((IndexVar) tree);
  }
}

// Declaration
public static void genCode (Dec tree){
  if(tree instanceof FunctionDec){
    genCode((FunctionDec) tree);
  }
  else if (tree instanceof VarDec){
    genCode((VarDec) tree);
  }
}


// Variable Declaration
public static void genCode(VarDec tree){
  if (tree instanceof SimpleDec){
    genCode((SimpleDec) tree);
  }
  else if (tree instanceof ArrayDec){
    genCode((ArrayDec) tree);
  }
}

// General Expression
public static void genCode (Exp tree){
  if (tree instanceof NilExp){
    genCode((NilExp) tree);
  }
  else if (tree instanceof VarExp){
    genCode((VarExp) tree);
  }
  else if (tree instanceof CallExp){
    genCode((CallExp) tree);
  }
  else if (tree instanceof OpExp){
    genCode((OpExp) tree);
  }
  else if (tree instanceof AssignExp){
    genCode((AssignExp) tree);
  }
  else if (tree instanceof IfExp){
    genCode((IfExp) tree);
  }
  else if (tree instanceof WhileExp){
    genCode((WhileExp) tree);
  }
  else if (tree instanceof ReturnExp){
    genCode((ReturnExp) tree);
  }
  else if (tree instanceof CompoundExp){
    genCode((CompoundExp) tree);
  }
  else if (tree instanceof IntExp){
    genCode((IntExp) tree);
  }
}

/* ==== Concrete Classes ==== */
// Var subclasses
// SimpleVar
public static void genCode(SimpleVar tree){
}

// IndexVar
public static void genCode(IndexVar tree){
}

// Dec subclasses : FunctionDec, VarDec (SimpleDec, ArrayDec)
//FunctionDec
public static void genCode(FunctionDec tree){

}

// SimpleDec
public static void genCode(SimpleDec tree){

}

// ArrayDec
public static void genCode(ArrayDec tree){

}

// Exp subclasses
// NilExp
public static void genCode(NilExp tree){

}

// VarExp
public static void genCode(VarExp tree){

}

// IntExp
public static void genCode(IntExp tree){
  // Check if null for function declarations/calls of the form int x[]
  if (tree != null){

  }
}

// CallExp
public static void genCode(CallExp tree){

  // Args
  genCode(tree.args);
}

// OpExp
public static void genCode(OpExp tree){

  // Operator
  switch(tree.op){
    case OpExp.PLUS:

      break;
    case OpExp.MINUS:

      break;
    case OpExp.MUL:

      break;
    case OpExp.DIV:

      break;
    case OpExp.EQ:

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
    default:

      break;
  }
}

// AssignExp
public static void genCode(AssignExp tree){

  // Var = left
  genCode(tree.lhs);

  // Exp = right;
  genCode(tree.rhs);
}

// IfExp
// Expression should be printed on the same line
public static void genCode(IfExp tree){

  genCode(tree.test); // Test Exp
  genCode(tree.thenpart); // Then Exp
  genCode(tree.elsepart); // Else Exp (NilExp)
}

// WhileExp
// Expression should be printed on the same line
public static void genCode(WhileExp tree){
  genCode(tree.test); // While condition Exp
  genCode(tree.body); // Loop body Exp
}

// ReturnExp
public static void genCode(ReturnExp tree){

  if(tree.exp != null){
    genCode(tree.exp); // Return Exp
  }
}

// CompoundExp
public static void genCode(CompoundExp tree){

  genCode(tree.decs); // VarDecList
  genCode(tree.exps); //ExpList
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
  ++emitLoc;
  /* if (TraceCode) write("\n" + comment); */
  write("\n");
  if(highEmitLoc < emitLoc){
    highEmitLoc = emitLoc;
  }
}

void emitComment(String comment){
  comment = "* " + comment;
  writeCode(comment);
}

public static void writeCode(String content)
{
  PrintWriter outputStream = null;
  try{
    outputStream = new PrintWriter(new FileOutputStream(FILENAME, true));
  }catch(FileNotFoundException e){
    e.printStackTrace();
  }
  outputStream.printf(content);
  outputStream.close();
}
