package ModelBuilder;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import VGL.Messages;

public class TwoSimplePanel extends ModelDetailsPanel {
	
	public TwoSimplePanel(String[] phenos, 
			JComboBox t1Choices, 
			JComboBox t2Choices) {
		this.t1Choices = t1Choices;
		this.t2Choices = t2Choices;
		t2Choices = new JComboBox(phenos);
		add(t2Choices);
		add(new JLabel(Messages.getInstance().getString("VGLII.IsDominantTo")));
		t1Choices = new JComboBox(phenos);
		add(t1Choices);
	}
	
	public String[] getChoices() {
		String[] r = new String[2];
		r[0] = t1Choices.getSelectedItem().toString();
		r[1] = t2Choices.getSelectedItem().toString();
		return r;
	}


}
