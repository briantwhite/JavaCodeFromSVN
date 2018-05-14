package PathwayPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Biochemistry.Pathway;
import Biochemistry.SingleMutantStrain;
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

	// for going back and forth between
	//  internal enzyme/gene numbers and user-assigned CG letters
	String[] cgNames;
	TreeMap<String, Integer> cgNumbers;
	
	PathwayDrawingPanel pathwayDrawingPanel;
	JLabel pathwayStatusLabel;

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

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

		JPanel instructionPanel = new JPanel();
		instructionPanel.setLayout(new BoxLayout(instructionPanel, BoxLayout.Y_AXIS));
		instructionPanel.add(Box.createRigidArea(new Dimension(150,1)));
		instructionPanel.add(new PPInstructionPanel());
		instructionPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		topPanel.add(instructionPanel);
		
		genoPanel = new JPanel();
		genoPanel.setLayout(new BoxLayout(genoPanel, BoxLayout.Y_AXIS));
		genoPanel.add(Box.createRigidArea(new Dimension(100,1)));
		genoPanel.setBorder(BorderFactory.createTitledBorder("Genotype"));
		JLabel genotypeLabel = new JLabel(
				"Genotype (check boxes for mutations to be included in your test strain):");
		genoPanel.add(genotypeLabel);

		genoPanel.add(noWorkingSetWarningLabel);
		genoPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		topPanel.add(genoPanel);

		JPanel substrPanel = new JPanel();
		substrPanel.setLayout(new BoxLayout(substrPanel, BoxLayout.Y_AXIS));
		substrPanel.add(Box.createRigidArea(new Dimension(100,1)));
		substrPanel.setBorder(BorderFactory.createTitledBorder("Growth Medium"));
		JLabel substrateLabel = new JLabel("Which molecules are in the medium?");
		substrPanel.add(substrateLabel);				
		JLabel substrateLabel2 = new JLabel("Note that the precursor (P) is always present.");
		substrPanel.add(substrateLabel2);				
		substrateCheckboxes = new JCheckBox[numMolecules];
		for (int i = 1; i < numMolecules; i++) {
			if (yeastVGL.getPathway().getMolecules()[i].isTerminal()) {
				substrateCheckboxes[i] = new JCheckBox("<html><font color='red'>Molecule: " + i + "</font></html>");
			} else {
				substrateCheckboxes[i] = new JCheckBox("Molecule: " + i);
			}
			
			substrPanel.add(substrateCheckboxes[i]);
			substrateCheckboxes[i].setSelected(false);
			substrateCheckboxes[i].addItemListener(new checkBoxListener());
		}
		JPanel willItGrowPanel = new JPanel();
		willItGrowLabel = new JLabel("       ");
		willItGrowLabel.setMinimumSize(new Dimension(200,20));
		willItGrowLabel.setOpaque(true);
		willItGrowPanel.add(willItGrowLabel);
		willItGrowPanel.setBorder(BorderFactory.createTitledBorder("Will it grow?"));
		substrPanel.add(willItGrowPanel);
		substrPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		topPanel.add(substrPanel);


		this.add(topPanel);

		pathwayDrawingPanel = new PathwayDrawingPanel(yeastVGL);
		this.add(pathwayDrawingPanel);

		JButton b = new JButton("Check pathway");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String result = pathwayDrawingPanel.convertToPathway().isEquivalentTo(pathway);
					if (result == null) {
						pathwayStatusLabel.setText("<html><font color='green'>Pathway is correct.</font></html>");
					} else {
						pathwayStatusLabel.setText("<html><font color='red'>The pathway is formatted correctly but:<br>" 
								+ result + "</font></html>");
					}
				} catch (PathwayDrawingException e1) {
					pathwayStatusLabel.setText("<html><font color='red'>" + e1.getMessage() + "</font></html>");
				}
			}
		});
		this.add(b);

		pathwayStatusLabel = new JLabel();
		this.add(pathwayStatusLabel);
	}

	private class checkBoxListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			updateDisplay();
		}
	}

	/*
	 * update the checkboxes with the new working set
	 * ALSO update the mapping between internal enzyme/gene number and
	 *   user-assigned complemention group letter names
	 */
	public void updateWorkingSet(ArrayList<SingleMutantStrain> workingSet) {
		this.workingSet = workingSet;
		cgNames = new String[numEnzymes]; // names indexed by enzyme/gene number
		for (int i = 0; i < numEnzymes; i++) {
			cgNames[i] = "";
		}
		cgNumbers = new TreeMap<String, Integer>(); // numbers indexed by cg name letter
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
			int number = workingSet.get(i).getMutatedGeneIndex();
			String cg = workingSet.get(i).getComplementationGroup();
			cgNames[number] = cg;
			cgNumbers.put(cg, new Integer(number));
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
					}
				}
			}
		}
//		System.out.println("PP line 139: updating composite genotype");
//		for (int i = 0; i < numEnzymes; i++) {
//			if (compositeGenotype[i]) {		
//				System.out.println("E" + i + " ACTIVE");
//			} else {
//				System.out.println("E" + i + " inactive");			
//			}
//		}

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
	
	public String[] getCGNames() {
		return cgNames;
	}
	
	public TreeMap<String, Integer> getCGNumbers() {
		return cgNumbers;
	}

}
