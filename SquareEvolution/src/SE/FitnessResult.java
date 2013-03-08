package SE;

public class FitnessResult {
	
	private String bestDNA;
	private double maxFitness;
	private ProteinDatabaseEntry pde;
	private double totalFitness;
	private double[] fitnesses;
	private double[] cumulativeFitnesses;
	private int numberOfDNASpecies;
	private double shannonDNADiversity;
	private int numberOfProtSpecies;
	private double shannonProtDiversity;
	
	public FitnessResult(
			String bestDNA,
			double maxFitness, 
			ProteinDatabaseEntry bestPDE,
			double[] fitnesses,
			double[] cumulativeFitnesses,
			double totalFitness,
			int numberOfDNASpecies,
			double shannonDNADiversity,
			int numberOfProtSpecies,
			double shannonProtDiversity) {
		this.bestDNA = bestDNA;
		this.maxFitness = maxFitness;
		this.pde = bestPDE;
		this.fitnesses = fitnesses;
		this.cumulativeFitnesses = cumulativeFitnesses;
		this.totalFitness = totalFitness;
		this.numberOfDNASpecies = numberOfDNASpecies;
		this.shannonDNADiversity = shannonDNADiversity;
		this.numberOfProtSpecies = numberOfProtSpecies;
		this.shannonProtDiversity = shannonProtDiversity;
	}
	
	public String getBestDNA() {
		return bestDNA;
	}

	public double getMaxFitness() {
		return maxFitness;
	}

	public ProteinDatabaseEntry getBestEntry() {
		return pde;
	}
	
	public double[]	getFitnesses() {
		return fitnesses;
	}
	
	public double[] getCumulativeFitnesses() {
		return cumulativeFitnesses;
	}
	
	public double getTotalFitness() {
		return totalFitness;
	}
	
	public int getNumberOfDNASpecies() {
		return numberOfDNASpecies;
	}
	
	public double getShannonDNADiversity() {
		return shannonDNADiversity;
	}

	public int getNumberOfProtSpecies() {
		return numberOfProtSpecies;
	}
	
	public double getShannonProtDiversity() {
		return shannonProtDiversity;
	}
}
