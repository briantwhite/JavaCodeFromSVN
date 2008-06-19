package VGL;

import java.util.ArrayList;

import GeneticModels.Cage;
import GeneticModels.GeneticModel;

public class ProcessedWorkFileResult {
	private GeneticModel geneticModel;
	private ArrayList<Cage> cages;
	
	public ProcessedWorkFileResult(GeneticModel geneticModel, ArrayList<Cage> cages) {
		this.geneticModel = geneticModel;
		this.cages = cages;
	}

	public GeneticModel getGeneticModel() {
		return geneticModel;
	}

	public ArrayList<Cage> getCages() {
		return cages;
	}
}
