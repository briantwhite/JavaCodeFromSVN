package Requirements;

import java.util.List;

import org.jdom.Element;

import Problems.GenexState;

public class NoProteinRequirement extends Requirement {

	public NoProteinRequirement(List<Element> elList) {
		super(elList);
	}
	
	public boolean isSatisfied(GenexState state) {
		return (state.getProteinSequence().equals(""));
	}
	
}
