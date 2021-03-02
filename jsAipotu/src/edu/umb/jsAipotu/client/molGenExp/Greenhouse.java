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
	
	public ArrayList<Organism> getAllOrganisms() {
		ArrayList<Organism> orgs = new ArrayList<Organism>();
		Iterator<OrganismUI> ouiIt = organismUIs.iterator();
		while (ouiIt.hasNext()) {
			orgs.add(ouiIt.next().getOrganism());
		}
		return orgs;
	}

	// method used by GreenhouseLoader - it assumes the organism has a name already
	public void add(Organism org) {
		organismUIs.add(new OrganismUI(org, mge)); 
		updateDisplay();
	}

	private void updateDisplay() {
		innerPanel.clear();
		Iterator<OrganismUI> ouIt = organismUIs.iterator();
		while (ouIt.hasNext()) {
			innerPanel.add(ouIt.next());
		}
	}

	public void selectOnlyThisOrganism(OrganismUI oui) {
		Iterator<OrganismUI> ouIt = organismUIs.iterator();
		while (ouIt.hasNext()) {
			OrganismUI x = ouIt.next();
			if (x != oui) {
				x.setSelected(false);
			}
		}
	}

	public void clearAllSelections() {
		Iterator<OrganismUI> ouIt = organismUIs.iterator();
		while (ouIt.hasNext()) {
			ouIt.next().setSelected(false);
		}
	}
	
	public boolean isInGreenhouse(Organism o) {
		Iterator<OrganismUI> ouiIt = organismUIs.iterator();
		while (ouiIt.hasNext()) {
			if (ouiIt.next().getOrganism().equals(o)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean nameExistsAlready(String newName) {
		Iterator<OrganismUI> ouiIt = organismUIs.iterator();
		while (ouiIt.hasNext()) {
			if (ouiIt.next().getOrganism().getName().equals(newName)) {
				return true;
			}
		}
		return false;
	}

}
