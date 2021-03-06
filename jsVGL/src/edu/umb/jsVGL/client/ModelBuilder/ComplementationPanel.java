package edu.umb.jsVGL.client.ModelBuilder;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ListBox;

public class ComplementationPanel extends ModelDetailsPanel implements ChangeHandler {

	ListBox intermediateChoices;  // for middle choice; linked to 1st one

	public ComplementationPanel(String[] allPhenos,
			ListBox t1Choices,
			ListBox t2Choices,
			ModelPane mp) {
		
		this.mp = mp;

		t1Choices = new ListBox();
		t2Choices = new ListBox();
		intermediateChoices = new ListBox();

		AbsolutePanel mainPanel = new AbsolutePanel();
		mainPanel.setStyleName("jsVGL_ComplementationPanel");
		mainPanel.setSize("216px", "216px");

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

		mainPanel.add(t1Choices, 25, 5);
		mainPanel.add(intermediateChoices, 25, 90);
		mainPanel.add(t2Choices, 25, 170);
		setWidget(mainPanel);
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
				
	public String getAsHtml() {
		StringBuffer b = new StringBuffer();
		b.append("<ul>");
		b.append("<li>");
		b.append(t1Choices.getItemText(t1Choices.getSelectedIndex()));
		b.append(" ---(Gene A)--->");
		b.append(intermediateChoices.getItemText(intermediateChoices.getSelectedIndex()));
		b.append(" ---(Gene B)--->");
		b.append(t2Choices.getItemText(t2Choices.getSelectedIndex()));
		b.append("</li>");
		b.append("</ul>");
		return b.toString();
	}

}
