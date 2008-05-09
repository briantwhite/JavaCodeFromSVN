package GeneticModels;

import java.util.ArrayList;

public class ColorTraitSet extends TraitSet {
	
	public ColorTraitSet(String bodyPart) {
		traits = new ArrayList();
		traits.add(new ColorTrait("Red", bodyPart));
		traits.add(new ColorTrait("Green", bodyPart));
		traits.add(new ColorTrait("Blue", bodyPart));
		traits.add(new ColorTrait("Yellow", bodyPart));
		traits.add(new ColorTrait("Purple", bodyPart));
		traits.add(new ColorTrait("Black", bodyPart));
		traits.add(new ColorTrait("Brown", bodyPart));
	}
	
}
