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
				mp.setT1Value(t1Choices.getSelectedIndex());
			}
		
			if (e.getSource().equals(t2Choices)) {
				mp.setT2Value(t2Choices.getSelectedIndex());
			}
			
			if (e.getSource().equals(t3Choices)) {
				mp.setT3Value(t3Choices.getSelectedIndex());
			}
	
		}
	}

	public void updateT1Choices(int x) {
		t1Choices.setSelectedIndex(x);
	}
	
	public void updateT2Choices(int x) {
		t2Choices.setSelectedIndex(x);
	}
	
	public void updateT3Choices(int x) {
		t3Choices.setSelectedIndex(x);
	}
	
	public String getAsHtml() {
		StringBuffer b = new StringBuffer();
		b.append("<ul>");
		
		b.append("<li>" + (String)t1Choices.getSelectedItem() + " ");
		b.append(Messages.getInstance().getString("VGLII.IsPureBreeding") + "</li>");
		
		b.append("<li>" + (String)t3Choices.getSelectedItem() + " ");
		b.append(Messages.getInstance().getString("VGLII.IsInBetween") + " ");
		b.append((String)t1Choices.getSelectedItem() + " ");
		b.append(Messages.getInstance().getString("VGLII.And") + " ");
		b.append((String)t2Choices.getSelectedItem() + "</li>");
		
		b.append("<li>" + (String)t2Choices.getSelectedItem() + " ");
		b.append(Messages.getInstance().getString("VGLII.IsPureBreeding") + "</li>");
		
		b.append("</ul>");
		return b.toString();
	}


}
