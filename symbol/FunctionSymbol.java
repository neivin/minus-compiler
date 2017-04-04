package symbol;

import java.util.ArrayList;

public class FunctionSymbol extends Symb {
	
	public ArrayList<Symb> parameters;
	public int address;

	public FunctionSymbol(int type, String id, ArrayList<Symb> parameters){
		this.type = type;
		this.id = id;
		this.parameters = parameters;
	}

	public FunctionSymbol(int type, String id, ArrayList<Symb> parameters, int address){
		this.type = type;
		this.id = id;
		this.parameters = parameters;
		this.address = address;
	}

	public int paramCount(){
		return parameters.size();
	}
}