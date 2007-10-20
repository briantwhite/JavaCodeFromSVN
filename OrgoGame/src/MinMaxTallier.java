
public class MinMaxTallier {
	
	private int minX;
	private int minY;
	private int maxX;
	private int maxY;
	
	private MinMaxTallier mmt;
	
	private MinMaxTallier() {
		minX = Integer.MAX_VALUE;
		minY = Integer.MAX_VALUE;
		maxX = Integer.MIN_VALUE;
		maxY = Integer.MIN_VALUE;
	}
	
	public MinMaxTallier getInstance() {
		if (mmt == null) {
			mmt = new MinMaxTallier();
		}
		return mmt;
	}

	public void reset() {
		minX = Integer.MAX_VALUE;
		minY = Integer.MAX_VALUE;
		maxX = Integer.MIN_VALUE;
		maxY = Integer.MIN_VALUE;		
	}
	
	public void update(int x, int y) {
		if (x > maxX) {
			maxX = x;
		}
		if (x < minX) {
			minX = x;
		}
		if (y > maxY) {
			maxY = y;
		}
		if (y < minY) {
			minY = y;
		}
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getMinX() {
		return minX;
	}

	public int getMinY() {
		return minY;
	}
}
