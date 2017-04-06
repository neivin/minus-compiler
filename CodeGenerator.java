import absyn.*;
import symbol.*;
import java.io.*;
import java.util.ArrayList;

public class CodeGenerator {
  public static final int IN_ADDR = 4;
  public static final int OUT_ADDR = 7;

  public static final int GP = 6;
  public static final int AC = 0;
  public static final int PC = 7;
  public static final int FP = 5;
  public int emitLoc = 0;
  public int highEmitLoc = 0;
  public int globalOffset = 0;
  public String filename;

  private DecList program;
  private SymbolTable symTable;

  public CodeGenerator(DecList program, String filename){
    this.program = program;
    this.filename = filename + CM.EXT_TM;
    symTable = new SymbolTable(false, "");
  }

  public void generate(){
    genCode(this.program);
  }

  public void genCode(DecList tree)
  {
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
    FunctionSymbol a = (FunctionSymbol)symTable.getFunction("main");
    /* Generate Finale */
    emitRM("ST", FP, globalOffset, FP, "Push old frame pointer");
    emitRM("LDA", FP, globalOffset, FP, "Push frame");
    emitRM("LDA", 0, 1, PC, "Load ac with ret ptr");
    /* retrieve main function address from symbol table */
    emitRMAbs("LDA", PC, a.address, "Jump to main");
    emitRM("LD", FP, 0, FP, "Pop frame");
    emitOp("HALT", 0, 0, 0, "");

    // Exit global scope
    symTable.exitScope();

  }

  /* ==== List Structures ====*/
  // Variable Declaration List
  public int cGen (VarDecList tree, int offset, boolean isParameter){
    while (tree != null){
      if (tree.head != null){
        offset = cGen(tree.head, offset, isParameter);
      }
      tree = tree.tail;
    }
    return offset;
  }

  // Expression List
  public void cGen (ExpList tree, int offset){
    while (tree != null){
      if (tree.head != null){
        cGen(tree.head, offset, false);
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
  public void cGen (Dec tree){
    if(tree instanceof FunctionDec){
      cGen((FunctionDec) tree);
    }
    else if (tree instanceof VarDec){
      VarDec var = (VarDec)tree;
      if(var instanceof SimpleDec){
        SimpleDec sVar = (SimpleDec)var;
        VarSymbol s = new VarSymbol(Type.INT, sVar.name, globalOffset);
        symTable.addSymbol(sVar.name, s);
        emitComment("Allocating global var: " + sVar.name);
        emitComment("<- vardecl");
        globalOffset--;
      }
      else if(var instanceof ArrayDec){
        ArrayDec aVar = (ArrayDec)var;
        ArraySymbol s = new ArraySymbol(Type.INT, aVar.name, aVar.size.value, globalOffset-(aVar.size.value-1));
        symTable.addSymbol(aVar.name, s);
        emitComment("Allocating global var: " + aVar.name);
        emitComment("<- vardecl");
        globalOffset = globalOffset - aVar.size.value;
      }
    }
  }


// Variable/Parameter Declaration
  public int cGen(VarDec tree, int offset, boolean isParameter){
    if(isParameter == true){
      if (tree instanceof SimpleDec){
      SimpleDec d = (SimpleDec)tree;
      VarSymbol s = new VarSymbol(Type.INT, d.name, offset);
      offset--;
      symTable.addSymbol(d.name, s);

      }
      else if (tree instanceof ArrayDec){
        ArrayDec d = (ArrayDec)tree;
        offset = offset - (d.size.value+1);
        ArraySymbol s = new ArraySymbol(Type.INT, d.name, d.size.value, offset--);
        symTable.addSymbol(d.name, s);
      }
    }
    else{
      if (tree instanceof SimpleDec){
      SimpleDec d = (SimpleDec)tree;
      VarSymbol s = new VarSymbol(Type.INT, d.name, offset);
      offset--;
      symTable.addSymbol(d.name, s);
      emitComment("processing local var: " + d.name);
      }
      else if (tree instanceof ArrayDec){
        ArrayDec d = (ArrayDec)tree;
        offset = offset - (d.size.value-1);
        ArraySymbol s = new ArraySymbol(Type.INT, d.name, d.size.value, offset);
        offset--;
        symTable.addSymbol(d.name, s);
        emitComment("processing local var: " + d.name);
      }
    }
    return offset;
  }

// General Expression
  public int cGen (Exp tree, int offset, boolean isAddress){
    if (tree instanceof NilExp){
      //cGen((NilExp) tree);
    }
    else if (tree instanceof VarExp){
      cGen((VarExp) tree, offset, isAddress);
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
      cGen((ReturnExp) tree, offset);
    }
    else if (tree instanceof CompoundExp){
      offset = cGen((CompoundExp) tree, offset);
    }
    else if (tree instanceof IntExp){
      cGen((IntExp) tree);
    }
    return offset;
  }

  /* ==== Concrete Classes ==== */
// Var subclasses
// SimpleVar
  public void cGen(SimpleVar tree, int offset, boolean isAddress){
    SimpleVar e = (SimpleVar)tree;
    VarSymbol v = (VarSymbol) symTable.getSymbol(e.name);
    emitComment("-> id");
    emitComment("looking up id: " + e.name);
    if(symTable.symbolExists(e.name) == 0){
      if(isAddress == true)
        emitRM("LDA", 0, v.offset, GP, "load id address");
      else
        emitRM("LD", 0, v.offset, GP, "load id value");
    }
    else{
      if(isAddress == true)
      emitRM("LDA", 0, v.offset, FP, "load id address");
      else
        emitRM("LD", 0, v.offset, FP, "load id value");
    }
    emitComment("<- id");
  }

// IndexVar
  public void cGen(IndexVar tree, int offset, boolean isAddress){
    IndexVar e = (IndexVar)tree;
    ArraySymbol v = (ArraySymbol)symTable.getSymbol(e.name);
    emitComment("-> subs");
    if(symTable.symbolExists(e.name) == 0){
      emitRM("LD", AC, v.offset, GP, "load id value");
      emitRM("ST", AC, offset--, GP, "store array addr");
      cGen(e.index, offset, false);
      emitComment("<- subs");
    }
    else{
      emitRM("LD", AC, v.offset, FP, "load id value");
      emitRM("ST", AC, offset--, FP, "store array addr");
      cGen(e.index, offset, false);
      emitComment("<- subs");
    }
  }

// Dec subclasses : FunctionDec, VarDec (SimpleDec, ArrayDec)
//FunctionDec
    public void cGen(FunctionDec tree){
    int offset = -2;
    emitComment("-> fundecl");
    emitComment(" processing function: " + tree.func);
    emitComment(" jump around functions body here");


    int savedLoc = emitSkip(1);
    FunctionSymbol fun = new FunctionSymbol(Type.INT, tree.func, null, emitLoc);
    symTable.addSymbol(tree.func, fun);
    symTable.enterNewScope();
    emitRM("ST", 0, -1, FP, "store return");
    offset = cGen(tree.params, offset,  true); //VarDecList
    offset = cGen(tree.body, offset, false); //CompoundExp
    int savedLoc2 = emitSkip(0);
    emitBackup(savedLoc);
    emitRMAbs("LDA", PC, savedLoc2, "Jump around function body");
    emitRestore();
    emitComment("<- fundecl");
    symTable.exitScope();
  }

// SimpleDec
  /*public int cGen(SimpleDec tree, int offset){
    emitComment("processing local var: " + tree.name);
    symTable.getSymbol(tree.name).offset = offset;
    return offset--;
  }

// ArrayDec
  public int cGen(ArrayDec tree, int offset){
    emitComment("Processing local var: " + tree.name);
    symTable.getSymbol(tree.name).offset = offset-(tree.size-1);
    return offset-=tree.size;
  }

// Exp subclasses
// NilExp
  public void cGen(NilExp tree){

  }

*/// VarExp
  public void cGen(VarExp tree, int offset, boolean isAddress){
    if(tree.variable instanceof SimpleVar){
      SimpleVar e = (SimpleVar)tree.variable;
      VarSymbol v = (VarSymbol) symTable.getSymbol(e.name);
      emitComment("-> id");
      emitComment("looking up id: " + e.name);
      if(symTable.symbolExists(e.name) == 0){
        if(isAddress == true){
          emitRM("LDA", 0, v.offset, GP, "load id address");
        }
        else{
          emitRM("LD", 0, v.offset, GP, "load id value");
        }
      }
      else{
        if(isAddress == true)
          emitRM("LDA", 0, v.offset, FP, "load id address");
        else
          emitRM("LD", 0, v.offset, FP, "load id value");
      }
      emitComment("<- id");
    }
    else if (tree.variable instanceof IndexVar){
      IndexVar e = (IndexVar)tree.variable;
      ArraySymbol v = (ArraySymbol)symTable.getSymbol(e.name);
      emitComment("-> subs");
      if(symTable.symbolExists(e.name) == 0){
        emitRM("LD", AC, v.offset, GP, "load id value");
        emitRM("ST", AC, offset--, GP, "store array addr");
        cGen(e.index, offset, false);
        emitComment("<- subs");
      }
      else{
        emitRM("LD", AC, v.offset, FP, "load id value");
        emitRM("ST", AC, offset--, FP, "store array addr");
        cGen(e.index, offset, false);
        emitComment("<- subs");
      }
    }
  }

// IntExp
  public void cGen(IntExp tree){
    emitComment("-> constant");
    emitRM("LDC", AC, tree.value, 0, "load const");
    emitComment("<- constant");
  }

// CallExp
  public void cGen(CallExp tree, int offset){
  // Args
    int i = -2;
    FunctionSymbol f = (FunctionSymbol)symTable.getFunction(tree.func);
    emitComment("-> call");
    emitComment("call of function: " + tree.func);
    //cGen(tree.args, offset, true);

    while (tree.args != null){
      if (tree.args.head != null){
        cGen(tree.args.head, offset, false);
        /*if(tree.args.head instanceof VarExp){
          VarExp e = (VarExp)tree.args.head;
          if(e.variable instanceof SimpleVar){
            SimpleVar s = (SimpleVar)e.variable;
            VarSymbol v = (VarSymbol)symTable.getSymbol(s.name);
            emitRM("ST", AC, (offset+i), FP, "op: push left");
          }
        }
        else if(tree.args.head instanceof CallExp){
          emitRM("ST", AC, offset+i, FP, "op: push left");
        }*/
        emitRM("ST", AC, offset+i, FP, "op: push left");
        i--;
      }
      tree.args = tree.args.tail;
    }

    emitRM("ST", FP, offset, FP, "push ofp");
    emitRM("LDA", FP, offset, FP, "Push frame");
    emitRM("LDA", 0, 1, PC, "Load ac with ret ptr");
    emitRMAbs("LDA", PC, f.address, "jump to fun loc");
    emitRM("LD", FP, 0, FP, "Pop frame");
    emitComment("<- call");
  }

// OpExp
  public void cGen(OpExp tree, int offset){
    emitComment("-> op");
    if(tree.left instanceof IntExp){
      cGen(tree.left, offset, false);
      emitRM("ST", AC, offset--, FP, "op: push left");
    }
    else if(tree.left instanceof VarExp){
      VarExp e = (VarExp)tree.left;

      if(e.variable instanceof SimpleVar){
          cGen(e, offset, false);
          emitRM("ST", AC, offset--, FP, "op: push left");
      }
      else{
        /* Note: the boolean value for Index expression does nothing */
        cGen(e, offset--, true);
      }
    }
    else if(tree.left instanceof CallExp){
      cGen(tree.left, offset, false);
    }
    else if(tree.left instanceof OpExp){
      cGen(tree.left, offset, false);
      emitRM("ST", AC, offset--, FP, "");
    }

    if(tree.right instanceof IntExp){
      cGen(tree.right, offset, false);

    }
    else if(tree.right instanceof VarExp){
      VarExp e = (VarExp)tree.right;

      if(e.variable instanceof SimpleVar){
        cGen(e, offset, false);
      }
      else{
        /* Note: the boolean value for Index expression does nothing */
        cGen(e, offset, true);
      }
    }
    else if(tree.right instanceof CallExp){
      cGen(tree.right, offset, false);
    }
    else if(tree.right instanceof OpExp){
      cGen(tree.right, offset, false);
    }

    emitRM("LD", 1, ++offset, FP, "op: load left");

  // Operator
    switch(tree.op){
      case OpExp.PLUS:
        emitOp("ADD", AC, 1, AC, "op +");
        break;
      case OpExp.MINUS:
        emitOp("SUB", AC, 1, AC, "op -");
        break;
      case OpExp.MUL:
        emitOp("MUL", AC, 1, AC, "op *");
        break;
      case OpExp.DIV:
        emitOp("DIV", AC, 1, AC, "op /");
        break;
      case OpExp.EQ:
        emitOp("EQU", AC, 1, AC, "op =");
        break;
      case OpExp.EQUALEQUAL:
        emitOp("SUB", AC, 1, AC, "op ==");
        emitRM("JEQ", AC, 2, PC, "");
        emitRM("LDC", AC, 0, 0, "false case");
        emitRM("LDA", PC, 1, PC, "unconditional jump");
        emitRM("LDC", AC, 1, 0, "true case");
        break;
      case OpExp.NE:
        emitOp("SUB", AC, 1, AC, "op !=");
        emitRM("JNE", AC, 2, PC, "");
        emitRM("LDC", AC, 0, 0, "false case");
        emitRM("LDA", PC, 1, PC, "unconditional jump");
        emitRM("LDC", AC, 1, 0, "true case");
        break;
      case OpExp.LT:
        emitOp("SUB", AC, 1, AC, "op <");
        emitRM("JLT", AC, 2, PC, "");
        emitRM("LDC", AC, 0, 0, "false case");
        emitRM("LDA", PC, 1, PC, "unconditional jump");
        emitRM("LDC", AC, 1, 0, "true case");
        break;
      case OpExp.GT:
        emitOp("SUB", AC, 1, AC, "op >");
        emitRM("JGT", AC, 2, PC, "");
        emitRM("LDC", AC, 0, 0, "false case");
        emitRM("LDA", PC, 1, PC, "unconditional jump");
        emitRM("LDC", AC, 1, 0, "true case");
        break;
      case OpExp.LE:
        emitOp("SUB", AC, 1, AC, "op <=");
        emitRM("JLE", AC, 2, PC, "");
        emitRM("LDC", AC, 0, 0, "false case");
        emitRM("LDA", PC, 1, PC, "unconditional jump");
        emitRM("LDC", AC, 1, 0, "true case");
        break;
      case OpExp.GE:
        emitOp("SUB", AC, 1, AC, "op >=");
        emitRM("JGE", AC, 2, PC, "");
        emitRM("LDC", AC, 0, 0, "false case");
        emitRM("LDA", PC, 1, PC, "unconditional jump");
        emitRM("LDC", AC, 1, 0, "true case");
        break;
    }
    emitComment("<- op");
  }

// AssignExp
  public void cGen(AssignExp tree, int offset){
      emitComment("-> op");
    // Standard variable on left hand side
    if(tree.lhs instanceof SimpleVar){
      cGen((SimpleVar)tree.lhs, offset, true);
      emitRM("ST", AC, offset--, FP, "op: push left");
    }
    // Array on left hand side
    else if(tree.lhs instanceof IndexVar){
      cGen((IndexVar)tree.lhs, offset--, false);
    }

    if(tree.rhs instanceof IntExp){
      cGen(tree.rhs, offset, false);
    }
    else if(tree.rhs instanceof VarExp){
      cGen(tree.rhs, offset, false);
    }
    else if(tree.rhs instanceof CallExp){
      cGen(tree.rhs, offset, false);
    }
    else if(tree.rhs instanceof OpExp){
      cGen(tree.rhs, offset, false);
    }

    emitRM("LD", 1, ++offset, FP, "op: load left");

    /* store value in r0 in left operand (mem address stored in r1) */
    emitRM("ST", AC, 0, 1, "assign: store value");
    emitComment("<- op");
  }

  // IfExp
  // Expression should be printed on the same line
  public void cGen(IfExp tree, int offset){
    symTable.enterNewScope();
    emitComment("-> if");
    cGen(tree.test, offset, false); // Test Exp
    int savedLoc = emitSkip(1);
    //emitRM("LDA", PC, 1, PC, "if: true part");
    cGen(tree.thenpart, offset, false); // Then Exp
    int savedLoc2 = emitSkip(0);
    emitBackup(savedLoc);
    emitRMAbs("JEQ", 0, savedLoc2, "if: jump to else part");
    emitRestore();
    cGen(tree.elsepart, offset, false); // Else Exp (NilExp)
    emitComment("<- if");
    symTable.exitScope();
  }

  // WhileExp
  // Expression should be printed on the same line
  public void cGen(WhileExp tree, int offset){
    symTable.enterNewScope();
    emitComment("-> While");
    emitComment("While: jump after body comes back here");
    int savedLoc3 = emitSkip(0);
    cGen(tree.test, offset, false); // While condition Exp
    int savedLoc = emitSkip(1);
    cGen(tree.body, offset, false); // Loop body Exp
    int savedLoc2 = emitSkip(0);
    emitBackup(savedLoc);
    emitRMAbs("LDA", PC, savedLoc3, "While: absolute jmp to test");
    emitRMAbs("JEQ", 0, savedLoc2, "While: jmp to end");
    emitRestore();
    emitComment("<- While");
    symTable.exitScope();
  }

  // ReturnExp
  public void cGen(ReturnExp tree, int offset){
    emitComment("-> return");
    cGen(tree.exp, offset, false);
    emitRM("LD", PC, -1, FP, "return to caller");
    emitComment("<- return");
  }

  // CompoundExp
  public int cGen(CompoundExp tree, int offset){
    emitComment("-> compound statement");
    offset = cGen(tree.decs, offset, false); // VarDecList
    cGen(tree.exps, offset); //ExpList
    emitRM("LD", PC, -1, FP, "return caller");
    emitComment("<- compound statement");
    return offset;
  }

  /* ==== Code Emiting Routines ==== */
  /* Returns current label value for backpatching then increases the label value*/
  public int emitSkip(int distance){
    int i = emitLoc;
    emitLoc += distance;
    if(highEmitLoc < emitLoc){
      highEmitLoc = emitLoc;
    }
    return i;
  }

  public void emitBackup(int loc){
    if(loc > highEmitLoc){
      emitComment("Bug in emmitBackup");
    }
    emitLoc = loc;
  }

  public void emitRestore(){
    emitLoc = highEmitLoc;
  }

  public void emitRM(String op, int r, int offset, int r1, String comment){
    String code = emitLoc + ":  " + op + "  " + r + "," + offset + "(" + r1 + ")";
    writeCode(code);
    ++emitLoc;
    writeCode("\t" + comment);
    writeCode("\n");
    if(highEmitLoc < emitLoc){
      highEmitLoc = emitLoc;
    }
  }

  public void emitRMAbs(String op, int r, int a, String comment){
    String code = emitLoc + ":  " + op + "  " + r + "," + (a-(emitLoc+1)) + "(" + PC + ")";
    writeCode(code);
    ++emitLoc;
    writeCode("\t" + comment);
    writeCode("\n");
    if(highEmitLoc < emitLoc){
      highEmitLoc = emitLoc;
    }
  }

  public void emitOp(String op, int destination, int r, int r1, String comment){
    String code = emitLoc + ":  " + op + " " + destination + "," + r + "," + r1;
    writeCode(code);
    ++emitLoc;
    writeCode("\t" + comment);
    writeCode("\n");
  }

  public void emitComment(String comment){
    comment = "* " + comment + "\n";
    writeCode(comment);
  }

  public void writeCode(String content){
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
