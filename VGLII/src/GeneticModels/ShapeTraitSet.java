package GeneticModels;

import java.util.ArrayList;

public class ShapeTraitSet extends TraitSet {
	
	public ShapeTraitSet(String bodyPart) {
		traits = new ArrayList<Trait>();
		traits.add(new ShapeTrait("Normal", bodyPart));
		traits.add(new ShapeTrait("Long", bodyPart));
		traits.add(new ShapeTrait("Short", bodyPart));
		traits.add(new ShapeTrait("Bent", bodyPart));
		traits.add(new ShapeTrait("Pointy", bodyPart));
		traits.add(new ShapeTrait("Knobbed", bodyPart));
		traits.add(new ShapeTrait("Zigzag", bodyPart));
	}

}
