package SE;

import java.util.Random;

import net.sf.doodleproject.numerics4j.statistics.distribution.BinomialDistribution;

public class NumericTest {

	public static int getNumMutations(BinomialDistribution d, Random r) {
		int x = d.inverseCumulativeProbability(r.nextDouble());
		while (x < 0) {
			x = d.inverseCumulativeProbability(r.nextDouble());
		}
		return x;
	}
	
	public static void main(String[] args) {
		int numRuns = Integer.parseInt(args[0]);
		int popSize = Integer.parseInt(args[1]);
		float mutRate = Float.parseFloat(args[2]);
		BinomialDistribution d = new BinomialDistribution(popSize, mutRate);
		Random r = new Random();
		for (int i = 0; i < numRuns; i++) {
			System.out.println(getNumMutations(d, r));
		}
	}

}
