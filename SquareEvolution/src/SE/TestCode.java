package SE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

/*
 * class for testing genetic codes
 * makes the code (checks for errors)
 * prints it out nicely
 * 
 */
public class TestCode {

	private GeneticCode code;
	private String[] bases = {"T", "C", "A", "G"};

	private HashMap<String, Float> polarRequirements; 

	public TestCode(String fileName) {
		code = new GeneticCode(fileName);

		// set up Polar requirement table
		polarRequirements = new HashMap<String, Float>();
		polarRequirements.put("F",	new Float("+5.0f"));
		polarRequirements.put("L", new Float("+4.9f"));
		polarRequirements.put("I", new Float("+4.9f"));
		polarRequirements.put("M", new Float("+5.3f"));
		polarRequirements.put("V", new Float("+5.6f"));
		polarRequirements.put("S", new Float("+7.5f"));
		polarRequirements.put("P", new Float("+6.6f"));
		polarRequirements.put("T", new Float("+6.6f"));
		polarRequirements.put("A", new Float("+7.0f"));
		polarRequirements.put("Y", new Float("+5.4f"));
		polarRequirements.put("H", new Float("+8.4f"));
		polarRequirements.put("G", new Float("+7.9f"));
		polarRequirements.put("N", new Float("+10.0f"));
		polarRequirements.put("K", new Float("+10.1f"));
		polarRequirements.put("D", new Float("+13f"));
		polarRequirements.put("E", new Float("+12.5f"));
		polarRequirements.put("C", new Float("+4.8f"));
		polarRequirements.put("W", new Float("+5.2f"));
		polarRequirements.put("R", new Float("+9.1f"));
		polarRequirements.put("Q", new Float("+8.6f"));
	}

	public static void main(String[] args) {
		TestCode tc = new TestCode(args[0]);
		tc.prettyPrint();
		CodeStats r = tc.calculateStats();
		System.out.println("Number of valid mutants = " + r.numMuts);
		System.out.println("MS(0) = " + r.MS0);
		System.out.println("var0 = " + r.var0);
		float percent = ((float)r.numMissense)/3.8f;
		System.out.println("Achieves " + r.numMissense 
				+ " out of 380 possible missense mutants = " 
				+ percent + "%");
		System.out.println("Average Polar Requirement = " + r.avPR + " with " + r.numNonStopCodons + " non-stop codons.");
	}

	private void prettyPrint() {
		System.out.println("Amino acids in lower case are start codons; * = stop codon; ! = unassigned codon.");
		System.out.println();
		for (String b1 : bases) {
			for (String b3 : bases) {
				StringBuffer b = new StringBuffer();
				for (String b2 : bases) {
					String codon = b1 + b2 + b3;
					b.append(codon + " " + code.getAminoAcidByCodon(codon) + "\t");
				}
				System.out.println(b.toString());
			}
			System.out.println();
		}
	}

	/* calculate MS0 and var0 using Freeland's algorithm
	 * don't count mutations to/from stop codons
	 * sum up squared difference in Polar Requirement due to each mutation
	 * 
	 * also tally up the fraction of the 380 possible point mutations
	 * 		(20 x 20 - the 20 silent mutations = 380)
	 * that the code allows
	 * 
	 * the codon indicated by st = starting codon (before mutation)
	 *   end = the result of the mutation
	 *   
	 * also calculate the average polar requirement
	 */
	private CodeStats calculateStats() {
		ArrayList<Double> diffs = new ArrayList<Double>();
		TreeSet<String> missenseMutants = new TreeSet<String>();
		
		float totalPR = 0.0f;
		int totalNonStopCodons = 0;

		// iterate over all codons
		for (String st_b1 : bases) {
			for (String st_b2 : bases) {
				for (String st_b3 : bases) {
					String st_codon = st_b1 + st_b2 + st_b3;
					String st_aa = (code.getAminoAcidByCodon(st_codon)).toUpperCase();	// need to do this to deal with m/M
					
					// ignore if stop codon or unassigned
					if (!(st_aa.equals("*") || st_aa.equals("!"))) {
						float st_pr = polarRequirements.get(st_aa);
						
						// do the average PR calculations
						totalPR = totalPR + st_pr;
						totalNonStopCodons++;
						
						// pick a new base and try it in all positions
						for (String nb : bases) {
							
							// try in first position; be sure its a new base
							if (!nb.equals(st_b1)) {
								String end_codon = nb + st_b2 + st_b3;
								String end_aa = (code.getAminoAcidByCodon(end_codon)).toUpperCase();	// need to do this to deal with m/M
								// don't count it if it's stop codon or unassigned
								if (!(end_aa.equals("*") || end_aa.equals("!"))) {
									float end_pr = polarRequirements.get(end_aa);
									diffs.add(Math.pow((st_pr - end_pr), 2));
									if (!end_aa.equals(st_aa)) missenseMutants.add(end_aa + st_aa);
								}
							}	
							
							// try in second position; be sure its a new base
							if (!nb.equals(st_b2)) {
								String end_codon = st_b1 + nb + st_b3;
								String end_aa = (code.getAminoAcidByCodon(end_codon)).toUpperCase();	// need to do this to deal with m/M
								// don't count it if it's stop codon or unassigned
								if (!(end_aa.equals("*") || end_aa.equals("!"))) {
									float end_pr = polarRequirements.get(end_aa);
									diffs.add(Math.pow((st_pr - end_pr), 2));
									if (!end_aa.equals(st_aa)) missenseMutants.add(end_aa + st_aa);
								}
							}	

							// try in third position; be sure its a new base
							if (!nb.equals(st_b3)) {
								String end_codon = st_b1 + st_b2 + nb;
								String end_aa = (code.getAminoAcidByCodon(end_codon)).toUpperCase();	// need to do this to deal with m/M
								// don't count it if it's stop codon or unassigned
								if (!(end_aa.equals("*") || end_aa.equals("!"))) {
									float end_pr = polarRequirements.get(end_aa);
									diffs.add(Math.pow((st_pr - end_pr), 2));
									if (!end_aa.equals(st_aa)) missenseMutants.add(end_aa + st_aa);
								}
							}	
						}
					}
				}
			}
		}

		// find average = MS(0)
		double sum = 0.0f;
		for (double diff : diffs) {
			sum = sum + diff;
		}
		double MS0 = sum/diffs.size();
		
		// find var0 = sum of squared differences
		double diff_sum = 0.0f;
		for (double diff : diffs) {
			diff_sum = diff_sum + Math.pow((diff - MS0), 2);
		}
		double var0 = diff_sum/diffs.size();
		
		return new CodeStats(diffs.size(), 
				MS0, 
				var0, 
				missenseMutants.size(), 
				totalPR/((float)totalNonStopCodons), 
				totalNonStopCodons);
	}

	private class CodeStats {
		int numMuts;
		double MS0;
		double var0;
		int numMissense;
		float avPR;
		int numNonStopCodons;
		

		public CodeStats(int numMuts, double MS0, double var0, int numMissense, float avPR, int numNonStopCodons) {
			this.numMuts = numMuts;
			this.MS0 = MS0;
			this.var0 = var0;
			this.numMissense = numMissense;
			this.avPR = avPR;
			this.numNonStopCodons = numNonStopCodons;
		}
	}

}
