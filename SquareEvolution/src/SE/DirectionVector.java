package SE;

public class DirectionVector {
	
	int dX;
	int dY;
	
	public DirectionVector(int dX, int dY) {
		this.dX = dX;
		this.dY = dY;
	}
	
	public DirectionVector() {
		dX = 0;
		dY = 0;
	}
	
	public DirectionVector(String direction) {
		this();
		setDxDy(direction);
	}
	
	public DirectionVector(String direction, int steps) {
		this();
		setDxDy(rotateCW(direction, steps));
	}
	
	private void setDxDy(String direction) {
		if (direction.equals("U")) dY = 1;
		if (direction.equals("R")) dX = 1;
		if (direction.equals("D")) dY = -1;
		if (direction.equals("L")) dX = -1;		
	}
	
	public static String rotateCW(String direction, int steps) {
		String s = "URDLURDL";
		int x = s.indexOf(direction);
		return s.substring(x + steps, x + steps + 1);
	}

}
