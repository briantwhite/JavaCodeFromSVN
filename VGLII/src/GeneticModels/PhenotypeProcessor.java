package GeneticModels;

import java.util.ArrayList;

/*
 * this class deals with phenotypic interactions like
 *   epistasis & complementation
 *   it processes info from GeneticModel for display
 *   - all the underlying stuff is the same
 *     this is just a wrapper
 */

public class PhenotypeProcessor {

	private GeneticModel geneticModel;

	/*
	 * flag for presence of epistasis or complementation
	 *  if false, everything runs normally
	 *  (all phenos pass through and no other processing needed)
	 */
	private boolean epistasisOrComplementation;

	public PhenotypeProcessor(GeneticModel geneticModel) {
		this.geneticModel = geneticModel;
		epistasisOrComplementation = false;
	}

	public ArrayList<Phenotype> processPhenotypes(ArrayList<Phenotype> originalPhenotypes) {
		return originalPhenotypes;
	}

	public int getProcessedNumberOfCharacters(
			ChromosomeModel autosomeModel, ChromosomeModel sexChromosomeModel) {

		if (epistasisOrComplementation) {
			return 0;	
		} else {
			return autosomeModel.getNumberOfGeneModels() 
			+ sexChromosomeModel.getNumberOfGeneModels();
		}
	}

	public Organism getProcessedRandomOrganism(
			ChromosomeModel autosomeModel, 
			ChromosomeModel sexChromosomeModel,
			boolean trueBreeding) {

		if (epistasisOrComplementation) {
			return null;	
		} else {
			Chromosome[] autosomes = 
				autosomeModel.getChromosomePairWithRandomAlleles(trueBreeding);
			Chromosome[] sexChromosomes = 
				sexChromosomeModel.getChromosomePairWithRandomAlleles(trueBreeding);
			ArrayList<Phenotype> phenotypes = new ArrayList<Phenotype>();
			phenotypes.addAll(
					autosomeModel.getPhenotypes(autosomes[0], autosomes[1]));
			phenotypes.addAll(  
					sexChromosomeModel.getPhenotypes(sexChromosomes[0], sexChromosomes[1]));
			return new Organism(
					autosomes[0],
					autosomes[1],
					sexChromosomes[0],
					sexChromosomes[1],
					phenotypes,
					geneticModel.isMale(sexChromosomes[0], sexChromosomes[1]),
					geneticModel);
		}
	}

	public String getProcessedToolTipTextString(
			Chromosome maternalAutosome,
			Chromosome paternalAutosome,
			Chromosome maternalSexChromosome,
			Chromosome paternalSexChromosome) {

		if (epistasisOrComplementation) {
			return null;	
		} else {
			StringBuffer b = new StringBuffer();
			if (maternalAutosome.getAllAlleles().size() > 0) {
				b.append("A:{");
				b.append(maternalAutosome.toTranslatedString());
				b.append("}/{");
				b.append(paternalAutosome.toTranslatedString());
				b.append("} ");
			}

			if (geneticModel.getSexLinkageType() == GeneticModel.XX_XY) {
				b.append("X");
			} else {
				b.append("Z");
			}

			// see if there's any alleles on the sex chromosomes
			if ((maternalSexChromosome.getAllAlleles().size() > 0) ||
					(paternalSexChromosome.getAllAlleles().size() > 0)){

				// if so, print them, unless they're a null
				b.append(":");

				if (maternalSexChromosome != NullSexChromosome.getInstance()) {
					b.append("[");
					b.append(maternalSexChromosome.toTranslatedString());
					b.append("]/");
				}

				if (paternalSexChromosome != NullSexChromosome.getInstance()) {	
					b.append("[");
					b.append(paternalSexChromosome.toTranslatedString());
					b.append("]/");
				} 

				// if heterogametic, need to finish with Y or W
				if ((maternalSexChromosome == NullSexChromosome.getInstance()) ||
						(paternalSexChromosome == NullSexChromosome.getInstance())){
					if (geneticModel.getSexLinkageType() == GeneticModel.XX_XY) {
						b.append("Y");
					} else {
						b.append("W");
					}
				} else {
					// trim trailing slash
					b.deleteCharAt(b.length() - 1);
				}

			} else {

				// no alleles on sex chromosomes, so just show XX, XY, ZZ, or ZW as appropriate
				//  see if homo or heterogametic
				if ((maternalSexChromosome == NullSexChromosome.getInstance()) ||
						(paternalSexChromosome == NullSexChromosome.getInstance())) {

					//heterogametic
					if (geneticModel.getSexLinkageType() == GeneticModel.XX_XY) {
						b.append("Y");
					} else {
						b.append("W");
					}
				} else {

					//homogametic
					if (geneticModel.getSexLinkageType() == GeneticModel.XX_XY) {
						b.append("X");
					} else {
						b.append("Z");
					}
				}
			}
			return b.toString();
		}

	}

}
