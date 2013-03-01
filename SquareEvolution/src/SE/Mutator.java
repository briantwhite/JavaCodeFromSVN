package SE;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import net.sf.doodleproject.numerics4j.statistics.distribution.BinomialDistribution;

/*
 * Mutates an organism or a set of organisms.
 * Two modes:
 * 1) Single organism 
 * 	(slow; assumes nothing about genome sizes)
 * 	uses:
 * 		constructor: Mutator(double pointMutRate)
 * 		method mutate(Organism o)
 * 	each organism's DNA is processed base-by-base
 * 	if mutation occurs (at mutation freq)
 * 	then, that base is changed to one of the other three.
 * 	
 * 2) Mass mutate 
 * 	(fast, but assumes all orgs have same genome size)
 * 	uses:
 * 		constructor Mutator(double pointMutRate, int popSize, int genomeSize)
 * 		method massMutate(Organism[]) 
 * 	- mutates one generation's organsims at once.
 * 	Uses binomial distribution to figure out
 * 	how many mutations there'll be in a generation.
 * 	Then, randomly places those mutations in the
 * 	population of genomes.
 */

public class Mutator {

	private String[] aSubs = {"G", "C", "T"}; 
	private String[] gSubs = {"A", "C", "T"};
	private String[] cSubs = {"A", "G", "T"};
	private String[] tSubs = {"A", "G", "C"};

	private double pointFreq;

	private Random r;

	private BinomialDistribution binomialDistribution;

	public Mutator(double pointMutRate) {
		pointFreq = pointMutRate;
		r = new Random();
	}

	public Mutator(double pointMutRate, int popSize, int genomeSize) {
		binomialDistribution = new BinomialDistribution(popSize * genomeSize, pointMutRate);
		r = new Random();
	}

	public double getMutationRate() {
		return pointFreq;
	}

	public Organism mutate(Organism o) {
		StringBuffer dna = new StringBuffer(o.getDNA());
		for (int i = 0; i < dna.length(); i++) {
			if (r.nextDouble() < pointFreq) {
				String origBase = dna.substring(i, i + 1);
				String newBase = origBase;
				if (origBase.equals("A")) newBase = aSubs[r.nextInt(3)];
				if (origBase.equals("G")) newBase = gSubs[r.nextInt(3)];
				if (origBase.equals("C")) newBase = cSubs[r.nextInt(3)];
				if (origBase.equals("T")) newBase = tSubs[r.nextInt(3)];								
				dna.replace(i, i + 1, newBase);
			}
		}

		if (o instanceof TypeAOrganism) {
			return new TypeAOrganism(dna.toString(), o.getGeneticCode());
		}
		if (o instanceof TypeBOrganism) {
			return new TypeBOrganism(dna.toString(), o.getGeneticCode());			
		}
		return null;
	}


	public Organism[] massMutate(Organism[] startingOrgs) {
		int popSize = startingOrgs.length;
		Organism[] newOrgs = new Organism[popSize];
		// make new copies of all organisms
		for (int i = 0; i < popSize; i++) {
			Organism o = startingOrgs[i];
			if (o instanceof TypeAOrganism) {
				newOrgs[i] = new TypeAOrganism(o.getDNA(), o.getGeneticCode());
			} 
			if (o instanceof TypeBOrganism) {
				newOrgs[i] = new TypeBOrganism(o.getDNA(), o.getGeneticCode());
			}
		}

		/*
		 * make the required number of mutations
		 * 	by choosing the organism and base randomly
		 */
		int numberOfMutations = getNumMutations(binomialDistribution, r);
		for (int i = 0; i < numberOfMutations; i++) {
			// pick the target organism
			int indexOfTargetOrganism = r.nextInt(popSize);
			Organism o = newOrgs[indexOfTargetOrganism];
			StringBuffer DNAbuf = new StringBuffer(o.getDNA());

			// change a random base
			int indexOfTargetBase = r.nextInt(DNAbuf.length());
			String origBase = DNAbuf.substring(indexOfTargetBase, indexOfTargetBase + 1);
			String newBase = origBase;
			if (origBase.equals("A")) newBase = aSubs[r.nextInt(3)];
			if (origBase.equals("G")) newBase = gSubs[r.nextInt(3)];
			if (origBase.equals("C")) newBase = cSubs[r.nextInt(3)];
			if (origBase.equals("T")) newBase = tSubs[r.nextInt(3)];								
			DNAbuf.replace(indexOfTargetBase, indexOfTargetBase + 1, newBase);

			// replace organism
			if (o instanceof TypeAOrganism) {
				newOrgs[indexOfTargetOrganism] = new TypeAOrganism(DNAbuf.toString(), o.getGeneticCode());
			}
			if (o instanceof TypeBOrganism) {
				newOrgs[indexOfTargetOrganism] = new TypeBOrganism(DNAbuf.toString(), o.getGeneticCode());
			}
		}
		return newOrgs;
	}

	private int getNumMutations(BinomialDistribution d, Random r) {
		int x = d.inverseCumulativeProbability(r.nextDouble());
		while (x < 0) {
			x = d.inverseCumulativeProbability(r.nextDouble());
		}
		return x;
	}


	/*
	 * test code
	 * 	if there's no arg, does method (1) = org-by-org
	 * 	if there's an arg of any kind, does method (2) = mass mutate
	 */
	public static void main(String[] args) {
		GeneticCode gc = new GeneticCode();

		// make 100-mer of DNA
		StringBuffer db = new StringBuffer();
		for (int i = 0; i < 25; i++) {
			db.append("AGCT");
		}
		String origDNA = db.toString();

		if (args.length > 0) {
			Mutator m = new Mutator(0.00011, 1000, 100);
			
			Organism[] orgs = new Organism[1000];
			for (int i = 0; i < orgs.length; i++) {
				orgs[i] = new TypeAOrganism(origDNA, gc);
			}

			// timing run
			System.out.println("Mutating 10,000,000 100-mers in sets of 1000 (method 2)");
			Date start = new Date();
			for (int i = 0; i < 10000; i++) {
				m.massMutate(orgs);
			}
			Date end = new Date();
			long elapsedTime = end.getTime() - start.getTime();
			System.out.println("Mutating 10,000,000 100-mers took " + elapsedTime + " mSec");

			// now, another set of runs for stats
			HashMap<String, Integer> tally = new HashMap<String, Integer>();
			int total = 0;
			int[] multipleMutationCounts = new int[10];
			int[] individualSiteCounts = new int[100];
			for (int i = 0; i < 10000; i++) {
				Organism[] newOrgs = m.massMutate(orgs);
				for (int j = 0; j < newOrgs.length; j++) {
					Organism x = newOrgs[j];
					int count = 0;
					String newDNA = x.getDNA();
					for (int k = 0; k < 100; k++) {
						String origBase = origDNA.substring(k, k + 1);
						String newBase = newDNA.substring(k, k + 1);
						if (!origBase.equals(newBase)) {
							// found a mutation; tally it
							individualSiteCounts[k]++;
							String key = origBase + "->" + newBase;
							if (!tally.containsKey(key)) {
								tally.put(key, new Integer(0));
							}
							int oldCount = tally.get(key).intValue();
							tally.put(key, new Integer(oldCount + 1));
							total++;
							count++;
						}
					}
					multipleMutationCounts[count]++;
				}
			}
			float freq = ((float)total)/(((float)10000000) * (float)(100));
			System.out.println("Mutator test:");
			System.out.println("\t10,000,000 runs; set mutation rate = " + m.pointFreq);
			System.out.println("\tmeasured rate (per base) = " + freq);
			System.out.println("\tMut\tcount");
			Iterator<String> it = tally.keySet().iterator();
			while(it.hasNext()) {
				String key = it.next();
				System.out.println("\t" + key + "\t" + tally.get(key).intValue());
			}
			System.out.println("Frequencies of multiple mutations");
			System.out.println("\tNum\tcount");
			for (int i = 0; i < 10; i++) {
				System.out.println("\t" + i + "\t" + multipleMutationCounts[i]);
			}
			System.out.println();
			System.out.println("Mutation freqs for each site");
			System.out.println("\tSite\tcount");
			for (int i = 0; i < 100; i++) {
				System.out.println("\t" + i + "\t" + individualSiteCounts[i]);
			}

		
		} else {
			Mutator m = new Mutator(0.0001);
			
			TypeAOrganism o = new TypeAOrganism(origDNA, gc);
			int len = origDNA.length();

			// first do a pure timing run
			System.out.println("Mutating 10,000,000 100-mers one-by-one (method 1)");
			Date start = new Date();
			for (int i = 0; i < 10000000; i++) {
				m.mutate(o);
			}
			Date end = new Date();
			long elapsedTime = end.getTime() - start.getTime();
			System.out.println("Mutating 10,000,000 100-mers took " + elapsedTime + " mSec");

			// now do another run to count frequencies, etc.
			HashMap<String, Integer> tally = new HashMap<String, Integer>();
			int total = 0;
			int[] multipleMutationCounts = new int[10];
			int[] individualSiteCounts = new int[len];

			for (int i = 0; i < 10000000; i++) {
				Organism x = m.mutate(o);
				int count = 0;
				String newDNA = x.getDNA();
				for (int j = 0; j < len; j++) {
					String origBase = origDNA.substring(j, j + 1);
					String newBase = newDNA.substring(j, j + 1);
					if (!origBase.equals(newBase)) {
						// found a mutation; tally it
						individualSiteCounts[j]++;
						String key = origBase + "->" + newBase;
						if (!tally.containsKey(key)) {
							tally.put(key, new Integer(0));
						}
						int oldCount = tally.get(key).intValue();
						tally.put(key, new Integer(oldCount + 1));
						total++;
						count++;
					}
				}
				multipleMutationCounts[count]++;
			}

			float freq = ((float)total)/(((float)10000000) * (float)len);
			System.out.println("Mutator test:");
			System.out.println("\t10,000,000 runs; set mutation rate = " + m.pointFreq);
			System.out.println("\tmeasured rate (per base) = " + freq);
			System.out.println("\tMut\tcount");
			Iterator<String> it = tally.keySet().iterator();
			while(it.hasNext()) {
				String key = it.next();
				System.out.println("\t" + key + "\t" + tally.get(key).intValue());
			}
			System.out.println("Frequencies of multiple mutations");
			System.out.println("\tNum\tcount");
			for (int i = 0; i < 10; i++) {
				System.out.println("\t" + i + "\t" + multipleMutationCounts[i]);
			}
			System.out.println();
			System.out.println("Mutation freqs for each site");
			System.out.println("\tSite\tcount");
			for (int i = 0; i < len; i++) {
				System.out.println("\t" + i + "\t" + individualSiteCounts[i]);
			}
		}
	}


}
