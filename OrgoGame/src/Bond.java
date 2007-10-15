
public class Bond {

	private Atom atom1;
	private Atom atom2;
	private int bondOrder;

	public Bond(Atom a1, Atom a2, int index) {
		atom1 = a1;
		atom2 = a2;
		bondOrder = index;
	}

	public Atom getAtom1() {
		return atom1;
	}

	public Atom getAtom2() {
		return atom2;
	}

	public int getBondOrder() {
		return bondOrder;
	}

	public double getLength() {
		return Math.sqrt(
				(
						(atom1.getX() - atom2.getX()) 
						* (atom1.getX() - atom2.getX())
				) +
				(
						(atom1.getY() - atom2.getY()) 
						* (atom1.getY() - atom2.getY())
				)
		);
	}

	public String toString() {
		return "Bond: " + atom1.getId() + "-" + atom2.getId()
		+ " index=" + bondOrder;
	}
}
