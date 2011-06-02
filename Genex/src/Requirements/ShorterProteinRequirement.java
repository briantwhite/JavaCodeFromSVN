package Requirements;

import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import Problems.GenexState;

public class ShorterProteinRequirement extends Requirement {
	
	public ShorterProteinRequirement(List<Element> elList) {
		Iterator<Element> elIt = elList.iterator();
		while (elIt.hasNext()) {
			Element e = elIt.next();
			if (e.getName().equals("FailString")) failureString = e.getTextTrim();
		}
	}

	public boolean isSatisfied(GenexState state) {
		return (state.getProteinSequence().length() < state.getStartingProteinSequence().length());
	}

}
