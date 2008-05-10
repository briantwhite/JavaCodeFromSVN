package GeneticModels;

import java.util.ArrayList;
import java.util.Random;

public class CharacterSpecification {

	ArrayList<TraitSet> possibleTraitSets;
	
	public CharacterSpecification() {
		possibleTraitSets = new ArrayList<TraitSet>();
	}
	
	public void add(TraitSet ts) {
		possibleTraitSets.add(ts);
	}
	
	public int geteNumberOfMembers() {
		return possibleTraitSets.size();
	}
	
	//get TraitSet and remove it from list to prevent re-use
	public TraitSet getRandomTraitSet() {
		if (possibleTraitSets.size() == 0) {
			return null;
		}
		
		Random r = new Random();
		int i = r.nextInt(possibleTraitSets.size());
		TraitSet ts = possibleTraitSets.get(i);
		possibleTraitSets.remove(i);
		return ts;
	}
}
