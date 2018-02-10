import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class YeastVGL_GUI extends JFrame {

	Pathway pathway;
	JCheckBox[] genotypeCheckboxes;
	JCheckBox[] substrateCheckboxes;
	JLabel willItGrowLabel;
	

	public YeastVGL_GUI(Pathway pathway) {
		super("Yeast VGL 0.1");
		addWindowListener(new ApplicationCloser());
		this.pathway = pathway;
		setupUI();
	}
	
	class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	
	public void setupUI() {
		
		this.setLayout(new GridLayout(3,1));
		
		JPanel genoPanel = new JPanel();
		genoPanel.setLayout(new FlowLayout());
		JLabel genotypeLabel = new JLabel("Genotype (check all active enzymes present):");
		genoPanel.add(genotypeLabel);		
		genotypeCheckboxes = new JCheckBox[pathway.getNumberOfEnzymes()];
		for (int i = 0; i < pathway.getNumberOfEnzymes(); i++) {
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
		substrateCheckboxes = new JCheckBox[pathway.getNumberOfMolecules()];
		for (int i = 1; i < pathway.getNumberOfMolecules(); i++) {
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
		
		this.pack();
		updateDisplay();
	}

	private class checkBoxListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			updateDisplay();
		}
	}
	
	private void updateDisplay() {
		boolean[] genotype = new boolean[pathway.getNumberOfEnzymes()];
		for (int i = 0; i < pathway.getNumberOfEnzymes(); i++) {
			genotype[i] = genotypeCheckboxes[i].isSelected();
		}
		
		// minimal medium is assumed to contain molecule 0
		ArrayList<Integer> startingMolecules = new ArrayList<Integer>();
		startingMolecules.add(new Integer(0));
		for (int i = 1; i < pathway.getNumberOfMolecules(); i++) {
			if (substrateCheckboxes[i].isSelected()) {
				startingMolecules.add(new Integer(i));
			}
		}
		
//		String info = new String("Testing pathway:");
//		for (int i = 0; i < pathway.getNumberOfEnzymes(); i++) {
//			info = info + " enzyme " + i;
//			if (genotype[i]) {
//				info = info + " active;";
//			} else {
//				info = info + " inactive;";
//			}
//		}
//		info = info + " it is eating: ";
//		for (int i = 0; i < startingMolecules.size(); i++) {
//			info = info + startingMolecules.get(i).toString() + " ";
//		}
//		
//		System.out.println(info);
		
		if (pathway.willItGrow(genotype, startingMolecules)) {
			willItGrowLabel.setText("<html><font color=\"green\">It Will grow!</html>");
		} else {
			willItGrowLabel.setText("<html><font color=\"red\">It Won't grow!</html>");
		}
	}

}
