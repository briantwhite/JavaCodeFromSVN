package ComplementationTestPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;

import Biochemistry.MutantSet;
import Biochemistry.Pathway;
import Biochemistry.SingleMutantStrain;
import YeastVGL.State;
import YeastVGL.YeastVGL;

public class ComplementationTestPanel extends JPanel implements ActionListener, TableColumnModelListener {

	private YeastVGL yeastVGL;
	private JTable complementationTable;
	private JPanel ctp;
	private JPanel wsp;
	private Pathway pathway;
	private int numMutants;
	private int numEnzymes;
	private String[] columnHeadings;
	private JCheckBox[] workingSetCheckboxes;
	private JPopupMenu cgChoicePopup;
	private int cgTableRowforCGediting;
	private JLabel tableStatusLabel;
	private JLabel workingSetStatusLabel;

	private Object[][] data;

	private final TitledBorder comTestPanelGreenBorder = 
			BorderFactory.createTitledBorder(
					BorderFactory.createLineBorder(Color.GREEN), 
					"Complementation Test");
	private final TitledBorder comTestPanelRedBorder = 
			BorderFactory.createTitledBorder(
					BorderFactory.createLineBorder(Color.RED), 
					"Complementation Test");

	private final TitledBorder workingSetPanelGreenBorder = 
			BorderFactory.createTitledBorder(
					BorderFactory.createLineBorder(Color.GREEN), 
					"Working Set");
	private final TitledBorder workingSetPanelRedBorder = 
			BorderFactory.createTitledBorder(
					BorderFactory.createLineBorder(Color.RED), 
					"Working Set");

	public ComplementationTestPanel(YeastVGL yeastVGL) {
		this.yeastVGL = yeastVGL;
		pathway = yeastVGL.getPathway();
		numMutants = yeastVGL.getMutantSet().getNumberOfMutants();
		numEnzymes = yeastVGL.getPathway().getNumberOfEnzymes();

		// set up the complementation group popup menu
		cgChoicePopup = new JPopupMenu();
		JMenuItem[] cgChoices = new JMenuItem[numEnzymes + 3];
		// first a blank one
		cgChoices[0] = new JMenuItem("");
		cgChoices[0].addActionListener(this);
		cgChoicePopup.add(cgChoices[0]);
		for (int i = 1; i < cgChoices.length; i++) {
			cgChoices[i] = new JMenuItem(String.valueOf((char)(i + 0x40)));
			cgChoices[i].addActionListener(this);
			cgChoicePopup.add(cgChoices[i]);
		}

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
					data[row][col] = yeastVGL.getMutantSet().getMutantStrains()[row].getComplementationGroup();
				} else {
					data[row][col] = willDiploidGrow(row, col - 1, startingMaterials);
				}
			}
		}

		// set up the UI
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(Box.createRigidArea(new Dimension(900,10)));

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(Box.createRigidArea(new Dimension(150,1)));
		leftPanel.setAlignmentY(Component.TOP_ALIGNMENT);

		JPanel instructionPanel = new CTPInstructionPanel();
		instructionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		leftPanel.add(instructionPanel);
		mainPanel.add(leftPanel);

		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
		middlePanel.add(Box.createRigidArea(new Dimension(600,1)));
		middlePanel.setAlignmentY(Component.TOP_ALIGNMENT);

		ctp = new JPanel();
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
				yeastVGL.getGUI().haveSomethingToSave();
			}
		});
		ComTabModel ctModel = new ComTabModel();
		// need to update the mutant's complementation group if you changed it
		ctModel.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					int alteredMutantNumber = Integer.valueOf(
							data[e.getFirstRow()][0].toString().substring(1));
					yeastVGL.getMutantSet().getMutantStrains()[alteredMutantNumber].setComplementationGroup(
							data[e.getFirstRow()][e.getColumn()].toString());
				}
			}			
		});
		complementationTable.setModel(ctModel);
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
		complementationTable.addMouseListener(new ComGrpEditorListener());
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.add(complementationTable, BorderLayout.CENTER);
		tablePanel.add(complementationTable.getTableHeader(), BorderLayout.NORTH);

		tableStatusLabel = new JLabel();
		tablePanel.add(tableStatusLabel, BorderLayout.SOUTH);

		ctp.add(tablePanel);
		middlePanel.add(ctp);

		mainPanel.add(middlePanel);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setAlignmentY(Component.TOP_ALIGNMENT);

		wsp = new JPanel();
		wsp.setLayout(new BoxLayout(wsp, BoxLayout.Y_AXIS));
		wsp.setBorder(BorderFactory.createTitledBorder("Working Set"));

		// now the working set of mutants
		workingSetCheckboxes = new JCheckBox[numMutants];
		for (int i = 0; i < numMutants; i++) {
			workingSetCheckboxes[i] = new JCheckBox("M" + i);
			workingSetCheckboxes[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					yeastVGL.getGUI().haveSomethingToSave();
					updateWorkingSetStatusLabel();
				}
			});
		}
		for (int i = 0; i < workingSetCheckboxes.length; i++) {
			wsp.add(workingSetCheckboxes[i]);
			workingSetCheckboxes[i].setAlignmentX(Component.LEFT_ALIGNMENT);
		}
		workingSetStatusLabel = new JLabel();
		wsp.add(workingSetStatusLabel);
		workingSetStatusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		rightPanel.add(wsp);
		mainPanel.add(rightPanel);

		this.add(mainPanel);
		this.revalidate();

		updateTableStatusLabel();
		updateWorkingSetCheckboxLabels();
		updateWorkingSetStatusLabel();
	}

	private String willDiploidGrow(int m1num, int m2num, ArrayList<Integer>startingMolecules) {
		// combine both genotypes - assume that functional ("true") is dominant
		boolean[] diploidEffectiveGenotype = new boolean[numEnzymes];
		for (int i = 0; i < numEnzymes; i++) {
			diploidEffectiveGenotype[i] = false;
		}
		for (int i = 0; i < numEnzymes; i++) {
			if ((yeastVGL.getMutantSet().getMutantStrains()[m1num].getGenotype()[i]) 
					|| (yeastVGL.getMutantSet().getMutantStrains()[m2num].getGenotype()[i])) {
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

	// the popup that lets you edit the complementation group
	class ComGrpEditorListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			int row = complementationTable.rowAtPoint(e.getPoint());
			int col = complementationTable.columnAtPoint(e.getPoint());
			if (col == (numMutants + 1)) {
				Rectangle targetCell = complementationTable.getCellRect(row, col, false);
				cgChoicePopup.show(complementationTable, 
						targetCell.x + (targetCell.width/2), 
						targetCell.y + (targetCell.height/2));
				cgTableRowforCGediting = row;
			}
		}
	}
	// this will be fired when a choice is made in the cgChoicePopup
	public void actionPerformed(ActionEvent e) {
		String choice = ((JMenuItem)e.getSource()).getText();
		data[cgTableRowforCGediting][numMutants + 1] = choice;
		updateTableStatusLabel();
		// update label on checkboxes
		int mutantStrainNumber = Integer.parseInt(((String)data[cgTableRowforCGediting][0]).substring(1));
		workingSetCheckboxes[mutantStrainNumber].setText("M" + mutantStrainNumber + " CG: " + choice);
		// update mutantStrain's cg
		yeastVGL.getMutantSet().getMutantStrains()[mutantStrainNumber].setComplementationGroup(choice);
		updateWorkingSetStatusLabel();
	}

	public void updateTableStatusLabel() {
		StringBuffer tableStatusTextBuffer = new StringBuffer();

		// see if all the mutant strains have a CG
		boolean cgAssignmentsIncomplete = false;
		for (int row = 0; row < numMutants; row++) {
			if (data[row][numMutants + 1].equals("")) {
				cgAssignmentsIncomplete = true;
				break;
			}
		}
		if (cgAssignmentsIncomplete) {
			tableStatusTextBuffer.append("<font color='red'>WARNING: not all mutant strains have been assigned a cg.</font><br>");
		} 

		// now, be sure that all mutant strains mutated in each gene have the same cg
		boolean cgAssignmentsIncorrect = false;
		String[] mutantCGs = new String[numEnzymes];
		for (int row = 0; row < numMutants; row++) {
			int mutantStrainNumber = Integer.parseInt(((String)data[row][0]).substring(1));
			int indexOfMutatedGene = yeastVGL.getMutantSet().getMutantStrains()[mutantStrainNumber].getMutatedGeneIndex();
			if (mutantCGs[indexOfMutatedGene] == null) {
				// unassigned, so assign it
				mutantCGs[indexOfMutatedGene] = (String)data[row][numMutants + 1];
			} else {
				// assigned, so check it - do all the mutantStrains that have same mutation have same cg?
				if (!mutantCGs[indexOfMutatedGene].equals((String)data[row][numMutants + 1])) {
					cgAssignmentsIncorrect = true;
					break;
				}
			}
		}
		/*
		 * the test above "passes" if all have the same cg (like at the start)
		 * so need to be sure that there are at least 2 different cgs in the set.
		 */
		boolean onlyOneCGused = true;
		String cg = null;
		for (int i = 0; i < mutantCGs.length; i++) {
			if (cg == null) {
				// save the first one you find
				cg = mutantCGs[i];
			} else {
				// see if you find another
				if ((mutantCGs[i] != null) && (!mutantCGs[i].equals(cg))) {
					onlyOneCGused = false;
					break;
				}
			}
		}
		if (cgAssignmentsIncorrect || onlyOneCGused) {
			tableStatusTextBuffer.append("<font color='red'>WARNING: not all cgs assigned correctly.</font>");
		}

		if (tableStatusTextBuffer.length() != 0) {
			tableStatusLabel.setText("<html>" + tableStatusTextBuffer.toString() + "</html>");
			ctp.setBorder(comTestPanelRedBorder);
		} else {
			tableStatusLabel.setText("<html><font color='green'>AOK</font></html>");
			ctp.setBorder(comTestPanelGreenBorder);
		}
	}

	public void updateWorkingSetCheckboxLabels() {
		for (int row = 0; row < numMutants; row++) {
			int mutantStrainNumber = Integer.parseInt(((String)data[row][0]).substring(1));
			String cg = yeastVGL.getMutantSet().getMutantStrains()[mutantStrainNumber].getComplementationGroup();
			workingSetCheckboxes[mutantStrainNumber].setText("M" + mutantStrainNumber + " CG: " + cg);
		}
	}

	public void updateWorkingSetStatusLabel() {
		// be sure each of the cgs is represented once and only once in the working set
		HashMap<String, Integer> tallyMap = new HashMap<String, Integer>();
		// first, find all the CG names they used and create the hash keys with them
		for (int row = 0; row < numMutants; row++) {
			String cgName = (String)data[row][numMutants + 1];
			if (!tallyMap.containsKey(cgName)) {
				tallyMap.put(cgName, new Integer(0));
			}
		}
		
		// now, go thru the working set checkboxes and see how many times each CG is represented
		for (int i = 0; i < workingSetCheckboxes.length; i++) {
			if (workingSetCheckboxes[i].isSelected()) {
				String cg = yeastVGL.getMutantSet().getMutantStrains()[i].getComplementationGroup();
				int oldCount = tallyMap.get(cg).intValue();
				tallyMap.replace(cg, oldCount + 1);
			}
		}
		
		// now, check the tally for problems
		boolean workingSetIncomplete = false;	// not all cgs represented
		boolean workingSetIncorrect = false;	// one or more cgs with > 1 representative
		Iterator<String> cgIt = tallyMap.keySet().iterator();
		while (cgIt.hasNext()) {
			String cg = cgIt.next();
			int count = tallyMap.get(cg);
			if (count == 0) {
				workingSetIncomplete = true;
			}
			if (count > 1) {
				workingSetIncorrect = true;
			}
		}
		
		StringBuffer workingSetStatusBuffer = new StringBuffer();
		if (workingSetIncomplete) {
			workingSetStatusBuffer.append("<font color='red'>WARNING: not all cgs represented in working set.</font><br>");
		}
		if (workingSetIncorrect) {
			workingSetStatusBuffer.append("<font color='red'>WARNING: some cgs represented more than once in working set.</font>");
		}
		if (workingSetStatusBuffer.length() != 0) {
			workingSetStatusLabel.setText("<html>" + workingSetStatusBuffer.toString() + "</html>");
			wsp.setBorder(workingSetPanelRedBorder);
		} else {
			workingSetStatusLabel.setText("<html><font color='green'>AOK</font></html>");
			wsp.setBorder(workingSetPanelGreenBorder);
			
			// get the next panel ready
			yeastVGL.getPathwayPanel().updateWorkingSet(getWorkingSet());
			yeastVGL.getPathwayPanel().getPathwayDrawingPanel().updateCGChoices();
		}
	}

	public ArrayList<SingleMutantStrain> getWorkingSet() {
		ArrayList<SingleMutantStrain> workingSet = new ArrayList<SingleMutantStrain>();
		for (int i = 0; i < workingSetCheckboxes.length; i++) {
			if (workingSetCheckboxes[i].isSelected()) {
				workingSet.add(yeastVGL.getMutantSet().getMutantStrains()[i]);
			}
		}
		return workingSet;
	}

	public Object[][] getData() {
		return data;
	}

	public boolean[] getWorkingSetChoices() {
		boolean[] workingSet = new boolean[workingSetCheckboxes.length];
		for (int i = 0; i < workingSet.length; i++) {
			workingSet[i] = workingSetCheckboxes[i].isSelected();
		}
		return workingSet;
	}

	public void restoreSavedState(State state) {
		yeastVGL.setMutantSet(state.getMutantSet());
		data = state.getComplementationTableData();
		// need to fix column headings
		for (int i = 0; i < data.length; i++) {
			complementationTable.getColumnModel().getColumn(i + 1).setHeaderValue(data[i][0].toString());
		}
		complementationTable.getTableHeader().repaint();
		complementationTable.revalidate();
		complementationTable.repaint();

		for (int i = 0; i < state.getWorkingSetChoices().length; i++) {
			workingSetCheckboxes[i].setSelected(state.getWorkingSetChoices()[i]);
		}
		updateTableStatusLabel();
		updateWorkingSetCheckboxLabels();
		updateWorkingSetStatusLabel();
	}
}