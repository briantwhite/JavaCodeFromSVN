package edu.umb.jsVGL.client.ModelBuilder;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;

public class UnknownSpecificsPanel extends ModelDetailsPanel {

	public UnknownSpecificsPanel() {
		add(new Label("Please choose the interaction type."));
	}

	public void itemStateChanged(Event e) {}

	@Override
	public void onChange(ChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
