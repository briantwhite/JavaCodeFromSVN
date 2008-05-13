package GeneticModels;

import java.util.ArrayList;

public class Chromosome {
	
	private ArrayList<Allele> alleles;
	
	public Chromosome(ArrayList<Allele> alleles) {
		this.alleles = alleles;
	}
	
	public Allele getAllele(int i) {
		return alleles.get(i);
	}
	
	public ArrayList<Allele> getAllAlleles() {
		return alleles;
	}

}
