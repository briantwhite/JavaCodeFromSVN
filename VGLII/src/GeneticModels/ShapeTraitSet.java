package GeneticModels;

import java.util.ArrayList;

public class ShapeTraitSet extends TraitSet {
	
	public ShapeTraitSet() {
		traits = new ArrayList();
		traits.add(new ShapeTrait("Normal"));
		traits.add(new ShapeTrait("Long"));
		traits.add(new ShapeTrait("Short"));
		traits.add(new ShapeTrait("Bent"));
		traits.add(new ShapeTrait("Pointy"));
		traits.add(new ShapeTrait("Knobbed"));
		traits.add(new ShapeTrait("Zigzag"));
	}

}
