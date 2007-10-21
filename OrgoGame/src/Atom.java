
public class Atom {

	private String type;
	private int x;
	private int y;
	private int hAtomCount = 0;
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
	
	public void normalizeXandY(int avgX, int avgY) {
		x = x - avgX;
		y = y - avgY;
	}
	
	public Atom scale(int scaleFactor, 
			int xOffset, int yOffset,
			MinMaxTallier mmt) {
		int newX = (x / scaleFactor) + xOffset;
		int newY = (y / scaleFactor) + yOffset;
		mmt.update(newX, newY);
		return new Atom(type, newX, newY, hAtomCount, id);
	}
}
