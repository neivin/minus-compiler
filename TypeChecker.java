import java.util.ArrayList;
import absyn.*;
import symbol.*;

public class TypeChecker{
	private SymbolTable symTable;
	private DecList program;
	private int currentReturnType;

	private String outFileName;


	public TypeChecker(DecList program, boolean showScopes, String filename){
		symTable = new SymbolTable(showScopes);
		this.program = program;
		this.outFileName = filename + CM.EXT_SYM;
	}

	public void init(){
		// Start type checking on the Declaration list
		checkTypes(program); 
	}

/** Symbol
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
		
		// Add input function
		Symb inputFunction = new FunctionSymbol(Type.INT, "input", new ArrayList<Symb>());
		symTable.addSymbol("input",  inputFunction);

		// Output function
		ArrayList<Symb> parameters = new ArrayList<Symb>();
		parameters.add(new VarSymbol(Type.INT, "value"));

		Symb outputFunction = new FunctionSymbol(Type.VOID, "output", parameters);
		symTable.addSymbol("output",  outputFunction);


		while(tree != null){
			if(tree.head != null)
				checkTypes(tree.head);
			tree = tree.tail;
		}

		symTable.exitScope();
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
				checkTypes(tree.head, false);
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
		else if (tree instanceof IndexVar)
			checkTypes((IndexVar) tree);
	}


	public void checkTypes(Exp tree, boolean requiresInt){
		if (tree instanceof VarExp)
			checkTypes((VarExp) tree, true);
		else if (tree instanceof CallExp)
			checkTypes((CallExp) tree, requiresInt);
		else if (tree instanceof OpExp)
			checkTypes((OpExp) tree, true);
		else if (tree instanceof AssignExp)
			checkTypes((AssignExp) tree, true);
		else if (tree instanceof IfExp)
			checkTypes((IfExp) tree, false);
		else if (tree instanceof WhileExp)
			checkTypes((WhileExp) tree, false);
		else if (tree instanceof ReturnExp)
			checkTypes((ReturnExp) tree, requiresInt);
		else if (tree instanceof CompoundExp)
			checkTypes((CompoundExp) tree, requiresInt);
		else if (tree instanceof IntExp)
			checkTypes((IntExp) tree, requiresInt);
	}


	/* ---------- Concrete Classes ----------*/

	public void checkTypes(SimpleDec tree){
		String id = tree.name;
		int ty = tree.type.type;

		if(symTable.symbolExistsInCurrentScope(id)){
			System.err.println("Error: Redeclaration of variable \'" + id + "\' on line " + tree.pos);
			return;
		}

		if(ty != Type.INT){
			System.err.println("Error: Variable \'" + id +"\' declared as void on line " + tree.pos);	
		}

		Symb s = new VarSymbol(ty, id);
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

		if(symTable.symbolExistsInCurrentScope(id)){
			System.err.println("Error: Redeclaration of variable \'" + id + "\' on line " + tree.pos);
			return;
		}

		if(ty != Type.INT){
			System.err.println("Error: Variable \'" + id +"\' declared as void on line " + tree.pos);	
			return;
		}

		Symb s = new ArraySymbol(ty, id, arraySize);

		symTable.addSymbol(id, s);
	}

	/* ---------- Var subclasses ---------- */

	public void checkTypes(SimpleVar tree){

		// If symbol exists
		if(symTable.symbolExists(tree.name) != -1){
			if(symTable.getSymbol(tree.name) instanceof VarSymbol){
				// Check that all variables used are ints and not void
				if (symTable.getSymbol(tree.name).type != Type.INT)
				System.err.println("Error: Use of void variable \'" + tree.name + "\' when integer expected on line " + tree.pos);
			}
			else {
				System.err.println("Error: Cannot convert array type \'"+tree.name+"\' to int on line " + tree.pos);
			}

		}
		else {
			System.err.println("Error: Undefined variable \'" + tree.name +"\' on line " + tree.pos);
		}
	}
	

	public void checkTypes(IndexVar tree){
		// If symbol exists
		if(symTable.symbolExists(tree.name) != -1){
			if(!(symTable.getSymbol(tree.name) instanceof ArraySymbol)){
				System.err.println("Error: \'"+tree.name+"\' defined as int but referenced as array on line " + tree.pos);
			}
		}
		else {
			System.err.println("Error: Undefined array variable \'" + tree.name +"\' on line " + tree.pos);
		}

		// Check valdity of index
		checkTypes(tree.index, true); // Must be int
	}


	// Assume that params wont be declared as void (e.g. void xyz, void arr[])
	public void checkTypes(FunctionDec tree){
		String id = tree.func;
		int ty = tree.result.type;

		ArrayList<Symb> parameters = new ArrayList<Symb>();

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

		Symb s = new FunctionSymbol(ty, id, parameters);
		symTable.addSymbol(id, s);

		// Set return type for checker
		currentReturnType = ty;

		// Make new scope for function
		symTable.enterNewScope();

		// Add param list into scope and check types
		checkTypes(tree.params);
		
		// Check the body of the function
		checkTypes((CompoundExp)tree.body, (FunctionSymbol) s);
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
			checkTypes(tree.exps);

		// Exit scope
		symTable.exitScope();		
	}

	public void checkTypes(VarExp tree, boolean requiresInt){
		checkTypes(tree.variable);	
	}

	public void checkTypes(OpExp tree, boolean requiresInt){
		checkTypes(tree.left, true);
		checkTypes(tree.right, true);	
	}

	public void checkTypes(AssignExp tree, boolean requiresInt){
		checkTypes(tree.lhs);
		checkTypes(tree.rhs, true);
	}

	public void checkTypes(IfExp tree, boolean requiresInt){
		// Check test
		checkTypes(tree.test, true); // Test must be int

		// Check if part
		checkTypes(tree.thenpart, false);

		// Check else part
		if (tree.elsepart != null)
			checkTypes(tree.elsepart, false);	
	}

	public void checkTypes(WhileExp tree, boolean requiresInt){
		checkTypes(tree.test, true);
		checkTypes(tree.body, false);
	}

	public void checkTypes(ReturnExp tree, boolean requiresInt){
		if (currentReturnType != Type.INT){
			if (tree.exp != null){
				System.err.println("Error: Void function returns an integer value on line " + tree.pos);
				return;
			}
		}
		else {
			if(tree.exp == null){
				System.err.println("Error: Function defined as int returns nothing on line " + tree.pos);
			}
			else {
				checkTypes(tree.exp, true);
			}
		}
	}

	public void checkTypes(CallExp tree, boolean requiresInt){
		String id = tree.func;
		
		// Check if function exists
		if(!symTable.functionExists(id)){
			System.err.println("Error: Undefined function \'" + id + "\' on line " + tree.pos);
			return;
		}

		// Check that the symbol is a function
		if (!(symTable.getFunction(id) instanceof FunctionSymbol)){
			System.err.println("Error: Symbol \'" + id +"\' defined as variable, but used as function on line " + tree.pos);
			return;
		}

		if (symTable.getFunction(id).type != Type.INT && requiresInt){
			System.err.println("Error: Function \'" + id + "\' expected to return int, but returns void on line " + tree.pos);
		}

		FunctionSymbol f = (FunctionSymbol) symTable.getFunction(id);

		// Validate number of arguments from function definition
		if (f.paramCount() != tree.argsCount()) {
			System.err.println("Error: Incorrect number of arguments to function \'" + id + "\' on line " + tree.pos);
			return;
		}

		// Compare arguments
		ExpList funcArgs = tree.args;
		for(int i =0; i < f.paramCount(); i ++){
			Symb s = f.parameters.get(i);
			Exp e = funcArgs.head;

			if (s instanceof VarSymbol){
				checkTypes(e, true);
			}
			else if (s instanceof ArraySymbol){
				if ((e instanceof VarExp) && (((VarExp)e)).variable instanceof SimpleVar){
					String varName = ((SimpleVar)((VarExp)e).variable).name;

					if(symTable.symbolExists(varName) != -1 ){
						if (!(symTable.getSymbol(varName) instanceof ArraySymbol))
							System.err.println("Error: Expected int array, but read something else on line " + tree.pos);
					}
					else {
						System.err.println("Error: Undefined variable \'" + varName + "\' in function call on line " + tree.pos);
					}
				}
				else {
					System.err.println("Error: Expected int array, but read something else on line " + tree.pos);
				}
			}

			funcArgs = funcArgs.tail;
		}

	}

	public void checkTypes(CompoundExp tree, boolean requiresInt){
		symTable.enterNewScope();

		checkTypes(tree.decs);
		checkTypes(tree.exps);

		symTable.exitScope();
	}

	
	public void checkTypes(IntExp tree, boolean requiresInt){
		
	}
}