package edu.umb.jsAipotu.client.molGenExp;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;

public class CustomListSelectionModel extends DefaultListSelectionModel {
//	private MolGenExp mge;
//	private JList parentList;
//	
//	public CustomListSelectionModel(MolGenExp mge, JList parentList) {
//		this.mge = mge;
//		this.parentList = parentList;
//	}
//
//	//regular methods called by the JList
//	public void setSelectionInterval(int index0, int index1) {
//		
//		if (isSelectedIndex(index0)) {
//			super.removeSelectionInterval(index0, index0);
//			mge.deselectOrganism(new OrganismAndLocation(
//					(Organism)parentList.getModel().getElementAt(index0),
//					this,
//					index0));
//		} else {
//			super.addSelectionInterval(index0, index0);
//			mge.addSelectedOrganism(new OrganismAndLocation(
//					(Organism)parentList.getModel().getElementAt(index0),
//					this,
//					index0));
//		}
//	}
//	
//	
//	//custom versions of the methods to be used by mge
//	public void setSelectionIntervalDirectly(OrganismAndLocation oal) {
//		super.setSelectionInterval(oal.getIndex(), oal.getIndex());
//	}
//	
//	public void removeSelectionIntervalDirectly(OrganismAndLocation oal) {
//		super.removeSelectionInterval(oal.getIndex(), oal.getIndex());
//	}
}
