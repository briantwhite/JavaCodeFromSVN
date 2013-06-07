package edu.umb.bio.jsGenex.client.requirements;

import edu.umb.bio.jsGenex.client.problems.GenexState;
//import org.jdom.Element;

public class IntronNumberRequirement extends Requirement {
	
	private int desiredNumber;
	
	/*public IntronNumberRequirement(List<Element> elList) {
		Iterator<Element> elIt = elList.iterator();
		while (elIt.hasNext()) {
			Element e = elIt.next();
			if (e.getName().equals("Number")) desiredNumber = Integer.parseInt(e.getTextTrim());
			if (e.getName().equals("FailString")) failureString = e.getTextTrim();
		}

	}*/
	
	public void setNumber(int desiredNumber) {
		this.desiredNumber = desiredNumber;
	}
	
	public IntronNumberRequirement() {
	}

	public boolean isSatisfied(GenexState state) {
		return (state.getNumberOfExons() == desiredNumber + 1);
	}

}
