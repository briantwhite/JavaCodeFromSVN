package VGL;

import GeneticModels.CharacterSpecificationBank;
import GeneticModels.Trait;
import GeneticModels.TraitSet;

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
		for (int j = 0; j < 100; j++) {
			TraitSet ts = charSpecBank.getRandomTraitSet();
			if (ts != null) {
				for (int i = 0; i < 10; i++) {
					Trait t = ts.getRandomTrait();
					if (t != null) {
						System.out.println(t.toString());
					}
				}
			}
		}
	}
}
