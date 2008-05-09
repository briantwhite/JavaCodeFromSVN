package GeneticModels;

import java.util.ArrayList;

public class NumberTraitSet extends TraitSet {
	
	public NumberTraitSet(String bodyPart) {
		traits = new ArrayList();
		traits.add(new NumberTrait("One", bodyPart));
		traits.add(new NumberTrait("Two", bodyPart));
		traits.add(new NumberTrait("Three", bodyPart));
		traits.add(new NumberTrait("Four", bodyPart));
		traits.add(new NumberTrait("Five", bodyPart));
		traits.add(new NumberTrait("Six", bodyPart));
	}

}
