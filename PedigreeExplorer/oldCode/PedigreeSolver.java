package PE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import Pelican.PelicanPerson;

public class PedigreeSolver {
	private Vector<PelicanPerson> pedigree;
	private ArrayList<Family> families;
	private HashMap<PelicanPerson, PelicanPerson> husbands;
	private HashMap<PelicanPerson, PelicanPerson> wives;
	private GenotypeSet[] successfulGenotypeSets; 	//index is model number

	public PedigreeSolver(Vector<PelicanPerson> pedigree, HashSet<String> matingList) {
		successfulGenotypeSets = new GenotypeSet[4];
		successfulGenotypeSets[0] = new GenotypeSet();
		successfulGenotypeSets[1] = new GenotypeSet();
		successfulGenotypeSets[2] = new GenotypeSet();
		successfulGenotypeSets[3] = new GenotypeSet();
		
		this.pedigree = pedigree;

		// work out family structure
		families = new ArrayList<Family>();

		Iterator<String> matingPairIt = matingList.iterator();
		while (matingPairIt.hasNext()) {
			String[] matingPairStrings = matingPairIt.next().split(" ");
			PelicanPerson dad = findPersonById(Integer.parseInt(matingPairStrings[0]));
			PelicanPerson mom = findPersonById(Integer.parseInt(matingPairStrings[1]));
			Family f = new Family(dad, mom);
			dad.partner = mom;
			mom.partner = dad;

			// find their kids
			Iterator<PelicanPerson> personIt = pedigree.iterator();
			while (personIt.hasNext()) {
				PelicanPerson p = personIt.next();
				if ((p.mother != null) && (p.father != null)) {
					if ((p.mother.id == mom.id) && (p.father.id == dad.id)) f.addChild(p);
				}
			}
			families.add(f);
		}
		
		// sort by lower index of mom or dad - puts root family at top
		Collections.sort(families);

		Iterator<Family> i = families.iterator();
		while (i.hasNext()) {
			Family f = i.next();
			System.out.println(f);
		}
	}

	public PedigreeSolution solve() {

		// step thru the four models
		for (int modelNumber = 0; modelNumber < 4; modelNumber++) {
			assignBothParentGenos(modelNumber, families.get(0));


		}


		return new PedigreeSolution("done");
	}

	/*
	 * try the possible genotype pairs for the parents
	 *  this version is only for the root couple
	 *   - it tries genos for both parents
	 *   it logs successes 
	 */
	private void assignBothParentGenos(int modelNumber, Family f) {
		// first, figure out all the possible parent genotypes to run through
		Vector<GenotypePair> possibleParentGenoPairs = new Vector<GenotypePair>();
		Vector<String[]> fathersPossibleGenos = getPossibleGenotypes(modelNumber, f.getFather());
		Vector<String[]> mothersPossibleGenos = getPossibleGenotypes(modelNumber, f.getMother());
		Iterator<String[]> dadIt = fathersPossibleGenos.iterator();
		while (dadIt.hasNext()) {
			String[] dadPossible = dadIt.next();
			Iterator<String[]> momIt = mothersPossibleGenos.iterator();
			while (momIt.hasNext()) {
				String[] momPossible = momIt.next();
				possibleParentGenoPairs.add(new GenotypePair(momPossible, dadPossible));
			}
		}

		Iterator<GenotypePair> gpIt = possibleParentGenoPairs.iterator();
		while (gpIt.hasNext()) {
			GenotypePair gp = gpIt.next();
			/*
			 * this means this GP works all the way through the pedigree
			 * so we need to save it
			 */
			if (checkKids(modelNumber, f, gp)) logSuccessfulGenotypeSet(modelNumber);
		}
	}

	/*
	 * this is for when dad has assigned geno and need to try mom's possibilities
	 *   for use at non-root positions
	 */
	private boolean assignMomsGenos(int modelNumber, Family f, String[] dadsGeno) {
		// first, figure out all the possible parent genotypes to run through
		Vector<GenotypePair> possibleParentGenoPairs = new Vector<GenotypePair>();
		Vector<String[]> mothersPossibleGenos = getPossibleGenotypes(modelNumber, f.getMother());
		Iterator<String[]> momIt = mothersPossibleGenos.iterator();
		while (momIt.hasNext()) {
			String[] momPossible = momIt.next();
			possibleParentGenoPairs.add(new GenotypePair(momPossible, dadsGeno));
		}


		Iterator<GenotypePair> gpIt = possibleParentGenoPairs.iterator();
		while (gpIt.hasNext()) {
			GenotypePair gp = gpIt.next();
			/*
			 * this means this GP works so far
			 * so we need to return success
			 */
			if (checkKids(modelNumber, f, gp)) return true;
		}
		// didn't find any without an inconsistency
		return false;
	}

	/*
	 * this is for when mom has restricted geno and need to try dad's possibilities
	 *   for use at non-root positions
	 */
	private boolean assignDadsGenos(int modelNumber, Family f, String[] momsGeno) {
		// first, figure out all the possible parent genotypes to run through
		Vector<GenotypePair> possibleParentGenoPairs = new Vector<GenotypePair>();
		Vector<String[]> fathersPossibleGenos = getPossibleGenotypes(modelNumber, f.getFather());
		Iterator<String[]> dadIt = fathersPossibleGenos.iterator();
		while (dadIt.hasNext()) {
			String[] dadPossible = dadIt.next();
			possibleParentGenoPairs.add(new GenotypePair(momsGeno, dadPossible));
		}

		Iterator<GenotypePair> gpIt = possibleParentGenoPairs.iterator();
		while (gpIt.hasNext()) {
			GenotypePair gp = gpIt.next();
			/*
			 * this means this GP works so far
			 * so we need to return success
			 */
			if (checkKids(modelNumber, f, gp)) return true;
		}
		// didn't find any without an inconsistency
		return false;
	}

	/*
	 * check all the kids recursively
	 * 	return true if no inconsistencies
	 */
	private boolean checkKids(int modelNumber, Family f, GenotypePair gp) {
		// assign genos to parents
		f.getMother().genotype = gp.mothersGeno;
		f.getFather().genotype = gp.fathersGeno;
		
		System.out.println("Trying: mom(" + f.getMother().id + ")=" + f.getMother().getGenotypeAsString()
				+ "; dad(" + f.getFather().id + ")=" + f.getFather().getGenotypeAsString());

		// iterate thru the kids
		boolean success = false;
		Iterator<PelicanPerson> kidIt = f.getChildren().iterator();
		while (kidIt.hasNext()) {
			PelicanPerson child = kidIt.next();
			// loop over possible kid genos to see if possible
			Iterator<String[]> kidsPossibleGenoIt = getPossibleGenotypes(modelNumber, child).iterator();
			while (kidsPossibleGenoIt.hasNext()) {
				child.genotype = kidsPossibleGenoIt.next();
				System.out.println("\t testing kid(" + child.id + ")=" + child.getGenotypeAsString());
				// test it
				f.updatePossibleChildren();
				if (!f.isChildPossible(child)) {
					System.out.println("\t\tnot possible");
					success = false;
					continue;  // inconsistency, need to try new geno
				}

				/*
				 * see if married - then need to recurse
				 * but recurse with restricted set of genotypes
				 * for the "child" - since it's parents have known
				 * genotypes
				 */
				if (child.partner != null) {
					if (child.sex == PelicanPerson.male) {
						assignMomsGenos(modelNumber, f, child.genotype);
					} else {
						assignDadsGenos(modelNumber, f, child.genotype);
					}
				}
				/*
				 * if success so far, you've got a good set of genotype assignments
				 * need to move on to next kid
				 */
				success = true;
//				break;
			}
			// you've tried all possible kid genos for this kid
		}
		// if you've got thru all kids; you're done
		return success;
	}

	private void logSuccessfulGenotypeSet(int modelNumber) {
		StringBuffer b = new StringBuffer();
		String[] genos = new String[pedigree.size()];
		for (int i = 0; i < pedigree.size(); i++) {
			genos[i] = findPersonById(i + 1).getGenotypeAsString();
			b.append((i + 1) + ":" + genos[i].toString() + ";");
		}
		b.deleteCharAt(b.length() - 1);
		System.out.println(b.toString());
		successfulGenotypeSets[modelNumber].add(genos);
	}
	
	
	private PelicanPerson findPersonById(int id) {
		Iterator<PelicanPerson> pIt = pedigree.iterator();
		while (pIt.hasNext()) {
			PelicanPerson p = pIt.next();
			if (p.id == id) return p;
		}
		return null;
	}

	
	
	private Vector<String[]> getPossibleGenotypes(int modelNumber, PelicanPerson person) {
		Vector<String[]> possibleGenotypes = new Vector<String[]>();

		switch (modelNumber) {

		case 0:		// autosomal recessive
			if (person.affection == 2) {	// affected
				possibleGenotypes.add(new String[]{"a", "a"});
			} else {						// unaffected
				possibleGenotypes.add(new String[]{"A", "A"});
				possibleGenotypes.add(new String[]{"A", "a"});				
			}
			break;

		case 1:		// autosomal dominant
			if (person.affection == 2) {	// affected
				possibleGenotypes.add(new String[]{"B", "B"});
				possibleGenotypes.add(new String[]{"B", "b"});				
			} else {						// unaffected
				possibleGenotypes.add(new String[]{"b", "b"});
			}
			break;

		case 2:		// sex-linked recessive
			if (person.affection == 2) {	
				if (person.sex == 1) {		// affected male
					possibleGenotypes.add(new String[]{"Xd", "Y"});
				} else {					// affected female
					possibleGenotypes.add(new String[]{"Xd", "Xd"});
				}
			} else {
				if (person.sex == 1) {		// normal male
					possibleGenotypes.add(new String[]{"XD", "Y"});
				} else {					// normal female
					possibleGenotypes.add(new String[]{"XD", "XD"});
					possibleGenotypes.add(new String[]{"XD", "Xd"});
				}	
			}
			break;

		case 3:		// sex-linked dominant
			if (person.affection == 2) {	
				if (person.sex == 1) {		// affected male
					possibleGenotypes.add(new String[]{"XE", "Y"});
				} else {					// affected female
					possibleGenotypes.add(new String[]{"XE", "XE"});
					possibleGenotypes.add(new String[]{"XE", "Xe"});
				}
			} else {
				if (person.sex == 1) {		// normal male
					possibleGenotypes.add(new String[]{"Xe", "Y"});
				} else {					// normal female
					possibleGenotypes.add(new String[]{"Xe", "Xe"});
				}	
			}
			break;			
		}
		return possibleGenotypes;
	}

}
