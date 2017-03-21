import java.util.ArrayList;
import java.util.HashMap;


public class SymbolTable {
	private ArrayList<HashMap<String, Integer>> table;

	public SymbolTable(){
		table = new ArrayList<HashMap<String,Type>>(); 
	}

	
	/* Enters a new scope by creating a new HashMap.
	 * A new scope is created on entering new blocks
	 */
	public void enterNewScope(){
		table.add(new HashMap<String,Type>());
		System.out.println("Entering scope " + (table.size()-1));
	}

	/* Exits the most recent scope, and deletes 
	 * everything in the current HashMap.
	 */
	public void exitScope(){
		System.out.println("Exiting scope " + (table.size()-1));
		if(table.size()>0){
			table.remove(table.size()-1);
		}
	}

	public void printScope(){
		
	}

	/* Checks if the given String symbol exists in
	 * the Symbol Table or not. Returns the scope level,
	 * or -1 if it doesn't exist.
	 */
	public int symbolExists(String symbol){
		int curSize = table.size()-1;
		
		// Return the most recent scope where the symbol was found
		for(i = curSize ; i >= 0; i--){
			if(table.get(i).containsKey(symbol)){ // If symbol exists in table, return
				return i;
			}	
		}

		// Return -1 if the id is undefined
		return -1;
	} 

}