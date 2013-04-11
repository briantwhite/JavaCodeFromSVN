package edu.umb.jsVGL.client.ModelBuilder;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
		
		HorizontalPanel row1 = new HorizontalPanel();
		row1.add(t1Choices);
		Label l1 = new Label("is Pure Breeding");
		l1.setStyleName("jsVGL_InteractionText");
		row1.add(l1);

		HorizontalPanel row2 = new HorizontalPanel();
		row2.add(spacer);
		row2.add(combineArrow);
		Label l2 = new Label("combine to give");
		l2.setStyleName("jsVGL_InteractionText");
		row2.add(l2);
		row2.add(t4Choices);		

		HorizontalPanel row3 = new HorizontalPanel();
		row3.add(t2Choices);
		Label l3 = new Label("is Pure Breeding");
		l3.setStyleName("jsVGL_InteractionText");
		row3.add(l3);

		HorizontalPanel row4 = new HorizontalPanel();
		row4.add(spacer);
		row4.add(combineArrow);
		Label l4 = new Label("combine to give");
		l4.setStyleName("jsVGL_InteractionText");
		row4.add(l4);
		row4.add(t5Choices);		
		
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
