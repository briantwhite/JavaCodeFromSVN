package ModelBuilder;

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

import VGL.Messages;

public class ThreeIncPanel extends ModelDetailsPanel implements ItemListener {
	
	private JLabel t5;	// marker for arrow
	
	public ThreeIncPanel(String[] phenos,
			JComboBox t1Choices,
			JComboBox t2Choices,
			JComboBox t3Choices,
			JComboBox t4Choices,
			JComboBox t5Choices,
			JComboBox t6Choices,
			ModelPane mp) {

		t1Choices = new JComboBox(phenos);
		t2Choices = new JComboBox(phenos);
		t3Choices = new JComboBox(phenos);
		t4Choices = new JComboBox(phenos);
		t5Choices = new JComboBox(phenos);
		t6Choices = new JComboBox(phenos);
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(gridBag);
		
		// first line
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		gridBag.setConstraints(t1Choices, c);
		add(t1Choices);
		
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
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
		this.t4Choices = t1Choices;
		t4Choices.addItemListener(this);
		this.t5Choices = t2Choices;
		t5Choices.addItemListener(this);
		this.t6Choices = t3Choices;
		t6Choices.addItemListener(this);
		
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
			
			if (e.getSource().equals(t4Choices)) {
				mp.setT4Value((String)t4Choices.getSelectedItem());
			}
		
			if (e.getSource().equals(t5Choices)) {
				mp.setT5Value((String)t5Choices.getSelectedItem());
			}
			
			if (e.getSource().equals(t6Choices)) {
				mp.setT6Value((String)t6Choices.getSelectedItem());
			}

		}
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

}
