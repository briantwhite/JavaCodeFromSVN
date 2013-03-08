import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class FoldTestSet {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// read in test set
		HashMap<String, Double> sequencesAndFitnesses = new HashMap<String, Double>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader("TestSet.txt"));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				sequencesAndFitnesses.put(parts[0], Double.parseDouble(parts[1]));
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("Error opening TestSet.txt");
		}

		System.out.println("Read in " + sequencesAndFitnesses.size() + " protein sequences and fitnesses.");
		System.out.println("Sending out to fold");

		Socket s = null;

		try {
			s = new Socket("localhost", 12000);
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedReader in = null;
		PrintStream out = null;

		try {
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintStream(s.getOutputStream(), true /* autoFlush */);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Date start = new Date();
		
		HashMap<String,String> sequencesAndResults = new HashMap<String,String>();

		Iterator<String> it = sequencesAndFitnesses.keySet().iterator();
		int set = 0;
		while (it.hasNext()) {
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < 100; i++) {
				if (!it.hasNext()) break;
				String aaSeq = it.next();
				buf.append(aaSeq + ",*,*,-1;");
			}
			String submission = buf.toString();
			System.out.println("\tSending set " + set + " to fold");
			try {
				out.print(submission);
				out.flush();
				String[] results = (in.readLine()).split(";");
				System.out.println("\t\tGot " + results.length + " folds back");
				
				// go 1 to 100 since first one is just ";"
				for (int x = 1; x < 101; x++) {
					String[] parts = results[x].split(",");
					sequencesAndResults.put(parts[0], results[x]);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("\t\tFinished folding set " + set);
			set++;
		}
		
		out.print("?");

		try {
			in.close();
			out.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Date end = new Date();
		System.out.println("It took " + (end.getTime() - start.getTime()) + " ms.");

		System.out.println("Processing results");
		double totalFitness = 0.0f;
		double totalFitnessDiff = 0.0f;
		
		int[] rotamerTallies = new int[4];
		it = sequencesAndFitnesses.keySet().iterator();
		while (it.hasNext()) {
			String seq = it.next();
			String result = sequencesAndResults.get(seq);
			String[] parts = result.split(",");
			/*
			 *  process response string
			 *  pieces[0] = protein seq
			 *  pieces[1] = dGfolding
			 *  pieces[2] = protein structure
			 *  pieces[3] = best binding energy
			 *  pieces[4] = binding partition sum
			 *  pieces[5] = best binding rotamer
			 *  pieces[6] = best binding ligand X
			 *  pieces[7] = best binding ligand Y
			 */
			double dGf = Double.parseDouble(parts[1]);
			double bestBindNRG = Double.parseDouble(parts[3]);
			double bindPartSum = Double.parseDouble(parts[4]);
			int bestRotamer = Integer.parseInt(parts[5]);
			rotamerTallies[bestRotamer]++;
			
			double dGb = bestBindNRG + Math.log(bindPartSum + (double)1000000 - Math.exp(-bestBindNRG));
			double Kf = Math.exp(-dGf);
			double Kb = Math.exp(-dGb);
			double newFitness = (Kf/(1 + Kf)) * (Kb/(1 + Kb));

			totalFitness = totalFitness + newFitness;
			totalFitnessDiff = totalFitnessDiff + (newFitness - sequencesAndFitnesses.get(seq));
		}
		
		System.out.println("Best Rotamer tallies:");
		System.out.println("\tRotamer\tCount");
		for (int i = 0; i < rotamerTallies.length; i++) {
			System.out.println("\t" + i + "\t" + rotamerTallies[i]);
		}
		System.out.println("Average fitness = " + totalFitness/sequencesAndFitnesses.size());
		System.out.println("Average fitness error = " + totalFitnessDiff/sequencesAndFitnesses.size());
	}

}
