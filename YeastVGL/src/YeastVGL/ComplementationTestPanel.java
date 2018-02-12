package YeastVGL;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ComplementationTestPanel extends JPanel {
	
	private JTable complementationTable;
	private int numMutants;
	
	public ComplementationTestPanel(MutantSet mutantSet) {
		numMutants = mutantSet.getNumberOfMutants();
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
					data[row][col] = new String(row + ":" + (col - 1));
				}
			}
		}
		complementationTable = new JTable(data, columnHeadings);
		JScrollPane tablePane = new JScrollPane(complementationTable);
		complementationTable.setFillsViewportHeight(true);
		this.add(tablePane);
	}

}
