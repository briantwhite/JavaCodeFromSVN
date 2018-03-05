package ComplementationTestPanel;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableColumnModel;

import Biochemistry.MutantSet;
import Biochemistry.Pathway;
import YeastVGL.YeastVGL;

public class ComplementationTable extends JTable implements ActionListener, TableColumnModelListener {
	
	private YeastVGL yeastVGL;
	
	private Pathway pathway;
	private MutantSet mutantSet;
	private int numMutants;
	private int numEnzymes;
	private String[] columnHeadings;
	private JPopupMenu cgChoicePopup;
	private int cgTableRowforCGediting;

	private Object[][] data;

	private JTable table;

	public ComplementationTable(YeastVGL yeastVGL) {
		
		super();
		table = this;		// need this reference for locating the popup menu
		this.yeastVGL = yeastVGL;
		pathway = yeastVGL.getPathway();
		mutantSet = yeastVGL.getMutantSet();
		numMutants = mutantSet.getNumberOfMutants();
		numEnzymes = yeastVGL.getPathway().getNumberOfEnzymes();
		
		// set up the complementation group popup menu
		cgChoicePopup = new JPopupMenu();
		JMenuItem[] cgChoices = new JMenuItem[numEnzymes + 2];
		for (int i = 0; i < cgChoices.length; i++) {
			cgChoices[i] = new JMenuItem(String.valueOf((char)(i + 0x41)));
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
					data[row][col] = mutantSet.getMutantStrains()[row].getComplementationGroup();
				} else {
					data[row][col] = willDiploidGrow(row, col - 1, startingMaterials);
				}
			}
		}

		// make sure you can't drag the first or last columns
		setColumnModel(new DefaultTableColumnModel() {
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

		ComplementationTableModel ctModel = new ComplementationTableModel(data, columnHeadings);
		setModel(ctModel);
		setFillsViewportHeight(true);
		getColumnModel().addColumnModelListener(this);
		for (int i = 0; i < columnHeadings.length; i++) {
			if (i == (columnHeadings.length - 1)) {
				getColumnModel().getColumn(i).setPreferredWidth(100);
			} else {
				getColumnModel().getColumn(i).setPreferredWidth(30);
			}
		}
		setRowHeight(30);
		addMouseListener(new ComGrpEditorListener());
	}
	
	public String[] getColumnHeadings() {
		return columnHeadings;
	}
	
	public Object[][] getData() {
		return data;
	}
	
	public void setData(Object[][] d) {
		data = d;
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
			int row = rowAtPoint(e.getPoint());
			int col = columnAtPoint(e.getPoint());
			if (col == (numMutants + 1)) {
				Rectangle targetCell = getCellRect(row, col, false);
				cgChoicePopup.show(table, targetCell.x + (targetCell.width/2), targetCell.y + (targetCell.height/2));
				cgTableRowforCGediting = row;
			}
		}
	}
	// this will be fired when a choice is made in the cgChoicePopup
	public void actionPerformed(ActionEvent e) {
		String choice = ((JMenuItem)e.getSource()).getText();
		data[cgTableRowforCGediting][numMutants + 1] = choice;
	}

}
