package SE;

import java.util.HashMap;
import java.util.Iterator;

public class FitnessProcessor {
	
	private Configuration config;
	private ProteinDatabase proteinDatabase;
	
	public FitnessProcessor(Configuration config, ProteinDatabase proteinDatabase) {
		this.config = config;
		this.proteinDatabase = proteinDatabase;
	}
	
	public FitnessResult calculateFitnesses(Organism[] world, String[] proteins, boolean calculateDiversity) {
		
		// default values if we're not calculating the diversity
		int numberOfDNASpecies = -1;
		double shannonDNADiversity = -1.0f;
		int numberOfProtSpecies = -1;
		double shannonProtDiversity = -1.0f;

		//get fitnesses of this generation 
		String bestDNA = "";
		double maxFitness = Double.MIN_VALUE;
		ProteinDatabaseEntry bestEntry = null;
		double totalFitness = 0;
		int popSize = config.getPopulationSize();
		double[] fitnesses = new double[popSize];
		double[] cumulativeFitnesses = new double[popSize];
		for (int x = 0; x < popSize; x++) {
			ProteinDatabaseEntry pde = proteinDatabase.getEntry(
					proteins[x],
					config.getLigandSequence(),
					config.getLigandStructure());
			double fitness = pde.fitness;
			fitnesses[x] = fitness;
			totalFitness = totalFitness + fitness;
			cumulativeFitnesses[x] = totalFitness;
			if (fitness > maxFitness) {
				bestDNA = world[x].getDNA();
				maxFitness = fitness;
				bestEntry = pde;
			}
		}
		
		//if needed, calculate the Shannon diversity
		//  and count number of DNA 'species' (different DNA sequences)
		if (calculateDiversity) {
			
			// calculate cumulative fitness of each species
			HashMap<String, Double> dnaSpeciesFitnessTallies = new HashMap<String, Double>();
			HashMap<String, Double> protSpeciesFitnessTallies = new HashMap<String, Double>();
			for (int x = 0; x < popSize; x++) {
				String DNA = world[x].getDNA();
				if (dnaSpeciesFitnessTallies.get(DNA) == null) {
					dnaSpeciesFitnessTallies.put(DNA, new Double(0.0f));
				}
				double oldVal = dnaSpeciesFitnessTallies.get(DNA);
				dnaSpeciesFitnessTallies.put(DNA, new Double(oldVal + fitnesses[x]));
				
				String protein = proteins[x];
				if (protSpeciesFitnessTallies.get(protein) == null) {
					protSpeciesFitnessTallies.put(protein, new Double(0.0f));
				}
				oldVal = protSpeciesFitnessTallies.get(protein);
				protSpeciesFitnessTallies.put(protein, new Double(oldVal + fitnesses[x]));
			}
			
			// calculate Shannon diversity of DNA
			//  -sum(pln(p)) where p = probability of finding that org in population
			//  = it's fitness/(total fitness)
			shannonDNADiversity = 0;
			Iterator<String> it = dnaSpeciesFitnessTallies.keySet().iterator();
			while (it.hasNext()) {
				double p = dnaSpeciesFitnessTallies.get(it.next())/totalFitness;
				shannonDNADiversity = shannonDNADiversity - (p * Math.log(p));
			}
			numberOfDNASpecies = dnaSpeciesFitnessTallies.size();
			
			// Shannon protein diversity
			shannonProtDiversity = 0;
			it = protSpeciesFitnessTallies.keySet().iterator();
			while (it.hasNext()) {
				double p = protSpeciesFitnessTallies.get(it.next())/totalFitness;
				shannonProtDiversity = shannonProtDiversity - (p * Math.log(p));
			}
			numberOfProtSpecies = protSpeciesFitnessTallies.size();
		}
		
		return new FitnessResult(
				bestDNA,
				maxFitness, 
				bestEntry,
				fitnesses,
				cumulativeFitnesses,
				totalFitness,
				numberOfDNASpecies,
				shannonDNADiversity,
				numberOfProtSpecies,
				shannonProtDiversity);
	}

}
