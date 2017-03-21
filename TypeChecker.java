import java.lang.StringBuilder;

public class TypeChecker{
	private SymbolTable symTable;
	private boolean showScopes;
	private DecList program;

	/* Create indentation */
	public String indent(int scopeLevel){
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

	/* Check the whole program by delegating type checking */
	private void checkTypes(DecList tree){
		// Enter new global scope
		symTable.enterNewScope();
		
		while(tree != null){
			checkTypes(tree.head);
			tree = tree.tail;
		}
	}

	public void checkTypes(Var tree){
		if(tree instanceof SimpleVar)
			checkTypes((SimpleVar)tree);
		else if (head instanceof IndexVar)
			checkTypes((IndexVar) tree)
	}

	public void checkTypes(Exp tree){
		if (tree instanceof )
	}

}