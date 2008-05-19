package GeneticModels;

import java.util.ArrayList;

public class GeneticModel {

	public static final boolean XX_XY = true;
	public static final boolean ZZ_ZW = false;

	private ChromosomeModel autosomeModel;
	private ChromosomeModel sexChromosomeModel;

	private boolean XX_XYsexLinkage; 


	public GeneticModel(boolean XX_XYsexLinkage) {
		this.XX_XYsexLinkage = XX_XYsexLinkage;
		autosomeModel = new ChromosomeModel(false);
		sexChromosomeModel = new ChromosomeModel(true);
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
