package GeneticModels;

import java.util.ArrayList;

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
	
	public int getNumberOfMales() {
		return numberOfMales;
	}
	
	public int getNumberOfFemales() {
		return numberOfFemales;
	}
	
	public int getTotalNumber() {
		return numberOfMales + numberOfFemales;
	}
	

}
