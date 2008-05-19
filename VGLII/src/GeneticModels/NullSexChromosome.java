package GeneticModels;

import java.util.ArrayList;

public class NullSexChromosome extends Chromosome {
	
	private static NullSexChromosome instance;

	private NullSexChromosome() {
		super(new NullAlleleList());
	}
	
	public static NullSexChromosome getInstance() {
		if (instance == null) {
			instance = new NullSexChromosome();
		}
		return instance;
	}
	
	public String toString() {
		return "null sex chromosome";
	}
	

}
