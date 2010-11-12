package GeneticModels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

import VGL.Messages;

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

	private Random r;

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
		r = new Random();
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
			 * you have to do it this way because the phenos may not be in gene
			 * order in the ArrayList
			 * 
			 * first, get the names of the dominant and recessive forms of each gene
			 *  involved in the interaction
			 */
			String g1DomPheno = ((TwoAlleleSimpleDominanceGeneModel)geneticModel.getGeneModelByIndex(0))
			.getDominantTrait().toString();
			String g1RecPheno = ((TwoAlleleSimpleDominanceGeneModel)geneticModel.getGeneModelByIndex(0))
			.getRecessiveTrait().toString();
			String g2DomPheno = ((TwoAlleleSimpleDominanceGeneModel)geneticModel.getGeneModelByIndex(1))
			.getDominantTrait().toString();
			String g2RecPheno = ((TwoAlleleSimpleDominanceGeneModel)geneticModel.getGeneModelByIndex(1))
			.getRecessiveTrait().toString();
			// then, see what forms we have
			boolean g1Dom = false;
			boolean g2Dom = false;
			TreeSet<Integer> indicesToDelete = new TreeSet<Integer>();
			for (int i = 0; i < originalPhenotypes.size(); i++) {
				String pheno = originalPhenotypes.get(i).getTrait().toString();

				if (pheno.equals(g1DomPheno)) {
					g1Dom = true;
					indicesToDelete.add(new Integer(i));
				}

				if (pheno.equals(g1RecPheno)) {
					indicesToDelete.add(new Integer(i));
				}

				if (pheno.equals(g2DomPheno)) {
					g2Dom = true;
					indicesToDelete.add(new Integer(i));
				}

				if (pheno.equals(g2RecPheno)) {
					indicesToDelete.add(new Integer(i));
				}				
			}

			// add the combined phenotype to the revised list
			ArrayList<Phenotype> newPhenotypes = new ArrayList<Phenotype>();
			if (interactionType == COMPLEMENTATION) {
				if ((!g1Dom) || (!g2Dom)) {
					newPhenotypes.add(new Phenotype(t1));
				} else {
					newPhenotypes.add(new Phenotype(t2));
				}
			} else {
				if (!g1Dom) {
					newPhenotypes.add(new Phenotype(t1));
				} else if (!g2Dom) {
					newPhenotypes.add(new Phenotype(t2));
				} else {
					newPhenotypes.add(new Phenotype(t3));
				}
			}

			/*
			 * pass through whatever is left
			 * don't include the ones we flagged earlier
			 */
			for (int i = 0; i < originalPhenotypes.size(); i++) {
				if (!indicesToDelete.contains(new Integer(i))) {
					newPhenotypes.add(originalPhenotypes.get(i));
				}
			}
			return newPhenotypes;

		} else {
			return originalPhenotypes;
		}
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

		Chromosome[] autosomes = new Chromosome[2];
		Chromosome[] sexChromosomes = new Chromosome[2];

		if (interactionType != NO_INTERACTION) {
			// flags for configuring genes 1 and 2
			boolean g1Dom = false;
			boolean g2Dom = false;
			if (interactionType == COMPLEMENTATION) {
				// only two phenotypes in complementation
				if (r.nextInt(2) == 0) {
					// t1 - 3 ways to do this
					switch (r.nextInt(3)) {
					case 2:
						g1Dom = true;
						g2Dom = false;
						break;
					case 1:
						g1Dom = false;
						g2Dom = true;
						break;
					case 3:
						g1Dom = false;
						g2Dom = false;
						break;
					}
				} else {
					// t2 - only one way
					g1Dom = true;
					g2Dom = true;
				}

			} else {
				// three phenos in epistasis
				switch (r.nextInt(3)) {
				case 2: 	// t3 only one way
					g1Dom = true;
					g2Dom = true;
					break;
				case 1:		// t2 only one way
					g1Dom = true;
					g2Dom = false;
					break;
				case 0:		// t1 - two ways
					if (r.nextInt(2) == 0) {
						g1Dom = false;
						g2Dom = true;
						break;
					} else {
						g1Dom = false;
						g2Dom = false;
						break;
					}
				}
			}

			// go thru the genes and deal with them as needed
			autosomes = getProcessedChromsome(autosomeModel, g1Dom, g2Dom, trueBreeding);
			sexChromosomes = getProcessedChromsome(sexChromosomeModel, g1Dom, g2Dom, trueBreeding);
			//for sex-chromo, one chromo needs to be all null alleles (Y or W)
			// half of the time 
			if (r.nextInt(2) == 0) {
				//choose which one
				if (r.nextInt(2) == 0) {
					sexChromosomes[0] = NullSexChromosome.getInstance();
				} else {
					sexChromosomes[1] = NullSexChromosome.getInstance();;
				}
			}

		} else {
			autosomes = autosomeModel.getChromosomePairWithRandomAlleles(trueBreeding);
			sexChromosomes = sexChromosomeModel.getChromosomePairWithRandomAlleles(trueBreeding);
		}

		// build an organism
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
				processPhenotypes(phenotypes),
				geneticModel.isMale(sexChromosomes[0], sexChromosomes[1]),
				geneticModel);

	}

	private Chromosome[] getProcessedChromsome(
			ChromosomeModel chromosomeModel,
			boolean g1Dom,
			boolean g2Dom,
			boolean trueBreeding) {

		Chromosome[] chromos = new Chromosome[2];

		Iterator<GeneModel> gmIt = chromosomeModel.getGeneModels().iterator();
		ArrayList<Allele> maternalAlleles = new ArrayList<Allele>();
		ArrayList<Allele> paternalAlleles = new ArrayList<Allele>();
		while (gmIt.hasNext()) {
			Allele[] alleles = new Allele[2];
			GeneModel gm = gmIt.next();
			if (gm.getIndex() == 0) {
				alleles = getProcessedAllelePair(gm, g1Dom, trueBreeding);
			} else if (gm.getIndex() == 1) {
				alleles = getProcessedAllelePair(gm, g2Dom, trueBreeding);
			} else {
				// it's another gene; process it normally
				alleles = gm.getRandomAllelePair(trueBreeding);
			}
			// add alleles to chromosome
			maternalAlleles.add(alleles[0]);
			paternalAlleles.add(alleles[1]);
		}
		chromos[0] = new Chromosome(maternalAlleles);
		chromos[1] = new Chromosome(paternalAlleles);
		return chromos;
	}

	private Allele[] getProcessedAllelePair(GeneModel gm, boolean domPheno, boolean trueBreeding) {
		Allele[] alleles = new Allele[2];
		TwoAlleleSimpleDominanceGeneModel tasdGm = (TwoAlleleSimpleDominanceGeneModel)gm;
		if (domPheno) {
			// two ways AA and Aa
			if (r.nextBoolean() || trueBreeding) {
				alleles[0] = new Allele(tasdGm.getDominantTrait(), 2);
				alleles[1] = new Allele(tasdGm.getDominantTrait(), 2);
			} else {
				alleles[0] = new Allele(tasdGm.getDominantTrait(), 2);
				alleles[1] = new Allele(tasdGm.getRecessiveTrait(), 1);							
			}
		} else {
			// one way aa
			alleles[0] = new Allele(tasdGm.getRecessiveTrait(), 1);
			alleles[1] = new Allele(tasdGm.getRecessiveTrait(), 1);							
		}
		return alleles;
	}

	public String getProcessedToolTipTextString(
			Chromosome maternalAutosome,
			Chromosome paternalAutosome,
			Chromosome maternalSexChromosome,
			Chromosome paternalSexChromosome) {

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

		if (interactionType != NO_INTERACTION) {
			String g1DomPheno = Messages.getInstance().getTranslatedAlleleName(
					new Allele(
							((TwoAlleleSimpleDominanceGeneModel)geneticModel.getGeneModelByIndex(0))
							.getDominantTrait(), 1));
			String g1RecPheno = Messages.getInstance().getTranslatedAlleleName(
					new Allele(
							((TwoAlleleSimpleDominanceGeneModel)geneticModel.getGeneModelByIndex(0))
							.getRecessiveTrait(), 1));
			String g2DomPheno = Messages.getInstance().getTranslatedAlleleName(
					new Allele(
							((TwoAlleleSimpleDominanceGeneModel)geneticModel.getGeneModelByIndex(1))
							.getDominantTrait(), 1));
			String g2RecPheno = Messages.getInstance().getTranslatedAlleleName(
					new Allele(
							((TwoAlleleSimpleDominanceGeneModel)geneticModel.getGeneModelByIndex(1))
							.getRecessiveTrait(), 1));

			String s = b.toString();
			s = s.replaceAll(g1DomPheno, "A");
			s = s.replaceAll(g1RecPheno, "a");
			s = s.replaceAll(g2DomPheno, "B");
			s = s.replaceAll(g2RecPheno, "b");

			return s;

		} else {

			return b.toString();
		}
	}

}
