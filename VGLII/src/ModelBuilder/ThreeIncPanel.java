package ModelBuilder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import VGL.Messages;

public class ThreeIncPanel extends ModelDetailsPanel {
	
	private JLabel b5;	// marker for arrow
	
	public ThreeIncPanel(String[] phenos,
			JComboBox t1Choices,
			JComboBox t2Choices,
			JComboBox t3Choices,
			JComboBox t4Choices,
			JComboBox t5Choices,
			JComboBox t6Choices) {
		this.t1Choices = t1Choices;
		this.t2Choices = t2Choices;
		this.t3Choices = t3Choices;
		this.t4Choices = t4Choices;
		this.t5Choices = t5Choices;
		this.t6Choices = t6Choices;
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
		JLabel b0 = new JLabel("");
		gridBag.setConstraints(b0, c);
		add(b0);
		
		c.gridx = 1;
		c.gridy = 0;
		gridBag.setConstraints(t1Choices, c);
		add(t1Choices);
		
		c.gridx = 2;
		c.gridy = 0;
		JLabel t0 = new JLabel(Messages.getInstance().getString("VGLII.IsPureBreeding"));
		gridBag.setConstraints(t0, c);
		add(t0);
		
		// second line
		c.gridx = 0;
		c.gridy = 1;
		JLabel b1 = new JLabel("");
		gridBag.setConstraints(b1, c);
		add(b1);		
		
		c.gridx = 1;
		c.gridy = 1;
		JLabel t1 = new JLabel(
				Messages.getInstance().getString("VGLII.CombineToGive"), 
				combineArrow, 
				JLabel.LEADING);
		gridBag.setConstraints(t1, c);
		add(t1);
		
		c.gridx = 2;
		c.gridy = 1;
		gridBag.setConstraints(t4Choices, c);
		add(t4Choices);
		
		// third line
		c.gridx = 0;
		c.gridy = 2;
		JLabel b2 = new JLabel("");
		gridBag.setConstraints(b2, c);
		add(b2);
		
		c.gridx = 1;
		c.gridy = 2;
		gridBag.setConstraints(t2Choices, c);
		add(t2Choices);
		
		c.gridx = 2;
		c.gridy = 2;
		JLabel t2 = new JLabel(Messages.getInstance().getString("VGLII.IsPureBreeding"));
		gridBag.setConstraints(t2, c);
		add(t2);
		
		// fourth line
		c.gridx = 0;
		c.gridy = 3;
		JLabel b3 = new JLabel("");
		gridBag.setConstraints(b3, c);
		add(b3);
		
		c.gridx = 1;
		c.gridy = 3;
		JLabel t3 = new JLabel(
				Messages.getInstance().getString("VGLII.CombineToGive"), 
				combineArrow, 
				JLabel.LEADING);
		gridBag.setConstraints(t3, c);
		add(t3);
		
		c.gridx = 2;
		c.gridy = 3;
		gridBag.setConstraints(t5Choices, c);
		add(t5Choices);
		
		// fifth line
		c.gridx = 0;
		c.gridy = 4;
		JLabel b4 = new JLabel("");
		gridBag.setConstraints(b4, c);
		add(b4);
		
		c.gridx = 1;
		c.gridy = 4;
		gridBag.setConstraints(t3Choices, c);
		add(t3Choices);
		
		c.gridx = 2;
		c.gridy = 4;
		JLabel t4 = new JLabel(Messages.getInstance().getString("VGLII.IsPureBreeding"));
		gridBag.setConstraints(t4, c);
		add(t4);
		
		// sixth line
		c.gridx = 0;
		c.gridy = 5;
		b5 = new JLabel("");
		gridBag.setConstraints(b5, c);
		add(b5);
		
		c.gridx = 1;
		c.gridy = 5;
		JLabel t5 = new JLabel(
				Messages.getInstance().getString("VGLII.CombineToGive"), 
				combineArrow2, 
				JLabel.LEADING);
		gridBag.setConstraints(t5, c);
		add(t5);
		
		c.gridx = 2;
		c.gridy = 5;
		gridBag.setConstraints(t6Choices, c);
		add(t6Choices);
	}
	
	public String[] getChoices() {
		String[] r = new String[6];
		r[0] = t1Choices.getSelectedItem().toString();
		r[1] = t2Choices.getSelectedItem().toString();
		r[2] = t3Choices.getSelectedItem().toString();		
		r[3] = t4Choices.getSelectedItem().toString();
		r[4] = t5Choices.getSelectedItem().toString();
		r[5] = t6Choices.getSelectedItem().toString();		
		return r;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(5F));
		
		g2d.drawLine(0,10,10,10);
		g2d.drawLine(0, 10, 0, 75);
//		g2d.drawLine(0, b5.getLocation().y + 15, 10, b5.getLocation().y + 15);
//		
//		g2d.drawLine(105,b5.getLocation().y + 15,115,b5.getLocation().y + 15);
//		g2d.drawLine(115,b5.getLocation().y + 15,105,b5.getLocation().y + 10);
//		g2d.drawLine(115,b5.getLocation().y + 15,105,b5.getLocation().y + 20);

	}

}
