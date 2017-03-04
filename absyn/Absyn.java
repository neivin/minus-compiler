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


	// TODO

	/* Declarations - Function or Variable*/
	public static void showTree (Dec tree, int spaces){
		if (tree instanceof VarDec){
			showTree((VarDec) tree, spaces);
		}
		else if (tree instanceof FunDec){
			showTree((FunDec) tree, spaces);
		}
		else{
			indent(spaces);
			System.out.println("Illegal expression at line " + tree.pos);
		}
	}

	/* Type */
	public static void showTree(Type tree, int spaces){
		indent(spaces);

		if(tree.type == Type.INT)
			System.out.println("Type: INT");
		else
			System.out.println("Type: VOID");
	}

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

}
