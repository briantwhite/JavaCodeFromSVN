package edu.umb.jsAipotu.client.evolution;

import java.util.ArrayList;
import java.util.Random;

import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Grid;

import edu.umb.jsAipotu.client.biochem.FoldingException;
import edu.umb.jsAipotu.client.molGenExp.MolGenExp;
import edu.umb.jsAipotu.client.molGenExp.Organism;
import edu.umb.jsAipotu.client.molGenExp.OrganismFactory;
import edu.umb.jsAipotu.client.molGenExp.OrganismUI;
import edu.umb.jsAipotu.client.preferences.MGEPreferences;

public class World extends CaptionPanel {
	
	private MolGenExp mge;

	private MGEPreferences preferences;

	private ThinOrganismFactory thinOrganismFactory;
	private OrganismFactory organismFactory;

	private ColorCountsRecorder colorCountsRecorder;
	
	private Grid thinOrganismGrid;

	public World(MolGenExp mge) {
		super("World");
		setStyleName("world");
		this.mge = mge;
		preferences = MGEPreferences.getInstance();
		thinOrganismFactory = new ThinOrganismFactory();
		organismFactory = new OrganismFactory();
		colorCountsRecorder = ColorCountsRecorder.getInstance();
		thinOrganismGrid = new Grid(preferences.getWorldSize(), preferences.getWorldSize());
		setContentWidget(thinOrganismGrid);
		initialize();
	}

	public void initialize(ArrayList<OrganismUI> orgs) {
		Random r = new Random();
//		thinOrganismGrid.clear();
		for (int i = 0; i < preferences.getWorldSize(); i++) {
			for (int j = 0; j < preferences.getWorldSize(); j++) {
				ThinOrganism to = thinOrganismFactory.createThinOrganism(orgs.get(r.nextInt(orgs.size())).getOrganism());
				ThinOrganismUI toUI = new ThinOrganismUI(to, mge);
				thinOrganismGrid.setWidget(i, j, toUI);
			}
		}
	}
	
	// fills with gray thin orgs for initial condition
	public void initialize() {
		for (int i = 0; i < preferences.getWorldSize(); i++) {
			for (int j = 0; j < preferences.getWorldSize(); j++) {
				ThinOrganism to = new ThinOrganism();
				ThinOrganismUI toUI = new ThinOrganismUI(to, mge);
				thinOrganismGrid.setWidget(i, j, toUI);
			}
		}
	}

	public void updateCounts() {
		colorCountsRecorder.setAllToZero();
		//first, be sure that there are organisms in the world
		if (getThinOrganism(0,0) != null) {
			for (int i = 0; i < preferences.getWorldSize(); i++) {
				for (int j = 0; j < preferences.getWorldSize(); j++) {
					colorCountsRecorder.incrementCount(
							getThinOrganism(i, j).getOverallColor());
				}
			}
		} 
	}

	public ThinOrganism getThinOrganism(int i, int j) {
		return ((ThinOrganismUI)thinOrganismGrid.getWidget(i, j)).getThinOrganism();
	}
	
	public void setOrganisms(ThinOrganism[][] newOrgs) {
//		thinOrganismGrid.clear();
		for (int i = 0; i < preferences.getWorldSize(); i++) {
			for (int j = 0; j < preferences.getWorldSize(); j++) {
				thinOrganismGrid.setWidget(i, j, new ThinOrganismUI(newOrgs[i][j], mge));
			}
		}
	}

	public Organism getSelectedOrganism() throws FoldingException {
		for (int i = 0; i < preferences.getWorldSize(); i++) {
			for (int j = 0; j < preferences.getWorldSize(); j++) {
				if (((ThinOrganismUI)thinOrganismGrid.getWidget(i, j)).isSelected()) {
					return organismFactory.createOrganism(((ThinOrganismUI)thinOrganismGrid.getWidget(i, j)).getThinOrganism());
				}
			}
		}
		return null;

//		if (to.getOverallColor().equals(GlobalDefaults.DEAD_COLOR)) {
//			JOptionPane.showMessageDialog(null, 
//					"Unable to load that organism because it is not viable.\n"
//					+ "It is inviable because one of its proteins cannot be\n"
//					+ "Folded properly. Please choose another organism.", 
//					"Error Folding Protein", 
//					JOptionPane.WARNING_MESSAGE);
//		}
		
	}

	public void clearAllSelectedOrganisms() {
		for (int i = 0; i < preferences.getWorldSize(); i++) {
			for (int j = 0; j < preferences.getWorldSize(); j++) {
				((ThinOrganismUI)thinOrganismGrid.getWidget(i, j)).setSelected(false);
			}
		}
	}

	public MolGenExp getMolGenExp() {
		return mge;
	}
}
