package ModelBuilder;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import VGL.Messages;

public class TwoIncPanel extends ModelDetailsPanel {
	
	public TwoIncPanel(String[] phenos, 
			JComboBox t1Choices, 
			JComboBox t2Choices,
			JComboBox t3Choices) {
		this.t1Choices = t1Choices;
		this.t2Choices = t2Choices;
		this.t3Choices = t3Choices;
		setLayout(new GridLayout(3,2));
		t1Choices = new JComboBox(phenos);
		add(t1Choices);
		add(new JLabel(Messages.getInstance().getString("VGLII.IsPureBreeding")));
		
		add(new JLabel(
				Messages.getInstance().getString("VGLII.CombineToGive"), 
				combineArrow, 
				JLabel.LEADING));
		t3Choices = new JComboBox(phenos);
		add(t3Choices);		

		t2Choices = new JComboBox(phenos);
		add(t2Choices);
		add(new JLabel(Messages.getInstance().getString("VGLII.IsPureBreeding")));
	}

	public String[] getChoices() {
		String[] r = new String[3];
		r[0] = t1Choices.getSelectedItem().toString();
		r[1] = t2Choices.getSelectedItem().toString();
		r[2] = t3Choices.getSelectedItem().toString();		
		return r;
	}

}
