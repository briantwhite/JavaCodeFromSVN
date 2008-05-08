package GeneticModels;

import java.util.ArrayList;

public abstract class TraitSet {
	
	ArrayList<Trait> traits;
	
	public TraitSet() {
		
	}
	
	public int getNumberOfMembers() {
		return traits.size();
	}

}
