package YeastVGL;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;

public class ComplementationTestPanel extends JPanel implements TableColumnModelListener {

	private JTable complementationTable;
	private Pathway pathway;
	private MutantSet mutantSet;
	private int numMutants;
	private int numEnzymes;
	private String[] columnHeadings;
	private JCheckBox[] workingSetCheckboxes;

	private Object[][] data;

	public ComplementationTestPanel(YeastVGL yeastVGL) {
		pathway = yeastVGL.getPathway();
		mutantSet = yeastVGL.getMutantSet();
		numMutants = mutantSet.getNumberOfMutants();
		numEnzymes = yeastVGL.getPathway().getNumberOfEnzymes();

		// set up the column names
		String[] mutantNames = new String[numMutants];
		columnHeadings = new String[numMutants + 2];
		columnHeadings[0] = new String("");
		for (int i = 0; i < numMutants; i++) {
			mutantNames[i] = new String("M" + i);
			columnHeadings[i + 1] = new String("M" + i);
		}
		columnHeadings[numMutants + 1] = new String("Group");	

		// compute the complementation table
		ArrayList<Integer>startingMaterials = new ArrayList<Integer>();
		startingMaterials.add(new Integer(0));
		data = new Object[numMutants][numMutants + 2];
		for (int row = 0; row < numMutants; row++) {
			for (int col = 0; col < (numMutants + 2); col++) {
				if (col == 0) {
					data[row][col] = new String(mutantNames[row]);
				} else if (col == (numMutants + 1)){
					data[row][col] = new String("-enter name-");
				} else {
					data[row][col] = willDiploidGrow(row, col - 1, startingMaterials);
				}
			}
		}

		// set up the UI
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(Box.createRigidArea(new Dimension(700,10)));
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(Box.createRigidArea(new Dimension(600,1)));
		
		JPanel ctp = new JPanel();
		ctp.setBorder(BorderFactory.createTitledBorder("Complementation Table"));
		complementationTable = new JTable();
		// make sure you can't drag the first or last columns
		complementationTable.setColumnModel(new DefaultTableColumnModel() {
			public void moveColumn(int columnIndex, int newIndex) {
				if ((columnIndex == 0) || (newIndex == 0) 
						|| (columnIndex == (columnHeadings.length - 1))
						|| (newIndex == (columnHeadings.length - 1))) {
					return;
				}
				super.moveColumn(columnIndex, newIndex);
			}
		});
		complementationTable.setModel(new ComTabModel());
		complementationTable.setFillsViewportHeight(true);
		complementationTable.getColumnModel().addColumnModelListener(this);
		for (int i = 0; i < columnHeadings.length; i++) {
			if (i == (columnHeadings.length - 1)) {
				complementationTable.getColumnModel().getColumn(i).setPreferredWidth(100);
			} else {
				complementationTable.getColumnModel().getColumn(i).setPreferredWidth(30);
			}
		}
		//		complementationTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		complementationTable.setRowHeight(30);
		JScrollPane tablePane = new JScrollPane(complementationTable);
		tablePane.setPreferredSize(new Dimension(complementationTable.getPreferredSize().width + 30, 
				complementationTable.getRowHeight() * (columnHeadings.length + 1)));
		ctp.add(tablePane);
		leftPanel.add(ctp);
		mainPanel.add(leftPanel);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(Box.createRigidArea(new Dimension(100,1)));
		
		JPanel wsp = new JPanel();
		wsp.setLayout(new BoxLayout(wsp, BoxLayout.Y_AXIS));
		wsp.setBorder(BorderFactory.createTitledBorder("Working Set"));
		wsp.add(Box.createRigidArea(new Dimension(100,1)));
		// now the working set of mutants
		workingSetCheckboxes = new JCheckBox[numMutants];
		for (int i = 0; i < numMutants; i++) {
			workingSetCheckboxes[i] = new JCheckBox("M" + i);
		}
		for (int i = 0; i < workingSetCheckboxes.length; i++) {
			wsp.add(workingSetCheckboxes[i]);
		}
		rightPanel.add(wsp);
		mainPanel.add(rightPanel);
		
		this.add(mainPanel);
		this.revalidate();
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
			if (c == (columnHeadings.length - 1)) {
				return true;
			} else {
				return false;
			}
		}
		public Object getValueAt(int rowIndex, int columnIndex) {
			return data[rowIndex][columnIndex];
		}
		public void setValueAt(Object value, int row, int col) {
			data[row][col] = value;
			fireTableCellUpdated(row, col);
		}
	}

	// detect dragged columns and update rows accordingly
	public void columnAdded(TableColumnModelEvent e) {}
	public void columnRemoved(TableColumnModelEvent e) {}
	public void columnMarginChanged(ChangeEvent e) {}
	public void columnSelectionChanged(ListSelectionEvent e) {}
	public void columnMoved(TableColumnModelEvent e) {
		if (e.getFromIndex() != e.getToIndex()) {
			int oldRow = e.getFromIndex() - 1;
			int newRow = e.getToIndex() - 1;
			for (int i = 0; i < columnHeadings.length; i++) {
				Object temp = data[newRow][i];
				data[newRow][i] = data[oldRow][i];
				data[oldRow][i] = temp;
			}			
		}
	}
	

}
