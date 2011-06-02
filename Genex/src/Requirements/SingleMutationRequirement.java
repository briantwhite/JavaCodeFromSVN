package Requirements;

import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import Problems.GenexState;

public class SingleMutationRequirement extends Requirement {
	
	public SingleMutationRequirement(List<Element> elList) {
		Iterator<Element> elIt = elList.iterator();
		while (elIt.hasNext()) {
			Element e = elIt.next();
			if (e.getName().equals("FailString")) failureString = e.getTextTrim();
		}
	}

	public boolean isSatisfied(GenexState state) {
		String startingDNA = state.getStartingDNAsequence();
		String currentDNA = state.getCurrentDNASequence();
		if (startingDNA.length() != currentDNA.length()) return false;
		int numMutations = 0;
		for (int i = 0; i < startingDNA.length(); i++) {
			if (startingDNA.charAt(i) != currentDNA.charAt(i)) numMutations++;
		}
		if (numMutations == 1) return true;
		return false;
	}

}
