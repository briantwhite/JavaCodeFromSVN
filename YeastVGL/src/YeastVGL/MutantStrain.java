package YeastVGL;

import java.util.ArrayList;

public class MutantStrain {
	
	private boolean[] genotype;
	
	public MutantStrain(int numEnzymes, ArrayList<Integer>mutatedGenes) {
		// starts off as wild-type
		genotype = new boolean[numEnzymes];
		for (int i = 0; i < numEnzymes; i++) {
			genotype[i] = true;
		}
		// mutate as needed
		if (!mutatedGenes.isEmpty()) {
			for (int i = 0; i < mutatedGenes.size(); i++) {
				genotype[mutatedGenes.get(i).intValue()] = false;
			}
		}
	}
	
	public boolean[] getGenotype() {
		return genotype;
	}

}
