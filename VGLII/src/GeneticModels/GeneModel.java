package GeneticModels;

import java.util.Random;

public abstract class GeneModel {
	
	CharacterSpecificationBank charSpecBank;
	TraitSet traitSet;
	
	Random rand;
	
	Phenotype[][] genoPhenoTable;
	
	public GeneModel() {
		rand = new Random();
		charSpecBank = CharacterSpecificationBank.getInstance();
		traitSet = charSpecBank.getRandomTraitSet();
		setupGenoPhenoTable();
	}
	
	public abstract void setupGenoPhenoTable();
	
	public abstract Phenotype getPhenotype(Allele a1, Allele a2);
	
	public abstract Allele[] getRandomAllelePair();
	
	public abstract String toString();
	
}
