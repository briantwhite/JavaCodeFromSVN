
public class Atom {

	private String type;
	private int x;
	private int y;
	private int hAtomCount;
	private String id;

	public Atom(String type,
			int x,
			int y,
			int hAtomCount,
			String id) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.hAtomCount = hAtomCount;
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getHatomCount() {
		return hAtomCount;
	}

	public String getId() {
		return id;
	}

	public String toString() {
		return "Atom: " + type 
		+ " x=" + x 
		+ " y=" + y 
		+ " #H=" + hAtomCount
		+ " id=" + id;
	}
	
	public void scale(int scaleFactor, int xOffset, int yOffset) {
		x = (x / scaleFactor) + xOffset;
		y = (y / scaleFactor) + yOffset;
	}
}
