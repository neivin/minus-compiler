import java.util.ArrayList;
import java.util.Set;
import java.util.HashMap;
import java.lang.StringBuilder;
import java.io.PrintWriter;
import java.io.FileNotFoundException;


import absyn.*;
import symbol.*;


public class SymbolTable {
	private ArrayList<HashMap<String, Symb>> table;
	private boolean displayScopes;

	private String outFileName;
	private PrintWriter writer;


	public SymbolTable(boolean displayScopes, String outFileName){
		table = new ArrayList<HashMap<String,Symb>>(); 
		this.displayScopes = displayScopes;
		this.outFileName = outFileName;

		try{
			writer = new PrintWriter(outFileName);
			writer.println("---------- " + this.outFileName + ".cm Symbol Table ----------");
			writer.println();
		}
		catch (FileNotFoundException e){
			System.err.println("Error: Could not create output file.");
			e.printStackTrace();
		}
	}

	
	public void closeWriter(){
		writer.close();
	}

		/* Create indentation */
	private String indent(int scopeLevel){
		StringBuilder spaces = new StringBuilder();

		for(int i=0; i<scopeLevel*4;i++)
			spaces.append(' ');

		return spaces.toString();
	}

	private String getType(int type){
		if (type == Type.INT)
			return "INT";
		return "VOID";
	}


	public void printScope(){
		Set<String> keys = table.get(table.size()-1).keySet();
		String spaces = indent(table.size()-1);

		for (String key : keys){
			Symb s = table.get(table.size()-1).get(key);

			if(s instanceof VarSymbol){
				writer.println(spaces + "Variable: " + getType(s.type) + " " + key);
			}
			else if (s instanceof ArraySymbol){
				ArraySymbol as = (ArraySymbol) s;
				writer.println(spaces + "Array: " + getType(s.type) + " " + key + "[" + as.size +"]");
			}
			else if (s instanceof FunctionSymbol){
				StringBuilder funcSig = new StringBuilder(spaces);
				funcSig.append("Function: ");
				funcSig.append(getType(s.type));
				funcSig.append(' ');
				funcSig.append(key);
				funcSig.append(" (");

				// Iterate through all the parameters
				for (Symb sym : ((FunctionSymbol)s).parameters){
					if (sym instanceof VarSymbol){
						if (sym.type == Type.INT)
							funcSig.append("INT");
						else
							funcSig.append("VOID");
					}
					else if (sym instanceof ArraySymbol){
						if (sym.type == Type.INT)
							funcSig.append("INT []");
						else
							funcSig.append("VOID []");
					}

					// Commas between args
					funcSig.append(", ");
				}

				// Remove last comma and add closing parenthesis
				// only if there are some parameters
				if (((FunctionSymbol)s).parameters.size() > 0 ){
					funcSig.deleteCharAt(funcSig.length()-1);
					funcSig.deleteCharAt(funcSig.length()-1);
				}

				funcSig.append(")");

				writer.println(funcSig.toString());
			}
		}	
	}

	/* Enters a new scope by creating a new HashMap.
	 * A new scope is created on entering new blocks
	 */
	public void enterNewScope(){
		table.add(new HashMap<String,Symb>());
		if(displayScopes)
			writer.println(indent(table.size()-1) + "<< ENTER SCOPE " + (table.size()-1) + " >>");
	}

	/* Exits the most recent scope, and deletes 
	 * everything in the current HashMap.
	 */
	public void exitScope(){
		if(displayScopes){
			printScope();
			writer.println(indent(table.size()-1) + "<< EXIT SCOPE " + (table.size()-1) + " >>");
		}

		if(table.size()>0){
			table.remove(table.size()-1);
		}
	}

	/* Checks if the given String symbol exists in
	 * the Symbol Table or not. Returns the scope level,
	 * or -1 if it doesn't exist.
	 */
	public int symbolExists(String symbol){
		int curSize = table.size()-1;
		
		// Return the most recent scope where the symbol was found
		for(int i = curSize ; i >= 0; i--){
			if(table.get(i).containsKey(symbol)){ // If symbol exists in table, return
				return i;
			}	
		}

		// Return -1 if the id is undefined
		return -1;
	}

	public boolean functionExists(String symbol){
		return table.get(0).containsKey(symbol);
	}

	public Symb getFunction(String symbol){
		return table.get(0).get(symbol);
	}

	/* Checks if the String symbol exists in the most recent scope
	 * Returns true if it exists, false otherwise
	 */
	public boolean symbolExistsInCurrentScope(String symbol){
		return table.get(table.size()-1).containsKey(symbol);
	} 


	public void addSymbol(String id, Symb ty){
		table.get(table.size()-1).put(id, ty);
	}

	
	public Symb getSymbol(String symbol){
		int curSize = table.size()-1;
		
		// Return the most recent scope where the symbol was found
		for(int i = curSize ; i >= 0; i--){
			if(table.get(i).containsKey(symbol)){ // If symbol exists in table, return
				return table.get(i).get(symbol);
			}	
		}
		return null;
	}

}