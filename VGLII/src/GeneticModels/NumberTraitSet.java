package GeneticModels;

import java.util.ArrayList;

public class NumberTraitSet extends TraitSet {
	
	public NumberTraitSet() {
		traits = new ArrayList();
		traits.add(new NumberTrait("One"));
		traits.add(new NumberTrait("Two"));
		traits.add(new NumberTrait("Three"));
		traits.add(new NumberTrait("Four"));
		traits.add(new NumberTrait("Five"));
		traits.add(new NumberTrait("Six"));
	}

}
