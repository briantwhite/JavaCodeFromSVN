package GeneticModels;

public class GeneticModel {
	
	private ChromosomeModel autosomeModel;
	private ChromosomeModel sexChromosomeModel;
	
	public GeneticModel() {
		autosomeModel = new ChromosomeModel(false);
		sexChromosomeModel = new ChromosomeModel(true);
	}
	
	public Organism getRandomOrganism() {
		return null;
	}
	
	public Organism getOffspringOrganism(Organism mom, Organism dad) {
		return null;
	}

}
