package YeastVGL;

import Biochemistry.MutantSet;
import PathwayPanel.SavedPathwayDrawingState;

public class State {
	private MutantSet mutantSet;
	private Object[][] complementationTableData;
	private boolean[] workingSetChoices;
	private SavedPathwayDrawingState pathwayDrawingState;


	/*
	 * because, when you drag columns in the the JTable, it only changes the way the columns are DISPLAYED
	 * not the underlying column order (but it does swap the rows), you need to save the displayed state not
	 * the underlying state. 
	 * For this, use the column 1 headings to get a table of scrambled columns and save the data in the
	 * form as displayed - that way, when you load, it will look fine.
	 */
	public State(MutantSet mutantSet, 
			Object[][] rawTableData, 
			boolean[] workingSetChoices, 
			SavedPathwayDrawingState pathwayDrawingState) {
		this.mutantSet = mutantSet;
		this.workingSetChoices = workingSetChoices;
		this.pathwayDrawingState = pathwayDrawingState;

		complementationTableData = new Object[rawTableData.length][rawTableData[0].length];
		for (int r = 0; r < rawTableData.length; r++) {
			for (int c = 0; c < rawTableData[0].length; c++) {
				if ((c == 0) || (c == (rawTableData[0].length - 1))) {
					complementationTableData[r][c] = rawTableData[r][c];
				} else {
					/*
					 * there's some voodoo here with the -1 and +1 
					 * it's because the column numbering and mutant row numbering are strange
					 * anyway, it works...
					 */
					int newC = Integer.parseInt(((String)rawTableData[c - 1][0]).substring(1)) + 1;
					complementationTableData[r][c] = rawTableData[r][newC];
				}
			}
		}
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
	
	public SavedPathwayDrawingState getSavedPathwayDrawingState() {
		return pathwayDrawingState;
	}

}
