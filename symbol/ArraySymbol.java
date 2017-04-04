package symbol;

public class ArraySymbol extends Symb {
	public int size;
	public int offset;

	public ArraySymbol(int type, String id, int size){
		this.type = type;
		this.id = id;
		this.size = size;
	}

	public ArraySymbol(int type, String id, int size, int offset){
		this.type = type;
		this.id = id;
		this.size = size;
		this.offset = offset;
	}
}