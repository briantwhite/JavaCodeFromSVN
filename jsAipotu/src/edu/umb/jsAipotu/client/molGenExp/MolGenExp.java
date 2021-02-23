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
		//  oui2 = most recently selected organism
		//  oui1 = least recently selected organism
		//  new orgs added to oui2 and push down to oui1 and then dropped
		
		// first, see if this was a selection or a de-selection event
		if (oui.isSelected()) {
			if ((oui1 == null) & (oui2 == null)) {
				// none selected yet, so put in oui2
				oui2 = oui;
				
			} else if ((oui1 == null) & (oui2 != null)) {
				// only one previously selected, move old to 1 and put new in 2
				oui1 = oui2;
				oui2 = oui;
			} else if ((oui1 != null) & (oui2 != null)) {
				// two were previously selected, un-select old 1, move 2 to 1 and put new in 2
				oui1.setSelected(false);
				oui1 = oui2;
				oui2 = oui;
			} 
		} else {
			// a de-selection event
			if (oui.equals(oui1)) {
				// it's ok to have oui1 empty
				oui1 = null;
			} else if (oui.equals(oui2)) {
				// if you un-select oui2, need to move oui1 up
				oui2 = oui1;
				oui1 = null;
			}
		}
		updateGeneticsButtonStatus();
		return;
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

	public void clearSelectedOrganisms() {
		greenhouse.clearAllSelections();
		clearSelectedOrganismsInGeneticsWorkbench();
	}



	public void clearSelectedOrganismsInGeneticsWorkbench() {
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

	public OrganismUI getOUI2() {
		return oui2;
	}

	public void loadOrganismIntoActivePanel(Organism o) {

	}

	public void saveOrganismToGreenhouse(Organism o) {

	}

	public void updateGeneticsButtonStatus() {
		if ((oui1 == null) & (oui2 == null)) {
			jsA.getGeneticsWorkbench().setCrossTwoButtonsEnabled(false);
			jsA.getGeneticsWorkbench().setSelfCrossButtonsEnabled(false);
			jsA.getGeneticsWorkbench().setMutateButtonsEnabled(false);
		} else if ((oui1 == null) & (oui2 != null)) {
			jsA.getGeneticsWorkbench().setCrossTwoButtonsEnabled(false);
			jsA.getGeneticsWorkbench().setSelfCrossButtonsEnabled(true);
			jsA.getGeneticsWorkbench().setMutateButtonsEnabled(true);			
		} else if ((oui1 != null) & (oui2 != null)) {
			jsA.getGeneticsWorkbench().setCrossTwoButtonsEnabled(true);
			jsA.getGeneticsWorkbench().setSelfCrossButtonsEnabled(false);
			jsA.getGeneticsWorkbench().setMutateButtonsEnabled(false);			
		}
	}

	public void setAddToGreenhouseButtonEnabled(boolean enabled) {

	}
}
