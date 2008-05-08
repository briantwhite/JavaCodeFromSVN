package GeneticModels;

import java.util.ArrayList;

public class ColorTraitSet extends TraitSet {
	
	public ColorTraitSet() {
		traits = new ArrayList();
		traits.add(new ColorTrait("Red"));
		traits.add(new ColorTrait("Green"));
		traits.add(new ColorTrait("Blue"));
		traits.add(new ColorTrait("Yellow"));
		traits.add(new ColorTrait("Purple"));
		traits.add(new ColorTrait("Black"));
		traits.add(new ColorTrait("Brown"));
	}
	
	public int getNumberOfMembers() {
		return traits.size();
	}
	

}
