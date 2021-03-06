package Biochemistry;
import java.util.ArrayList;

public class Molecule {
	private int number;
	private ArrayList<Enzyme> nextEnzymeList;
	private boolean terminal;
	private String name;		// what it's called in the student-drawn pathway
	
	public Molecule(int number) {
		this.number = number;
		this.nextEnzymeList = new ArrayList<Enzyme>();
		terminal = false;
	}
	
	public void addNextEnzyme(Enzyme e) {
		nextEnzymeList.add(e);
	}
	
	public int getNumber() {
		return number;
	}
	
	public ArrayList<Enzyme> getNextEnzymeList() {
		return nextEnzymeList;
	}
	
	public void setTerminal() {
		this.terminal = true;
	}

	public boolean isTerminal() {
		return terminal;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isEquivalentTo(Molecule m) {
		if (number != m.getNumber()) {
			return false;
		}
		
		for (int i = 0; i < nextEnzymeList.size(); i++) {
			boolean foundIt = false;
			for (int j = 0; j < m.getNextEnzymeList().size(); j++) {
				if (nextEnzymeList.get(i).getNumber() == m.getNextEnzymeList().get(j).getNumber()) {
					foundIt = true;
				}
			}
			if (!foundIt) {
				return false;
			}
		}
		return true;
	}
}
