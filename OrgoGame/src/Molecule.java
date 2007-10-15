
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
