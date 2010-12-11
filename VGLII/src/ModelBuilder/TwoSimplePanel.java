package ModelBuilder;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import VGL.Messages;

public class TwoSimplePanel extends JPanel {
	
	public TwoSimplePanel(String[] phenos, 
			JComboBox t1Choices, 
			JComboBox t2Choices) {
		t1Choices = new JComboBox(phenos);
		add(t1Choices);
		add(new JLabel(Messages.getInstance().getString("VGLII.IsDominantTo")));
		t2Choices = new JComboBox(phenos);
		add(t2Choices);
	}

}
