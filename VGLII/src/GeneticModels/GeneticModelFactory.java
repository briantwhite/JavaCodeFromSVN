package GeneticModels;

import java.io.File;

public class GeneticModelFactory {
	
	private static GeneticModelFactory instance;
	
	private GeneticModelFactory() {
		
	}
	
	public static GeneticModelFactory getInstance() {
		if (instance == null) {
			instance = new GeneticModelFactory();
		}
		return instance;
	}

	public GeneticModel createRandomModel(File modelSpecFile) {
		return null;
	}
	
	public GeneticModel readModelFromFile(File workFile) {
		return null;
	}
	
	public GeneticModel createTestModel() {
		return null;
	}
}
