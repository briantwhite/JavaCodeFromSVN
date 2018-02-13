package YeastVGL;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableModel;

public class ComplementationTestPanel extends JPanel implements TableColumnModelListener {
	
	private JTable complementationTable;
	private Pathway pathway;
	private MutantSet mutantSet;
	private int numMutants;
	private int numEnzymes;
	private String[] columnHeadings;
	private Object[][] data;
	
	public ComplementationTestPanel(YeastVGL yeastVGL) {
		pathway = yeastVGL.getPathway();
		mutantSet = yeastVGL.getMutantSet();
		numMutants = mutantSet.getNumberOfMutants();
		numEnzymes = yeastVGL.getPathway().getNumberOfEnzymes();
		
		String[] mutantNames = new String[numMutants];
		columnHeadings = new String[numMutants + 1];
		columnHeadings[0] = new String("");
		for (int i = 0; i < numMutants; i++) {
			mutantNames[i] = new String("M" + i);
			columnHeadings[i + 1] = new String("M" + i);
		}
		
		ArrayList<Integer>startingMaterials = new ArrayList<Integer>();
		startingMaterials.add(new Integer(0));
		data = new Object[numMutants][numMutants + 1];
		for (int row = 0; row < numMutants; row++) {
			for (int col = 0; col < (numMutants + 1); col++) {
				if (col == 0) {
					data[row][col] = new String(mutantNames[row]);
				} else {
					data[row][col] = willDiploidGrow(row, col - 1, startingMaterials);
				}
			}
		}

//		complementationTable = new JTable(data, columnHeadings);
		complementationTable = new JTable(new ComTabModel());
		JScrollPane tablePane = new JScrollPane(complementationTable);
		complementationTable.setFillsViewportHeight(true);
		complementationTable.getColumnModel().addColumnModelListener(this);
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
	
	class ComTabModel extends DefaultTableModel {
		public int getRowCount() { 
			return data.length;
		}
		public int getColumnCount() {
			return columnHeadings.length;
		}
		public String getColumnName(int col) {
			return columnHeadings[col];
		}
		public boolean isCellEditable(int r, int c) {
			return false;
		}
		public Object getValueAt(int rowIndex, int columnIndex) {
			return data[rowIndex][columnIndex];
		}
	}
	
	// detect dragged columns and update rows accordingly
	public void columnAdded(TableColumnModelEvent e) {}
	public void columnRemoved(TableColumnModelEvent e) {}
	public void columnMarginChanged(ChangeEvent e) {}
	public void columnSelectionChanged(ListSelectionEvent e) {}
	public void columnMoved(TableColumnModelEvent e) {
		DefaultTableModel tm = (DefaultTableModel)complementationTable.getModel();
		tm.moveRow(e.getFromIndex(), (e.getFromIndex() + 1), e.getToIndex());
		complementationTable.revalidate();
		if (e.getFromIndex() != e.getToIndex()) {
			System.out.println("from " + e.getFromIndex() + " to " + e.getToIndex());
		}
	}

}
