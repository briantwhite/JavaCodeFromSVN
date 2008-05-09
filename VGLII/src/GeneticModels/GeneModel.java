package GeneticModels;

public abstract class GeneModel {
	
	CharacterSpecificationBank charSpecBank;
	TraitSet traitSet;
	
	boolean sexLinked;
	Phenotype[][] genoPhenoTable;
	
	public GeneModel(boolean sexLinked) {
		this.sexLinked = sexLinked;
		charSpecBank = CharacterSpecificationBank.getInstance();
		traitSet = charSpecBank.getRandomTraitSet();
		setupGenoPhenoTable();
	}
	
	public abstract void setupGenoPhenoTable();
	
	public abstract Phenotype getPhenotype(Allele a1, Allele a2);
	
	public abstract Allele[] getRandomAllelePair();
	
	public abstract String toString();
	
	public boolean isSexLinked() {
		return sexLinked;
	}
	
	public String getSexLinkageString() {
		if (sexLinked) {
			return "Sex-Linked";
		} else {
			return "Autosomal";
		}
	}
}
