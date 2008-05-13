package GeneticModels;

public class GeneticModel {

	public static final int MALE = 0;
	public static final int FEMALE = 1;

	private ChromosomeModel autosomeModel;
	private ChromosomeModel sexChromosomeModel;

	private boolean XX_XYsexLinkage; 


	public GeneticModel(boolean XX_XYsexLinkage) {
		this.XX_XYsexLinkage = XX_XYsexLinkage;
		autosomeModel = new ChromosomeModel(false);
		sexChromosomeModel = new ChromosomeModel(true);
	}

	public Organism getRandomOrganism() {
		return null;
	}

	public Organism getOffspringOrganism(Organism mom, Organism dad) {
		return null;
	}

	public int getSex(Organism o) {		
		//see if one of the chromos has any null alleles (Y or W)
		Allele a1 = o.getMaternalSexChromosome().getAllele(0);
		Allele a2 = o.getPaternalSexChromosome().getAllele(0);
		boolean heterogametic = false;
		if((a1.getIntVal() == 0) || (a2.getIntVal() == 0)) {
			heterogametic = true;
		}	

		if (XX_XYsexLinkage) {
			if (heterogametic) {
				return MALE;
			} else {
				return FEMALE;
			}
		} else {
			if (heterogametic) {
				return FEMALE;
			} else {
				return MALE;
			}
		}
	}

}
