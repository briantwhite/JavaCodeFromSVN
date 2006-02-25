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
				return new Calculator();
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
	
	class Calculator {
		Calculator() {
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
				statusMessage = "Completed " + x + " out of " + numCodes + ".";
				current = x;
			}
			done = true;
		}
		
	}
}
