package PathwayPanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Biochemistry.SingleMutantStrain;
import Biochemistry.Pathway;
import ComplementationTestPanel.MutantStrainCheckbox;
import YeastVGL.YeastVGL;

public class PathwayPanel extends JPanel {

	private YeastVGL yeastVGL;

	private ArrayList<SingleMutantStrain> workingSet;

	JCheckBox[] genotypeCheckboxes;
	JCheckBox[] substrateCheckboxes;
	JLabel willItGrowLabel;
	Pathway pathway;
	int numEnzymes;
	int numMolecules;
	
	PathwayDrawingPanel pathwayDrawingPanel;

	JPanel genoPanel;
	JLabel noWorkingSetWarningLabel = new JLabel("<html>"
			+ "<font color='red'>There are no mutants selected in your working set.\n"
			+ "Please go back and select a Working Set on the Complementation Test Panel."
			+ "</font></html>");

	public PathwayPanel(YeastVGL yeastVGL) {
		this.yeastVGL = yeastVGL;
		this.pathway = yeastVGL.getPathway();
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
		genoPanel.setBorder(BorderFactory.createTitledBorder("Genotype"));
		JLabel genotypeLabel = new JLabel(
				"Genotype (check boxes for mutations to be included in your test strain):");
		genoPanel.add(genotypeLabel);

		genoPanel.add(noWorkingSetWarningLabel);
		middlePanel.add(genoPanel);

		JPanel substrPanel = new JPanel();
		substrPanel.setLayout(new BoxLayout(substrPanel, BoxLayout.Y_AXIS));
		substrPanel.add(Box.createRigidArea(new Dimension(100,1)));
		substrPanel.setBorder(BorderFactory.createTitledBorder("Growth Medium"));
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
		
		pathwayDrawingPanel = new PathwayDrawingPanel(yeastVGL);
		this.add(pathwayDrawingPanel);
		
		JButton b = new JButton("convert");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pathwayDrawingPanel.convertToPathway();
			}
		});
		this.add(b);

	}

	private class checkBoxListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			updateDisplay();
		}
	}

	public void updateWorkingSet(ArrayList<SingleMutantStrain> workingSet) {
		this.workingSet = workingSet;
		genoPanel.removeAll();
		if (workingSet.isEmpty()) {
			genoPanel.add(noWorkingSetWarningLabel);
		}
		genotypeCheckboxes = new MutantStrainCheckbox[workingSet.size()];
		for (int i = 0; i < workingSet.size(); i++) {
			genotypeCheckboxes[i] = new MutantStrainCheckbox(workingSet.get(i));
			genoPanel.add(genotypeCheckboxes[i]);
			genotypeCheckboxes[i].setSelected(false);
			genotypeCheckboxes[i].addItemListener(new checkBoxListener());
		}
	}

	public void updateDisplay() {
		boolean[] compositeGenotype = new boolean[numEnzymes];
		for (int i = 0; i < numEnzymes; i++) {
			compositeGenotype[i] = true;
		}
		/*
		 * combine genotypes of the mutants selected in the working set
		 *  collect ALL mutations = if an enzyme is false (inactive) in any
		 *  of the working set, then it's inactive in the overall genotype
		 */
		for (int i = 0; i < genotypeCheckboxes.length; i++) {
			if (genotypeCheckboxes[i].isSelected()) {
				SingleMutantStrain ms = workingSet.get(i);
				for (int j = 0; j < numEnzymes; j++) {
					if (!ms.getGenotype()[j]) {
						compositeGenotype[j] = false;
						System.out.println("M" + ms.getIndex() + " told me to inactivate E" + j);
					}
				}
			}
		}
		System.out.println("PP line 139: updating composite genotype");
		for (int i = 0; i < numEnzymes; i++) {
			if (compositeGenotype[i]) {		
				System.out.println("E" + i + " ACTIVE");
			} else {
				System.out.println("E" + i + " inactive");			
			}
		}

		// minimal medium is assumed to contain molecule 0
		ArrayList<Integer> startingMolecules = new ArrayList<Integer>();
		startingMolecules.add(new Integer(0));
		for (int i = 1; i < numMolecules; i++) {
			if (substrateCheckboxes[i].isSelected()) {
				startingMolecules.add(new Integer(i));
			}
		}

		if (pathway.willItGrow(compositeGenotype, startingMolecules)) {
			willItGrowLabel.setText("<html><font color=\"green\">It Will grow!</html>");
		} else {
			willItGrowLabel.setText("<html><font color=\"red\">It Won't grow!</html>");
		}
	}
	
	public PathwayDrawingPanel getPathwayDrawingPanel() {
		return pathwayDrawingPanel;
	}

}
