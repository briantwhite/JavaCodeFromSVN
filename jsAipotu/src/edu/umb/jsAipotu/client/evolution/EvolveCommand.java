package edu.umb.jsAipotu.client.evolution;

import java.util.ArrayList;
import java.util.Random;

import com.google.gwt.core.client.Scheduler.RepeatingCommand;

import edu.umb.jsAipotu.client.biochem.FoldingException;
import edu.umb.jsAipotu.client.molGenExp.MolGenExp;
import edu.umb.jsAipotu.client.preferences.GlobalDefaults;
import edu.umb.jsAipotu.client.preferences.MGEPreferences;


// makes one new generation of organisms
//  must be constructed new for each generation
public class EvolveCommand implements RepeatingCommand {
	
	private World world;
	private ArrayList<String> genePool;
	int orgNum;
	int i;
	int j;
	private ThinOrganism[][] nextGen;
	
	private ThinOrganismFactory thinOrganismFactory;
	
	public EvolveCommand(World world, FitnessSettingsPanel fitnessSettingsPanel) {
		this.world = world;
		world.updateCounts();
		genePool = createGenePool(world, fitnessSettingsPanel);
		nextGen = new ThinOrganism[MGEPreferences.getInstance().getWorldSize()][MGEPreferences.getInstance().getWorldSize()];
		orgNum = 0;
		i = 0;
		j = 0;
		thinOrganismFactory = new ThinOrganismFactory();
	}

	// called over and over by browser; returns true if busy; false if done
	//  add one mutant org to next gen
	public boolean execute() {
		String DNA1 = getRandomAlleleFromPool();
		String DNA2 = getRandomAlleleFromPool();
		
		ThinOrganism o = null;
		try {
			o = thinOrganismFactory.createThinOrganism(DNA1, DNA2);
		} catch (FoldingException e) {
			// TO DO - deal with folded in a corner and make a useful thin org
		}
		
		nextGen[i][j] = o;
		
		j++;
		if (j > MGEPreferences.getInstance().getWorldSize()) {
			j = 0;
			i++;
			if (i > MGEPreferences.getInstance().getWorldSize()) {
				world.setOrganisms(nextGen);
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
