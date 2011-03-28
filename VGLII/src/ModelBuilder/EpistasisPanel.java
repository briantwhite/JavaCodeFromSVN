package ModelBuilder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import VGL.Messages;

public class EpistasisPanel extends ModelDetailsPanel implements ItemListener {
	JLabel gALabel;
	JLabel gBLabel;


	public EpistasisPanel(String[] phenos,
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

	public void updateT1Choices(int x) {
		t1Choices.setSelectedIndex(x);
	}
	
	public void updateT2Choices(int x) {
		t2Choices.setSelectedIndex(x);
	}
	
	public void updateT3Choices(int x) {
		t3Choices.setSelectedIndex(x);
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

	public String getAsHtml(boolean isForGrader) {
		StringBuffer b = new StringBuffer();
		b.append("<ul>");
		b.append("<li>");
		b.append((String)t1Choices.getSelectedItem());
		b.append(" ---(");
		if (isForGrader) {
			b.append("Gene");
		} else {
			b.append(Messages.getInstance().getString("VGLII.Gene"));			
		}
		b.append(" A)--->");
		
		b.append((String)t2Choices.getSelectedItem());
		
		b.append(" ---(");
		if (isForGrader) {
			b.append("Gene");
		} else {
			b.append(Messages.getInstance().getString("VGLII.Gene"));			
		}
		b.append(" B)--->");
		
		b.append((String)t3Choices.getSelectedItem());
		b.append("</li></ul>");
		return b.toString();
	}

}

