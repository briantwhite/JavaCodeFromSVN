package Requirements;

import java.util.List;

import org.jdom.Element;

import Problems.GenexState;

public class LongermRNARequirment extends Requirement {

	public LongermRNARequirment(List<Element> elList) {
		super(elList);
	}

	public boolean isSatisfied(GenexState state) {
		return (state.getRnaSequence().length() > state.getStartingMatureMRNAsequence().length());
	}

}
