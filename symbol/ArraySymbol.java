package symbol;

public class ArraySymbol extends Symb {
	public int size;

	public ArraySymbol(int type, String id, int size){
		this.type = type;
		this.id = id;
		this.size = size;
	}
}