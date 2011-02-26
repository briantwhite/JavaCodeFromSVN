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

public class ThreeCircPanel extends ModelDetailsPanel implements ItemListener {
	
	JLabel l;

	public ThreeCircPanel(String[] phenos,
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
		g2d.drawArc(70, 15, 80, l.getLocation().y, -90, 180);
		
		g2d.drawLine(95,15,105,15);
		g2d.drawLine(105,10,95,15);
		g2d.drawLine(105,20,95,15);
		
		g2d.drawLine(95,l.getLocation().y + 15,105,l.getLocation().y + 15);
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

	public String getAsHtml() {
		StringBuffer b = new StringBuffer();
		b.append("<ul>");
		
		b.append("<li>" + (String)t1Choices.getSelectedItem() + " ");
		b.append(Messages.getInstance().getString("VGLII.IsDominantTo") + " ");
		b.append((String)t3Choices.getSelectedItem() + "; ");
		b.append(Messages.getInstance().getString("VGLII.IsRecessiveTo") + " ");
		b.append((String)t2Choices.getSelectedItem() + "</li>");
		
		b.append("<li>" + (String)t2Choices.getSelectedItem() + " ");
		b.append(Messages.getInstance().getString("VGLII.IsDominantTo") + " ");
		b.append((String)t1Choices.getSelectedItem() + "; ");
		b.append(Messages.getInstance().getString("VGLII.IsRecessiveTo") + " ");
		b.append((String)t3Choices.getSelectedItem() + "</li>");
		
		b.append("<li>" + (String)t3Choices.getSelectedItem() + " ");
		b.append(Messages.getInstance().getString("VGLII.IsDominantTo") + " ");
		b.append((String)t2Choices.getSelectedItem() + "; ");
		b.append(Messages.getInstance().getString("VGLII.IsRecessiveTo") + " ");
		b.append((String)t1Choices.getSelectedItem() + "</li>");
		
		b.append("</ul>");
		return b.toString();
	}

}
