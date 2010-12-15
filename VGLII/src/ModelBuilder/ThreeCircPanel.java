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
import javax.swing.JTextField;

import VGL.Messages;

public class ThreeCircPanel extends ModelDetailsPanel implements ItemListener {
	
	JTextField t3ReDisplay;

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
		
		add(new JLabel(""));
		add(t3Choices);
		t3Choices.addItemListener(this);
		
		add(new JLabel(""));
		add(new JLabel(Messages.getInstance().getString("VGLII.IsDominantTo")));

		add(new JLabel(""));
		add(t2Choices);

		add(new JLabel(""));
		add(new JLabel(Messages.getInstance().getString("VGLII.IsDominantTo")));

		add(new JLabel(""));
		add(t1Choices);
		
		add(new JLabel(""));
		add(new JLabel(Messages.getInstance().getString("VGLII.IsDominantTo")));
		
		// set up echo of t3
		int maxLen = 0;
		for (int i = 0; i < phenos.length; i++) {
			if (phenos[i].length() > maxLen) maxLen = phenos[i].length();
		}
		t3ReDisplay = new JTextField(maxLen);
		t3ReDisplay.setText(phenos[0]);
		t3ReDisplay.setEditable(false);
		add(new JLabel(""));
		add(t3ReDisplay);
	}

	public String[] getChoices() {
		String[] r = new String[3];
		r[0] = t1Choices.getSelectedItem().toString();
		r[1] = t2Choices.getSelectedItem().toString();
		r[2] = t3Choices.getSelectedItem().toString();		
		return r;
	}

	public void itemStateChanged(ItemEvent arg0) {
		t3ReDisplay.setText(t3Choices.getSelectedItem().toString());
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(5F));
		g2d.drawArc(50, 15, 80, t3ReDisplay.getLocation().y, 90, 180);
		
		g2d.drawLine(90,15,100,15);
		g2d.drawLine(95,10,100,15);
		g2d.drawLine(95,20,100,15);
		
		g2d.drawLine(90,t3ReDisplay.getLocation().y + 15,100,t3ReDisplay.getLocation().y + 15);
		g2d.drawLine(95,t3ReDisplay.getLocation().y + 20,100,t3ReDisplay.getLocation().y + 15);
		g2d.drawLine(95,t3ReDisplay.getLocation().y + 10,100,t3ReDisplay.getLocation().y + 15);
	}


}
