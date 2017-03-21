import java.lang.StringBuilder;
import java.util.*;
import absyn.*;

public class TypeChecker{
	private SymbolTable symTable;
	private boolean showScopes;
	private DecList program;

	/* Create indentation */
	private String indent(int scopeLevel){
		StringBuilder spaces;

		for(int i=0; i<scopeLevel*4;i++)
			spaces.append(' ');

		return spaces.toString();
	}

	public TypeChecker(DecList program, boolean showScopes){
		symTable = new SymbolTable();
		this.showScopes = showScopes;
		this.program = program;
	}

	public void init(){
		// Start type checking on the Declaration list
		checkTypes(program); 
	}

/**
 * Lists: DecList, VarDecList, ExpList
 * Abstract classes: Var, Dec, VarDec, Exp
 * Concrete:
 * 			Var: SimpleVar, IndexVar
 *		Dec: FunctionDec, VarDec (SimpleDec, ArrayDec)
 *		Exp: VarExp, IntExp, CallExp, OpExp, AssignExp, IfExp, WhileExp, Return
 * 				CompoundExp
 *
 * Enter new scopes: DecList, CompoundExp
 *
 */

	/* ---------- List Structures ----------- */

	/* Check the whole program by delegating type checking */
	private void checkTypes(DecList tree){
		// Enter new global scope
		symTable.enterNewScope();
		
		while(tree != null){
			if(tree.head != null)
				checkTypes(tree.head);
			tree = tree.tail;
		}
	}

	private void checkTypes(VarDecList tree){
		while (tree != null){
			if (tree.head != null)
				checkTypes(tree.head);
			tree = tree.tail;
		}
	}

	private void checkTypes(ExpList tree){
		while (tree != null){
			if (tree.head != null)
				checkTypes(tree.head);
			tree = tree.tail;
		}
	}


	/* ---------- Abstract Classes ---------*/
	/* Dec, Var, VarDec, Exp */

	public void checkTypes(Dec tree){
		if (tree instanceof VarDec)
			checkTypes((VarDec) tree);
		else if (tree instanceof FunctionDec)
			checkTypes((FunctionDec) tree);
	}	

	public void checkTypes(VarDec tree){
		if (tree instanceof SimpleDec)
			checkTypes((SimpleDec) tree);
		else if (tree instanceof ArrayDec)
			checkTypes((ArrayDec) tree);
	}

	public void checkTypes(Var tree){
		if(tree instanceof SimpleVar)
			checkTypes((SimpleVar)tree);
		else if (head instanceof IndexVar)
			checkTypes((IndexVar) tree)
	}

	public void checkTypes(VarDec tree){
		if (tree instanceof SimpleDec)
			checkTypes((SimpleDec) tree)
		else if (tree instanceof ArrayDec)
			checkTypes((ArrayDec) tree)
	}

	public void checkTypes(Exp tree){
		if (tree instanceof VarExp)
			checkTypes((VarExp) tree)
		else if (tree instanceof CallExp)
			checkTypes((CallExp) tree)
		else if (tree instanceof OpExp)
			checkTypes((OpExp) tree)
		else if (tree instanceof AssignExp)
			checkTypes((AssignExp) tree)
		else if (tree instanceof IfExp)
			checkTypes((IfExp) tree)
		else if (tree instanceof WhileExp)
			checkTypes((WhileExp) tree)
		else if (tree instanceof ReturnExp)
			checkTypes((ReturnExp) tree)
		else if (tree instanceof CompoundExp)
			checkTypes((CompoundExp) tree)
		else if (tree instanceof IntExp)
			checkTypes((IntExp) tree)
	}


	/* ---------- Concrete Classes ----------*/

	public void checkTypes(SimpleDec tree){
		String id = tree.name;
		int ty = tree.type.type;

		if(symTable.existsInCurrentScope(id)){
			System.err.println("Error: Redeclaration of variable \'" + id + "\' on line " + tree.pos);
			return;
		}

		if(ty != Type.INT){
			System.err.println("Error: Variable \'" + id +"\' declared as void on line " + tree.pos);	
			return;
		}

		symTable.addSymbol(id,ty);
	}	

	public void checkTypes(ArrayDec tree){
		String id = tree.name;
		int ty = tree.type.type;
		int arraySize = tree.
	}
		

}