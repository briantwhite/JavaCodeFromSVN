package VGL;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import GeneticModels.Trait;

public class SummaryChartUI extends JDialog implements ActionListener {
	
	private SummaryChartManager manager;
	
	private JCheckBox[] traitCheckBoxes;
	
	private JPanel resultPanel;
	
	public SummaryChartUI(VGLII master) {
		super(master, "SummaryChart", false);
		manager = SummaryChartManager.getInstance();
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
		for (int i = 0; i < traits.length; i++) {
			traitSelectionPanel.add(
					new JLabel(traits[i].getBodyPart() 
							+ " " + traits[i].getType()));
			traitCheckBoxes[i] = new JCheckBox();
			traitCheckBoxes[i].addActionListener(this);
			traitCheckBoxes[i].setSelected(true);
			traitSelectionPanel.add(traitCheckBoxes[i]);
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
		TreeSet<Integer> selectedTraits = new TreeSet<Integer>();
		for (int i = 0; i < manager.getTraitSet().length; i++) {
			if (traitCheckBoxes[i].isSelected()) {
				selectedTraits.add(i);
			}
		}

		int[] traitsToCount = new int[selectedTraits.size()];
		int j = 0;
		Iterator<Integer> it = selectedTraits.iterator();
		while (it.hasNext()) {
			traitsToCount[j] = it.next();
			j++;
		}
				
		PhenotypeCount[] result = manager.calculateTotals(traitsToCount);
		
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
