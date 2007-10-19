
public class Molecule {

	private Atom[] atoms;
	private Bond[] bonds;
	
	public Molecule (Atom[] atoms, Bond[] bonds) {
		this.atoms = atoms;
		this.bonds = bonds;
	}
	
	public Atom[] getAtoms() {
		return atoms;
	}
	
	public Bond[] getBonds() {
		return bonds;
	}
	
	public int getOneBondLength() {
		return bonds[0].getLength();
	}
	
	// normalize by subtracting the average x from the x's
	//  and the average y from the y's
	public void normalizeXandY() {
		int sumX = 0;
		int sumY = 0;
		for (int i = 0; i < atoms.length; i++) {
			sumX = sumX + atoms[i].getX();
			sumY = sumY + atoms[i].getY();
		}
		int avgX = sumX/atoms.length;
		int avgY = sumY/atoms.length;
		for (int i = 0; i < atoms.length; i++) {
			atoms[i].normalizeXandY(avgX, avgY);
		}
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Molecule\n");
		buf.append("  Atoms:\n");
		for (int i = 0; i < atoms.length; i++) {
			buf.append("    " + atoms[i].toString() + "\n");
		}
		buf.append("  Bonds:\n");
		for (int i = 0; i < bonds.length; i++) {
			buf.append("    " + bonds[i].toString() + "\n");
		}
		return buf.toString();
	}
}
