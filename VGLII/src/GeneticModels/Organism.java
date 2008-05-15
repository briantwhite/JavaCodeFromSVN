package GeneticModels;

import java.util.ArrayList;

public class Organism {
	
	private Chromosome maternalAutosome;
	private Chromosome paternalAutosome;
	private Chromosome maternalSexChromosome;
	private Chromosome paternalSexChromosome;
	
	private ArrayList<Phenotype> phenotypes;
	
	private boolean male;
	
	public Organism(Chromosome maternalAutosome,
			Chromosome paternalAutosome,
			Chromosome maternalSexChromosome,
			Chromosome paternalSexChromosome,
			ArrayList<Phenotype> phenotypes,
			boolean male) {
		this.maternalAutosome = maternalAutosome;
		this.paternalAutosome = paternalAutosome;
		this.maternalSexChromosome = maternalSexChromosome;
		this.paternalSexChromosome = paternalSexChromosome;
		this.phenotypes = phenotypes;
		this.male = male;
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
		b.append("**organism**\n");
		return b.toString();
	}
}
