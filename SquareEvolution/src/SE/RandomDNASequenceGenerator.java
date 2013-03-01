package SE;

import java.util.Date;
import java.util.Random;

public class RandomDNASequenceGenerator {

	private Random r;
	private static RandomDNASequenceGenerator instance;
	private String[] DNA = {"A", "G", "C", "T"};

	private RandomDNASequenceGenerator() {
		r = new Random();
	}

	public static RandomDNASequenceGenerator getInstance() {
		if (instance == null) {
			instance = new RandomDNASequenceGenerator();
		}
		return instance;
	}

	public String generateRandomNmer(int n) {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < n; i++)	{
			b.append(DNA[r.nextInt(4)]);
		}
		return b.toString();
	}

	public String generateRandomNonCodingNmer(GeneticCode gc, int n) {
		String DNA = generateRandomNmer(n);
		while (!gc.translateWithCache(DNA).equals("")) {
			DNA = generateRandomNmer(n);
		}
		return DNA;
	}

	public static void main(String[] args) {
		GeneticCode gc = null;
		if (args.length > 0) {
			gc = new GeneticCode(args[0]);
		}

		Date start = new Date();
		int[][] counts = new int[100][4];
		RandomDNASequenceGenerator rgen = RandomDNASequenceGenerator.getInstance();
		for (int i = 0; i < 10000; i++) {
			char[] dna;
			if (gc == null) {
				dna = rgen.generateRandomNmer(100).toCharArray();
			} else {
				dna = rgen.generateRandomNonCodingNmer(gc, 100).toCharArray();
			}
			for (int x = 0; x < 100; x++) {
				char base = dna[x];
				switch (base) {
				case 'A':
					counts[x][0]++;
					break;
				case 'G':
					counts[x][1]++;
					break;
				case 'C':
					counts[x][2]++;
					break;
				case 'T':
					counts[x][3]++;
				}
			}
		}
		Date end = new Date();
		long time = end.getTime() - start.getTime();
		System.out.println("It took " + time + " ms.");
		System.out.println("Base\tA\tG\tC\tT");
		for (int i = 0; i < 100; i++) {
			System.out.println(i + "\t" + counts[i][0] + "\t" + counts[i][1] + "\t" 
					+ counts[i][2] + "\t" + counts[i][3]);
		}
	}	

}
