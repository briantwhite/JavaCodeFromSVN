package edu.umb.bio.genex.Requirements;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.Element;

import edu.umb.bio.genex.client.Problems.GenexState;

public class BaseSelectionRequirement extends Requirement {

	private HashSet<Integer> desiredBaseList;

	public BaseSelectionRequirement(List<Element> elList) {
		desiredBaseList = new HashSet<Integer>();
		Iterator<Element> elIt = elList.iterator();
		while (elIt.hasNext()) {
			Element e = elIt.next();
			if (e.getName().equals("Bases")) {
				String[] bases = e.getTextTrim().split(",");
				for (int i = 0; i < bases.length; i++) {
					desiredBaseList.add(new Integer(Integer.parseInt(bases[i])));
				}
			}
			if (e.getName().equals("FailString")) failureString = e.getTextTrim();
		}
	}

	public boolean isSatisfied(GenexState state) {
		return desiredBaseList.contains(state.getSelectedBase());
	}

}
