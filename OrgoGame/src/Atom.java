
public class Atom {

	private String type;
	int x;
	int y;
	private int hAtomCount;
	
	public Atom(String type,
			int x,
			int y,
			int hAtomCount) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.hAtomCount = hAtomCount;
	}
	
	public String getType() {
		return type;
	}
	
	public int getHatomCount() {
		return hAtomCount;
	}
}
