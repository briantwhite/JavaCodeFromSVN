package edu.umb.bio.jsGenex.client.requirements;

import edu.umb.bio.jsGenex.client.problems.GenexState;
//import org.jdom.Element;

public class ShortermRNARequirement extends Requirement {

	/*public ShortermRNARequirement(List<Element> elList) {
		super(elList);
	}*/
	
	public ShortermRNARequirement() {
	}

	public boolean isSatisfied(GenexState state) {
		return (state.getRnaSequence().length() < state.getStartingMatureMRNAsequence().length());
	}

}
