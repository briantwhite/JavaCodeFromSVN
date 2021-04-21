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
import edu.umb.jsAipotu.client.preferences.GlobalDefaults;
import edu.umb.jsAipotu.client.preferences.MGEPreferences;

public class World extends CaptionPanel {
	
	private MolGenExp mge;

	private MGEPreferences preferences;

	private ThinOrganismFactory thinOrganismFactory;
	private OrganismFactory organismFactory;

	private ThinOrganism[][] organisms;
	private ColorCountsRecorder colorCountsRecorder;
	
	private Grid organismGrid;

	private int selectedCelli = -1;
	private int selectedCellj = -1;

	public World(MolGenExp mge) {
		super("World");
		setStyleName("world");
		this.mge = mge;
		preferences = MGEPreferences.getInstance();
		thinOrganismFactory = new ThinOrganismFactory(mge);
		organismFactory = new OrganismFactory();
		colorCountsRecorder = ColorCountsRecorder.getInstance();
		organismGrid = new Grid(preferences.getWorldSize(), preferences.getWorldSize());
		setContentWidget(organismGrid);
		initialize();
	}

	public void initialize(ArrayList<OrganismUI> orgs) {
		Random r = new Random();
		for (int i = 0; i < preferences.getWorldSize(); i++) {
			for (int j = 0; j < preferences.getWorldSize(); j++) {
				ThinOrganism to = thinOrganismFactory.createThinOrganism(orgs.get(r.nextInt(orgs.size())).getOrganism());
				organisms[i][j] = to;
				organismGrid.setWidget(i, j, to);
			}
		}
	}
	
	// fills with gray thin orgs for initial condition
	public void initialize() {
		for (int i = 0; i < preferences.getWorldSize(); i++) {
			for (int j = 0; j < preferences.getWorldSize(); j++) {
				organismGrid.setWidget(i, j, new ThinOrganism());
			}
		}
	}

	public void updateCounts() {
		//first, be sure that there are organisms in the world
		if (getThinOrganism(0,0) != null) {
			colorCountsRecorder.setAllToZero();
			for (int i = 0; i < preferences.getWorldSize(); i++) {
				for (int j = 0; j < preferences.getWorldSize(); j++) {
					colorCountsRecorder.incrementCount(
							getThinOrganism(i, j).getOverallColor());
				}
			}
		}
	}

	public ThinOrganism getThinOrganism(int i, int j) {
		return organisms[i][j];
	}

	public void setOrganisms(ThinOrganism[][] newOrgs) {
		organisms = null;
		for (int i = 0; i < preferences.getWorldSize(); i++) {
			for (int j = 0; j < preferences.getWorldSize(); j++) {
				organisms[i][j] = newOrgs[i][j];
				organismGrid.add(newOrgs[i][j]);
			}
		}
	}

	public Organism getSelectedOrganism() throws FoldingException {
		if ((selectedCelli < 0) && (selectedCellj < 0)) {
			return null;
		}
		ThinOrganism to = organisms[selectedCelli][selectedCellj];
		if (to.getOverallColor().equals(GlobalDefaults.DEAD_COLOR)) {
//			JOptionPane.showMessageDialog(null, 
//					"Unable to load that organism because it is not viable.\n"
//					+ "It is inviable because one of its proteins cannot be\n"
//					+ "Folded properly. Please choose another organism.", 
//					"Error Folding Protein", 
//					JOptionPane.WARNING_MESSAGE);
		}
		return organismFactory.createOrganism(to);
	}

	public void clearSelectedOrganism() {
		selectedCelli = -1;
		selectedCellj = -1;
	}

}
