package edu.umb.bio.jsGenex.client.requirements;

import edu.umb.bio.jsGenex.client.problems.GenexState;
//import org.jdom.Element;

public class SingleMutationRequirement extends Requirement {
	
	/*public SingleMutationRequirement(List<Element> elList) {
		super(elList);
	}*/
	
	public SingleMutationRequirement() {
	}

	public boolean isSatisfied(GenexState state) {
		String startingDNA = state.getStartingDNAsequence();
		String currentDNA = state.getCurrentDNASequence();
		if (startingDNA.length() != currentDNA.length()) return false;
		int numMutations = 0;
		for (int i = 0; i < startingDNA.length(); i++) {
			if (startingDNA.charAt(i) != currentDNA.charAt(i)) numMutations++;
		}
		if (numMutations == 1) return true;
		return false;
	}
}
