package ModelBuilder;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import VGL.Messages;

public class ThreeHierPanel extends ModelDetailsPanel {

	public ThreeHierPanel(String[] phenos,
			JComboBox t1Choices,
			JComboBox t2Choices,
			JComboBox t3Choices) {
		this.t1Choices = t1Choices;
		this.t2Choices = t2Choices;
		this.t3Choices = t3Choices;
		t1Choices = new JComboBox(phenos);
		t2Choices = new JComboBox(phenos);
		t3Choices = new JComboBox(phenos);
		setLayout(new GridLayout(5,1));
		add(t3Choices);
		add(new JLabel(Messages.getInstance().getString("VGLII.IsDominantTo")));
		add(t2Choices);
		add(new JLabel(Messages.getInstance().getString("VGLII.IsDominantTo")));
		add(t1Choices);
	}
	
	public String[] getChoices() {
		String[] r = new String[3];
		r[0] = t1Choices.getSelectedItem().toString();
		r[1] = t2Choices.getSelectedItem().toString();
		r[2] = t3Choices.getSelectedItem().toString();		
		return r;
	}

}
