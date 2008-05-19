package GeneticModels;

public class Allele {
		
	private int intVal;
	private Trait trait;
	
	public Allele(Trait trait, int intVal) {
		this.trait = trait;
		this.intVal = intVal;
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
