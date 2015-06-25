package ModelBuilder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import VGL.Messages;

public class BloodTypePanel extends ModelDetailsPanel implements ItemListener {

	JLabel l;

	public BloodTypePanel(String[] phenos,
			JComboBox t1Choices,
			JComboBox t2Choices,
			JComboBox t3Choices,
			JComboBox t4Choices,
			ModelPane mp) {
		t1Choices = new JComboBox(phenos);
		t2Choices = new JComboBox(phenos);
		t3Choices = new JComboBox(phenos);
		t4Choices = new JComboBox(phenos);
		this.t1Choices = t1Choices;
		t1Choices.addItemListener(this);
		this.t2Choices = t2Choices;
		t2Choices.addItemListener(this);
		this.t3Choices = t3Choices;
		t3Choices.addItemListener(this);
		this.t4Choices = t4Choices;
		t4Choices.addItemListener(this);
		this.mp = mp;

		setLayout(new GridLayout(4,2));

		add(t4Choices);
		add(new JLabel("~ " + Messages.getInstance().getString("VGLII.Type") + " AB"));

		add(t3Choices);
		add(new JLabel("~ " + Messages.getInstance().getString("VGLII.Type") + " B"));

		add(t2Choices);
		add(new JLabel("~ " + Messages.getInstance().getString("VGLII.Type") + " A"));

		add(t1Choices);
		add(new JLabel("~ " + Messages.getInstance().getString("VGLII.Type") + " O"));
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
	
	public void updateT4Choices(int x) {
		t4Choices.setSelectedIndex(x);
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
			
			if (e.getSource().equals(t4Choices)) {
				mp.setT4Value(t4Choices.getSelectedIndex());
			}

		}
	}

	public String getAsHtml(boolean isForGrader) {
		StringBuffer b = new StringBuffer();
		b.append("<ul>");
		if (isForGrader) {
			b.append("<li>" + (String)t1Choices.getSelectedItem() + " is recessive to all (like type O)<li>");

			b.append("<li>" + (String)t2Choices.getSelectedItem() + " ");
			b.append("is dominant to ");
			b.append((String)t1Choices.getSelectedItem() + "; ");
			b.append("and co-dominant with ");
			b.append((String)t3Choices.getSelectedItem() + " (like type A)</li>");

			b.append("<li>" + (String)t3Choices.getSelectedItem() + " ");
			b.append("is dominant to ");
			b.append((String)t1Choices.getSelectedItem() + "; ");
			b.append("and co-dominant with  ");
			b.append((String)t2Choices.getSelectedItem() + " (like type B)</li>");
			b.append((String)t4Choices.getSelectedItem() + " is the combination of ");
			b.append((String)t2Choices.getSelectedItem() + " and " + (String)t3Choices.getSelectedItem());
			b.append(" like type AB</li>");
		} else {
			b.append("<li>" + (String)t1Choices.getSelectedItem() + "~ " + Messages.getInstance().getString("VGLII.Type") + " O</li>");
			b.append("<li>" + (String)t2Choices.getSelectedItem() + "~ " + Messages.getInstance().getString("VGLII.Type") + " A</li>");
			b.append("<li>" + (String)t3Choices.getSelectedItem() + "~ " + Messages.getInstance().getString("VGLII.Type") + " B</li>");
			b.append("<li>" + (String)t4Choices.getSelectedItem() + "~ " + Messages.getInstance().getString("VGLII.Type") + " AB</li>");
		}
		b.append("</ul>");
		return b.toString();
	}

}
