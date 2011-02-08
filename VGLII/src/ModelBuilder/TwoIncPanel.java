package ModelBuilder;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import VGL.Messages;

public class TwoIncPanel extends ModelDetailsPanel implements ItemListener {
	
	public TwoIncPanel(String[] phenos, 
			JComboBox t1Choices, 
			JComboBox t2Choices,
			JComboBox t3Choices,
			ModelPane mp) {
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
		
		this.t1Choices = t1Choices;
		t1Choices.addItemListener(this);
		this.t2Choices = t2Choices;
		t2Choices.addItemListener(this);
		this.t3Choices = t3Choices;
		t3Choices.addItemListener(this);
		
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
			
			if (e.getSource().equals(t3Choices)) {
				mp.setT3Value((String)t3Choices.getSelectedItem());
			}
	
		}
	}

	public void updateT1Choices(String s) {
		t1Choices.setSelectedItem(s);
	}
	
	public void updateT2Choices(String s) {
		t2Choices.setSelectedItem(s);
	}
	
	public void updateT3Choices(String s) {
		t3Choices.setSelectedItem(s);
	}

}
