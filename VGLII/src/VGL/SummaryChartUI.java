package VGL;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import GeneticModels.Trait;
/**
 * Brian White Summer 2008
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * @author Brian White
 * @version 1.0 $Id: SummaryChartUI.java,v 1.8 2009-09-22 19:06:35 brian Exp $
 */

public class SummaryChartUI extends JDialog implements ActionListener, TableModelListener {

	private VGLII vglII;

	private SummaryChartManager manager;

	private JCheckBox[] traitCheckBoxes;
	private JCheckBox sexCheckBox;

	private JLabel[] traitCheckBoxLabels;

	private JTextArea[] expectedCounts;

	private JPanel resultPanel;

	private Object[][] data;

	private int[] scrambledTraitOrder;

	private JLabel chiSquaredLabel;

	public SummaryChartUI(VGLII vglII) {
		super(vglII, Messages.getInstance().getString("VGLII.SummaryChart"), false);
		this.vglII = vglII;
		manager = SummaryChartManager.getInstance();
		scrambledTraitOrder =manager.getScrambledCharacterOrder();
		setTitle(Messages.getInstance().getString("VGLII.SummaryChartForCages") + " " + manager.toString());
		setLayout(new BorderLayout());
		resultPanel = new JPanel();
		setupTraitSelectionPanel();
		updateDisplay();

		pack();
		setVisible(true);
	}

	private void setupTraitSelectionPanel() {
		JPanel traitSelectionPanel = new JPanel();
		traitSelectionPanel.setLayout(
				new BoxLayout(traitSelectionPanel, BoxLayout.X_AXIS));
		traitSelectionPanel.setBorder(
				BorderFactory.createTitledBorder(
						Messages.getInstance().getString("VGLII.SortOffspringBy") + ":"));

		Trait[] traits = manager.getTraitSet();
		traitCheckBoxes = new JCheckBox[traits.length];
		traitCheckBoxLabels = new JLabel[traits.length];
		//make the check boxes and labels
		for (int i = 0; i < traits.length; i++) {
			traitCheckBoxes[i] = new JCheckBox();
			traitCheckBoxes[i].addActionListener(this);
			traitCheckBoxes[i].setSelected(true);

			traitCheckBoxLabels[i] = 
				new JLabel(Messages.getInstance().getTranslatedCharacterName(traits[i]));
		}

		//put them in GUI in randomized order
		for (int i = 0; i < traits.length; i++) {
			traitSelectionPanel.add(traitCheckBoxLabels[scrambledTraitOrder[i]]);
			traitSelectionPanel.add(traitCheckBoxes[scrambledTraitOrder[i]]);
			traitSelectionPanel.add(Box.createHorizontalStrut(15));
		}

		// add sex check box
		sexCheckBox = new JCheckBox();
		sexCheckBox.addActionListener(this);
		sexCheckBox.setSelected(true);

		traitSelectionPanel.add(new JLabel(Messages.getInstance().getString("VGLII.Sex")));
		traitSelectionPanel.add(sexCheckBox);
		traitSelectionPanel.add(Box.createHorizontalStrut(15));

		add(traitSelectionPanel, BorderLayout.NORTH);
		add(resultPanel, BorderLayout.CENTER);

		chiSquaredLabel = new JLabel("Chi-squared p-value = ");
		add(chiSquaredLabel, BorderLayout.SOUTH);
	}


	public void actionPerformed(ActionEvent e) {
		updateDisplay();
	}

	private void updateDisplay() {
		//find out which buttons have been selected
		ArrayList<Integer> selectedTraits = new ArrayList<Integer>();
		for (int i = 0; i < manager.getTraitSet().length; i++) {
			if (traitCheckBoxes[i].isSelected()) {
				selectedTraits.add(i);
			}
		}

		PhenotypeCount[] result = manager.calculateTotals(selectedTraits, sexCheckBox.isSelected());

		String[] columnHeadings = {
				Messages.getInstance().getString("VGLII.Phenotype"), 
				Messages.getInstance().getString("VGLII.Total"),
				Messages.getInstance().getString("VGLII.Expected")
		};

		data = new Object[result.length][3];
		for (int i = 0; i < result.length; i++) {
			data[i][0] = Messages.getInstance().translateLongPhenotypeName(result[i].getPhenotype());
			data[i][1] = result[i].getCount();
			data[i][2] = "";
		}

		//if none selected, the "phenotype" is "organism"
		if ((selectedTraits.size() == 0) && !sexCheckBox.isSelected()) data[0][0] = Messages.getInstance().getString("VGLII.Organism");

		// set width of columns sensibly - find longest one
		int maxPhenoStringLength = 0;
		for (int i = 0; i < result.length; i++) {
			if (data[i][0].toString().length() > maxPhenoStringLength) maxPhenoStringLength = data[i][0].toString().length();
		}
		int phenoStringWidth = maxPhenoStringLength * 8;

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JTable table = new JTable(new SummaryDataTableModel(data, columnHeadings));
		table.setGridColor(Color.BLACK);
		table.setShowGrid(true);
		table.getColumnModel().getColumn(0).setPreferredWidth(phenoStringWidth);
		table.getModel().addTableModelListener(this);
		panel.add(table.getTableHeader());
		panel.add(table);
		resultPanel.removeAll();
		resultPanel.add(panel);
		resultPanel.revalidate();
		repaint();
	}

	public void tableChanged(TableModelEvent arg0) {

		// be sure all "expected" values are non blank
		boolean haveAllEntries = true;
		for (int i = 0; i < data.length; i++) {
			if (data[i][2] == "") {
				haveAllEntries = false;
				break;
			}
		}
		
		if (haveAllEntries) {
			float[] expectedCounts = new float[data.length];
			int totalObserved = 0;
			int totalExpected = 0;
			for (int i = 0; i < data.length; i++) {
				totalObserved = totalObserved + (Integer)data[i][1];
				totalExpected = totalExpected + (Integer)data[i][2];				
			}
			
			float scaleFactor = (float)totalObserved/(float)totalExpected;
			for (int i = 0; i < data.length; i++) {
				expectedCounts[i] = (float)((Integer)data[i][2]) * scaleFactor;
				System.out.println("obs = " + (Integer)data[i][1] + " exp = " + expectedCounts[i]);
			}
			
			chiSquaredLabel.setText("obs = " + totalObserved + " exp = " + totalExpected);
			
		} else {
			chiSquaredLabel.setText("Chi-squared p-value = ");
		}

	}

	private class SummaryDataTableModel extends AbstractTableModel {

		Object[][] data;
		String[] columnHeadings;

		public SummaryDataTableModel(Object[][] data, String[] columnHeadings) {
			super();
			this.data = data;
			this.columnHeadings = columnHeadings;
		}

		public int getColumnCount() {
			return data[0].length;
		}

		public String getColumnName(int i) {
			return columnHeadings[i];
		}

		public int getRowCount() {
			return data.length;
		}

		public Class getColumnClass(int c) {
			if (c == 2) return Integer.class;
			return String.class;
		}

		public boolean isCellEditable(int row, int col) { 
			if (col == 2) return true;
			return false; 
		}

		public Object getValueAt(int arg0, int arg1) {
			return data[arg0][arg1];
		}

		public void setValueAt(Object value, int row, int col) {
			data[row][col] = value;
			fireTableCellUpdated(row, col);
		}

	}

}
