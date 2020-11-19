package edu.umb.jsAipotu.genetics;

import java.util.Random;

import molBiol.ExpressedGene;
import molBiol.GeneExpresser;
import molGenExp.ExpressedAndFoldedGene;
import preferences.MGEPreferences;
import biochem.FoldingException;
import biochem.FoldingManager;

public class Mutator {

	private static Mutator instance;

	private MGEPreferences preferences;
	private GeneExpresser expresser;
	private FoldingManager foldingManager;

	private Random rand;

	private String[] baseArray = {"A", "G", "C", "T"};

	private Mutator() {
		preferences = MGEPreferences.getInstance();
		expresser = new GeneExpresser();
		rand = new Random();
		foldingManager = new FoldingManager();
	}

	public static Mutator getInstance() {
		if (instance == null) {
			instance = new Mutator();
		}
		return instance;
	}

	public ExpressedAndFoldedGene mutateGene(ExpressedAndFoldedGene efg) 
	throws FoldingException {
		//change one base in the DNA
		if (efg.getExpressedGene().getDNA().length() == 0) {
			return efg;
		}
		ExpressedGene newGene = expresser.expressGene(
				mutateDNASequence(efg.getExpressedGene().getDNA()), -1);

		//fold it & get color etc
		ExpressedAndFoldedGene newEfg = 
			new ExpressedAndFoldedGene(
					newGene, foldingManager.foldWithPix(newGene.getProtein()));

		return newEfg;
	}

	public String mutateDNASequence(String DNASequence) {
		preferences = MGEPreferences.getInstance();

		StringBuffer DNABuffer = new StringBuffer(DNASequence);

		if (preferences.isMutationsEnabled()) {

			//mutation: pointMutationRate chance of changing each base
			if (preferences.getPointMutationRate() != 0) {
				int pointOdds = Math.round(1/preferences.getPointMutationRate());
				for (int i = 0; i < DNABuffer.length(); i++) {
					if (rand.nextInt(pointOdds) == 0) {
						DNABuffer = DNABuffer.replace(i, i + 1, 
								baseArray[rand.nextInt(4)]);
					}
				}
			}

			//deletion mutations
			if (preferences.getDeletionMutationRate() != 0) {
				int delOdds = Math.round(1/preferences.getDeletionMutationRate());
				for (int i = 0; i < DNABuffer.length(); i++) {
					if (rand.nextInt(delOdds) == 0) {
						DNABuffer = DNABuffer.deleteCharAt(i);
					}
				}
			}

			//insertion mutations
			if (preferences.getInsertionMutationRate() != 0) {
				int insOdds = Math.round(1/preferences.getInsertionMutationRate());
				for (int i = 0; i < DNABuffer.length(); i++) {
					if (rand.nextInt(insOdds) == 0) {
						DNABuffer = DNABuffer.insert(i, baseArray[rand.nextInt(4)]);
					}
				}
			}
		}

		return DNABuffer.toString();
	}


}
