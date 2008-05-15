package VGL;

import GeneticModels.CharacterSpecificationBank;
import GeneticModels.GeneticModel;
import GeneticModels.GeneticsException;
import GeneticModels.Organism;
import GeneticModels.PlaceHolderGeneModel;
import GeneticModels.TwoAlleleSimpleDominanceGeneModel;

public class VGL {

	CharacterSpecificationBank charSpecBank;

	public VGL() {
		charSpecBank = CharacterSpecificationBank.getInstance();
	}

	public static void main(String[] args) {
		VGL vgl = new VGL();
		vgl.run();
	}

	private void run() {
		GeneticModel model = new GeneticModel(GeneticModel.XX_XY);
		try {
			model.addFirstAutosomalGeneModel(new TwoAlleleSimpleDominanceGeneModel());
			model.addFirstSexLinkedGeneModel(new TwoAlleleSimpleDominanceGeneModel());
		} catch (GeneticsException e) {
			e.printStackTrace();
		}
		System.out.println(model.toString());
		Organism dad = null;
		Organism mom = null;
		for (int i = 0; i < 10; i++ ) {
			Organism o = model.getRandomOrganism();
			if (o.isMale()) {
				dad = o;
			} else {
				mom = o;
			}
		}
		System.out.println("mom:\n" + mom.toString());
		System.out.println("dad:\n" + dad.toString());
		System.out.println("kids:\n");
		for (int i = 0; i < 10; i++) {
			System.out.println(model.getOffspringOrganism(mom, dad) + "\n");
		}
	}
}
