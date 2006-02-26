import java.util.TreeMap;

import cern.colt.matrix.impl.SparseDoubleMatrix3D;

public class TripleHistogramCalculator extends Thread {
	
	private TreeMap histogram;
	private SparseDoubleMatrix3D triples;
	private int numCodes;
	private String statusMessage;
	private int current;
	private boolean done;
	
	public TripleHistogramCalculator(SparseDoubleMatrix3D triples, int numCodes) {
		super();
		this.triples = triples;
		histogram = new TreeMap();
		this.numCodes = numCodes;
		done = false;
	}
	
	public void go() {
		current = 0;
		done = false;
		final SwingWorker worker = new SwingWorker() {
			public Object construct() {
				return new TripleCalculator();
			}
		};
		worker.start();
	}
	
	int getLengthOfTask() {
		return numCodes;
	}
	
	int getCurrent() {
		return current;
	}
		
	boolean done() {
		return done;
	}
	
	String getMessage() {
		return statusMessage;
	}
	
	public TreeMap getHistogram(){
		return histogram;
	}
	
	class TripleCalculator {
		TripleCalculator() {
			for (int x = 0; x < numCodes; x++){
				for (int y = 0; y < numCodes; y++){
					for (int z = 0; z < numCodes; z++){
						Double count = new Double(triples.getQuick(x,y,z));
						if (histogram.containsKey(count)) {
							Double oldTally = (Double)histogram.get(count);
							histogram.put(count, new Double(
									oldTally.doubleValue() + 1));
						} else {
							histogram.put(count, new Double(1));
						}
					}
				}
				statusMessage = "Completed " + x + " out of " + numCodes + ".";
				current = x;
			}
			done = true;
		}
		
	}
}
