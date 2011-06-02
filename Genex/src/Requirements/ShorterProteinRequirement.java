package Requirements;

import Problems.GenexState;

public class ShorterProteinRequirement extends Requirement {

	public boolean isSatisfied(GenexState state) {
		return (state.getProteinSequence().length() < state.getStartingProteinSequence().length());
	}

}
