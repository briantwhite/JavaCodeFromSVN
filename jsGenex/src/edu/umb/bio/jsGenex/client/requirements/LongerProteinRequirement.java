package edu.umb.bio.jsGenex.client.requirements;

import edu.umb.bio.jsGenex.client.problems.GenexState;
//import org.jdom.Element;

public class LongerProteinRequirement extends Requirement {

	/*public LongerProteinRequirement(List<Element> elList) {
		super(elList);
	}
	
	public LongerProteinRequirement() {
	}*/

	public boolean isSatisfied(GenexState state) {
		return (state.getProteinSequence().length() > state.getStartingProteinSequence().length());
	}

}
