package GeneticModels;

import java.util.ArrayList;
import java.util.Random;

public class ChromosomeModel {
	
	private ArrayList<GeneModel> geneModels;
	private ArrayList<Float> recombinationFrequencies;
	
	private boolean sexChromosome;
	
	public ChromosomeModel(boolean sexChromosome) {
		this.sexChromosome = sexChromosome;
		geneModels = new ArrayList<GeneModel>();
		recombinationFrequencies = new ArrayList<Float>();
	}
	
	public void addGeneModel(GeneModel gm) {
		geneModels.add(gm);
	}
	
	public void addRecombinationFrequency(float rf) {
		recombinationFrequencies.add(rf);
	}
	
	public ArrayList<Phenotype> getPhenotypes(Chromosome cr1, Chromosome cr2) {
		ArrayList<Phenotype> phenos = new ArrayList<Phenotype>();
		for (int i = 0; i < geneModels.size(); i++) {
			phenos.add( 
					(geneModels.get(i)).getPhenotype(
							cr1.getAllele(i), cr2.getAllele(i)));
		}
		return phenos;
	}
	
	public Chromosome[] getChromosomePairWithRandomAlleles() {
		ArrayList<Allele> maternalAlleles = new ArrayList<Allele>();
		ArrayList<Allele> paternalAlleles = new ArrayList<Allele>();
		Chromosome[] chromos = new Chromosome[2];
		for (int i = 0; i < geneModels.size(); i++) {
			Allele[] allelePair = (geneModels.get(i)).getRandomAllelePair();
			maternalAlleles.add(allelePair[0]);
			paternalAlleles.add(allelePair[1]);
		}
		//if sex-chromo, one chromo needs to be all null alleles
		// half of the time
		if (sexChromosome) {
			Random r = new Random();
			//a male - so make one chromo nulls
			if (r.nextInt(2) == 0) {
				//choose which one
				if (r.nextInt(2) == 0) {
					maternalAlleles = makeAllNulls(maternalAlleles);
				} else {
					paternalAlleles = makeAllNulls(paternalAlleles);
				}
			}
		}
		chromos[0] = new Chromosome(maternalAlleles);
		chromos[1] = new Chromosome(paternalAlleles);
		return chromos;
	}
	
	private ArrayList<Allele> makeAllNulls(ArrayList<Allele> input) {
		ArrayList<Allele> output = new ArrayList<Allele>();
		for (int i = 0; i < input.size(); i++) {
			output.add(new Allele(
					Trait.getNullVersion((input.get(i)).getTrait()), 0));
		}
		return output;
	}
	
	public Chromosome getGamete(Organism o) {
		return null;
	}

}
