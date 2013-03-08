package SE;

class ProteinAndFitness {
	private String protein;
	private double fitness;

	ProteinAndFitness(String protein, double fitness) {
		this.protein = protein;
		this.fitness = fitness;
	}
	
	public String toString() {
		return protein + "," + String.valueOf(fitness);
	}
}
