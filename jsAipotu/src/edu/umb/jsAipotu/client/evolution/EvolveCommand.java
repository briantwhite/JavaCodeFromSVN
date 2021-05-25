package edu.umb.jsAipotu.client.evolution;

import java.util.ArrayList;
import java.util.Random;

import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

import edu.umb.jsAipotu.client.biochem.FoldingException;
import edu.umb.jsAipotu.client.preferences.GlobalDefaults;
import edu.umb.jsAipotu.client.preferences.MGEPreferences;


// makes one new generation of organisms
//  must be constructed new for each generation
public class EvolveCommand implements RepeatingCommand {
	
	private EvolutionWorkArea ewa;
	private ArrayList<String> genePool;
	int orgNum;
	int i;
	int j;
	private ThinOrganism[][] nextGen;
	
	private ThinOrganismFactory thinOrganismFactory;
	
	PopupPanel busyPanel;
	HTML busyPanelLabel;
	
	public EvolveCommand() {
		thinOrganismFactory = new ThinOrganismFactory();
//		busyPanel = new PopupPanel();
//		busyPanel.setStyleName("busyPopup");
//		busyPanel.setPopupPosition(300, 300);
//		busyPanelLabel = new HTML("<center><h1>Getting ready to make mutations.<br> Please be patient.</h1></center>");
//		busyPanel.setWidget(busyPanelLabel);
//		busyPanel.show();
	}
	
	public void prepareToEvolve(EvolutionWorkArea ewa) {
		ewa.getWorld().updateCounts();
		genePool = createGenePool(ewa.getWorld(), ewa.getFitnessSettingsPanel());
		nextGen = new ThinOrganism[MGEPreferences.getInstance().getWorldSize()][MGEPreferences.getInstance().getWorldSize()];
		orgNum = 1;
		i = 0;
		j = 0;
	}

	// called over and over by browser; returns true if busy; false if done
	//  add one mutant org to next gen
	public boolean execute() {
		
		String DNA1 = getRandomAlleleFromPool();
		String DNA2 = getRandomAlleleFromPool();
				
//		if (ewa.mutationsEnabled()) {
//			busyPanelLabel.setHTML("<center><h1>Mutating " + String.valueOf(orgNum) + "/100 organisms.<br> Please be patient.</h1></center>");
//			DNA1 = Mutator.getInstance().mutateDNASequence(getRandomAlleleFromPool());
//			DNA2 = Mutator.getInstance().mutateDNASequence(getRandomAlleleFromPool());
//		} else {
		//	busyPanel.hide();
//			DNA1 = getRandomAlleleFromPool();
//			DNA2 = getRandomAlleleFromPool();
//		}
		
		ThinOrganism o = null;
		try {
			o = thinOrganismFactory.createThinOrganism(DNA1, DNA2);
		} catch (FoldingException e) {
			// TO DO - deal with folded in a corner and make a useful thin org
		}
		
		nextGen[i][j] = o;

		orgNum++;
		
		j++;
		if (j >= MGEPreferences.getInstance().getWorldSize()) {
			j = 0;
			i++;
			if (i >= MGEPreferences.getInstance().getWorldSize()) {
				ewa.getWorld().setOrganisms(nextGen);
				ewa.updateCountsAndDisplays();
//				busyPanel.hide();
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
	
	private String getRandomAlleleFromPool() {
		Random r = new Random();
		int x = r.nextInt(genePool.size());
		return genePool.get(x);
	}

	
	// each organism contributes absolute fitness (integer) random DNA sequences to pool
	private ArrayList<String> createGenePool(World world, FitnessSettingsPanel fitnessSettingsPanel) {
		int[] fitnesses = fitnessSettingsPanel.getRelativeFitnesses();
		ArrayList<String> genePool = new ArrayList<String>();
		for (int i = 0; i < MGEPreferences.getInstance().getWorldSize(); i++) {
			for (int j = 0; j < MGEPreferences.getInstance().getWorldSize(); j++) {
				ThinOrganism to = world.getThinOrganism(i, j);
				int colorNum = GlobalDefaults.colorModel.getColorNumber(to.getOverallColor());
				// color number -1 is non-foldable protein = dead organism (fitness = 0)
				if (colorNum != -1) {
					int fitness = fitnesses[colorNum];
					for (int x = 0; x < fitness; x++) {
						genePool.add(to.getRandomDNASequence());
					}
				}
			}
		}
		return genePool;
	}
}
