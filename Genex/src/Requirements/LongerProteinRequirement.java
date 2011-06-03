package Requirements;

import java.util.List;

import org.jdom.Element;

import Problems.GenexState;

public class LongerProteinRequirement extends Requirement {

	public LongerProteinRequirement(List<Element> elList) {
		super(elList);
	}

	public boolean isSatisfied(GenexState state) {
		return (state.getProteinSequence().length() > state.getStartingProteinSequence().length());
	}

}
