package ModelBuilder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import VGL.Messages;

public class EpistasisPanel extends ModelDetailsPanel {
	JLabel gALabel;
	JLabel gBLabel;

	public EpistasisPanel(String[] phenos,
			JComboBox t1Choices,
			JComboBox t2Choices,
			JComboBox t3Choices) {
		t1Choices = new JComboBox(phenos);
		t2Choices = new JComboBox(phenos);
		t3Choices = new JComboBox(phenos);
		this.t1Choices = t1Choices;
		this.t2Choices = t2Choices;
		this.t3Choices = t3Choices;

		add(t1Choices);
		gALabel = new JLabel(Messages.getInstance().getString("VGLII.Gene") + " A");
		add(gALabel);
		add(t2Choices);
		gBLabel = new JLabel(Messages.getInstance().getString("VGLII.Gene") + " B");
		add(gBLabel);
		add(t3Choices);
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
		g2d.drawLine(gALabel.getLocation().x, gALabel.getLocation().y + 18, 
				gALabel.getLocation().x + 40, gALabel.getLocation().y + 18);
		g2d.drawLine(gALabel.getLocation().x + 35, gALabel.getLocation().y + 13, 
				gALabel.getLocation().x + 40, gALabel.getLocation().y + 18);
		g2d.drawLine(gALabel.getLocation().x + 35, gALabel.getLocation().y + 23, 
				gALabel.getLocation().x + 40, gALabel.getLocation().y + 18);
		g2d.drawLine(gBLabel.getLocation().x, gBLabel.getLocation().y + 18, 
				gBLabel.getLocation().x + 40, gBLabel.getLocation().y + 18);
		g2d.drawLine(gBLabel.getLocation().x + 35, gBLabel.getLocation().y + 13, 
				gBLabel.getLocation().x + 40, gBLabel.getLocation().y + 18);
		g2d.drawLine(gBLabel.getLocation().x + 35, gBLabel.getLocation().y + 23, 
				gBLabel.getLocation().x + 40, gBLabel.getLocation().y + 18);
	}


}

