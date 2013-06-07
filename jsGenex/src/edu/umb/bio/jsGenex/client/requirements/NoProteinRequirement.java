package edu.umb.bio.jsGenex.client.requirements;

import edu.umb.bio.jsGenex.client.problems.GenexState;
//import org.jdom.Element;

public class NoProteinRequirement extends Requirement {

	/*public NoProteinRequirement(List<Element> elList) {
		super(elList);
	}*/
	
	public NoProteinRequirement() {
	}
	
	public boolean isSatisfied(GenexState state) {
		return (state.getProteinSequence().equals(""));
	}
	
}
