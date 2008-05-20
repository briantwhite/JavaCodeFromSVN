package VGL;

import GeneticModels.Cage;
import GeneticModels.CharacterSpecificationBank;
import GeneticModels.GeneticModel;
import GeneticModels.GeneticsException;
import GeneticModels.Organism;
import GeneticModels.TwoAlleleSimpleDominanceGeneModel;

public class VGL {

	CharacterSpecificationBank charSpecBank;
	GeneticModel geneticModel;

	public VGL() {
		charSpecBank = CharacterSpecificationBank.getInstance();
	}

	public static void main(String[] args) {
		VGL vgl = new VGL();
		vgl.run();
	}
	
	public GeneticModel getGeneticModel() {
		return geneticModel;
	}

	private void run() {
		geneticModel = new GeneticModel(GeneticModel.XX_XY);
		try {
			geneticModel.addFirstAutosomalGeneModel(new TwoAlleleSimpleDominanceGeneModel());
			geneticModel.addNextAutosomalGeneModel(0.1f, new TwoAlleleSimpleDominanceGeneModel());
			geneticModel.addNextAutosomalGeneModel(0.2f, new TwoAlleleSimpleDominanceGeneModel());

//			geneticModel.addFirstSexLinkedGeneModel(new TwoAlleleSimpleDominanceGeneModel());
//			geneticModel.addNextSexLinkedGeneModel(0.2f, new TwoAlleleSimpleDominanceGeneModel());
		} catch (GeneticsException e) {
			e.printStackTrace();
		}
		System.out.println(geneticModel.toString());
		Organism dad = null;
		Organism mom = null;
		for (int i = 0; i < 10; i++ ) {
			Organism o = geneticModel.getRandomOrganism();
			if (o.isMale()) {
				dad = o;
			} else {
				mom = o;
			}
		}
		System.out.println("mom:\n" + mom.toString());
		System.out.println("dad:\n" + dad.toString());
		System.out.println("kids:\n");
		Cage cage = new Cage();
		for (int i = 0; i < 1000; i++) {
			Organism o = geneticModel.getOffspringOrganism(mom, dad);
//			System.out.println(o.getToolTipTextString());
			cage.add(o);
		}
		System.out.println(cage);
	}
}
