package ModelBuilder;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import VGL.Messages;

public class ComplementationPanel extends ModelDetailsPanel {
	
	JComboBox intermediateChoices;  // for middle choice; linked to 1st one
	JLabel gALabel;
	JLabel gBLabel;
	
	public ComplementationPanel(String[] allPhenos,
			JComboBox t1Choices,
			JComboBox t2Choices) {
		// don't use last pheno if complementation
		String[] phenos = new String[3];
		phenos[0] = allPhenos[0];
		phenos[1] = allPhenos[1];
		phenos[2] = allPhenos[2];
		t1Choices = new JComboBox(phenos);
		t2Choices = new JComboBox(phenos);
		this.t1Choices = t1Choices;
		this.t2Choices = t2Choices;
		intermediateChoices = new JComboBox(phenos);
		
		add(t1Choices);
		gALabel = new JLabel(Messages.getInstance().getString("VGLII.Gene") + " A");
		add(gALabel);
		add(intermediateChoices);
		gBLabel = new JLabel(Messages.getInstance().getString("VGLII.Gene") + " B");
		add(gBLabel);
		add(t2Choices);
	}

	public String[] getChoices() {
		return null;
	}
	
	

}
