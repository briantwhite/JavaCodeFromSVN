package GeneticModels;

import org.jdom.Element;

public class TraitFactory {
	
	/**
	 * place to put all Traits for access by Organisms
	 * indexed by 	[chromosome: 0 = auto, 1 = sex]
	 * 				[gene# on chromosome]
	 * 				[trait# for that gene on that chromo]
	 * ** must be initialized based on the current model
	 */
	Trait[][][] traitBank;  
	
	private static TraitFactory instance;
	
	private TraitFactory() {
		
	}
	
	public static TraitFactory getInstance() {
		if (instance == null) {
			instance = new TraitFactory();
		}
		return instance;
	}
	
	public void initializeTraitBank(
			int numChromos, 
			int numGenes, 
			int maxNumTraitsPerGene) {
		
		traitBank = new Trait[numChromos][numGenes][maxNumTraitsPerGene + 1];
		
		for (int i = 0; i < numChromos; i++) {
			for (int j = 0; j < numGenes; j++) {
				for (int k = 0; k < maxNumTraitsPerGene + 1; k++) {
					traitBank[i][j][k] = null;
				}
			}
		}
	}
	
	public Trait buildTrait(Element e, int chromo, int gene, int traitNum) {
		String traitName = e.getAttributeValue("TraitName");
		String type = e.getAttributeValue("Type");
		String bodyPart = e.getAttributeValue("BodyPart");
		Trait t = null;
		if (type.equals("Color")) {
			t = new ColorTrait(traitName, bodyPart);
		} else if (type.equals("Number")) {
			t = new NumberTrait(traitName, bodyPart);
		} else if (type.equals("Shape")) {
			t = new ShapeTrait(traitName, bodyPart);
		} 
		traitBank[chromo][gene][traitNum] = t;
		return t;
	}
	
	public Trait getTrait(int chromoNum, int geneNum, int traitNum) {
		return traitBank[chromoNum][geneNum][traitNum];
	}

}
