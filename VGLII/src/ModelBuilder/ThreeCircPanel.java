package ModelBuilder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import VGL.Messages;

public class ThreeCircPanel extends ModelDetailsPanel {
	
	JLabel l;

	public ThreeCircPanel(String[] phenos,
			JComboBox t1Choices,
			JComboBox t2Choices,
			JComboBox t3Choices) {
		t1Choices = new JComboBox(phenos);
		t2Choices = new JComboBox(phenos);
		t3Choices = new JComboBox(phenos);
		this.t1Choices = t1Choices;
		this.t2Choices = t2Choices;
		this.t3Choices = t3Choices;
				
		setLayout(new GridLayout(7,2));
		
		add(t3Choices);
		add(new JLabel(""));
		
		add(new JLabel(Messages.getInstance().getString("VGLII.IsDominantTo")));
		add(new JLabel(""));

		add(t2Choices);
		add(new JLabel(""));

		add(new JLabel(Messages.getInstance().getString("VGLII.IsDominantTo")));
		add(new JLabel(""));

		add(t1Choices);
		add(new JLabel(""));
		
		l = new JLabel(Messages.getInstance().getString("VGLII.IsDominantTo"));
		add(l);
		add(new JLabel(""));
		
	}

	public String[] getChoices() {
		String[] r = new String[3];
		r[0] = t1Choices.getSelectedItem().toString();
		r[1] = t2Choices.getSelectedItem().toString();
		r[2] = t3Choices.getSelectedItem().toString();		
		return r;
	}

	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.GRAY);
		g2d.setStroke(new BasicStroke(5F));
		g2d.drawArc(70, 15, 80, l.getLocation().y, -90, 180);
		
		g2d.drawLine(95,15,105,15);
		g2d.drawLine(105,10,95,15);
		g2d.drawLine(105,20,95,15);
		
		g2d.drawLine(95,l.getLocation().y + 15,105,l.getLocation().y + 15);
	}


}
