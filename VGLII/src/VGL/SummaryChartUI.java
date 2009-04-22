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
import javax.swing.JPanel;
import javax.swing.JTable;

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
 * @version 1.0 $Id: SummaryChartUI.java,v 1.6 2009-04-22 01:46:21 brian Exp $
 */

public class SummaryChartUI extends JDialog implements ActionListener {
	
	private VGLII vglII;
	
	private SummaryChartManager manager;
	
	private JCheckBox[] traitCheckBoxes;
	
	private JPanel resultPanel;
	
	private int[] scrambledTraitOrder;
	
	public SummaryChartUI(VGLII vglII) {
		super(vglII, "SummaryChart", false);
		this.vglII = vglII;
		manager = SummaryChartManager.getInstance();
		scrambledTraitOrder =manager.getScrambledCharacterOrder();
		setTitle("Summary Chart for Cages " + manager.toString());
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
				BorderFactory.createTitledBorder("Sort Offspring by:"));
		
		Trait[] traits = manager.getTraitSet();
		traitCheckBoxes = new JCheckBox[traits.length];
		//make the check boxes
		for (int i = 0; i < traits.length; i++) {
			traitCheckBoxes[i] = new JCheckBox();
			traitCheckBoxes[i].addActionListener(this);
			traitCheckBoxes[i].setSelected(true);			
		}
		//put them in GUI in randomized order
		for (int i = 0; i < traits.length; i++) {
			traitSelectionPanel.add(
					new JLabel(traits[scrambledTraitOrder[i]].getBodyPart() 
							+ " " + traits[scrambledTraitOrder[i]].getType()));
			traitSelectionPanel.add(traitCheckBoxes[scrambledTraitOrder[i]]);
			traitSelectionPanel.add(Box.createHorizontalStrut(10));
		}
		
		add(traitSelectionPanel, BorderLayout.NORTH);
		add(resultPanel, BorderLayout.CENTER);
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

		PhenotypeCount[] result = manager.calculateTotals(selectedTraits);
		
		String[] columnHeadings = {"Phenotype", "Males", "Females", "Total"};
		
		Object[][] data = new Object[result.length][4];
		for (int i = 0; i < result.length; i++) {
			data[i][0] = result[i].getPhenotype();
			data[i][1] = result[i].getCounts().getMales();
			data[i][2] = result[i].getCounts().getFemales();
			data[i][3] = result[i].getCounts().getTotal();
		}
		
		//if none selected, the "phenotype" is "organism"
		if (selectedTraits.size() == 0) data[0][0] = "Organism";
			
		int phenoStringWidth = data[0][0].toString().length() * 8;
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JTable table = new JTable(data, columnHeadings);
		table.setGridColor(Color.BLACK);
		table.setShowGrid(true);
		table.getColumnModel().getColumn(0).setPreferredWidth(phenoStringWidth);
		panel.add(table.getTableHeader());
		panel.add(table);
		resultPanel.removeAll();
		resultPanel.add(panel);
		resultPanel.revalidate();
		repaint();
	}
}
