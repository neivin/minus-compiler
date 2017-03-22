public class FunctionSymbol extends Symbol {
	
	public ArrayList<Symbol> parameters;

	public FunctionSymbol(int type, String id, ArrayList<Symbol> parameters){
		this.type = type;
		this.id = id;
		this.parameters = parameters;
	}

	public int paramCount(){
		return parameters.size();
	}
}