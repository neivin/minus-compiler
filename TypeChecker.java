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

		Symbol s = new VarSymbol(ty, id);
		symTable.addSymbol(id,s);
	}	

	// TODO: If error in ArrayDec, make it zero in cup file
	public void checkTypes(ArrayDec tree){
		String id = tree.name;
		int ty = tree.type.type;
		int arraySize;

		if (tree.size != null)
			arraySize = tree.size.value;
		else
			arraySize = -1;

		if(symTable.existsInCurrentScope(id)){
			System.err.println("Error: Redeclaration of variable \'" + id + "\' on line " + tree.pos);
			return;
		}

		if(ty != Type.INT){
			System.err.println("Error: Variable \'" + id +"\' declared as void on line " + tree.pos);	
			return;
		}

		Symbol s = new ArraySymbol(ty, id, arraySize);

		symTable.addSymbol(id, s);
	}


	public void checkTypes(SimpleVar tree){

	}
	

	public void checkTypes(IndexVar tree){
		
	}

	// Assume that params wont be declared as void (e.g. void xyz, void arr[])
	public void checkTypes(FunctionDec tree){
		String id = tree.func;
		int ty = tree.result.type;

		ArrayList<Symbol> parameters = new Arraylist<Symbol>();

		// Iterate through list of params and add them as Symbols
		// If p is null -> Params = VOID
		VarDecList p = tree.params;
		while(p != null ){
			if (p.head instanceof ArrayDec)
				parameters.add(new ArraySymbol(p.head.type.type, p.head.name, -1));
			else if (p.head instanceof SimpleDec)
				parameters.add(new VarSymbol(p.head.type.type, p.head.name));
		
			p = p.tail;
		}

		Symbol s = new FunctionSymbol(ty, id, p);
		symTable.addSymbol(id, s);

		// Make new scope for function
		symTable.enterNewScope();

		// Add param list into scope and check types
		checkTypes(p);
		
		// Check the body of the function
		checkTypes((CompoundExp)tree.body, s);
	}





	// If compound Exp belongs to a function
	public void checkTypes(CompoundExp tree, FunctionSymbol func){
		// Establish return type
		boolean needsReturn = false;

		if (func.type == Type.INT)
			needsReturn = true;

		// Go through local declarations
		if (tree.decs != null)
			checkTypes(tree.decs);

		// Go through statement list
		// Pass in if return statement is required
		if (tree.exps != null)
			checkTypes(tree.exps, needsReturn);

		// Exit scope
		symTable.exitScope();		
	}


}