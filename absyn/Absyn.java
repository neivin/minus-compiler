package absyn;

import java.io.PrintWriter;
import java.io.FileNotFoundException;

abstract public class Absyn {
	public int pos;
	public final static int SPACES = 4;
	public static String outFileName;

	private static PrintWriter writer;

	private static void indent (int spaces) {
		for(int i =0; i < spaces; i++)
			writer.print(" ");
	}

	/* ==== List Structures ==== */

	// Declaration List
	public static void showTree (DecList tree, int spaces, String filename){
		// Set output file name
		outFileName = filename + ".ast"; 

		// Setup output stream
		try{
			writer = new PrintWriter(outFileName);
			writer.println("---------- " + outFileName + ".cm Abstract Syntax Tree ----------");
			writer.println();
		}
		catch (FileNotFoundException e){
			System.err.println("Error: Could not create output file.");
			e.printStackTrace();
		}


		while (tree != null){
			if (tree.head != null){
				showTree(tree.head, spaces);
			}
			tree = tree.tail;
		}

		// Close writer
		writer.close();
	}

	// Variable Declaration List
	public static void showTree (VarDecList tree, int spaces){
		while (tree != null){
			if (tree.head != null){
				showTree(tree.head, spaces);
			}
			tree = tree.tail;
		}
	}

	// Expression List
	public static void showTree (ExpList tree, int spaces){
		while (tree != null){
			if (tree.head != null){
				showTree(tree.head, spaces);
			}
			tree = tree.tail;
		}
	}


	/* ==== Abstract classes ==== */
	// Variable
	public static void showTree (Var tree, int spaces){
		if (tree instanceof SimpleVar){
			showTree((SimpleVar) tree, spaces);
		}
		else if (tree instanceof IndexVar){
			showTree((IndexVar) tree, spaces);
		}
	}

	// Declaration
	public static void showTree (Dec tree, int spaces){
		if(tree instanceof FunctionDec){
			showTree((FunctionDec) tree, spaces);
		}
		else if (tree instanceof VarDec){
			showTree((VarDec) tree, spaces);
		}
		else {
			indent(spaces);
			System.err.println("(Dec) Illegal expression at line " + ((ErrorDec)tree).pos);
		}
	}

	// Variable Declaration
	public static void showTree(VarDec tree, int spaces){
		if (tree instanceof SimpleDec){
			showTree((SimpleDec) tree, spaces);
		}
		else if (tree instanceof ArrayDec){
			showTree((ArrayDec) tree, spaces);
		}
		else {
			indent(spaces);
			System.err.println("(VarDec) Illegal expression at line " + ((ErrorVarDec)tree).pos);
		}
	}

	// General Expression
	public static void showTree (Exp tree, int spaces){
		if (tree instanceof NilExp){
			showTree((NilExp) tree, spaces);
		}
		else if (tree instanceof VarExp){
			showTree((VarExp) tree, spaces);
		}
		else if (tree instanceof VarExp){
			showTree((VarExp) tree, spaces);
		}
		else if (tree instanceof CallExp){
			showTree((CallExp) tree, spaces);
		}
		else if (tree instanceof OpExp){
			showTree((OpExp) tree, spaces);
		}
		else if (tree instanceof AssignExp){
			showTree((AssignExp) tree, spaces);
		}
		else if (tree instanceof IfExp){
			showTree((IfExp) tree, spaces);
		}
		else if (tree instanceof WhileExp){
			showTree((WhileExp) tree, spaces);
		}
		else if (tree instanceof ReturnExp){
			showTree((ReturnExp) tree, spaces);
		}
		else if (tree instanceof CompoundExp){
			showTree((CompoundExp) tree, spaces);
		}
		else if (tree instanceof IntExp){
			showTree((IntExp) tree, spaces);
		}
		else {
			indent(spaces);
			System.err.println("(Exp) Illegal expression at line " + ((ErrorExp)tree).pos);
		}
	}


	/* ==== Concrete classes ==== */
	// Type
	public static String showTree(Type tree, int spaces){
		//indent(spaces);

		if(tree.type == Type.INT)
			return "Type: INT";
		else
			return "Type: VOID";
	}


	// Var subclasses
	// SimpleVar
	public static void showTree(SimpleVar tree, int spaces){
		indent(spaces);
		writer.println("SimpleVar: " + tree.name);
	}

	// IndexVar
	public static void showTree(IndexVar tree, int spaces){
		indent(spaces);
		writer.println("IndexVar: " + tree.name);

		spaces+=SPACES;
		showTree(tree.index, spaces); // index is Exp object
	}


	// Dec subclasses : FunctionDec, VarDec (SimpleDec, ArrayDec)
	//FunctionDec
	public static void showTree(FunctionDec tree, int spaces){
		indent(spaces);
		writer.println("FunctionDec: Name: " + tree.func + ", " + showTree(tree.result, spaces));

		spaces+=SPACES;
		indent(spaces);

		// Parameters
		writer.println("Params:");
		spaces+=SPACES;
		showTree(tree.params, spaces);
		spaces-=SPACES;

		// Body
		showTree(tree.body, spaces);
	}

	// SimpleDec
	public static void showTree (SimpleDec tree, int spaces){
		indent(spaces);
		writer.println("SimpleDec: Name: " + tree.name + ", " + showTree(tree.type, spaces));
	}

	// ArrayDec
	public static void showTree (ArrayDec tree, int spaces){
		indent(spaces);
		if(tree.size != null)
			writer.println("ArrayDec: Name: " + tree.name + ", " + showTree(tree.type, spaces) + ", Size: " + tree.size.value);
		else
			writer.println("ArrayDec: Name: " + tree.name + ", " + showTree(tree.type, spaces));
	}

	// Exp subclasses
	// NilExp
	public static void showTree(NilExp tree, int spaces){
		indent(spaces);
		writer.println("NilExp");
	}

	// VarExp
	public static void showTree(VarExp tree, int spaces){
		indent(spaces);
		writer.println("VarExp:");
		spaces+=SPACES;
		showTree(tree.variable, spaces);
	}

	// IntExp
	public static void showTree(IntExp tree, int spaces){
		// Check if null for function declarations/calls of the form int x[]
		if (tree != null){
			indent(spaces);
			writer.println("IntExp:" + tree.value);
		}
	}

	// CallExp
	public static void showTree(CallExp tree, int spaces){
		indent(spaces);
		writer.println("CallExp: " + tree.func);
		spaces+=SPACES;

		// Args
		showTree(tree.args, spaces);
	}

	// OpExp
	public static void showTree(OpExp tree, int spaces){
		indent(spaces);
		writer.print("OpExp: ");

		// Operator
		switch(tree.op){
			case OpExp.PLUS:
				writer.println("+");
				break;
			case OpExp.MINUS:
				writer.println("-");
				break;
			case OpExp.MUL:
				writer.println("*");
				break;
			case OpExp.DIV:
				writer.println("/");
				break;
			case OpExp.EQ:
				writer.println("=");
				break;
			case OpExp.EQUALEQUAL:
				writer.println("==");
				break;
			case OpExp.NE:
				writer.println("!=");
				break;
			case OpExp.LT:
				writer.println("<");
				break;
			case OpExp.GT:
				writer.println(">");
				break;
			case OpExp.LE:
				writer.println("<=");
				break;
			case OpExp.GE:
				writer.println(">=");
				break;
			default:
				System.err.println( "Unrecognized operator at line " + tree.pos);
				break;
		}

		spaces+=SPACES;
		showTree( tree.left, spaces ); // Left operand
		showTree( tree.right, spaces ); // Right operand
	}

	// AssignExp
	public static void showTree(AssignExp tree, int spaces){
		indent(spaces);
		writer.println("AssignExp:");
		spaces+=SPACES;

		// Var = left
		showTree(tree.lhs, spaces);

		// Exp = right;
		showTree(tree.rhs, spaces);
	}

	// IfExp
	// Expression should be printed on the same line
	public static void showTree(IfExp tree, int spaces){
		indent(spaces);
		writer.println("IfExp:");
		spaces+=SPACES;

		showTree(tree.test, spaces); // Test Exp
		showTree(tree.thenpart, spaces); // Then Exp
		showTree(tree.elsepart, spaces); // Else Exp (NilExp)
	}

	// WhileExp
	// Expression should be printed on the same line
	public static void showTree(WhileExp tree, int spaces){
		indent(spaces);
		writer.println("WhileExp:");
		spaces+=SPACES;

		showTree(tree.test, spaces); // While condition Exp
		showTree(tree.body, spaces); // Loop body Exp
	}

	// ReturnExp
	public static void showTree(ReturnExp tree, int spaces){
		indent(spaces);
		writer.println("ReturnExp:");
		spaces+=SPACES;
		if(tree.exp != null){
			showTree(tree.exp, spaces); // Return Exp
		}
	}

	// CompoundExp
	public static void showTree(CompoundExp tree, int spaces){
		indent(spaces);
		writer.println("CompoundExp:");
		spaces+=SPACES;

		showTree(tree.decs, spaces); // VarDecList
		showTree(tree.exps, spaces); //ExpList
	}
}
