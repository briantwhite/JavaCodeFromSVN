package GeneticModels;


//to be used on a sex chromosome model when there are no
//  other sex-linked geneModels 
//  in order to keep the sex determination model working
public class PlaceHolderGeneModel extends GeneModel {
	
	Trait placeHolderTrait;
	Allele placeHolderAllele;

	public Phenotype getPhenotype(Allele a1, Allele a2) {
		return new Phenotype(placeHolderTrait);
	}

	public Allele[] getRandomAllelePair() {
		Allele[] alleles = new Allele[2];
		alleles[0] = placeHolderAllele;
		alleles[1] = placeHolderAllele;
		return alleles;
	}

	public void setupGenoPhenoTable() {
		placeHolderTrait = new PlaceHolderTrait();
		placeHolderAllele = new Allele(placeHolderTrait, 0);
	}

	public String toString() {
		return "";
	}

}
