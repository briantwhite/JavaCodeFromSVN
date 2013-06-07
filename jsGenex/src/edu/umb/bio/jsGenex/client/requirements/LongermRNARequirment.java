package edu.umb.bio.jsGenex.client.requirements;

import edu.umb.bio.jsGenex.client.problems.GenexState;
//import org.jdom.Element;

public class LongermRNARequirment extends Requirement {

	/*public LongermRNARequirment(List<Element> elList) {
		super(elList);
	}*/
	
	public LongermRNARequirment() {
	}

	public boolean isSatisfied(GenexState state) {
		return (state.getRnaSequence().length() > state.getStartingMatureMRNAsequence().length());
	}

}
