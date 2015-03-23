package SE;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
		/*
		 * 	two versions:
				1) one parameter CodeName.xml - just test that code
				2) two parameters CodeData.csv CodeDirectory
					CodeData.csv has the code name and MS0 and avgPR to check against what TestCode finds
						when read, check column headings to be sure you're reading the right fields
					CodeDirectory has the code.xml files in is
					- outputs to stdout: one line per code 
							code name OK or not if these all hold
								MS0 is right
								avgPR is right
								3 stop codons
								1 met start codon
								right # of valid mutations
								right % of missense accessible
					- outputs to CodeTables.txt
							the regular printout
		 */

		if ((args.length == 3) && args[0].endsWith(".csv")) {

			// get params
			File codeDataFile = new File(args[0]);
			File codeDirectory = new File(args[1]);
			File resultFile = new File(args[2]);

			// sanity checks
			if (!codeDataFile.exists()) {
				System.out.println("Error: can't find code data file " + args[0]);
				System.exit(0);
			}
			if (!codeDirectory.exists()) {
				System.out.println("Error: can't find code directory " + args[1]);
				System.exit(0);
			}

			// read in the file and process the lines one by one
			String line = "";
			BufferedReader br = null;
			BufferedWriter out = null;
			try {
				br = new BufferedReader(new FileReader(codeDataFile));
				int MS0columnIndex = -1;
				int avgPRcolumnIndex = -1;
				while ((line = br.readLine()) != null) {
					String[] parts = line.split(",");
					/*
					 * check for first line
					 * 		sometimes Code	MS0	Var0	avgPR
					 * 		sometimes Code	MS0	avgPR
					 * need to get this right
					 */
					if (parts[0].equals("Code")) {
						for (int i = 0; i < parts.length; i++) {
							if (parts[i].equals("MS0")) {
								MS0columnIndex = i;
							} else if (parts[i].equals("avgPR")) {
								avgPRcolumnIndex = i;
							}
						}
					} else {
						// it's a data line, see if we're set to read it
						if ((MS0columnIndex == -1) || (avgPRcolumnIndex == -1)) {
							System.out.println("Error: could not find MS0 and avgPR columns in " + codeDataFile.getName());
							System.exit(0);
						}
						// it must be ok
						String codeFileName = codeDirectory.getAbsolutePath() + "/" + parts[0] + ".xml";
						float MS0 = Float.parseFloat(parts[MS0columnIndex]);
						float avgPR = Float.parseFloat(parts[avgPRcolumnIndex]);
						TestCode tc = new TestCode(codeFileName);
						CodeStats r = tc.calculateStats();
						double MS0diff = Math.abs(MS0 - r.MS0);
						double avgPRdiff = Math.abs(avgPR - r.avPR);

						if ((MS0diff < 1E-6) 
								&& (avgPRdiff < 1E-6) 
								&& (r.numNonStopCodons == 61) 
								&& (r.numStartCodons == 1)
								&& (r.numMuts == 526)
								&& (r.numMissense == 150)) {
							System.out.println(codeFileName + " OK");
						} else {
							System.out.println(codeFileName + " Bad");
						}
					}

				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		} else if (args.length == 1) {
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
			System.out.println("Average Polar Requirement = " + r.avPR 
					+ " with " + r.numStartCodons + " start codons and " 
					+ r.numNonStopCodons + " non-stop codons.");
		} else {
			System.out.println("Error: wrong number of arguments");
		}
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
		int numStartCodons = 0;

		// iterate over all codons
		for (String st_b1 : bases) {
			for (String st_b2 : bases) {
				for (String st_b3 : bases) {
					String st_codon = st_b1 + st_b2 + st_b3;

					// count start codons
					if (code.getAminoAcidByCodon(st_codon).equals("m")) {
						numStartCodons++;
					}

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
				totalNonStopCodons,
				numStartCodons);
	}

	private class CodeStats {
		int numMuts;
		double MS0;
		double var0;
		int numMissense;
		float avPR;
		int numNonStopCodons;
		int numStartCodons;


		public CodeStats(int numMuts, double MS0, double var0, int numMissense, float avPR, int numNonStopCodons,
				int numStartCodons) {
			this.numMuts = numMuts;
			this.MS0 = MS0;
			this.var0 = var0;
			this.numMissense = numMissense;
			this.avPR = avPR;
			this.numNonStopCodons = numNonStopCodons;
			this.numStartCodons = numStartCodons;
		}
	}

}
