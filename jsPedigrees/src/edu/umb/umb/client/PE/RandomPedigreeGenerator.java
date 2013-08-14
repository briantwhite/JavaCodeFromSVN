package PE;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import Pelican.Pelican;
import Pelican.PelicanPerson;

public class RandomPedigreeGenerator {

	private Pelican pelican;
	private static String[] DEFAULT_GENO = {"?", "?"};

	public RandomPedigreeGenerator(Pelican pelican) {
		this.pelican = pelican;
	}

	public Vector<PelicanPerson> generateRandomPedigree() {
		Random r = new Random();
		Vector<PelicanPerson> pedigree = new Vector<PelicanPerson>();
		int numPeople = r.nextInt(15);
		int currentPersonId = 1;

		// first mom
		PelicanPerson topMom = new PelicanPerson(pelican, currentPersonId, null, null, PelicanPerson.female, PelicanPerson.unaffected, "", 0, DEFAULT_GENO);
		pedigree.add(topMom);
		currentPersonId++;

		// then dad
		PelicanPerson topDad = new PelicanPerson(pelican, currentPersonId, null, null, PelicanPerson.male, PelicanPerson.unaffected, "", 0, DEFAULT_GENO);
		pedigree.add(topDad);
		currentPersonId++;

		// their kids
		int numGen1Kids = r.nextInt(5) + 1;
		for (int i = 0; i < numGen1Kids; i++) {
			if (r.nextBoolean()) {
				pedigree.add(new PelicanPerson(pelican, currentPersonId, topDad, topMom, PelicanPerson.female, PelicanPerson.unaffected, "", 1, DEFAULT_GENO));
				currentPersonId++;
			} else {
				pedigree.add(new PelicanPerson(pelican, currentPersonId, topDad, topMom, PelicanPerson.male, PelicanPerson.unaffected, "", 1, DEFAULT_GENO));
				currentPersonId++;
			}
		}

		/*
		 * pick up to two of them to be parents of next generation
		 *    remember that, in the Vector, index 0 = mom; 1 = dad
		 *    so these kids indices in the Vector are 2 thru (numGen1Kids + 1)
		 */
		PelicanPerson chosenGen1Kid = pedigree.elementAt(r.nextInt(numGen1Kids) + 1);
		System.out.println("gen 1 kids id =" + chosenGen1Kid.id);
		PelicanPerson gen1KidsSpouse = null;
		PelicanPerson gen2sMom = null;
		PelicanPerson gen2sDad = null;
		if (chosenGen1Kid.sex == PelicanPerson.male) {
			gen1KidsSpouse = new PelicanPerson(pelican, currentPersonId, null, null, PelicanPerson.female, PelicanPerson.unaffected, "", 1, DEFAULT_GENO);
			currentPersonId++;
			gen2sDad = chosenGen1Kid;
			gen2sMom = gen1KidsSpouse;
		} else {
			gen1KidsSpouse = new PelicanPerson(pelican, currentPersonId, null, null, PelicanPerson.male, PelicanPerson.unaffected, "", 1, DEFAULT_GENO);
			currentPersonId++;
			gen2sDad = gen1KidsSpouse;
			gen2sMom = chosenGen1Kid;
		}
		// give them some kids
//		int numGen2Kids = r.nextInt(5) + 1;
//		for (int i = 0; i < numGen2Kids; i++) {
//			if (r.nextBoolean()) {
//				pedigree.add(new PelicanPerson(pelican, currentPersonId, gen2sDad, gen2sMom, PelicanPerson.female, PelicanPerson.unaffected, "", 2, DEFAULT_GENO));
//				currentPersonId++;
//			} else {
//				pedigree.add(new PelicanPerson(pelican, currentPersonId, gen2sDad, gen2sMom, PelicanPerson.male, PelicanPerson.unaffected, "", 2, DEFAULT_GENO));
//				currentPersonId++;
//			}
//		}

		Iterator<PelicanPerson> pIt = pedigree.iterator();
		while (pIt.hasNext()) {
			System.out.println(pIt.next().toString());
		}
		return pedigree;
	}

}
