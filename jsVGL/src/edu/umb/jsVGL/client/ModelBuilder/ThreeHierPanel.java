package edu.umb.jsVGL.client.ModelBuilder;

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

	public String getAsHtml(boolean isForGrader) {
		StringBuffer b = new StringBuffer();
		b.append("<ul>");
		if (isForGrader) {
			b.append("<li>" + (String)t1Choices.getSelectedItem() + " ");
			b.append("is recessive to all</li>");

			b.append("<li>" + (String)t2Choices.getSelectedItem() + " ");
			b.append("is dominant to ");
			b.append((String)t1Choices.getSelectedItem() + "; ");
			b.append("is recessive to ");
			b.append((String)t3Choices.getSelectedItem() + "</li>");

			b.append("<li>" + (String)t3Choices.getSelectedItem() + " ");
			b.append("is dominant to all</li>");
		} else {
			b.append("<li>" + (String)t1Choices.getSelectedItem() + " ");
			b.append(Messages.getInstance().getString("VGLII.IsRecessiveToAll") + "</li>");

			b.append("<li>" + (String)t2Choices.getSelectedItem() + " ");
			b.append(Messages.getInstance().getString("VGLII.IsDominantTo") + " ");
			b.append((String)t1Choices.getSelectedItem() + "; ");
			b.append(Messages.getInstance().getString("VGLII.IsRecessiveTo") + " ");
			b.append((String)t3Choices.getSelectedItem() + "</li>");

			b.append("<li>" + (String)t3Choices.getSelectedItem() + " ");
			b.append(Messages.getInstance().getString("VGLII.IsDominantToAll") + "</li>");
		}
		b.append("</ul>");
		return b.toString();
	}

}
