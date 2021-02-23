package edu.umb.jsAipotu.client.molGenExp;

import edu.umb.jsAipotu.client.JsAipotu;

public class MolGenExp {
	
	private JsAipotu jsA;

	//indices for tabbed panes
	public final static int GENETICS = 0;
	public final static int BIOCHEMISTRY = 1;
	public final static int MOLECULAR_BIOLOGY = 2;
	public final static int EVOLUTION = 3;

	private Greenhouse greenhouse;
	private GreenhouseLoader greenhouseLoader;

	//for genetics only; the two selected organisms
	private OrganismUI oui1;
	private OrganismUI oui2;

	public MolGenExp(JsAipotu jsA) {
		this.jsA = jsA;
		greenhouse = new Greenhouse(this);
		greenhouseLoader = new GreenhouseLoader(greenhouse);
		greenhouseLoader.load();

		// the two selected organisms in genetics
		oui1 = null;
		oui2 = null;
	}

	public Greenhouse getGreenhouse() {
		return greenhouse;
	}

	// deal with organism selections in greenhouse (location -1) or tray (location = tray #: 0 or higher)
	public void organismWasClicked(OrganismUI oui) {
		if (jsA.getSelectedTabIndex() == GENETICS) {
			processSelectionInGenetics(oui);
		} else {
			processSelectionInMoboOrBiochem(oui);
		}

	}
	
	private void processSelectionInGenetics(OrganismUI oui) {
		
	}
	
	private void processSelectionInMoboOrBiochem(OrganismUI oui) {
		greenhouse.selectOnlyThisOrganism(oui);
		if (jsA.getSelectedTabIndex() == MOLECULAR_BIOLOGY) {
			jsA.getMolBiolWorkbench().loadOrganism(oui.getOrganism());
		}
		if (jsA.getSelectedTabIndex() == BIOCHEMISTRY) {
			jsA.getBiochemWorkbench().loadOrganism(oui.getOrganism());
		}
	}
	
	public void clearGreenhouseSelections() {
		greenhouse.clearAllSelections();
	}
	//handlers for selections of creatures in Genetics mode
	//  max of two at a time.
	//these are called by the CustomListSelectionMode
	//	public void deselectOrganism(OrganismAndLocation oal, TabLayoutPanel explorerPane) {
	//
	//		// only do this in genetics
	//		if (explorerPane.getSelectedIndex() != GENETICS) {
	//			return;
	//		}
	//
	//		//remove from list of selected organisms
	//		//if #1 is being deleted, delete it and move #2 up
	//		if ((oal.getOrganism()).equals(oal1.getOrganism())) {
	//			oal1 = oal2;
	//			oal2 = null;
	//			updateGeneticsButtonStatus();
	//			return;
	//		}
	//
	//		//otherwise just delete #2
	//		if ((oal.getOrganism()).equals(oal2.getOrganism())) {
	//			oal2 = null;
	//			updateGeneticsButtonStatus();
	//			return;
	//		}
	//
	//		//should not get to here
	//		updateGeneticsButtonStatus();
	//		return;
	//	}
	//
	//	public void addSelectedOrganism(OrganismAndLocation oal, TabLayoutPanel explorerPane) {
	//
	//		// only do this in genetics
	//		if (explorerPane.getSelectedIndex() != GENETICS) {
	//			return;
	//		}
	//
	//		//if none selected so far, put it in #1
	//		if ((oal1 == null) && (oal2 == null)) {
	//			oal1 = oal;
	//			updateGeneticsButtonStatus();
	//			return;
	//		}
	//
	//		// if only one selected so far, it should be in #1
	//		// so move #1 to #2 and put this in #1
	//		if ((oal1 != null) && (oal2 == null)) {
	//			oal2 = oal1;
	//			oal1 = oal;
	//			updateGeneticsButtonStatus();
	//			return;
	//		}
	//
	//		//it must be that there are 2 selected orgs
	//		// so you have to drop #2, move 1 to 2 and put the
	//		// new one in 1.
	//		if ((oal1 != null) && (oal2 != null)) {
	//			//drop #2
	////			oal2.getListLocation().removeSelectionIntervalDirectly(oal2);
	//			//move 1 to 2
	//			oal2 = oal1;
	//			//put new one in 1
	//			oal1 = oal;
	//			updateGeneticsButtonStatus();
	//		}
	//
	//		//should not get to here
	//		return;
	//	}
	//
	public void clearSelectedOrganismsInGenetics() {
		if (oui1 != null) {
			oui1.setSelected(false);
		}

		if (oui2 != null) {
			oui2.setSelected(false);
		}

		oui1 = null;
		oui2 = null;
		updateGeneticsButtonStatus();
	}

	public OrganismUI getOUI1() {
		return oui1;
	}

	public OrganismUI getOrg2() {
		return oui2;
	}

	public void loadOrganismIntoActivePanel(Organism o) {

	}

	public void saveOrganismToGreenhouse(Organism o) {

	}

	public void updateGeneticsButtonStatus() {

	}

	public void setAddToGreenhouseButtonEnabled(boolean enabled) {

	}
}
