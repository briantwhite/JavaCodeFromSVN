package utilities;

import molBiol.ExpressedGene;
import genetics.MutantGenerator;

// utility methods class for all to use
public class MGEUtilities {
	
	private Mutator mutator;
	
	public MGEUtilities() {
		mutator = new Mutator();
	}
	
	//mutation methods
	public ExpressedGene mutateGene(ExpressedGene eg) {
		return mutator.mutateGene(eg);
	}
	
	public String mutateDNASequence(String DNASequence) {
		return mutator.mutateDNASequence(DNASequence);
	}

}
