package YeastVGL;

public class State {
	private MutantSet mutantSet;
	private Object[][] complementationTableData;
	private boolean[] workingSetChoices;
	
	public State(MutantSet mutantSet, Object[][] complementationTableData, boolean[] workingSetChoices) {
		this.mutantSet = mutantSet;
		this.complementationTableData = complementationTableData;
		this.workingSetChoices = workingSetChoices;
	}
	
	public MutantSet getMutantSet() {
		return mutantSet;
	}
	
	public Object[][] getComplementationTableData() {
		return complementationTableData;
	}
	
	public boolean[] getWorkingSetChoices() {
		return workingSetChoices;
	}

}
