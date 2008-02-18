package evolution;

import genetics.MutantGenerator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import molGenExp.MolGenExp;

public class Evolver implements Runnable {
	
	private MolGenExp mge;
	private EvolutionWorkArea evolutionWorkArea;
	private World world;
	private ArrayList genePool;
	private int current;
	
	public Evolver(final MolGenExp mge) {
		this.mge = mge;
		this.evolutionWorkArea = mge.getEvolutionWorkArea();
		this.world = mge.getEvolutionWorkArea().getWorld();
	}

	public void run() {
		createGenePool();
		makeNextGeneration();
		mge.notifyDone();
	}

	private void createGenePool() {
		// find the gene pool
		// get the fitness settings
		int[] fitnessSettings = evolutionWorkArea.getFitnessValues();
		
		System.out.println("creating gene pool");
		
		// each organism in the world contributes fitness # of alleles to pool		
		genePool = new ArrayList();
		for (int i = 0; i < MolGenExp.worldSize; i++) {
			for (int j = 0; j < MolGenExp.worldSize; j++) {
				ThinOrganism org = world.getThinOrganism(i, j);
				int colorNumber = 
					MolGenExp.colorModel.getColorNumber(org.getColor());
				for (int x = 0; x < fitnessSettings[colorNumber]; x++) {
					genePool.add(org.getRandomDNASequence());
				}
			}
		}
		System.out.println("gene pool has " + genePool.size() + " members");
	}
	
	private void makeNextGeneration() {
		current = 0;
		System.out.println("starting next generation");
		ThinOrganism[][] nextGeneration = 
			new ThinOrganism[MolGenExp.worldSize][MolGenExp.worldSize];
		//make next generation
		// choose random alleles from gene pool, mutate and make new organisms
		for (int i = 0; i < MolGenExp.worldSize; i++) {
			for (int j = 0; j < MolGenExp.worldSize; j++) {
				String alleleDNA = getRandomAlleleFromPool();
				String newAlleleDNA = MutantGenerator.mutateDNASequence(alleleDNA);
				nextGeneration[i][j] = new ThinOrganism(
						MutantGenerator.mutateDNASequence(getRandomAlleleFromPool()),
						MutantGenerator.mutateDNASequence(getRandomAlleleFromPool()));
				current++;
				System.out.println(current);
			}
		}
		world.setOrganisms(nextGeneration);
	}
	
	private String getRandomAlleleFromPool() {
		Random r = new Random();
		int x = r.nextInt(genePool.size());
		return (String)genePool.get(x);
	}
	
	public boolean done() {
		if (current == getLengthOfTask()) {
			return true;
		}
		return false;
	}
	
	public int getLengthOfTask() {
		return MolGenExp.worldSize * MolGenExp.worldSize;
	}
	
	public int getCurrent() {
		return current;
	}

}
