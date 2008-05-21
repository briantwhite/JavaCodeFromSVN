package GeneticModels;

public class ThreeAlleleCircularDominanceGeneModel extends GeneModel {

	private Trait t1;  // totally recessive trait
	private Trait t2;  // intermediate trait
	private Trait t3;  // totally dominant trait

	public ThreeAlleleCircularDominanceGeneModel() {
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
			// phenotype 1
			// 3 possibilities 1,3 3,1 1,1
			switch (rand.nextInt(3)) {
			case 0:
				allelePair[0] = new Allele(t1, 1);
				allelePair[1] = new Allele(t3, 3);
				break;
			case 1:
				allelePair[0] = new Allele(t3, 3);
				allelePair[1] = new Allele(t1, 1);
				break;
			case 2:
				allelePair[0] = new Allele(t1, 1);
				allelePair[1] = new Allele(t1, 1);
				break;
			}
			break;

		case 1:
			// phenotype 2
			// 3 possibilities: 1,2  2,1 and 2,2
			switch (rand.nextInt(3)) {
			case 0:
				allelePair[0] = new Allele(t1, 1);
				allelePair[1] = new Allele(t2, 2);
				break;
			case 1:
				allelePair[0] = new Allele(t2, 2);
				allelePair[1] = new Allele(t1, 1);
				break;
			case 2:
				allelePair[0] = new Allele(t2, 2);
				allelePair[1] = new Allele(t2, 2);
				break;
			}
			break;
			
		case 2:
			// phenotype 3
			// 3 possibilities 2,3 3,2 3,3
			switch(rand.nextInt(3)) {
			case 0:
				allelePair[0] = new Allele(t2, 2);
				allelePair[1] = new Allele(t3, 3);
			case 1:
				allelePair[0] = new Allele(t3, 3);
				allelePair[1] = new Allele(t2, 2);
			case 2:
				allelePair[0] = new Allele(t3, 3);
				allelePair[1] = new Allele(t3, 3);
			}
		}
		return allelePair;
	}

	public void setupGenoPhenoTable() {
		genoPhenoTable = new Phenotype[4][4];

		//there are two alleles and two possible phenos
		// get the phenos first; then load table
		t1 = traitSet.getRandomTrait();   // dom to 3; rec to 2
		t2 = traitSet.getRandomTrait();   // dom to 1; rec to 3
		t3 = traitSet.getRandomTrait();   // dom to 2; rec to 1
		
		genoPhenoTable[0][0] = null;  				//impossible
		genoPhenoTable[0][1] = new Phenotype(t1);  	// 1,Y = 1
		genoPhenoTable[0][2] = new Phenotype(t2);   // 2,Y = 2
		genoPhenoTable[0][3] = new Phenotype(t3);   // 3,Y = 3	
		
		genoPhenoTable[1][0] = new Phenotype(t1);  	// 1,Y = 1
		genoPhenoTable[1][1] = new Phenotype(t1);  	// 1,1 = 1 
		genoPhenoTable[1][2] = new Phenotype(t2);   // 1,2 = 2 (1 is rec to 2)
		genoPhenoTable[1][3] = new Phenotype(t1);   // 1,3 = 1 (1 is dom to 3)
		
		genoPhenoTable[2][0] = new Phenotype(t2);  	// 2,Y = 2
		genoPhenoTable[2][1] = new Phenotype(t2);   // 2,1 = 2 (2 dom to 1)
		genoPhenoTable[2][2] = new Phenotype(t2);   // 2,2 = 2
		genoPhenoTable[2][3] = new Phenotype(t3);   // 2,3 = 3 (2 rec to 3)
		
		genoPhenoTable[3][0] = new Phenotype(t3);   // 3,Y = 3
		genoPhenoTable[3][1] = new Phenotype(t1);   // 3,1 = 1 (3 is rec to 1)
		genoPhenoTable[3][2] = new Phenotype(t3);   // 3,2 = 3 (3 is dom to 2)
		genoPhenoTable[3][3] = new Phenotype(t3);   // 3,3 = 3 
	}

	public String toString() {
		return "Three Allele " 
		+ "Circular Dominance; " 
		+ t1.toString()
		+ " is dominant to "
		+ t3.getTraitName()
		+ " and recessive to "
		+ t2.getTraitName()
		+ ", "
		+ t2.getTraitName()
		+ " is dominant to "
		+ t1.getTraitName()
		+ " and recessive to "
		+ t3.getTraitName()
		+ ", and "
		+ t3.getTraitName()
		+ " is dominant to "
		+ t2.getTraitName()
		+ " and recessive to "
		+ t1.getTraitName()
		+ ".";
	}

}
