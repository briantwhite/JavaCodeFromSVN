import java.util.TreeMap;

public class PairHistogramCalculator extends Thread {

	private TreeMap histogram;
	private int[][] pairs;
	private int numCodes;
	
	public PairHistogramCalculator(int[][] pairs) {
		this.pairs = pairs;
		histogram = new TreeMap();
		numCodes = pairs.length;
	}
	
	public void run () {
		for (int x = 0; x < numCodes; x++){
			for (int y = 0; y < numCodes; y++){
				Integer count = new Integer(pairs[x][y]);
				if (histogram.containsKey(count)) {
					Integer oldTally = (Integer)histogram.get(count);
					histogram.put(count, new Integer(oldTally.intValue() + 1));
				} else {
					histogram.put(count, new Integer(1));
				}
			}
		}
	}
	
	public TreeMap getHistogram() {
		return histogram;
	}
}
