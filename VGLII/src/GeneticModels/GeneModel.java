package GeneticModels;

public abstract class GeneModel {
	
	CharacterSpecificationBank charSpecBank;
	TraitSet traitSet;
	
	Phenotype[][] genoPhenoTable;
	
	public GeneModel() {
		charSpecBank = CharacterSpecificationBank.getInstance();
		traitSet = charSpecBank.getRandomTraitSet();
		setupGenoPhenoTable();
	}
	
	public abstract void setupGenoPhenoTable();
	
	public abstract Phenotype getPhenotype(Allele a1, Allele a2);
	
	public abstract Allele[] getRandomAllelePair();
	
	public abstract String toString();
	
}
