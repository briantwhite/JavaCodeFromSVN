package YeastVGL;

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PathwayPanel extends JPanel {

	JCheckBox[] genotypeCheckboxes;
	JCheckBox[] substrateCheckboxes;
	JLabel willItGrowLabel;
	Pathway pathway;
	int numEnzymes;
	int numMolecules;
	
	public PathwayPanel(Pathway pathway) {
		this.pathway = pathway;
		numEnzymes = pathway.getNumberOfEnzymes();
		numMolecules = pathway.getNumberOfMolecules();
		
		this.setLayout(new FlowLayout());

		JPanel genoPanel = new JPanel();
		genoPanel.setLayout(new FlowLayout());
		JLabel genotypeLabel = new JLabel("Genotype (check all active enzymes present):");
		genoPanel.add(genotypeLabel);		
		genotypeCheckboxes = new JCheckBox[numEnzymes];
		for (int i = 0; i < numEnzymes; i++) {
			genotypeCheckboxes[i] = new JCheckBox("Enzyme: " + i);
			genoPanel.add(genotypeCheckboxes[i]);
			genotypeCheckboxes[i].setSelected(true);
			genotypeCheckboxes[i].addItemListener(new checkBoxListener());
		}
		this.add(genoPanel);

		JPanel substrPanel = new JPanel();
		substrPanel.setLayout(new FlowLayout());
		JLabel substrateLabel = new JLabel("Which molecules are in the medium?");
		substrPanel.add(substrateLabel);				
		substrateCheckboxes = new JCheckBox[numMolecules];
		for (int i = 1; i < numMolecules; i++) {
			substrateCheckboxes[i] = new JCheckBox("Molecule: " + i);
			substrPanel.add(substrateCheckboxes[i]);
			substrateCheckboxes[i].setSelected(false);
			substrateCheckboxes[i].addItemListener(new checkBoxListener());
		}
		this.add(substrPanel);

		JPanel willItGrowPanel = new JPanel();
		willItGrowLabel = new JLabel();
		willItGrowPanel.add(willItGrowLabel);
		this.add(willItGrowPanel);
	}
	
	private class checkBoxListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			updateDisplay();
		}
	}
	
	public void updateDisplay() {
		boolean[] genotype = new boolean[numEnzymes];
		for (int i = 0; i < numEnzymes; i++) {
			genotype[i] = genotypeCheckboxes[i].isSelected();
		}
		
		// minimal medium is assumed to contain molecule 0
		ArrayList<Integer> startingMolecules = new ArrayList<Integer>();
		startingMolecules.add(new Integer(0));
		for (int i = 1; i < numMolecules; i++) {
			if (substrateCheckboxes[i].isSelected()) {
				startingMolecules.add(new Integer(i));
			}
		}
				
		if (pathway.willItGrow(genotype, startingMolecules)) {
			willItGrowLabel.setText("<html><font color=\"green\">It Will grow!</html>");
		} else {
			willItGrowLabel.setText("<html><font color=\"red\">It Won't grow!</html>");
		}
	}

}
