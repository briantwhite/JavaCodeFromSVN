package GeneticModels;

import java.util.ArrayList;

public class Chromosome {

	private ArrayList<Allele> alleles;

	public Chromosome(ArrayList<Allele> alleles) {
		this.alleles = alleles;
	}

	public Chromosome(Chromosome c) {
		this(c.getAllAlleles());
	}

	public Allele getAllele(int i) {
		return alleles.get(i);
	}

	public ArrayList<Allele> getAllAlleles() {
		return alleles;
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		for(Allele a: alleles) {
			b.append(a.getTrait().getTraitName().toString()
					+ "-"
					+ a.getTrait().getBodyPart().toString()
					+ ";");
		}
		if (b.length() > 0) {
			b.deleteCharAt(b.length() - 1);
		}
		return b.toString();
	}

}
