package Requirements;

import java.util.List;

import org.jdom.Element;

import Problems.GenexState;

public class SameProteinRequirement extends Requirement {
	
	public SameProteinRequirement(List<Element> elList) {
		super(elList);
	}

	public boolean isSatisfied(GenexState state) {
		return (state.getProteinSequence().equals(state.getStartingProteinSequence()));
	}

}
