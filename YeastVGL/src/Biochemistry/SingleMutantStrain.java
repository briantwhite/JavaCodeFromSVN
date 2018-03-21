package Biochemistry;

import java.util.ArrayList;

public class SingleMutantStrain {
	
	private int index;
	private boolean[] genotype;
	private String complementationGroup;
	private int mutatedGeneIndex;
	
	public SingleMutantStrain(int index, int numEnzymes, int mutatedGeneIndex) {
		
		this.index = index;
		this.mutatedGeneIndex = mutatedGeneIndex;
		
		// starts off as wild-type
		genotype = new boolean[numEnzymes];
		for (int i = 0; i < numEnzymes; i++) {
			genotype[i] = true;
		}
		// mutate as needed
		genotype[mutatedGeneIndex] = false;
		complementationGroup = "";
	}
	
//	public SingleMutantStrain(int index, int numEnzymes, int mutatedGeneIndex, String complementationGroup) {
//		this.index = index;
//		this.mutatedGeneIndex = mutatedGeneIndex;
//		
//		// starts off as wild-type
//		genotype = new boolean[numEnzymes];
//		for (int i = 0; i < numEnzymes; i++) {
//			genotype[i] = true;
//		}
//		// mutate as needed
//		genotype[mutatedGeneIndex] = false;
//		this.complementationGroup = complementationGroup;
//	}
	
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
	
	public int getMutatedGeneIndex() {
		return mutatedGeneIndex;
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
		b.append(" mutatedGeneIndex:" + mutatedGeneIndex);
		b.append(" CG:");
		b.append(complementationGroup);
		return b.toString();
	}
}
