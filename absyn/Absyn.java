package absyn;

abstract public class Absyn {
	public int pos;
	public final static int SPACES = 4;

	private static void indent (int spaces) {
		for(int i =0; i < spaces; i++)
			System.out.print(" ");
	}

	/* ==== List Structures ==== */

	// Declaration List
	public static void showTree (DecList tree, int spaces){
		while (tree != null){
			if (tree.head != null){
				showTree(tree.head, spaces);
			}
			tree = tree.tail;
		}
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
			System.out.println("(Dec) Illegal expression at line " + tree.pos);
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
			System.out.println("(VarDec) Illegal expression at line " + tree.pos)
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
		else {
			indent(spaces);
			System.out.println("(Exp) Illegal expression at line " + tree.pos);
		}
	}


	/* ==== Concrete classes ==== */
	// Type 
	public static void showTree(Type tree, int spaces){
		indent(spaces);

		if(tree.type == Type.INT)
			System.out.println("Type: INT");
		else
			System.out.println("Type: VOID");
	}


	// Var subclasses
	// SimpleVar
	public static void showTree(SimpleVar tree, int spaces){
		indent(spaces);
		System.out.println("SimpleVar: " + tree.name);
	}

	// IndexVar
	public static void showTree(IndexVar tree, int spaces){
		indent(spaces);
		System.out.println("IndexVar:");

		spaces+=SPACES;
		indent(spaces);
		System.out.println("Name: " + tree.name);
		showTree(tree.index, spaces); // index is Exp object
	}



/*
	private static void showTree (FunDec tree, int spaces){
		indent(spaces);
		System.out.println("FunctionDec: ");
		spaces+=SPACES;
		showTree(tree.type, spaces);
		System.out.println("FunctionName: " + tree.id);
		showTree(tree.params, spaces); // ParamList
		showTree(tree.compound, spaces); // CompoundStatement
	}

	// TODO: Add types of var declarations
	private static void showTree (VarDec tree, int spaces){
		if (tree instanceof SimpleDec){
			showTree((SimpleDec) tree, spaces);
		}
		else if (tree instanceof ArrayDec){
			showTree((ArrayDec) tree, spaces);
		}
		else{
			indent(spaces);
			System.out.println("Illegal expression at line " + tree.pos);
		}
	}

	public static showTree (SimpleDec tree, int spaces){
		indent(spaces);
		System.out.println("SimpleDec:");
		spaces += SPACES;
		showTree(tree.type, spaces);
		indent(spaces);
		System.out.println("ID: " + tree.id);
	}

	public static showTree (ArrayDec tree, int spaces) {
		indent(spaces);
		System.out.println("ArrayDec:");
		spaces += SPACES;
		showTree(tree.type, spaces);
		indent(spaces);
		System.out.println("ID: " + tree.id);
		indent(spaces);
		System.out.println("Size: " + tree.num);
	}

	public static showTree (Param tree, int spaces){
		indent(spaces);
		System.out.println("Param:");
		spaces+=SPACES;
		showTree(tree.type, spaces);
		indent(spaces);
		System.out.print("ID: " + tree.id);
		
		indent(spaces);
		if(tree.decl == Param.ARRAYPARAM) {
			System.out.println(" [ ]");
		}
		
		System.out.println();

	}
	*/

}
