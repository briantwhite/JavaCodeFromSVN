package SE;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class RandomProteinFitness {

	private static String[] aas = {
		"A", "C", "D", "E", "F",
		"G", "H", "I", "K", "L",
		"M", "N", "P", "Q", "R",
		"S", "T", "V", "W", "Y"};


	/**
	 * length of proteins to make
	 * number of proteins to make
	 * config file with settings
	 */
	public static void main(String[] args) {
		int proteinLength = Integer.parseInt(args[0]);
		int numProteins = Integer.parseInt(args[1]);
		Configuration config = new Configuration(args[2]);
		
		ProteinAndInfo[] results = new ProteinAndInfo[numProteins];

		Socket s = null;

		Random r = new Random();

		try {
			// start connection to folding server
			s = new Socket("localhost", config.getPortNum());

			BufferedReader in = new BufferedReader(
					new InputStreamReader(s.getInputStream()));
			PrintStream out = new PrintStream(
					s.getOutputStream(), true /* autoFlush */);

			// make proteins
			System.out.println("Making " + numProteins + " " + proteinLength + "-mers");
			String[] proteins = new String[numProteins];
			for (int i = 0; i < numProteins; i++) {
				StringBuffer b = new StringBuffer("M"); // first aa is always met
				for (int x = 0; x < proteinLength - 1; x++) {
					b.append(aas[r.nextInt(20)]);
				}
				proteins[i] = b.toString();
			}

			// fold 'em
			System.out.println("Folding them");

			int i = 0;  // index for results

			// break set into groups of 100 per server request (run)
			int numFoldingRuns = (proteins.length + 99)/100;

			for (int run = 0; run < numFoldingRuns; run++) {

				// build the request string for this run
				StringBuffer b = new StringBuffer();
				int x = 0;
				for (x = 0; x < 100; x++) {
					if (((100 * run) + x) >= proteins.length) break;
					b.append(proteins[((100 * run) + x)] + ","
							+ config.getLigandSequence() + ","
							+ config.getLigandStructure() + ","
							+ config.getLigandRotamer() + ";");
				}
				String request = b.toString();
				int numProtsInThisSet = x;
				// send to server & get response
				out.print(request);
				out.flush();
				String response = in.readLine();

				/*
				 *  process response string
				 *  pieces[0] = protein seq
				 *  pieces[1] = dGfolding
				 *  pieces[2] = protein structure
				 *  pieces[3] = best binding energy
				 *  pieces[4] = binding partition sum
				 *  pieces[5] = best bindging rotamer
				 *  pieces[6] = best binding ligand X
				 *  pieces[7] = best binding ligand Y
				 */
				String[] responses = response.split(";");
				if (responses.length != (numProtsInThisSet + 1)) {  // first is padding
					System.out.println(
							"Monster error! Sent " 
									+ numProtsInThisSet 
									+ " seqs to fold; got back " + responses.length);
				}
				for (int y = 0; y < responses.length; y++) {
					String[] pieces = responses[y].split(",");
					if (pieces.length == 8) {
						String proteinStructure = pieces[2];
						if (proteinStructure.contains("Folding or Binding")) {
							// log the error
							FoldingErrorLog.getInstance().addError(proteinStructure);
							// give it 'none' structure
							proteinStructure = "None";
						}

						/*
						 * with some proteins, you get an infinite energy
						 *    probably an overflow
						 *    if so, log error and give no conformation
						 */
						boolean badEnergyError = false;
						if (pieces[1].equals("inf") || pieces[1].equals("nan")) {
							FoldingErrorLog.getInstance().addError("Infinite or Nan dGfolding");
							badEnergyError = true;
						}
						if (pieces[3].equals("inf") || pieces[3].equals("nan")) {
							FoldingErrorLog.getInstance().addError("Infinite or Nan best binding energy");
							badEnergyError = true;
						}
						if (pieces[4].equals("inf") || pieces[4].equals("nan")) {
							FoldingErrorLog.getInstance().addError("Infinite or Nan binding partition sum");
							badEnergyError = true;
						}

						if(badEnergyError) {
							/*
							 * in that case, add a dummy entry
							 * note that fitness is adjusted in constructor to account for neutrality
							 * see Square Evolution Log 06 page 25
							 * 
							 */
							results[i] = new ProteinAndInfo(pieces[0], SquareEvolution.NO_STRUCTURE_FITNESS);
							i++;
						} else {
							double bestBindingEnergy = Double.parseDouble(pieces[3]);
							double bindingPartitionSum = Double.parseDouble(pieces[4]);
							double z = bindingPartitionSum - Math.exp(-bestBindingEnergy);
							/*
							 * theoretically, this should NEVER be < 0
							 * 	since it's the sum - the max!
							 *  but floating point errors can do this
							 *  so set to 0 
							 *  see SquareProteinEvolution Log 6 pages 10, 18-20 
							 *  
							 *  but only do this if the protein has a structure
							 *    (it turns out that z < 0 if protein has no structure
							 *      or is too long, etc and didn't get bound)
							 *      
							 */
							if ((z < 0) && (!proteinStructure.equals("None"))) {
								z = 0;
							}
							double dGb = bestBindingEnergy + Math.log(z + config.getNumUnboundStates());
							double Kf = Math.exp(-Double.parseDouble(pieces[1]));
							double Kb = Math.exp(-dGb);
							double proteinFitness = (Kf/(1 + Kf)) * (Kb/(1 + Kb));

							/*
							 * see discussion of neutrality in "Square Evolution Log 02.docx" pages 14, 17, 25, 26
							 * see updates in "Square Evolution Log 06.docx" page 24
							 */
							double fitness = config.getNeutrality() + (1 - config.getNeutrality()) * proteinFitness;

							results[i] = new ProteinAndInfo(pieces[0], 
									proteinStructure,
									Double.parseDouble(pieces[1]),
									dGb,
									Integer.parseInt(pieces[5]),
									Integer.parseInt(pieces[6]),
									Integer.parseInt(pieces[7]),
									fitness);
							i++;
						}
					}
				}
			}

			out.print("?");  // tell the server to close the socket
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// write results to file
		FileWriter outFile;
		try {
			outFile = new FileWriter("RandomProteinsAndFitnesses.txt");
			PrintWriter out = new PrintWriter(outFile);
			out.write("N,Protein,Conformation,dGf,dGb,Rotamer,X,Y,Fitness\n");
			for (int i = 0; i < results.length; i++) {
				out.write(String.valueOf(i) + "," + results[i].toString() + "\n");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
