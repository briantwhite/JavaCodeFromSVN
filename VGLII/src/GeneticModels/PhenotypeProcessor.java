package GeneticModels;

import java.util.ArrayList;
import java.util.Iterator;

/*
 * this class deals with phenotypic interactions like
 *   epistasis & complementation
 *   it processes info from GeneticModel for display
 *   - all the underlying stuff is the same
 *     this is just a wrapper
 */

public class PhenotypeProcessor {

	public static int NO_INTERACTION = 0;
	public static int COMPLEMENTATION = 1;
	public static int EPISTASIS = 2;

	private GeneticModel geneticModel;

	private int interactionType;

	/*
	 * need traits for the combined pheno if there's an interaction
	 *  if complementation:
	 *  	t1 --(gene1)--> t1 --(gene2)--> t2 (like albinism)
	 *  if epistasis
	 *  	t1 --(gene1)--> t2 --(gene2)--> t3 (like eye color)
	 */
	private TraitSet traitSet;
	private Trait t1; 
	private Trait t2;
	private Trait t3;

	public PhenotypeProcessor(GeneticModel geneticModel) {
		this.geneticModel = geneticModel;
		interactionType = NO_INTERACTION;
		traitSet = null;
	}

	// methods for setting up the processor
	public void setInteractionType(int i) {
		interactionType = i;
		// if interaction, will need trait set for combined pheno
		if (interactionType != NO_INTERACTION) {
			if (traitSet == null) {
				traitSet = CharacterSpecificationBank.getInstance().getRandomTraitSet();
				t1 = traitSet.getRandomTrait();
				t2 = traitSet.getRandomTrait();
				t3 = traitSet.getRandomTrait();
			}
		}
	}
	
	public int getInteractionType() {
		return interactionType;
	}

	// methods for processing phenotypes once the Processor is set up

	/*
	 * here, we know that gene1 and gene2 are involved in the interaction
	 *   so, replace their phenotype with the combined pheno
	 */
	public ArrayList<Phenotype> processPhenotypes(ArrayList<Phenotype> originalPhenotypes) {
		if (interactionType != NO_INTERACTION) {

			/*
			 * find, process, and pull out the phenos involved in the interaction
			 */
			ArrayList<Phenotype> newPhenotypes = new ArrayList<Phenotype>();
			if (interactionType == COMPLEMENTATION) {
				
			} else {
				
			}
			
			/*
			 * pass through whatever is left
			 */
			Iterator<Phenotype> pI = originalPhenotypes.iterator();
			while(pI.hasNext()) {
				newPhenotypes.add(pI.next());
			}
			return newPhenotypes;
			
		} else {
			return originalPhenotypes;
		}
	}
	
	/*
	 * look up in the gene model indicated by index
	 * to see if the submitted phenotype represents
	 * the dominant (the gene is functional in epi & comp) 
	 * or recessive (non-functional)
	 */
	private boolean isDominantPheno(int index, Phenotype pheno) {
		TwoAlleleSimpleDominanceGeneModel gm = 
			(TwoAlleleSimpleDominanceGeneModel) geneticModel.getGeneModelByIndex(index);
		return gm.getDominantTrait().toString().equals(pheno.getTrait().toString());
	}

	public int getProcessedNumberOfCharacters(
			ChromosomeModel autosomeModel, ChromosomeModel sexChromosomeModel) {

		int rawNumberOfChars = autosomeModel.getNumberOfGeneModels() 
		+ sexChromosomeModel.getNumberOfGeneModels();

		if (interactionType != NO_INTERACTION) {
			return rawNumberOfChars - 1;  // one fewer chars b/c 2 phenos interacting	
		} else {
			return rawNumberOfChars;
		}
	}

	public Organism getProcessedRandomOrganism(
			ChromosomeModel autosomeModel, 
			ChromosomeModel sexChromosomeModel,
			boolean trueBreeding) {

		if (interactionType != NO_INTERACTION) {
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

		if (interactionType != NO_INTERACTION) {
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
