package edu.umb.bio.jsGenex.client.requirements;

import edu.umb.bio.jsGenex.client.problems.GenexState;
//import org.jdom.Element;

public class ProteinLengthRequirement extends Requirement {
	
	private int desiredLength;
	
	/*public ProteinLengthRequirement(List<Element> elList) {
		Iterator<Element> elIt = elList.iterator();
		while (elIt.hasNext()) {
			Element e = elIt.next();
			if (e.getName().equals("Length")) desiredLength = Integer.parseInt(e.getTextTrim());
			if (e.getName().equals("FailString")) failureString = e.getTextTrim();
		}

	}*/
	
	public void setLength(int desiredLength) {
		this.desiredLength = desiredLength;
	}
	
	public ProteinLengthRequirement() {
	}

	public boolean isSatisfied(GenexState state) {
		return (state.getProteinSequence().length() == desiredLength);
	}

}
