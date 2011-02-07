package ModelBuilder;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import VGL.Messages;

public class ThreeHierPanel extends ModelDetailsPanel implements ItemListener {

	public ThreeHierPanel(String[] phenos,
			JComboBox t1Choices,
			JComboBox t2Choices,
			JComboBox t3Choices,
			ModelPane mp) {
		t1Choices = new JComboBox(phenos);
		t2Choices = new JComboBox(phenos);
		t3Choices = new JComboBox(phenos);
		this.t1Choices = t1Choices;
		t1Choices.addItemListener(this);
		this.t2Choices = t2Choices;
		t2Choices.addItemListener(this);
		this.t3Choices = t3Choices;
		t3Choices.addItemListener(this);
		this.mp = mp;
		
		setLayout(new GridLayout(5,1));
		add(t3Choices);
		add(new JLabel(Messages.getInstance().getString("VGLII.IsDominantTo")));
		add(t2Choices);
		add(new JLabel(Messages.getInstance().getString("VGLII.IsDominantTo")));
		add(t1Choices);
	}
	
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			if (e.getSource().equals(t1Choices)) {
				mp.setT1Value((String)t1Choices.getSelectedItem());
			}
		
			if (e.getSource().equals(t2Choices)) {
				mp.setT2Value((String)t2Choices.getSelectedItem());
			}
			
			if (e.getSource().equals(t3Choices)) {
				mp.setT3Value((String)t3Choices.getSelectedItem());
			}
	
		}
	}

}
