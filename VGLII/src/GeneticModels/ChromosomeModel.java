package GeneticModels;

import java.util.ArrayList;
import java.util.Iterator;
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
	
	public int getNumberOfGeneModels() {
		return geneModels.size();
	}
	
	public ArrayList<Phenotype> getPhenotypes(Chromosome cr1, Chromosome cr2) 
	throws GeneticsException {
		if((cr1.getAllAlleles().size() != geneModels.size()) ||
				(cr2.getAllAlleles().size() != geneModels.size())) {
			throw new GeneticsException("Number of genes in chromosome does not" 
					+ " match number of genes in model");
		}
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
			output.add(Allele.makeNullVersion(input.get(i)));
		}
		return output;
	}

	//generate a random gamete from a pair of homologous chromosomes
	//  using recombination
	public Chromosome getGamete(Chromosome cr1, Chromosome cr2) throws GeneticsException {
		Random r = new Random();

		if ((cr1.getAllAlleles().size() != geneModels.size()) ||
				(cr2.getAllAlleles().size() != geneModels.size())) {
			throw new GeneticsException("Number of genes in chromosome does not" 
					+ " match number of genes in model");
		}

		//first, see if it's a heterogametic sex chromosome pair (XY or ZW)
		//  if so, then return without recombination
		// get first allele from each chromosome and see if its a null
		//  that would show it as the null sex chromosome
		if(sexChromosome && 
				((cr1.getAllele(0).getIntVal() == 0) ||
						(cr2.getAllele(0).getIntVal() == 0))) {
			if (r.nextInt(2) == 0) {
				return new Chromosome(cr1);
			} else {
				return new Chromosome(cr2);
			}
		}

		//so, its either an autosome or homogametic sex chromo pair
		//  so we need to do recombination
		//  recombinationFreqs(0) is the RF between gene 0 and 1, etc.
		ArrayList<Allele> oldAlleles1 = cr1.getAllAlleles();
		ArrayList<Allele> oldAlleles2 = cr2.getAllAlleles();
		ArrayList<Allele> newAlleles = new ArrayList<Allele>();

		float choiceBias = 0.5f; // equally likely to choose from
		//  either chromo on the first allele
		boolean pickedFrom1LastTime = false;

		Iterator<Float> rfIt = recombinationFrequencies.iterator();

		for (int i = 0; i < oldAlleles1.size(); i++) {
			if(r.nextFloat() >= choiceBias) {
				newAlleles.add(oldAlleles1.get(i));
				pickedFrom1LastTime = true;
			} else {
				newAlleles.add(oldAlleles2.get(i));
				pickedFrom1LastTime = false;
			}

			//if there's a recombination frequency, then need to
			//  update the 'rf' for the next round
			//  - if you picked from chromo1 last time, then
			//    the chance to get from chr1 again is rf
			if (rfIt.hasNext()) {
				float rf = rfIt.next();
				if (pickedFrom1LastTime) {
					choiceBias = rf;
				} else {
					choiceBias = 1.0f - rf;
				}
			}
		}
		return new Chromosome(newAlleles);
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		if(sexChromosome) {
			b.append("Sex Chromosome:\n");
		} else {
			b.append("Autosome:\n");
		}
		for (GeneModel gm: geneModels) {
			b.append(gm.toString() + "\n");
		}
		b.append("*******\n");
		return b.toString();
	}
}
