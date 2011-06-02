package Problems;

import java.util.HashSet;
import java.util.Iterator;

import Requirements.Requirement;

public class Problem {

	private HashSet<Requirement> requirements;
	private String name;
	private int number;
	private String description;
	private String failureString;

	public Problem() {
		requirements = new HashSet<Requirement>();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}

	public void addRequirement(Requirement r) {
		requirements.add(r);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setFailureString(String fail) {
		failureString = fail;
	}

	public String getFailureString() {
		return failureString;
	}

	/*
	 * if any of the requirements fail, this fails
	 */
	public String evaluate(GenexState gs) {
		Iterator<Requirement> rIt = requirements.iterator();
		boolean satisfied = true;
		while (rIt.hasNext()) {
			Requirement r = rIt.next();
			if (!r.isSatisfied(gs)) satisfied = false;
		}
		if (satisfied) {
			return "OK";
		} else {
			return failureString;
		}
	}

}
