package edu.umb.bio.genex.Requirements;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.Element;

import edu.umb.bio.genex.client.Problems.GenexState;

public class ProteinLengthRequirement extends Requirement {
	
	private int desiredLength;
	
	public ProteinLengthRequirement(List<Element> elList) {
		Iterator<Element> elIt = elList.iterator();
		while (elIt.hasNext()) {
			Element e = elIt.next();
			if (e.getName().equals("Length")) desiredLength = Integer.parseInt(e.getTextTrim());
			if (e.getName().equals("FailString")) failureString = e.getTextTrim();
		}

	}

	public boolean isSatisfied(GenexState state) {
		return (state.getProteinSequence().length() == desiredLength);
	}

}
