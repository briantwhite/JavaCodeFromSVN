package GeneticModels;

import java.util.ArrayList;

public abstract class Character {

	ArrayList<TraitSet> possibleTraitSets;
	
	public Character() {
		possibleTraitSets = new ArrayList();
	}
	
	public int geteNumberOfMembers() {
		return possibleTraitSets.size();
	}
}
