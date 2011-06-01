package Problems;

import java.util.HashSet;

public class Problem {
	
	private HashSet<Requirement> requirements;
	private String name;
	private int number;
	
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
	
	public String evaluate(GenexState gs) {
		return "OK";
	}

}
