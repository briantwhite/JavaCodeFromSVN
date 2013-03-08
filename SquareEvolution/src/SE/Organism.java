package SE;

public abstract class Organism {
	private String dna;
	private GeneticCode geneticCode;
	
	public Organism(String dna, GeneticCode geneticCode) {
		this.dna = dna;
		this.geneticCode = geneticCode;
	}
	
	public GeneticCode getGeneticCode() {
		return geneticCode;
	}
	
	public String getDNA() {
		return dna;
	}

}
