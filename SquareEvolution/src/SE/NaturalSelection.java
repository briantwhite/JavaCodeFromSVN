package SE;
import java.util.Random;


public class NaturalSelection {

	private double[] cumulativeFitnesses;
	private double totalFitness;
	private Random r;


	public NaturalSelection(double[] cumulativeFitnesses) {
		r = new Random();
		this.cumulativeFitnesses = cumulativeFitnesses;
		totalFitness = cumulativeFitnesses[cumulativeFitnesses.length - 1];
	}

	public int getRandomOrganismIndexByFitness() {
		double target = totalFitness * r.nextDouble();
		int left = 0; 
		int right = cumulativeFitnesses.length - 1;
		while (left <= right) {
			int middle = left + (right - left)/2;
			double found = cumulativeFitnesses[middle];

			if (found < target) {
				left = middle + 1;
			} else if (found > target) {
				right = middle - 1;
			} else {
				break;
			}
		}
		return left;
	}

	public static void main(String[] args) {
		// test with set of 20 random fitnesses
		double[] cumulativeFitnesses = new double[20];
		double[] fitnesses = new double[cumulativeFitnesses.length];
		Random r = new Random();
		double totalFitness = 0.0f;
		for (int i = 0; i < cumulativeFitnesses.length; i++) {
			double fit = r.nextDouble();
			fitnesses[i] = fit;
			totalFitness = totalFitness + fit;
			cumulativeFitnesses[i] = totalFitness;
		}

		NaturalSelection ns = new NaturalSelection(cumulativeFitnesses);

		int runs = 100000000;
		int[] counts = new int[cumulativeFitnesses.length];
		for (int i = 0; i < runs; i++) {
			counts[ns.getRandomOrganismIndexByFitness()]++;
		}

		System.out.println("result");
		System.out.println("i\tFit\tCumFit\tHits\tratio");
		int totalCount = 0;
		for (int i = 0; i < cumulativeFitnesses.length; i++) {
			totalCount = totalCount + counts[i];
			double normHit = (double)counts[i]/runs;
			double normFit = fitnesses[i]/ns.totalFitness;
			double ratio = normHit/normFit;
			System.out.format("%d\t%3.3f\t%d\t%3.3f\n"
					, i, fitnesses[i], counts[i], ratio);
		}
		System.out.println("Total counts = " + totalCount);
	}

}


