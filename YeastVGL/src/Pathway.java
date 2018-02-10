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
	
	public int getNumberOfEnzymes() {
		return enzymes.length;
	}
	
	public int getNumberOfMolecules() {
		return molecules.length;
	}
	
	// test if a given strain will grow under these conditions:
	//  - specific set of mutations in genotype (array of booleans for enzyme function)
	//  - specific starting molecule(s)
	// it tests to see if all terminal molecules (assumed to be the only essential ones) are made
	public boolean willItGrow(boolean[] genotype, ArrayList<Integer>startingMolecules) {
		// set up the pathway with the particular set of mutants (false entries in genotype)
		activateAllEnzymes();
		for (int i = 0; i < genotype.length; i++) {
			if (!genotype[i]) {
				inactivateEnzyme(i);
			}
		}
		// collect the resulting products for each of the starting molecules
		//  initialize result array
		boolean[] moleculesProduced = new boolean[molecules.length];
		for (int i = 0; i < moleculesProduced.length; i++) {
			moleculesProduced[i] = false;
		}
		// iterate over the different substrates provided
		for (int i = 0; i < startingMolecules.size(); i++) {
			boolean[] outputs = getOutputs(startingMolecules.get(i).intValue());
			// add to results
			for (int j = 0; j < outputs.length; j++) {
				if (outputs[j]) {
					moleculesProduced[j] = true;
				}
			}
		}
		
		// check on the terminal molecules
		//  if any missing, it won't grow.
		boolean result = true;
		for (int i = 0; i < moleculesProduced.length; i++) {
			if (!moleculesProduced[i] && molecules[i].isTerminal()) {
				result = false;
			}
		}
		return result;
	}
	
	// given the pathway as it is, figure out which molecules get made
	// start with molecule m as the input
	public boolean[] getOutputs(int m) {
		boolean[] result = new boolean[molecules.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = false;
		}
		tracePathway(molecules[m], result);				
		return result;
	}
	
	// recursive tracing function
	private void tracePathway(Molecule startingMolecule, boolean[] result) {
		// tag the starting molecule as present - since we got here somehow
		result[startingMolecule.getNumber()] = true;
		
		ArrayList<Enzyme> nextEnzymeList = startingMolecule.getNextEnzymeList();
		// see if you've reached a terminal molecule
		if (nextEnzymeList.size() == 0) {
			return;
		}
		for (int i = 0; i < nextEnzymeList.size(); i++) {
			Enzyme e = nextEnzymeList.get(i);
			// only continue if enzyme is working
			if (e.isActive()) {
				tracePathway(e.getProduct(), result);;
			}
		}
	}
	
	
	private void checkPathwayIntegrity() {
		// make sure everything is connected OK
		boolean OK = true;
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
				OK = false;
			}
			if (enzymes[i].getProduct() == null) {
				System.out.println("Enzyme " + i + " lacks a product!");
				OK = false;
			}
		}
		
		// need to be sure that all intermediates get produced if all enzymes present
		boolean[] outputs = getOutputs(0);
		for (int i = 0; i < outputs.length; i++) {
			if (!outputs[i]) {
				System.out.println("Molecule " + i + " is NOT produced!");
				OK = false;
			}
		}
		if (OK) {
			System.out.println("Pathway is OK");
		}
	}
}
