package GeneticModels;

public class TwoAlleleIncompleteDominanceGeneModel extends GeneModel {

	private Trait t1;  // one homozygote trait
	private Trait t2;  // other homozygote trait
	private Trait t3;  // heterozygote trait

	public TwoAlleleIncompleteDominanceGeneModel() {
		super();
	}

	public Phenotype getPhenotype(Allele a1, Allele a2) {
		return genoPhenoTable[a1.getIntVal()][a2.getIntVal()];
	}

	public Allele[] getRandomAllelePair() {
		// want equal frequency of each PHENOTYPE
		Allele[] allelePair = new Allele[2];
		switch (rand.nextInt(3)) {

		case 0:
			// 1,1 homozygote
			allelePair[0] = new Allele(t1, 1);
			allelePair[1] = new Allele(t1, 1);
			break;

		case 1:
			// 1,2 heterozygote
			// 2 possibilities: 1,2 and 2,1
			if(rand.nextInt(2) == 0) {
				allelePair[0] = new Allele(t1, 1);
				allelePair[1] = new Allele(t2, 2);								
			} else {
				allelePair[0] = new Allele(t2, 2);
				allelePair[1] = new Allele(t1, 1);								
			}
			break;
			
		case 2:
			// 2,2 homozygote
			allelePair[0] = new Allele(t2, 2);
			allelePair[1] = new Allele(t2, 2);	
			
		}
		return allelePair;
	}

	public void setupGenoPhenoTable() {
		genoPhenoTable = new Phenotype[3][3];

		//there are two alleles and two possible phenos
		// get the phenos first; then load table
		t1 = traitSet.getRandomTrait();   // homozygote 1
		t2 = traitSet.getRandomTrait();   // homozygote 2
		t3 = traitSet.getRandomTrait();   // heterozygote
		
		genoPhenoTable[0][0] = null;  				//impossible
		genoPhenoTable[0][1] = new Phenotype(t1);  	// 1,Y = 1
		genoPhenoTable[0][2] = new Phenotype(t2);   // 2,Y = 2
		
		genoPhenoTable[1][0] = new Phenotype(t1);  	// 1,Y = 1
		genoPhenoTable[1][1] = new Phenotype(t1);  	// 1,1 = 1
		genoPhenoTable[1][2] = new Phenotype(t3);   // 1,2 = 3 (inc dom)
		
		genoPhenoTable[2][0] = new Phenotype(t2);  	// 2,Y
		genoPhenoTable[2][1] = new Phenotype(t3);   // 1,2 = 3 (inc dom)
		genoPhenoTable[2][2] = new Phenotype(t2);   // 2,2
	}

	public String toString() {
		return "Two Allele " 
		+ "Incomplete Dominance; " 
		+ t1.toString()
		+ " is one homozygote, "
		+ t2.getTraitName()
		+ " is the other homozygote,"
		+ t3.getTraitName()
		+ " is the heterozygote.";
	}

}
