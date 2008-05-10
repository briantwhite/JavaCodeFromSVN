package GeneticModels;

public class Allele {
		
	private int intVal;
	private Trait trait;
	
	public Allele(Trait trait, int intVal) {
		this.trait = trait;
		this.intVal = intVal;
	}
		
	public Allele makeNullAllele(Allele a) {
		return new Allele(Trait.getNullVersion(a.getTrait()), 0);
	}
	
	public Trait getTrait() {
		return trait;
	}

	public int getIntVal() {
		return intVal;
	}
	
	public String toString() {
		return "#" + intVal + " " + trait.toString();
	}
}
