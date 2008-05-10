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
}
