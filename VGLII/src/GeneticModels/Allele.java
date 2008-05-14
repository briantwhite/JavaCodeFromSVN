package GeneticModels;

public class Allele implements Cloneable {
		
	private int intVal;
	private Trait trait;
	
	public Allele(Trait trait, int intVal) {
		this.trait = trait;
		this.intVal = intVal;
	}
	
	public Object clone() {
		Allele a = null;
		try {
			a = (Allele)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		a.trait = (Trait)a.trait.clone();
		return a;
	}
	
	// make into a null allele (for Y or W chromo)
	//  keep all the trait details (type &body part)
	//  but set traitName = "-"
	public static Allele makeNullVersion(Allele a) {
		Allele newAllele = (Allele)a.clone();
		newAllele.intVal = 0;
		newAllele.trait.setTraitName("-");
		return newAllele;
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
