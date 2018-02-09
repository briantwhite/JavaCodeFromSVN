import java.util.ArrayList;

public class Pathway {
	private Molecule[] molecules;
	private Enzyme[] enzymes;

	public Pathway() {
		// build a "hard-wired" pathway
		/*
		 * 
		 *							|--(enz 1)--> 2 --(enz 2)--> 3
		 *	molec 0 --(enz 0)--> 1 -|
	     *   						|--(enz 3)--> 4 --(enz 4)--> 5 --(enz 5)--> 6
	     *
		 */
		
		// first, the molecules and enzymes
		molecules = new Molecule[7];
		for (int i = 0; i < 7; i++) {
			molecules[i] = new Molecule(i);
		}
		enzymes = new Enzyme[6];
		for (int i = 0; i < 6; i++) {
			enzymes[i] = new Enzyme(i);
		}
		// now, wire them up
		molecules[0].addNextEnzyme(enzymes[0]);
		enzymes[0].setSubstrate(molecules[0]);
		enzymes[0].setProduct(molecules[1]);
		
		molecules[1].addNextEnzyme(enzymes[1]);
		molecules[1].addNextEnzyme(enzymes[3]);
		enzymes[1].setSubstrate(molecules[1]);
		enzymes[1].setProduct(molecules[2]);
		enzymes[3].setSubstrate(molecules[1]);
		enzymes[3].setProduct(molecules[4]);
		
		molecules[2].addNextEnzyme(enzymes[2]);
		enzymes[2].setSubstrate(molecules[2]);
		enzymes[2].setProduct(molecules[3]);
		
		molecules[4].addNextEnzyme(enzymes[4]);
		enzymes[4].setSubstrate(molecules[4]);
		enzymes[4].setProduct(molecules[5]);
		
		molecules[5].addNextEnzyme(enzymes[5]);
		enzymes[5].setSubstrate(molecules[5]);
		enzymes[5].setProduct(molecules[6]);
		
		checkPathwayIntegrity();
	}
	
	public void activateAllEnzymes() {
		for (int i = 0; i < enzymes.length; i++) {
			enzymes[i].activate();
		}
	}
	
	public void inactivateEnzyme(int number) {
		enzymes[number].inactivate();
	}
	
	// given the pathway as it is, figure out which molecules get made
	public boolean[] getOutputs() {
		boolean[] result = new boolean[molecules.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = false;
		}
		
		
		return result;
	}
	
	private void checkPathwayIntegrity() {
		// make sure everything is connected OK
		
		// all molecules have at least one enzyme 
		// 	if not, then they're terminal 
		for (int i = 0; i < molecules.length; i++) {
			if (molecules[i].getNextEnzymeList().size() == 0) {
				molecules[i].setTerminal();
			}
		}
		
		// all enzymes have a substrate and a product
		//	if not, make a fuss
		for (int i = 0; i < enzymes.length; i++) {
			if (enzymes[i].getSubstrate() == null) {
				System.out.println("Enzyme " + i + " lacks a substrate!");
			}
			if (enzymes[i].getProduct() == null) {
				System.out.println("Enzyme " + i + " lacks a product!");
			}
		}
		
		// need to be sure that all intermediates get produced if all enzymes present
		
	}
}
