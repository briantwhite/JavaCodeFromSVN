package edu.umb.jsAipotu.molGenExp;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.CellList;

public class Greenhouse extends CellList<Organism> {

	MolGenExp mge;
	private List<Organism> organismList;
	private CellList<Organism> organismCells;
	
	public Greenhouse (final MolGenExp mgeX, OrganismCell organismCell) {
		super(organismCell);
		this.mge = mgeX;
		organismList = new ArrayList<Organism>();
		organismCells = new CellList<Organism>(organismCell);
	}

	public void add(Organism o) {
		organismList.add(o);
		organismCells.setRowData(0, organismList);
	}

//	public void deleteSelected() {
//		if (getSelectedIndex() != -1 ) {
//			greenhouseDataModel.removeElementAt(getSelectedIndex());
//		} else {
//			JOptionPane.showMessageDialog(null, "You have not selected an "
//					+ "item to delete.",
//					"None Selected", JOptionPane.WARNING_MESSAGE);
//		}
//	}

	public void clearList() {
		organismList = new ArrayList<Organism>();
		organismCells.setRowData(0, organismList);
	}

	public Object[] getAll() {
		return organismList.toArray();
	}

	public boolean nameExistsAlready(String newName) {
		Object[] allOrgs = organismList.toArray();
		ArrayList<String> allNames = new ArrayList<String>();
		for (int i = 0; i < allOrgs.length; i++) {
			allNames.add(((Organism)allOrgs[i]).getName());
		}
		return allNames.contains(newName);
	}

//	public Organism getSelectedOrganism() {
//		if (getSelectedIndex() < 0) {
//			return null;
//		}
//		return (Organism)greenhouseDataModel.getElementAt(
//				getSelectedIndex());
//	}
//	
//	public Organism[] getSelectedOrganisms() {
//		if (getSelectedIndex() < 0) {
//			return null;
//		}
//		Object[] values = getSelectedValues();
//		Organism[] orgs = new Organism[values.length];
//		for (int i = 0; i < orgs.length; i++) {
//			orgs[i] = (Organism)values[i];
//		}
//		return orgs;
//	}
//	
//	public void setDefaultSelectionSettings() {
//		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		setSelectionModel(new DefaultListSelectionModel());
//	}
//	
//	public void setCustomSelectionSettings() {
//		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//		setSelectionModel(new CustomListSelectionModel(mge, this));
//	}
//	
//	public void setEvolutionSelectionSettings() {
//		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//		setSelectionModel(new DefaultListSelectionModel());
//	}
//
}
