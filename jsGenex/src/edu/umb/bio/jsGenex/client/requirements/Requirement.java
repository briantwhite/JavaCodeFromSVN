package edu.umb.bio.jsGenex.client.requirements;

import edu.umb.bio.jsGenex.client.problems.GenexState;
//import org.jdom.Element;

public abstract class Requirement {
	
	protected String failureString = "unassigned";
	
	/*public Requirement(List<Element> elList) {
		Iterator<Element> elIt = elList.iterator();
		while (elIt.hasNext()) {
			Element e = elIt.next();
			if (e.getName().equals("FailString")) failureString = e.getTextTrim();
		}
	}*/
	
	public Requirement() {
		
	}
	
	public void setFailureString(String failureString) {
		this.failureString = failureString;
	}
	
	public String getFailureString() {
		return failureString;
	}
	
	public abstract boolean isSatisfied(GenexState state);

}
