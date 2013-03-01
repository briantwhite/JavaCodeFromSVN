package edu.umb.bio.genex.Requirements;

import java.util.List;

import com.google.gwt.user.client.Element;

import edu.umb.bio.genex.client.Problems.GenexState;

public class NoProteinRequirement extends Requirement {

	public NoProteinRequirement(List<Element> elList) {
		super(elList);
	}
	
	public boolean isSatisfied(GenexState state) {
		return (state.getProteinSequence().equals(""));
	}
	
}
