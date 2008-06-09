package GeneticModels;

import java.util.ArrayList;

import VGL.GeneticsException;

public class OrganismList {
	
	private ArrayList<Organism> organisms;
	private int numberOfMales;
	private int numberOfFemales;
	
	public OrganismList() {
		organisms = new ArrayList<Organism>();
		numberOfFemales = 0;
		numberOfMales = 0;
	}
	
	public void add(Organism o) {
		organisms.add(o);
		if (o.isMale()) {
			numberOfMales++;
		} else {
			numberOfFemales++;
		}
	}
	
	/**
	 * Return the organism which is at the given index.
	 * 
	 * @param index
	 *            the index
	 * @return the organism at the given index
	 */
	public Organism get(int index) {
		return organisms.get(index);
	}
	
	/**
	 * Return the organism which has the given id.
	 * 
	 * @param id
	 *            the organism's id
	 * @return the organism which has the given id
	 */
	public Organism find(int id) throws Exception {
		for (int i = 0; i < organisms.size(); i++) {
			Organism o = organisms.get(i);
			if (o.getId() == id)
				return o;
		}
		throw new GeneticsException("Cannot find Organism");
	}

	
	public int getNumberOfMales() {
		return numberOfMales;
	}
	
	public int getNumberOfFemales() {
		return numberOfFemales;
	}
	
	public int getTotalNumber() {
		return numberOfMales + numberOfFemales;
	}
	
	public ArrayList<Organism> getAllOrganisms() {
		return organisms;
	}

}
