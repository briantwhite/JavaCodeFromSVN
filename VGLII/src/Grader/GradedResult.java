package Grader;

import java.util.ArrayList;

import org.jdom.Element;

import GeneticModels.Cage;
import GeneticModels.GeneticModel;

public class GradedResult {
	
	private GeneticModel geneticModel;
	private ArrayList<Cage> cageSet;
	private Element modelBuilderState;
	
	public GradedResult(
			GeneticModel geneticModel, 
			ArrayList<Cage> cageSet,
			Element modelBuilderState) {
		
		this.geneticModel = geneticModel;
		this.cageSet = cageSet;
		this.modelBuilderState = modelBuilderState;
	}

	public GeneticModel getGeneticModel() {
		return geneticModel;
	}
	
	public ArrayList<Cage> getCageSet() {
		return cageSet;
	}
	
	public Element getModelBuilderState() {
		return modelBuilderState;
	}
}
