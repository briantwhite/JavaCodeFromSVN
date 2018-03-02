package YeastVGL;

import java.util.ArrayList;

public class MutantStrain {
	
	private int index;
	private boolean[] genotype;
	private String complementationGroup;
	
	public MutantStrain(int index, int numEnzymes, ArrayList<Integer>mutatedGenes) {
		
		this.index = index;
		
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
		complementationGroup = "";
	}
	
	public int getIndex() {
		return index;
	}
	
	public boolean[] getGenotype() {
		return genotype;
	}
	
	public String getComplementationGroup() {
		return complementationGroup;
	}
	
	public void setComplementationGroup(String cg) {
		complementationGroup = cg;
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < genotype.length; i++) {
			b.append("E" + i);
			if (genotype[i]) {
				b.append("+ ");
			} else {
				b.append("- ");
			}
		}
		b.append(" CG:");
		b.append(complementationGroup);
		return b.toString();
	}
}
