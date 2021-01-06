package edu.umb.jsAipotu.client.molGenExp;

import com.google.gwt.user.client.ui.TabLayoutPanel;

public class MolGenExp {
	
	//indices for tabbed panes
	public final static int GENETICS = 0;
	public final static int BIOCHEMISTRY = 1;
	public final static int MOLECULAR_BIOLOGY = 2;
	public final static int EVOLUTION = 3;

	private Greenhouse greenhouse;
	private GreenhouseLoader greenhouseLoader;
	
	//for genetics only; the two selected organisms
//	private OrganismAndLocation oal1;
//	private OrganismAndLocation oal2;

	public MolGenExp() {
		greenhouse = new Greenhouse(new GreenhouseCell());
		greenhouseLoader = new GreenhouseLoader(greenhouse);
		greenhouseLoader.load();
		
		// the two selected organisms in genetics
//		oal1 = null;
//		oal2 = null;
	}
		
	public Greenhouse getGreenhouse() {
		return greenhouse;
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
//	public void clearSelectedOrganisms() {
//		if (oal1 != null) {
////			oal1.getListLocation().removeSelectionIntervalDirectly(oal1);
//		}
//
//		if (oal2 != null) {
////			oal2.getListLocation().removeSelectionIntervalDirectly(oal2);
//		}
//
//		oal1 = null;
//		oal2 = null;
//		updateGeneticsButtonStatus();
//	}
//
//	public Organism getOrg1() {
//		return oal1.getOrganism();
//	}
//
//	public Organism getOrg2() {
//		return oal2.getOrganism();
//	}
	
	public void saveOrganismToGreenhouse(Organism o) {
		
	}
	
	public void updateGeneticsButtonStatus() {
		
	}
	
	public void setAddToGreenhouseButtonEnabled(boolean enabled) {
		
	}
}
