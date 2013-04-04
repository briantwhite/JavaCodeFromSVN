package edu.umb.jsVGL.client.ModelBuilder;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class ComplementationPanel extends ModelDetailsPanel implements ChangeHandler {

	ListBox intermediateChoices;  // for middle choice; linked to 1st one
	Label gALabel;
	Label gBLabel;

	public ComplementationPanel(String[] allPhenos,
			ListBox t1Choices,
			ListBox t2Choices,
			ModelPane mp) {
		
		this.mp = mp;

		t1Choices = new ListBox();
		t2Choices = new ListBox();
		intermediateChoices = new ListBox();

		// don't use last pheno if complementation
		for (int i = 0; i < 3; i++) {
			t1Choices.addItem(allPhenos[i]);
			t2Choices.addItem(allPhenos[i]);
			intermediateChoices.addItem(allPhenos[i]);
		}
		t1Choices.addChangeHandler(this);
		this.t1Choices = t1Choices;
		t1Choices.addChangeHandler(this);
		this.t2Choices = t2Choices;
		t2Choices.addChangeHandler(this);
		intermediateChoices.addChangeHandler(this);

		add(t1Choices);
		gALabel = new Label("Gene A");
		add(gALabel);
		add(intermediateChoices);
		gBLabel = new Label("Gene B");
		add(gBLabel);
		add(t2Choices);
	}


	public void updateT1Choices(int x) {
		t1Choices.setSelectedIndex(x);
	}
	
	public void updateT2Choices(int x) {
		t2Choices.setSelectedIndex(x);
	}

	// make the first 2 choices track each other and report changes to UI
	public void onChange(ChangeEvent e) {
		if (e.getSource().equals(t1Choices)) {
			intermediateChoices.setSelectedIndex(t1Choices.getSelectedIndex());
			mp.setT1Value(t1Choices.getSelectedIndex());
		}

		if (e.getSource().equals(intermediateChoices)) {
			t1Choices.setSelectedIndex(intermediateChoices.getSelectedIndex());
		}
		
		if (e.getSource().equals(t2Choices)) {
			mp.setT2Value(t2Choices.getSelectedIndex());
		}
	}
		
//	public void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		Graphics2D g2d = (Graphics2D)g;
//		g2d.setColor(Color.GRAY);
//		g2d.setStroke(new BasicStroke(5F));
//		g2d.drawLine(gALabel.getLocation().x, gALabel.getLocation().y + 18, 
//				gALabel.getLocation().x + 40, gALabel.getLocation().y + 18);
//		g2d.drawLine(gALabel.getLocation().x + 35, gALabel.getLocation().y + 13, 
//				gALabel.getLocation().x + 40, gALabel.getLocation().y + 18);
//		g2d.drawLine(gALabel.getLocation().x + 35, gALabel.getLocation().y + 23, 
//				gALabel.getLocation().x + 40, gALabel.getLocation().y + 18);
//		g2d.drawLine(gBLabel.getLocation().x, gBLabel.getLocation().y + 18, 
//				gBLabel.getLocation().x + 40, gBLabel.getLocation().y + 18);
//		g2d.drawLine(gBLabel.getLocation().x + 35, gBLabel.getLocation().y + 13, 
//				gBLabel.getLocation().x + 40, gBLabel.getLocation().y + 18);
//		g2d.drawLine(gBLabel.getLocation().x + 35, gBLabel.getLocation().y + 23, 
//				gBLabel.getLocation().x + 40, gBLabel.getLocation().y + 18);
//	}
}
