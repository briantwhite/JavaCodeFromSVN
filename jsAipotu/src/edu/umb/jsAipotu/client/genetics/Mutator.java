package edu.umb.jsAipotu.client.genetics;

import com.google.gwt.user.client.Random;

import edu.umb.jsAipotu.client.biochem.FoldingException;
import edu.umb.jsAipotu.client.biochem.FoldingManager;
import edu.umb.jsAipotu.client.molBiol.ExpressedGene;
import edu.umb.jsAipotu.client.molBiol.GeneExpresser;
import edu.umb.jsAipotu.client.molGenExp.ExpressedAndFoldedGene;
import edu.umb.jsAipotu.client.preferences.MGEPreferences;

public class Mutator {

	private static Mutator instance;

	private MGEPreferences preferences;
	private GeneExpresser expresser;
	private FoldingManager foldingManager;

	private String[] baseArray = {"A", "G", "C", "T"};

	private Mutator() {
		preferences = MGEPreferences.getInstance();
		expresser = new GeneExpresser();
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


		//mutation: pointMutationRate chance of changing each base
		if (preferences.getPointMutationRate() != 0) {
			int pointOdds = Math.round(1/preferences.getPointMutationRate());
			for (int i = 0; i < DNABuffer.length(); i++) {
				if (Random.nextInt(pointOdds) == 0) {
					DNABuffer = DNABuffer.replace(i, i + 1, 
							baseArray[Random.nextInt(4)]);
				}
			}
		}

		//deletion mutations
		if (preferences.getDeletionMutationRate() != 0) {
			int delOdds = Math.round(1/preferences.getDeletionMutationRate());
			for (int i = 0; i < DNABuffer.length(); i++) {
				if (Random.nextInt(delOdds) == 0) {
					DNABuffer = DNABuffer.deleteCharAt(i);
				}
			}
		}

		//insertion mutations
		if (preferences.getInsertionMutationRate() != 0) {
			int insOdds = Math.round(1/preferences.getInsertionMutationRate());
			for (int i = 0; i < DNABuffer.length(); i++) {
				if (Random.nextInt(insOdds) == 0) {
					DNABuffer = DNABuffer.insert(i, baseArray[Random.nextInt(4)]);
				}
			}
		}


		return DNABuffer.toString();
	}


}
