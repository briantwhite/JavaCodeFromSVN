package edu.umb.bio.genex.Requirements;

import java.util.List;

import com.google.gwt.user.client.Element;

import edu.umb.bio.genex.client.Problems.GenexState;

public class SingleMutationRequirement extends Requirement {
	
	public SingleMutationRequirement(List<Element> elList) {
		super(elList);
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
