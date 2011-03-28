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
				mp.setT1Value(t1Choices.getSelectedIndex());
			}

			if (e.getSource().equals(t2Choices)) {
				mp.setT2Value(t2Choices.getSelectedIndex());
			}

		}
	}

	public void updateT1Choices(int x) {
		t1Choices.setSelectedIndex(x);
	}

	public void updateT2Choices(int x) {
		t2Choices.setSelectedIndex(x);
	}

	public String getAsHtml(boolean isForGrader) {
		StringBuffer b = new StringBuffer();
		b.append("<ul>");
		if (isForGrader) {
			b.append("<li>" + (String)t1Choices.getSelectedItem() + " ");
			b.append("is recessive</li>");

			b.append("<li>" + (String)t2Choices.getSelectedItem() + " ");
			b.append("is dominant</li>");
		} else {
			b.append("<li>" + (String)t1Choices.getSelectedItem() + " ");
			b.append(Messages.getInstance().getString("VGLII.IsRecessive") + "</li>");

			b.append("<li>" + (String)t2Choices.getSelectedItem() + " ");
			b.append(Messages.getInstance().getString("VGLII.IsDominant") + "</li>");
		}
		b.append("</ul>");
		return b.toString();
	}

}
