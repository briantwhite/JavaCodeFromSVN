package Biochemistry;

import java.util.ArrayList;
import java.util.Random;

public class MutantSet {
	
	private SingleMutantStrain[] mutantStrains;
	
	public MutantSet(int numMutants, int numEnzymes) {
		Random r = new Random();
		// keep rolling the dice until you get at least one mutant in each gene in the set
		boolean allEnzymesHitAtLeastOnce = false;
		while (!allEnzymesHitAtLeastOnce) {
//			System.out.println("-----------making a mutant set------------");
			// make trial set of mutants and tally as you go
			int[] mutatedGeneTallies = new int[numEnzymes]; 
			for (int i = 0; i < numEnzymes; i++) {
				mutatedGeneTallies[i] = 0;
			}
			mutantStrains = new SingleMutantStrain[numMutants];
			for (int i = 0; i < numMutants; i++) {
				int mutantGeneNumber = r.nextInt(numEnzymes);
				mutantStrains[i] = new SingleMutantStrain(i, numEnzymes, mutantGeneNumber);
				// tally it
				mutatedGeneTallies[mutantGeneNumber]++;
			}
			// check them
			allEnzymesHitAtLeastOnce = true;
			for (int i = 0; i < numEnzymes; i++) {
//				System.out.println("gene:" + i + " tally:" + mutatedGeneTallies[i]);
				if (mutatedGeneTallies[i] == 0) {
					allEnzymesHitAtLeastOnce = false;
				}
			}
		}
		System.out.println(this.toString());
	}
	
	public SingleMutantStrain[] getMutantStrains() {
		return mutantStrains;
	}
	
	public int getNumberOfMutants() {
		return mutantStrains.length;
	}
	
	public String toString() {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < mutantStrains.length; i++) {
			b.append("M:" + i + "  ");
			b.append(mutantStrains[i].toString());
			b.append("\n");
		}
		return b.toString();
	}

}
