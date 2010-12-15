package ModelBuilder;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import VGL.Messages;

public class ThreeIncPanel extends ModelDetailsPanel {
	
	public ThreeIncPanel(String[] phenos,
			JComboBox t1Choices,
			JComboBox t2Choices,
			JComboBox t3Choices,
			JComboBox t4Choices,
			JComboBox t5Choices,
			JComboBox t6Choices) {
		this.t1Choices = t1Choices;
		this.t2Choices = t2Choices;
		this.t3Choices = t3Choices;
		this.t4Choices = t4Choices;
		this.t5Choices = t5Choices;
		this.t6Choices = t6Choices;
		t1Choices = new JComboBox(phenos);
		t2Choices = new JComboBox(phenos);
		t3Choices = new JComboBox(phenos);
		t4Choices = new JComboBox(phenos);
		t5Choices = new JComboBox(phenos);
		t6Choices = new JComboBox(phenos);
		add(t1Choices);
		add(new JLabel(Messages.getInstance().getString("VGLII.IsPureBreeding")));
		add(new JLabel(Messages.getInstance().getString("VGLII.CombineToGive")));
		add(t4Choices);
		add(t2Choices);
		add(new JLabel(Messages.getInstance().getString("VGLII.IsPureBreeding")));
		add(new JLabel(Messages.getInstance().getString("VGLII.CombineToGive")));
		add(t5Choices);
		add(t3Choices);
		add(new JLabel(Messages.getInstance().getString("VGLII.IsPureBreeding")));
		add(new JLabel(Messages.getInstance().getString("VGLII.CombineToGive")));
		add(t6Choices);
	}
	
	public String[] getChoices() {
		String[] r = new String[6];
		r[0] = t1Choices.getSelectedItem().toString();
		r[1] = t2Choices.getSelectedItem().toString();
		r[2] = t3Choices.getSelectedItem().toString();		
		r[3] = t4Choices.getSelectedItem().toString();
		r[4] = t5Choices.getSelectedItem().toString();
		r[5] = t6Choices.getSelectedItem().toString();		
		return r;
	}


}
