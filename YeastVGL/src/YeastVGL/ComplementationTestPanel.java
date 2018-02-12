package YeastVGL;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ComplementationTestPanel extends JPanel {
	
	private JTable complementationTable;
	private Pathway pathway;
	private MutantSet mutantSet;
	private int numMutants;
	private int numEnzymes;
	
	public ComplementationTestPanel(YeastVGL yeastVGL) {
		pathway = yeastVGL.getPathway();
		mutantSet = yeastVGL.getMutantSet();
		numMutants = mutantSet.getNumberOfMutants();
		numEnzymes = yeastVGL.getPathway().getNumberOfEnzymes();
		
		String[] mutantNames = new String[numMutants];
		String[] columnHeadings = new String[numMutants + 1];
		columnHeadings[0] = new String("");
		for (int i = 0; i < numMutants; i++) {
			mutantNames[i] = new String("M" + i);
			columnHeadings[i + 1] = new String("M" + i);
		}
		
		Object[][] data = new Object[numMutants][numMutants + 1];
		for (int row = 0; row < numMutants; row++) {
			for (int col = 0; col < (numMutants + 1); col++) {
				if (col == 0) {
					data[row][col] = new String(mutantNames[row]);
				} else {
					data[row][col] = willDiploidGrow(row, col - 1, new ArrayList<Integer>(new Integer(0)));
				}
			}
		}
		complementationTable = new JTable(data, columnHeadings);
		JScrollPane tablePane = new JScrollPane(complementationTable);
		complementationTable.setFillsViewportHeight(true);
		this.add(tablePane);
	}

	private String willDiploidGrow(int m1num, int m2num, ArrayList<Integer>startingMolecules) {
		// combine both genotypes - assume that functional ("true") is dominant
		boolean[] diploidEffectiveGenotype = new boolean[numEnzymes];
		for (int i = 0; i < numEnzymes; i++) {
			diploidEffectiveGenotype[i] = false;
		}
		for (int i = 0; i < numEnzymes; i++) {
			if ((mutantSet.getMutantStrains()[m1num].getGenotype()[i]) 
				|| (mutantSet.getMutantStrains()[m2num].getGenotype()[i])) {
				diploidEffectiveGenotype[i] = true;
			}
		}
		if (pathway.willItGrow(diploidEffectiveGenotype, startingMolecules)) {
			return "+";
		} else {
			return "-";
		}
	}
}
