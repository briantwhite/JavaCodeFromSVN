package GeneticModels;

import java.util.ArrayList;

// This is the wrapper class for the entire genetic model
//  - it chooses from the characters and traits randomly to set up 
//  each gene model.
//  - it generates random organisms to populate the field cage
//  - it generates offspring from selected parents
//
//  It works like this:
//  - organisms contain 4 chromosomes
// 		- 2 copies of the autosome (maternal and paternal)
//		- 2 copies of the sex chromosome (maternal and paternal)
//  - each chromosome is an ArrayList of Alleles
//
//  - there is a ChromosomeModel for each type of chromosome (auto & sex)
//	- each ChromosomeModel consists of 0 or more GeneModels
//	- the GeneModels handle the genotype-phenotype conversion for one gene each
//
//  - there is a NullSexChromosome with no alleles (actually all Null alleles)
//		this is the Y or W chromosome
//
//  - to make a random organism
//		1) the GeneticModel asks each GeneModel for 2 random alleles
//			chosen to give roughly equal #s of each phenotype
//		2) the GeneticModel builds these into a chromosome pair
//			and turns this into an Organism
//
//  - cross 2 organisms
//		1) the genetic model takes care of recombination in each parent
//			using the CHromosomeModel to make 2 gametes
//		2) the gametes are combined in to an Organism and its phenotype
//			is determined
//
//  - phenotypes are determined :
//		1) the GeneticModel has each ChromosomeModel distribute its Alleles
//			to the GeneModels to get a Phenotype from each
//		2) these are pooled into a set of phenotypes for display, sorting, etc.
//
//  - An Allele consists of:
//		- an intVal = used as an integer index to the genotype-phenotype table
//			(0 = the null allele - from the Y or W chromosome)
//		- a Trait which contains:
//			bodyPart (eye, antenna, etc)
//			type (shape, colo, etc)
//			traitName (green, long, etc)
//
// to build a GeneticModel: 
//	1) Choose XX/XY or ZZ/ZW sex-linkage when you build one
//	2) add the gene models to the autosome or sex chromosome as needed
//		- the first one is added just as a model
//		- any more on the same chromo must be added with a recombination freq
//			unlinked genes have a 50% rf.
//			(therefore, autosomes are modeled as one big autosome where the
//			total rf can be bigger than 100%)

public class GeneticModel {

	public static final boolean XX_XY = true;
	public static final boolean ZZ_ZW = false;

	private ChromosomeModel autosomeModel;
	private ChromosomeModel sexChromosomeModel;

	private boolean XX_XYsexLinkage; 


	public GeneticModel(boolean XX_XYsexLinkage) {
		this.XX_XYsexLinkage = XX_XYsexLinkage;
		autosomeModel = new AutosomeModel();
		sexChromosomeModel = new SexChromosomeModel();
	}

	public void addFirstAutosomalGeneModel(GeneModel gm) throws GeneticsException {
		if (autosomeModel.getNumberOfGeneModels() != 0) {
			throw new GeneticsException("Can't add first autosomal model when" 
					+ " one is already present");
		}
		autosomeModel.addGeneModel(gm);
	}

	public void addFirstSexLinkedGeneModel(GeneModel gm) throws GeneticsException {
		if (sexChromosomeModel.getNumberOfGeneModels() != 0) {
			throw new GeneticsException("Can't add first sex-linked model when" 
					+ " one is already present");
		}
		sexChromosomeModel.addGeneModel(gm);
	}

	public void addNextAutosomalGeneModel(float rf, GeneModel gm) throws GeneticsException {
		if (autosomeModel.getNumberOfGeneModels() == 0) {
			throw new GeneticsException("Can't add next autosomal model to" 
					+ " empty list");
		}
		autosomeModel.addGeneModel(gm);
		autosomeModel.addRecombinationFrequency(rf);
	}

	public void addNextSexLinkedGeneModel(float rf, GeneModel gm) throws GeneticsException {
		if (sexChromosomeModel.getNumberOfGeneModels() == 0) {
			throw new GeneticsException("Can't add next sex-linked model to" 
					+ " empty list");
		}
		sexChromosomeModel.addGeneModel(gm);
		sexChromosomeModel.addRecombinationFrequency(rf);
	}

	public Organism getRandomOrganism() {
		Chromosome[] autosomes = 
			autosomeModel.getChromosomePairWithRandomAlleles();
		Chromosome[] sexChromosomes = 
			sexChromosomeModel.getChromosomePairWithRandomAlleles();
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
				isMale(sexChromosomes[0], sexChromosomes[1]),
				this);
	}

	public Organism getOffspringOrganism(Organism mom, Organism dad) {
		Chromosome maternalAutosomeContribution = null;
		Chromosome maternalSexChromosomeContribution = null;
		Chromosome paternalAutosomeContribution = null;
		Chromosome paternalSexChromosomeContribution = null;
		maternalAutosomeContribution = 
			autosomeModel.getGamete(
					mom.getMaternalAutosome(), mom.getPaternalAutosome());
		maternalSexChromosomeContribution = 
			sexChromosomeModel.getGamete(
					mom.getMaternalSexChromosome(), mom.getPaternalSexChromosome());
		paternalAutosomeContribution = 
			autosomeModel.getGamete(
					dad.getMaternalAutosome(), dad.getPaternalAutosome());
		paternalSexChromosomeContribution = 
			sexChromosomeModel.getGamete(
					dad.getMaternalSexChromosome(), dad.getPaternalSexChromosome());

		ArrayList<Phenotype> phenotypes = new ArrayList<Phenotype>();
		phenotypes.addAll(
				autosomeModel.getPhenotypes(
						maternalAutosomeContribution, 
						paternalAutosomeContribution));
		phenotypes.addAll(  
				sexChromosomeModel.getPhenotypes(
						maternalSexChromosomeContribution, 
						paternalSexChromosomeContribution));
		return new Organism(
				maternalAutosomeContribution,
				paternalAutosomeContribution,
				maternalSexChromosomeContribution,
				paternalSexChromosomeContribution,
				phenotypes,
				isMale(maternalSexChromosomeContribution, 
						paternalSexChromosomeContribution),
						this);
	}

	public boolean isMale(Chromosome sexChr1, Chromosome sexChr2) {		
		//see if one of the chromos is a NullSexChromosome (Y or W)
		boolean heterogametic = false;
		if((sexChr1 == NullSexChromosome.getInstance()) || 
				(sexChr2 == NullSexChromosome.getInstance())) {
			heterogametic = true;
		}	

		if (XX_XYsexLinkage) {
			if (heterogametic) {
				return true;
			} else {
				return false;
			}
		} else {
			if (heterogametic) {
				return false;
			} else {
				return true;
			}
		}
	}

	public boolean getSexLinkageType() {
		return XX_XYsexLinkage;
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("Genetic Model: ");
		if (XX_XYsexLinkage) {
			b.append("XX/XY sex linkage");
		} else {
			b.append("ZZ/ZW sex linkage");
		}
		b.append("\n");
		b.append(autosomeModel.toString());
		b.append(sexChromosomeModel.toString());
		b.append("$$$$$$$$\n");
		return b.toString();
	}

}
