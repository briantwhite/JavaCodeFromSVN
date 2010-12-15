package ModelBuilder;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import VGL.Messages;

public class TwoIncPanel extends ModelDetailsPanel {
	
	public TwoIncPanel(String[] phenos, 
			JComboBox t1Choices, 
			JComboBox t2Choices,
			JComboBox t3Choices) {
		this.t1Choices = t1Choices;
		this.t2Choices = t2Choices;
		this.t3Choices = t3Choices;
		t1Choices = new JComboBox(phenos);
		add(t1Choices);
		add(new JLabel(Messages.getInstance().getString("VGLII.IsPureBreeding")));
		t2Choices = new JComboBox(phenos);
		add(t2Choices);
		add(new JLabel(Messages.getInstance().getString("VGLII.IsPureBreeding")));
		add(new JLabel(Messages.getInstance().getString("VGLII.CombineToGive")));
		t3Choices = new JComboBox(phenos);
		add(t3Choices);		
	}

	public String[] getChoices() {
		String[] r = new String[3];
		r[0] = t1Choices.getSelectedItem().toString();
		r[1] = t2Choices.getSelectedItem().toString();
		r[2] = t3Choices.getSelectedItem().toString();		
		return r;
	}

}
