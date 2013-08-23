package edu.umb.jsPedigrees.client.PE;

import java.util.ArrayList;

public class GenotypeSet {
	
	private ArrayList<String[]> sets;
	
	public GenotypeSet() {
		sets = new ArrayList<String[]>();
	}
	
	public void add(String[] set) {
		sets.add(set);
	}
	
	public String[] get(int i) {
		return sets.get(i);
	}
	
	public void setAll(ArrayList<String[]> newOnes) {
		sets = newOnes;
	}
	
	public ArrayList<String[]> getAll() {
		return sets;
	}
	
}
