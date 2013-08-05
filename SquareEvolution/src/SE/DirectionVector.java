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

	public DirectionVector(String direction, int rotamer) {
		this();
		setDxDy(rotateCW(direction, rotamer));
	}

	private void setDxDy(String direction) {
		if (direction.equals("U")) dY = 1;
		if (direction.equals("R")) dX = 1;
		if (direction.equals("D")) dY = -1;
		if (direction.equals("L")) dX = -1;		
	}

	/*
	 * needs to deal with rotamers > 3
	 *  rotamers 0-3 are rotations of normal protein
	 *  rotamers 4-7 are 0-3 of flipped protein
	 *  see "Square Evolution log 08 p1+" 
	 *  and "running blooms stuff log 02 p1+"
	 */
	public static String rotateCW(String direction, int rotamer) {
		String s = "URDLURDL";
		if (rotamer < 4) {
			int x = s.indexOf(direction);
			return s.substring(x + rotamer, x + rotamer + 1);
		} else {
			int x = s.indexOf(flip(direction));
			return s.substring(x + rotamer - 4, x + rotamer - 3);
		}
	}

	/*
	 * flip about a vertical axis (for rotamers 4-7)
	 */
	public static String flip(String direction) {
		if (direction.equals("L")) return "R";
		if (direction.equals("R")) return "L";
		return direction;
	}

}
