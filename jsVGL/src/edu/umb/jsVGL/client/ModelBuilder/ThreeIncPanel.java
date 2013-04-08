package edu.umb.jsVGL.client.ModelBuilder;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class ThreeIncPanel extends ModelDetailsPanel implements ChangeHandler {

	private Label t5;	// marker for arrow

	public ThreeIncPanel(String[] phenos,
			ListBox t1Choices,
			ListBox t2Choices,
			ListBox t3Choices,
			ListBox t4Choices,
			ListBox t5Choices,
			ListBox t6Choices,
			ModelPane mp) {

		t1Choices = new ListBox();
		t2Choices = new ListBox();
		t3Choices = new ListBox();
		t4Choices = new ListBox();
		t5Choices = new ListBox();
		t6Choices = new ListBox();
		for (int i = 0; i < phenos.length; i++) {
			t1Choices.addItem(phenos[i]);
			t2Choices.addItem(phenos[i]);
			t3Choices.addItem(phenos[i]);
			t4Choices.addItem(phenos[i]);
			t5Choices.addItem(phenos[i]);
			t6Choices.addItem(phenos[i]);
		}
		
		// first line
		add(t1Choices);

		Label t0 = new Label("Is Pure Breeding");
		add(t0);

		// second line
		Label t1 = new Label("Combine To Give");
		add(t1);

		add(t4Choices);

		// third line
		add(t2Choices);

		Label t2 = new Label("Is Pure Breeding");
		add(t2);

		// fourth line
		Label t3 = new Label("Combine To Give");
		add(t3);

		add(t5Choices);

		// fifth line
		add(t3Choices);

		Label t4 = new Label("Is Pure Breeding");
		add(t4);

		// sixth line
		t5 = new Label("Combine To Give");
		add(t5);

		add(t6Choices);

		this.t1Choices = t1Choices;
		t1Choices.addChangeHandler(this);
		this.t2Choices = t2Choices;
		t2Choices.addChangeHandler(this);
		this.t3Choices = t3Choices;
		t3Choices.addChangeHandler(this);
		this.t4Choices = t4Choices;
		t4Choices.addChangeHandler(this);
		this.t5Choices = t5Choices;
		t5Choices.addChangeHandler(this);
		this.t6Choices = t6Choices;
		t6Choices.addChangeHandler(this);

		this.mp = mp;

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

	public void updateT4Choices(int x) {
		t4Choices.setSelectedIndex(x);
	}

	public void updateT5Choices(int x) {
		t5Choices.setSelectedIndex(x);
	}

	public void updateT6Choices(int x) {
		t6Choices.setSelectedIndex(x);
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

		if (e.getSource().equals(t4Choices)) {
			mp.setT4Value(t4Choices.getSelectedIndex());
		}

		if (e.getSource().equals(t5Choices)) {
			mp.setT5Value(t5Choices.getSelectedIndex());
		}

		if (e.getSource().equals(t6Choices)) {
			mp.setT6Value(t6Choices.getSelectedIndex());
		}
	}

}
