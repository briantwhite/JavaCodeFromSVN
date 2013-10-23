package SE;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class GetFitness {

	/**
	 * params
	 * 	aa seq
	 * 	config file with settings
	 */
	public static void main(String[] args) {
		String proteinSequence = args[0];
		Configuration config = new Configuration(args[1]);

		Socket s = null;


		try {
			// start connection to folding server
			s = new Socket("localhost", config.getPortNum());

			BufferedReader in = new BufferedReader(
					new InputStreamReader(s.getInputStream()));
			PrintStream out = new PrintStream(
					s.getOutputStream(), true /* autoFlush */);


			// fold 'it
			System.out.println("Folding protein " + proteinSequence);
			System.out.println("Ligand: seq= " + config.getLigandSequence()
					+ " str= " + config.getLigandStructure()
					+ " desired rotamer= " + config.getLigandRotamer());


			// build the request string for this run
			StringBuffer b = new StringBuffer();
			for (int x = 0; x < 100; x++) {
				b.append(proteinSequence + ","
						+ config.getLigandSequence() + ","
						+ config.getLigandStructure() + ","
						+ config.getLigandRotamer() + ";");
			}
			String request = b.toString();

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
			String[] pieces = responses[1].split(",");
			System.out.println("got response:" + responses[1]);
			if (pieces.length == 8) {
				
				// first, check for errors and flag them
				boolean noStructureError = false;
				boolean badEnergyError = false;

				String proteinStructure = pieces[2];
				if (proteinStructure.contains("Folding or Binding")) {
					// log the error
					FoldingErrorLog.getInstance().addError(proteinStructure);
					noStructureError = true;
				}

				/*
				 * with some proteins, you get an infinite energy
				 *    probably an overflow
				 *    if so, log error and give no conformation
				 */
				if (pieces[1].equals("inf") || pieces[1].equals("nan")) {
					System.out.println("Infinite or Nan dGfolding");
					badEnergyError = true;
				}
				if (pieces[3].equals("inf") || pieces[3].equals("nan")) {
					System.out.println("Infinite or Nan best binding energy");
					badEnergyError = true;
				}
				if (pieces[4].equals("inf") || pieces[4].equals("nan")) {
					System.out.println("Infinite or Nan binding partition sum");
					badEnergyError = true;
				}

				if(badEnergyError) {
					System.out.println("Bad energy error");
				} else if (noStructureError) {
					System.out.println("No structure error");
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
						System.out.println("Z<0, setting z=0");
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
					System.out.println("dG fold = " + Double.parseDouble(pieces[1]));
					System.out.println("best bind NRG = " + bestBindingEnergy);
					System.out.println("bind part sum = " + bindingPartitionSum);
					System.out.println("Z = " + z);
					System.out.println("dG bind = " + dGb);
					System.out.println("Kfold = " + Kf);
					System.out.println("Kbind = " + Kb);
					System.out.println("fract folded = " + (Kf/(1 + Kf)));
					System.out.println("fract bound = " + (Kb/(1 + Kb)));
					System.out.println("fitness = " + fitness);
					System.out.println("Structure:");
					System.out.println(DisplayStructure.getStructure(
							pieces[0],		// protSeq 
							pieces[2],		// protStr
							config.getLigandSequence(),		// ligSeq, 
							config.getLigandStructure(),	// ligStr, 
							Integer.parseInt(pieces[5]),		// ligandRotamer, 
							Integer.parseInt(pieces[6]),		// ligX, 
							Integer.parseInt(pieces[7])));	// ligY

				}
			}

			out.print("?");  // tell the server to close the socket
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
