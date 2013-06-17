// IncrementalFolder.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota
/* 
 * License Information
 * 
 * This  program  is  free  software;  you can redistribute it and/or modify it 
 * under  the  terms  of  the GNU General Public License  as  published  by the 
 * Free  Software  Foundation; either version 2  of  the  License,  or (at your 
 * option) any later version.
 */

package protex.client;

/**
 * Incremental approximation algorithm searching for minimal energy.
 * 
 * Backtrack for lookAhead acid placements, find the minimum so far, and then
 * place step of the tentatively placed acids.
 */
public class IncrementalFolder extends BruteForceFolder {
	public static final int defaultLookAhead = 8;

	public static final int defaultStep = 4;

	// at each step:
	// number of acids to place greedily
	private int lookAhead = defaultLookAhead;

	// number of acids to pin down
	private int step = defaultStep;
	
	//current amino acid being worked in
	int current;
	
	@Override
	public int getCurrent() {
		return current;
	}

	@Override
	public String getName() {
		return "Incremental folding - " + "lookahead: " + lookAhead + " step: "
				+ step;
	}

	public IncrementalFolder(boolean custom, Polypeptide pp, Grid g, int lookAhead, int step) {
		// 	super(pp);
		this(custom, pp, g);
		this.lookAhead = lookAhead;
		this.step = step;
	}

	//     public IncrementalFolder( Grid g, Polypeptide pp )
	public IncrementalFolder(boolean custom, Polypeptide pp, Grid g) {
		super(custom, pp, g);
	}

	public void setLookAhead(int lookAhead) {
		this.lookAhead = lookAhead;
	}

	public void setStep(int step) {

		this.step = step;
	}
	
	public void done() {
		restore();
		// unplace some acids
		for (int i = current + step; i < localLookAhead; i++) {
			grid.unset(acids[i]);
		}
		current += step;
		
		if (current < numAcids) {
			resetEnergy();
			//int localLookAhead = Math.min(current + lookAhead, numAcids);
			localLookAhead = Math.min(current + lookAhead, numAcids);
			recurse(grid.getNextDirection(acids[current - 2].next), current,
					localLookAhead);
		}
		else {
			protexGWT.drawProtein();
		}
	}
	
	public void progress(int val) {
		protexGWT.progress(val);
	}
	
	int localLookAhead;
	
	@Override
	protected void placeRestOfAcids() {
		current = 2; // ready to place 3rd acid
		resetEnergy();
		localLookAhead = Math.min(current + lookAhead, numAcids);
		recurse(grid.getNextDirection(acids[current - 2].next), current,
					localLookAhead);
	}
	
	//For debugging
	public static native void log(String msg)
	/*-{
		console.log(msg);
	}-*/;
	
	public static native void alert(String msg)
	/*-{
		$wnd.alert(msg);
	}-*/;
	
	//Original function
	/*protected void placeRestOfAcids() {
		current = 2; // ready to place 3rd acid
		while (current < numAcids) {
			resetEnergy();
			int localLookAhead = Math.min(current + lookAhead, numAcids);
			recurse(grid.getNextDirection(acids[current - 2].next), current,
					localLookAhead);
			restore();
			// unplace some acids
			for (int i = current + step; i < localLookAhead; i++) {
				grid.unset(acids[i]);
			}
			current += step;
		}
	}*/
	
	/*
	 * Method main for unit testing.
	 *
	 */
//	public static void main(String[] args) {
//		Options opts = new Options("help",
//				"step:4 lookahead:8 table:standard length:10 seed:10", args);
//		if (opts.isOpt("help")) {
//			System.out.println("usage: java IncrementalFolder "
//					+ "\n    [-help] [-lookahead n] [-step s] "
//					+ "\n    [-table standard|virtual] "
//					+ "\n    [-length len] [-seed seed ]");
//			System.exit(0);
//		}
//		args = opts.getShiftArray();
//		Polypeptide pp = null;
//		try {
//			pp = PolypeptideFactory.getInstance().createPolypeptide("", false,
//					true, "" + opts.getIntOpt("length"),
//					"" + opts.getIntOpt("seed"), opts.getOpt("table"), null);
//		} catch (FoldingException e) {
//			System.out.println(e);
//			System.exit(0);
//		}
//		IncrementalFolder folder = new IncrementalFolder(pp, new HexGrid(pp));
//		folder.setLookAhead(opts.getIntOpt("lookahead"));
//		folder.setStep(opts.getIntOpt("step"));
//		folder.fold();
//		System.out.println(folder.report());
//	}
}
