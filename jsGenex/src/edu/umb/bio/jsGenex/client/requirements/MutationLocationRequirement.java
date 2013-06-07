package edu.umb.bio.jsGenex.client.requirements;

import java.util.HashSet;

import edu.umb.bio.jsGenex.client.problems.GenexState;
//import org.jdom.Element;

/*
 * makes sure that the 5'-most mutation is in a specified range of bases
 */
public class MutationLocationRequirement extends Requirement {
	
	private HashSet<Integer> desiredLocations;
	
	/*public MutationLocationRequirement(List<Element> elList) {
		desiredLocations = new HashSet<Integer>();
		Iterator<Element> elIt = elList.iterator();
		while (elIt.hasNext()) {
			Element e = elIt.next();
			if (e.getName().equals("Locations")) {
				String[] bases = e.getTextTrim().split(",");
				for (int i = 0; i < bases.length; i++) {
					desiredLocations.add(new Integer(Integer.parseInt(bases[i])));
				}
			}
			if (e.getName().equals("FailString")) failureString = e.getTextTrim();
		}
	}*/
	
	//TO DO
	public MutationLocationRequirement() {
	}

	public boolean isSatisfied(GenexState state) {
		String startingDNA = state.getStartingDNAsequence();
		String currentDNA = state.getCurrentDNASequence();
		if (startingDNA.length() != currentDNA.length()) return false;
		int mutationLocation = -1;
		for (int i = 0; i < startingDNA.length(); i++) {
			if (startingDNA.charAt(i) != currentDNA.charAt(i)) {
				mutationLocation = i;
				break;
			}
		}
		if (desiredLocations.contains(new Integer(mutationLocation))) return true;
		return false;
	}

}
