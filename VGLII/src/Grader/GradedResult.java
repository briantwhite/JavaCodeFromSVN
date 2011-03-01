package Grader;

import org.jdom.Element;

import GeneticModels.GeneticModel;

public class GradedResult {
	
	private GeneticModel geneticModel;
	private Element modelBuilderState;
	
	public GradedResult(GeneticModel geneticModel, Element modelBuilderState) {
		this.geneticModel = geneticModel;
		this.modelBuilderState = modelBuilderState;
		
	}

}
