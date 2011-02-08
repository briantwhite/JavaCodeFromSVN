package ModelBuilder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import VGL.Messages;

public class ComplementationPanel extends ModelDetailsPanel implements ItemListener {

	JComboBox intermediateChoices;  // for middle choice; linked to 1st one
	JLabel gALabel;
	JLabel gBLabel;

	public ComplementationPanel(String[] allPhenos,
			JComboBox t1Choices,
			JComboBox t2Choices,
			ModelPane mp) {
		
		this.mp = mp;
		
		// don't use last pheno if complementation
		String[] phenos = new String[3];
		phenos[0] = allPhenos[0];
		phenos[1] = allPhenos[1];
		phenos[2] = allPhenos[2];
		t1Choices = new JComboBox(phenos);
		t1Choices.addItemListener(this);
		t2Choices = new JComboBox(phenos);
		this.t1Choices = t1Choices;
		t1Choices.addItemListener(this);
		this.t2Choices = t2Choices;
		t2Choices.addItemListener(this);
		intermediateChoices = new JComboBox(phenos);
		intermediateChoices.addItemListener(this);

		add(t1Choices);
		gALabel = new JLabel(Messages.getInstance().getString("VGLII.Gene") + " A");
		add(gALabel);
		add(intermediateChoices);
		gBLabel = new JLabel(Messages.getInstance().getString("VGLII.Gene") + " B");
		add(gBLabel);
		add(t2Choices);
	}


	// make the first 2 choices track each other and report changes to UI
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			if (e.getSource().equals(t1Choices)) {
				intermediateChoices.setSelectedItem(t1Choices.getSelectedItem());
				mp.setT1Value((String)t1Choices.getSelectedItem());
			}

			if (e.getSource().equals(intermediateChoices)) {
				t1Choices.setSelectedItem(intermediateChoices.getSelectedItem());
			}
			
			if (e.getSource().equals(t2Choices)) {
				mp.setT2Value((String)t2Choices.getSelectedItem());
			}
		}
	}

	public void updateT1Choices(String s) {
		t1Choices.setSelectedItem(s);
	}
	
	public void updateT2Choices(String s) {
		t2Choices.setSelectedItem(s);
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
