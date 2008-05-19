package GeneticModels;

import java.util.ArrayList;

public class Organism {

	private Chromosome maternalAutosome;
	private Chromosome paternalAutosome;
	private Chromosome maternalSexChromosome;
	private Chromosome paternalSexChromosome;

	private ArrayList<Phenotype> phenotypes;

	private boolean male;

	private GeneticModel geneticModel;

	public Organism(Chromosome maternalAutosome,
			Chromosome paternalAutosome,
			Chromosome maternalSexChromosome,
			Chromosome paternalSexChromosome,
			ArrayList<Phenotype> phenotypes,
			boolean male,
			GeneticModel geneticModel) {
		this.maternalAutosome = maternalAutosome;
		this.paternalAutosome = paternalAutosome;
		this.maternalSexChromosome = maternalSexChromosome;
		this.paternalSexChromosome = paternalSexChromosome;
		this.phenotypes = phenotypes;
		this.male = male;
		this.geneticModel = geneticModel;
	}

	public Chromosome getMaternalAutosome() {
		return maternalAutosome;
	}

	public Chromosome getPaternalAutosome() {
		return paternalAutosome;
	}

	public Chromosome getMaternalSexChromosome() {
		return maternalSexChromosome;
	}

	public Chromosome getPaternalSexChromosome() {
		return paternalSexChromosome;
	}

	public ArrayList<Phenotype> getPhenotypes() {
		return phenotypes;
	}

	public boolean isMale() {
		return male;
	}

	public String getPhenotypeString() {
		StringBuffer b = new StringBuffer();
		for (Phenotype p: phenotypes) {
			b.append(p.getTrait().getTraitName());
			b.append("-");
			b.append(p.getTrait().getBodyPart());
			b.append("/");
		}
		b.deleteCharAt(b.length() - 1);
		return b.toString();
	}

	//shows genotype
	public String getToolTipTextString() {
		StringBuffer b = new StringBuffer();
		b.append("A:{");
		b.append(maternalAutosome.toString());
		b.append("}/{");
		b.append(paternalAutosome.toString());
		b.append("} ");
		if (geneticModel.getSexLinkageType() == GeneticModel.XX_XY) {
			b.append("X");
		} else {
			b.append("Z");
		}
		b.append(":[");
		if (maternalSexChromosome != NullSexChromosome.getInstance()) {
			b.append(maternalSexChromosome.toString());
			b.append("]/");
		} 
		if (paternalSexChromosome != NullSexChromosome.getInstance()) {	
			if (maternalSexChromosome != NullSexChromosome.getInstance()) {
				b.append("[");
			}
			b.append(paternalSexChromosome.toString());
			b.append("]/");
		}

		if ((maternalSexChromosome == NullSexChromosome.getInstance()) || 
				(paternalSexChromosome == NullSexChromosome.getInstance())) {
			if (geneticModel.getSexLinkageType() == GeneticModel.XX_XY) {
				b.append("Y");
			} else {
				b.append("W");
			}
		} else {
			b.deleteCharAt(b.length() - 1);
		}

		return b.toString();
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("Organism: [");
		if (!male) {
			b.append("fe");
		}
		b.append("male]\n");
		b.append("Genotype:\n");
		b.append("Maternal auto:\n");
		b.append(maternalAutosome.toString() + "\n");
		b.append("Paternal auto:\n");
		b.append(paternalAutosome.toString() + "\n");
		b.append("Maternal sex chr:\n");
		b.append(maternalSexChromosome.toString() + "\n");
		b.append("Paternal sex chr:\n");
		b.append(paternalSexChromosome.toString() + "\n");
		b.append("Phenotypes:\n");
		for (Phenotype p: phenotypes) {
			b.append(p.toString() + "\n");
		}
		b.append("pheno string=" + getPhenotypeString() + "\n");
		b.append("**organism**\n");
		return b.toString();
	}
}
