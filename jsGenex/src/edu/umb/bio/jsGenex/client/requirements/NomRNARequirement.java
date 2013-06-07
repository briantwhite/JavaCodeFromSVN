package edu.umb.bio.jsGenex.client.requirements;

import edu.umb.bio.jsGenex.client.problems.GenexState;
//import org.jdom.Element;

public class NomRNARequirement extends Requirement {
	
	/*public NomRNARequirement(List<Element> elList) {
		super(elList);
	}*/
	
	public NomRNARequirement() {
	}

	public boolean isSatisfied(GenexState state) {
		return (state.getRnaSequence().equals(""));
	}

}
