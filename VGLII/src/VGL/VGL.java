package VGL;

import GeneticModels.Allele;
import GeneticModels.CharacterSpecificationBank;
import GeneticModels.GeneModel;
import GeneticModels.Trait;
import GeneticModels.TraitSet;
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
		GeneModel model = new TwoAlleleSimpleDominanceGeneModel(false);
		System.out.println(model.toString());
		for (int j = 0; j < 20; j++) {
			Allele[] alleles = model.getRandomAllelePair();
			System.out.println("-----");
			System.out.println("a1: " + alleles[0].toString());
			System.out.println("a2: " + alleles[1].toString());
			System.out.println("ph: " + (model.getPhenotype(alleles[0], alleles[1])).toString());
			System.out.println(".......");
		}
	}
}
