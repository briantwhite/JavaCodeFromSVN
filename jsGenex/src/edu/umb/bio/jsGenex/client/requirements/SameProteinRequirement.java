package edu.umb.bio.jsGenex.client.requirements;

import edu.umb.bio.jsGenex.client.problems.GenexState;
//import org.jdom.Element;

public class SameProteinRequirement extends Requirement {
	
	/*public SameProteinRequirement(List<Element> elList) {
		super(elList);
	}*/
	
	public SameProteinRequirement() {
	}

	public boolean isSatisfied(GenexState state) {
		return (state.getProteinSequence().equals(state.getStartingProteinSequence()));
	}

}
