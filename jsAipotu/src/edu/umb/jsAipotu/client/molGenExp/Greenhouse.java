package edu.umb.jsAipotu.client.molGenExp;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Greenhouse extends ScrollPanel {

	private ArrayList<OrganismUI> organismUIs;
	private MolGenExp mge;
	private VerticalPanel innerPanel;
	
	public Greenhouse(MolGenExp mge) {
		super();
		this.mge = mge;
		organismUIs = new ArrayList<OrganismUI>();
		innerPanel = new VerticalPanel();
		this.add(innerPanel);
	}

	// method used by GreenhouseLoader - it assumes the organism has a name already
	public void add(Organism org) {
		organismUIs.add(new OrganismUI(org, -1, mge));  // location -1 means 'in the greenhouse'
		updateDisplay();
	}
	
	private void updateDisplay() {
		innerPanel.clear();
		Iterator<OrganismUI> ouIt = organismUIs.iterator();
		while (ouIt.hasNext()) {
			innerPanel.add(ouIt.next());
		}
	}
}
