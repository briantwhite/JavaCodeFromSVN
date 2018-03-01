package YeastVGL;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
	
	JPanel genoPanel;
	
	public PathwayPanel(Pathway pathway) {
		this.pathway = pathway;
		numEnzymes = pathway.getNumberOfEnzymes();
		numMolecules = pathway.getNumberOfMolecules();
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(Box.createRigidArea(new Dimension(900,10)));
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(Box.createRigidArea(new Dimension(150,1)));
		
		JPanel instructionPanel = new JPanel();
		instructionPanel.setLayout(new BoxLayout(instructionPanel, BoxLayout.Y_AXIS));
		instructionPanel.setBorder(BorderFactory.createTitledBorder("Instructions"));
		instructionPanel.add(Box.createRigidArea(new Dimension(150,1)));
		instructionPanel.add(new JLabel("<html>"
				+ "bla"
				+ "</html>"));
		leftPanel.add(instructionPanel);
		mainPanel.add(leftPanel);
		
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
		middlePanel.add(Box.createRigidArea(new Dimension(600,1)));

		genoPanel = new JPanel();
		genoPanel.setLayout(new BoxLayout(genoPanel, BoxLayout.Y_AXIS));
		genoPanel.add(Box.createRigidArea(new Dimension(100,1)));
		JLabel genotypeLabel = new JLabel(
				"Genotype (check boxes for mutations to be included in your test strain):");
		genoPanel.add(genotypeLabel);		
		genotypeCheckboxes = new JCheckBox[numEnzymes];
		for (int i = 0; i < numEnzymes; i++) {
			genotypeCheckboxes[i] = new JCheckBox("Enzyme: " + i);
			genoPanel.add(genotypeCheckboxes[i]);
			genotypeCheckboxes[i].setSelected(true);
			genotypeCheckboxes[i].addItemListener(new checkBoxListener());
		}
		middlePanel.add(genoPanel);

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
		middlePanel.add(substrPanel);

		JPanel willItGrowPanel = new JPanel();
		willItGrowLabel = new JLabel();
		willItGrowPanel.add(willItGrowLabel);
		middlePanel.add(willItGrowPanel);
		mainPanel.add(middlePanel);
		
		this.add(mainPanel);
	}
	
	private class checkBoxListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			updateDisplay();
		}
	}
	
	public void updateWorkingSet(ArrayList<MutantStrain> workingSet) {
		
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
