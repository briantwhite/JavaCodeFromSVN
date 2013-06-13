package edu.umb.bio.jsGenex.client.problems;

import java.util.HashSet;
import java.util.Iterator;

import edu.umb.bio.jsGenex.client.requirements.Requirement;

public class Problem {

	private HashSet<Requirement> requirements;
	private String name;
	private int number;
	private String description;

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


	/*
	 * if any of the requirements fail, this fails
	 */
	public String evaluate(GenexState gs) {
		StringBuffer failBuffer = new StringBuffer();
		Iterator<Requirement> rIt = requirements.iterator();
		boolean satisfied = true;
		while (rIt.hasNext()) {
			Requirement r = rIt.next();
			if (!r.isSatisfied(gs)) {
				satisfied = false;
				failBuffer.append(r.getFailureString());
				failBuffer.append("<br>");
			}
		}
		if (satisfied) {
			return "OK";
		} else {
			return failBuffer.toString();
		}
	}

}
