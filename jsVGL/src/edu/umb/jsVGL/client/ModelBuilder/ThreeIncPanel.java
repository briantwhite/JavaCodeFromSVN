package edu.umb.jsVGL.client.ModelBuilder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;

public class ThreeIncPanel extends ModelDetailsPanel implements ChangeHandler {

	private Label t5;	// marker for arrow

	public ThreeIncPanel(String[] phenos,
			ListBox t1Choices,
			ListBox t2Choices,
			ListBox t3Choices,
			ListBox t4Choices,
			ListBox t5Choices,
			ListBox t6Choices,
			ModelPane mp) {

		t1Choices = new ListBox(phenos);
		t2Choices = new ListBox(phenos);
		t3Choices = new ListBox(phenos);
		t4Choices = new ListBox(phenos);
		t5Choices = new ListBox(phenos);
		t6Choices = new ListBox(phenos);
		for (int i = 0; i < phenos.length; i++) {
			t1Choices.addItem(phenos[i]);
			t2Choices.addItem(phenos[i]);
			t3Choices.addItem(phenos[i]);
			t4Choices.addItem(phenos[i]);
			t5Choices.addItem(phenos[i]);
			t6Choices.addItem(phenos[i]);
		}
		
		// first line
		add(t1Choices);

		JLabel t0 = new JLabel(Messages.getInstance().getString("VGLII.IsPureBreeding"));
		gridBag.setConstraints(t0, c);
		add(t0);

		c.gridx = 2;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		JLabel b0 = new JLabel("");
		gridBag.setConstraints(b0, c);
		add(b0);

		// second line
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		JLabel b1 = new JLabel("");
		gridBag.setConstraints(b1, c);
		add(b1);		

		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.WEST;
		JLabel t1 = new JLabel(
				Messages.getInstance().getString("VGLII.CombineToGive"), 
				combineArrow, 
				JLabel.LEADING);
		gridBag.setConstraints(t1, c);
		add(t1);

		c.gridx = 2;
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		gridBag.setConstraints(t4Choices, c);
		add(t4Choices);

		// third line
		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.EAST;
		gridBag.setConstraints(t2Choices, c);
		add(t2Choices);

		c.gridx = 1;
		c.gridy = 2;
		c.anchor = GridBagConstraints.WEST;
		JLabel t2 = new JLabel(Messages.getInstance().getString("VGLII.IsPureBreeding"));
		gridBag.setConstraints(t2, c);
		add(t2);

		c.gridx = 2;
		c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER;
		JLabel b2 = new JLabel("");
		gridBag.setConstraints(b2, c);
		add(b2);

		// fourth line
		c.gridx = 0;
		c.gridy = 3;
		c.anchor = GridBagConstraints.CENTER;
		JLabel b3 = new JLabel("");
		gridBag.setConstraints(b3, c);
		add(b3);

		c.gridx = 1;
		c.gridy = 3;
		c.anchor = GridBagConstraints.WEST;
		JLabel t3 = new JLabel(
				Messages.getInstance().getString("VGLII.CombineToGive"), 
				combineArrow, 
				JLabel.LEADING);
		gridBag.setConstraints(t3, c);
		add(t3);

		c.gridx = 2;
		c.gridy = 3;
		c.anchor = GridBagConstraints.EAST;
		gridBag.setConstraints(t5Choices, c);
		add(t5Choices);

		// fifth line
		c.gridx = 0;
		c.gridy = 4;
		c.anchor = GridBagConstraints.EAST;
		gridBag.setConstraints(t3Choices, c);
		add(t3Choices);

		c.gridx = 1;
		c.gridy = 4;
		c.anchor = GridBagConstraints.WEST;
		JLabel t4 = new JLabel(Messages.getInstance().getString("VGLII.IsPureBreeding"));
		gridBag.setConstraints(t4, c);
		add(t4);

		c.gridx = 2;
		c.gridy = 4;
		c.anchor = GridBagConstraints.CENTER;
		JLabel b4 = new JLabel("");
		gridBag.setConstraints(b4, c);
		add(b4);

		// sixth line
		c.gridx = 0;
		c.gridy = 5;
		c.anchor = GridBagConstraints.CENTER;
		JLabel b5 = new JLabel("");
		gridBag.setConstraints(b5, c);
		add(b5);

		c.gridx = 1;
		c.gridy = 5;
		c.anchor = GridBagConstraints.CENTER;
		t5 = new JLabel(
				Messages.getInstance().getString("VGLII.CombineToGive"), 
				combineArrow2, 
				JLabel.LEADING);
		gridBag.setConstraints(t5, c);
		add(t5);

		c.gridx = 2;
		c.gridy = 5;
		c.anchor = GridBagConstraints.EAST;
		gridBag.setConstraints(t6Choices, c);
		add(t6Choices);

		this.t1Choices = t1Choices;
		t1Choices.addItemListener(this);
		this.t2Choices = t2Choices;
		t2Choices.addItemListener(this);
		this.t3Choices = t3Choices;
		t3Choices.addItemListener(this);
		this.t4Choices = t4Choices;
		t4Choices.addItemListener(this);
		this.t5Choices = t5Choices;
		t5Choices.addItemListener(this);
		this.t6Choices = t6Choices;
		t6Choices.addItemListener(this);

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

			if (e.getSource().equals(t4Choices)) {
				mp.setT4Value(t4Choices.getSelectedIndex());
			}

			if (e.getSource().equals(t5Choices)) {
				mp.setT5Value(t5Choices.getSelectedIndex());
			}

			if (e.getSource().equals(t6Choices)) {
				mp.setT6Value(t6Choices.getSelectedIndex());
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

	public void updateT4Choices(int x) {
		t4Choices.setSelectedIndex(x);
	}

	public void updateT5Choices(int x) {
		t5Choices.setSelectedIndex(x);
	}

	public void updateT6Choices(int x) {
		t6Choices.setSelectedIndex(x);
	}


	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.GRAY);
		g2d.setStroke(new BasicStroke(5F));

		g2d.drawLine(0,10,10,10);
		g2d.drawLine(0, 10, 0, t5.getLocation().y + 15);
		g2d.drawLine(0, t5.getLocation().y + 15, t5.getLocation().x - 5, t5.getLocation().y + 15);

		g2d.drawLine(t5.getLocation().x - 10, t5.getLocation().y + 10,
				t5.getLocation().x - 5, t5.getLocation().y + 15);
		g2d.drawLine(t5.getLocation().x - 10, t5.getLocation().y + 20,
				t5.getLocation().x - 5, t5.getLocation().y + 15);
	}

	public String getAsHtml(boolean isForGrader) {
		StringBuffer b = new StringBuffer();
		b.append("<ul>");
		if (isForGrader) {
			b.append("<li>" + (String)t1Choices.getSelectedItem() + " ");
			b.append("is pure breeding.</li>");

			b.append("<li>" + (String)t4Choices.getSelectedItem() + " ");
			b.append("is in between ");
			b.append((String)t1Choices.getSelectedItem() + " ");
			b.append("and ");
			b.append((String)t2Choices.getSelectedItem() + "</li>");

			b.append("<li>" + (String)t2Choices.getSelectedItem() + " ");
			b.append("is pure breeding.</li>");

			b.append("<li>" + (String)t5Choices.getSelectedItem() + " ");
			b.append("is in between ");
			b.append((String)t2Choices.getSelectedItem() + " ");
			b.append("and ");
			b.append((String)t3Choices.getSelectedItem() + "</li>");

			b.append("<li>" + (String)t3Choices.getSelectedItem() + " ");
			b.append("is pure breeding.</li>");

			b.append("<li>" + (String)t6Choices.getSelectedItem() + " ");
			b.append("is in between ");
			b.append((String)t1Choices.getSelectedItem() + " ");
			b.append("and ");
			b.append((String)t3Choices.getSelectedItem() + "</li>");
		} else {
			b.append("<li>" + (String)t1Choices.getSelectedItem() + " ");
			b.append(Messages.getInstance().getString("VGLII.IsPureBreeding") + "</li>");

			b.append("<li>" + (String)t4Choices.getSelectedItem() + " ");
			b.append(Messages.getInstance().getString("VGLII.IsInBetween") + " ");
			b.append((String)t1Choices.getSelectedItem() + " ");
			b.append(Messages.getInstance().getString("VGLII.And") + " ");
			b.append((String)t2Choices.getSelectedItem() + "</li>");

			b.append("<li>" + (String)t2Choices.getSelectedItem() + " ");
			b.append(Messages.getInstance().getString("VGLII.IsPureBreeding") + "</li>");

			b.append("<li>" + (String)t5Choices.getSelectedItem() + " ");
			b.append(Messages.getInstance().getString("VGLII.IsInBetween") + " ");
			b.append((String)t2Choices.getSelectedItem() + " ");
			b.append(Messages.getInstance().getString("VGLII.And") + " ");
			b.append((String)t3Choices.getSelectedItem() + "</li>");

			b.append("<li>" + (String)t3Choices.getSelectedItem() + " ");
			b.append(Messages.getInstance().getString("VGLII.IsPureBreeding") + "</li>");

			b.append("<li>" + (String)t6Choices.getSelectedItem() + " ");
			b.append(Messages.getInstance().getString("VGLII.IsInBetween") + " ");
			b.append((String)t1Choices.getSelectedItem() + " ");
			b.append(Messages.getInstance().getString("VGLII.And") + " ");
			b.append((String)t3Choices.getSelectedItem() + "</li>");
		}
		b.append("</ul>");
		return b.toString();
	}

}
