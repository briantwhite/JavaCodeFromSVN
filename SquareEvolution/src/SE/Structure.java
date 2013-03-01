package SE;
import java.util.ArrayList;


public class Structure {
	int minX;
	int minY;
	int maxX;
	int maxY;
	
	private ArrayList<Symbol> symbols;
	
	public Structure() {
		minX = Integer.MAX_VALUE;
		minY = Integer.MAX_VALUE;
		maxX = Integer.MIN_VALUE;
		maxY = Integer.MIN_VALUE;
		symbols = new ArrayList<Symbol>();
	}
	
	public void addSymbol(Symbol s) {
		symbols.add(s);
		if (s.x < minX) minX = s.x;
		if (s.y < minY) minY = s.y;
		if (s.x > maxX) maxX = s.x;
		if (s.y > maxY) maxY = s.y;
	}

	public ArrayList<Symbol> getSymbols() {
		return symbols;
	}

	public int getMinX() {
		return minX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}
}
