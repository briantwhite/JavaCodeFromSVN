package edu.umb.jsVGL.client.ModelBuilder;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ThreeHierPanel extends ModelDetailsPanel implements ChangeHandler {

	public ThreeHierPanel(String[] phenos,
			ListBox t1Choices,
			ListBox t2Choices,
			ListBox t3Choices,
			ModelPane mp) {

		t1Choices = new ListBox();
		t2Choices = new ListBox();
		t3Choices = new ListBox();
		for (int i = 0; i < phenos.length; i++) {
			t1Choices.addItem(phenos[i]);
			t2Choices.addItem(phenos[i]);
			t3Choices.addItem(phenos[i]);
		}
		this.t1Choices = t1Choices;
		t1Choices.addChangeHandler(this);
		this.t2Choices = t2Choices;
		t2Choices.addChangeHandler(this);
		this.t3Choices = t3Choices;
		t3Choices.addChangeHandler(this);
		this.mp = mp;

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(t3Choices);
		Label l1 = new Label("Is Dominant To");
		l1.setStyleName("jsVGL_InteractionText");
		mainPanel.add(l1);
		mainPanel.add(t2Choices);
		Label l2 = new Label("Is Dominant To");
		l2.setStyleName("jsVGL_InteractionText");
		mainPanel.add(l2);
		mainPanel.add(t1Choices);

		setWidget(mainPanel);
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

	public void onChange(ChangeEvent e) {
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

	public String getAsHtml() {
		StringBuffer b = new StringBuffer();
		b.append("<ul>");
		b.append("<li>" + t1Choices.getItemText(t1Choices.getSelectedIndex()) + " ");
		b.append("is recessive to all</li>");

		b.append("<li>" + t2Choices.getItemText(t2Choices.getSelectedIndex()) + " ");
		b.append("is dominant to ");
		b.append(t1Choices.getItemText(t1Choices.getSelectedIndex()) + "; ");
		b.append("is recessive to ");
		b.append(t3Choices.getItemText(t3Choices.getSelectedIndex()) + "</li>");

		b.append("<li>" + t3Choices.getItemText(t3Choices.getSelectedIndex()) + " ");
		b.append("is dominant to all</li>");
		b.append("</ul>");
		return b.toString();
	}

}
