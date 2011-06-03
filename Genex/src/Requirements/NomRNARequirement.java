package Requirements;

import java.util.List;

import org.jdom.Element;

import Problems.GenexState;

public class NomRNARequirement extends Requirement {
	
	public NomRNARequirement(List<Element> elList) {
		super(elList);
	}

	public boolean isSatisfied(GenexState state) {
		return (state.getRnaSequence().equals(""));
	}

}
