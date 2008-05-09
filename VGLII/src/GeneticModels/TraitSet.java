package GeneticModels;

import java.util.ArrayList;
import java.util.Random;

public abstract class TraitSet {
	
	ArrayList<Trait> traits;
	
	public TraitSet() {
		
	}
	
	public int getNumberOfMembers() {
		return traits.size();
	}
	
	//pick a random one and delete it from the list
	public Trait getRandomTrait() {
		if (traits.size() == 0) {
			return null;
		}
		Random r = new Random();
		int i = r.nextInt(traits.size());
		Trait t = traits.get(i);
		traits.remove(i);
		return t;
	}

}
