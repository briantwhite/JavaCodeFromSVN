package edu.umb.bio.jsGenex.client.requirements;

import java.util.HashSet;

import edu.umb.bio.jsGenex.client.problems.GenexState;
//import org.jdom.Element;

public class BaseSelectionRequirement extends Requirement {

	private HashSet<Integer> desiredBaseList;

	/*public BaseSelectionRequirement(List<Element> elList) {
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
	}*/
	
	//TO DO
	public BaseSelectionRequirement() {
	}

	public boolean isSatisfied(GenexState state) {
		return desiredBaseList.contains(state.getSelectedBase());
	}

}
