package edu.umb.bio.genex.Requirements;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.Element;

import edu.umb.bio.genex.client.Problems.GenexState;

public abstract class Requirement {
	
	protected String failureString = "unassigned";
	
	public Requirement(List<Element> elList) {
		Iterator<Element> elIt = elList.iterator();
		while (elIt.hasNext()) {
			Element e = elIt.next();
			if (e.getName().equals("FailString")) failureString = e.getTextTrim();
		}
	}
	
	public Requirement() {}
	
	public String getFailureString() {
		return failureString;
	}
	
	public abstract boolean isSatisfied(GenexState state);

}
