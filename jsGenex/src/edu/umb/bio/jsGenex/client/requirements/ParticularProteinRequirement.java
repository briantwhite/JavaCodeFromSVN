package edu.umb.bio.jsGenex.client.requirements;

import edu.umb.bio.jsGenex.client.problems.GenexState;
//import org.jdom.Element;

public class ParticularProteinRequirement extends Requirement {
	
	private String desiredProtein;
	
	/*public ParticularProteinRequirement(List<Element> elList) {
		Iterator<Element> elIt = elList.iterator();
		while (elIt.hasNext()) {
			Element e = elIt.next();
			if (e.getName().equals("Protein")) desiredProtein = e.getTextTrim();
			if (e.getName().equals("FailString")) failureString = e.getTextTrim();
		}

	}*/
	
	public ParticularProteinRequirement() {
	}
	
	public void setProtein(String desiredPortein) {
		this.desiredProtein  = desiredProtein;
	}

	public boolean isSatisfied(GenexState state) {
		return (state.getProteinSequence().equals(desiredProtein));
	}

}
