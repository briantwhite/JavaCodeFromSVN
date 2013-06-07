package edu.umb.bio.jsGenex.client.requirements;

import edu.umb.bio.jsGenex.client.problems.GenexState;
//import org.jdom.Element;

public class ShorterProteinRequirement extends Requirement {
	
	/*public ShorterProteinRequirement(List<Element> elList) {
		super(elList);
	}*/
	
	public ShorterProteinRequirement() {
	}

	public boolean isSatisfied(GenexState state) {
		return (state.getProteinSequence().length() < state.getStartingProteinSequence().length());
	}

}
