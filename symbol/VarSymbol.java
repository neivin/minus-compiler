package symbol;

public class VarSymbol extends Symb {

	public int offset;

	public VarSymbol(int type, String id){
		this.type = type;
		this.id = id;
	}

	public VarSymbol(int type, String id, int offset){
		this.type = type;
		this.id = id;
		this.offset = offset;
	}
}