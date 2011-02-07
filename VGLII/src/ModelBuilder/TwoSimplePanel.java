package ModelBuilder;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import VGL.Messages;

public class TwoSimplePanel extends ModelDetailsPanel implements ItemListener {
	
	public TwoSimplePanel(String[] phenos, 
			JComboBox t1Choices, 
			JComboBox t2Choices,
			ModelPane mp) {
		setLayout(new GridLayout(3,1));
		t2Choices = new JComboBox(phenos);
		add(t2Choices);
		add(new JLabel(Messages.getInstance().getString("VGLII.IsDominantTo")));
		t1Choices = new JComboBox(phenos);
		add(t1Choices);
		
		this.t1Choices = t1Choices;
		t1Choices.addItemListener(this);
		this.t2Choices = t2Choices;
		t2Choices.addItemListener(this);
		
		this.mp = mp;

	}
	
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			if (e.getSource().equals(t1Choices)) {
				mp.setT1Value((String)t1Choices.getSelectedItem());
			}
		
			if (e.getSource().equals(t2Choices)) {
				mp.setT2Value((String)t2Choices.getSelectedItem());
			}
				
		}
	}


}
