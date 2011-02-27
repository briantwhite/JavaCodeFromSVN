package Grader;

import java.io.File;

import GeneticModels.GeneticModel;
import ModelBuilder.ModelBuilderUI;

public class Grader {
	
	private File workingDir;
	private GeneticModel geneticModel;
	private ModelBuilderUI modelBuilder;
	
	public Grader(File workingDir, GeneticModel geneticModel, ModelBuilderUI modelBuilder) {
		this.workingDir = workingDir;
		this.geneticModel = geneticModel;
		this.modelBuilder = modelBuilder;
	}

}
