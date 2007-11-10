package protex;

import java.awt.Dimension;

public class ShapeMatcher {
	
	private String targetString;
	private boolean strictMode;
	private Direction[] targetDirections;
	private GridPoint targetMin;
	private GridPoint targetMax;
	private int targetWidth;
	private int targetHeight;
	private GridPoint[] targetPoints;
	
	public ShapeMatcher(String targetString, boolean strictMode) {
		
		this.strictMode = strictMode;
		this.targetString = targetString;
		
		//parse the target string into a direction list
		String[] targetStringParts = targetString.split(";");
		targetDirections = new Direction[targetStringParts.length];
		for (int i = 0; i < targetStringParts.length; i++){
			targetDirections[i] = Direction.getDirection(targetStringParts[i]);
		}
		
		//make list of x,y coordinates as well as boundaries
		// (max x and y) (GridPoints) of the target shape
		// use this for comparison to rotamers of guess
		HexGrid targetGrid = ProteinImageFactory.layoutOntoHexGrid(targetDirections);
		AcidInChain[] targetAcids = targetGrid.getPP().getAcidArray();
		targetMax = targetGrid.getMax();
		targetMin = targetGrid.getMin();
		targetWidth = targetMax.x - targetMin.x;
		targetHeight = targetMax.y - targetMin.y;
		targetPoints = new GridPoint[targetAcids.length];
		for (int i = 0; i < targetPoints.length; i++) {
			targetPoints[i] = targetAcids[i].xyz;
		}
	}
	
	public boolean matchesTarget(String guess) {
		
		guess = guess.trim();
		// read in guess direction list
		String[] guessStrings = guess.split(";");
		Direction[] guessDirections = new Direction[guessStrings.length];
		for (int i = 0; i < guessStrings.length; i++){
			guessDirections[i] = Direction.getDirection(guessStrings[i]);
		}
		
		// if they're not the same # of aas, they can't be the same shape
		if (guessDirections.length != targetDirections.length) {
			return false;
		}
		
		if (strictMode) {
			for (int i = 0; i < guessDirections.length; i++) {
				if (guessDirections[i] != targetDirections[i]) {
					return false;
				}
			}
			return true;
			
		} else {
			
			// make and test rotamers
			for (int i = 0; i < 6; i++){
				if (isSameShape(guessDirections)) {
					return true;
				} else {
					// make next rotamer
					for (int j = 0; j < guessDirections.length; j++){
						guessDirections[j] = Direction.increment(guessDirections[j]);
					}
				}
			}
			
			return false;
		}
	}
	
	private boolean isSameShape(Direction[] guessDirections) {
		// get parameters of guess shape
		HexGrid guessGrid = ProteinImageFactory.layoutOntoHexGrid(guessDirections);
		AcidInChain[] guessAcids = guessGrid.getPP().getAcidArray();
		GridPoint guessMax = guessGrid.getMax();
		GridPoint guessMin = guessGrid.getMin();
		int guessWidth = guessMax.x - guessMin.x;
		int guessHeight = guessMax.y - guessMin.y;
		
		// if different size, it can't be the same protein
		if ((guessWidth != targetWidth) || (guessHeight != targetHeight)) {
			return false;
		}
		
		GridPoint[] guessPoints = new GridPoint[targetPoints.length];
		for (int i = 0; i < targetPoints.length; i++) {
			guessPoints[i] = guessAcids[i].xyz;
		}
		
		// same shape, now need to shift the guess in x & y to line up with
		//  target
		int xOffset = guessMin.x - targetMin.x;
		int yOffset = guessMin.y - targetMin.y;
		
		// compare each point in target with each shifted point in guess
		for (int i = 0; i < targetPoints.length; i++) {
			boolean foundAMatch = false;
			for (int j = 0; j < guessPoints.length; j++) {
				if (((guessPoints[j].x - xOffset) == targetPoints[i].x) &&
						(((guessPoints[j].y - yOffset) == targetPoints[i].y))) {
					foundAMatch = true;
					break;
				}
			}
			if (!foundAMatch) {
				return false;
			}
		}
		return true;
	}
	
	//method for command line ShapeMatching (for PI cellphone game)
	// args[0] = amino acid sequence of guess (single letter)
	// args[1] = target shape folding string
	//  returns folding string of guess and match/not
	public static void main(String[] args) {
		String guessAASeq = args[0];
		String targetFoldingString = args[1];
		
		//fold the guess sequence
		FoldingManager manager = FoldingManager.getInstance();
		Attributes attributes = new Attributes(guessAASeq.trim(), 
						1, "0.0", "straight", "test");
		OutputPalette outputPalette = new OutputPalette();
		try {
			manager.fold(attributes);
		} catch (FoldingException e) {
			e.printStackTrace();
		}	
		manager.createCanvas(outputPalette);
			outputPalette.getDrawingPane().getRequiredCanvasSize();
		outputPalette.setssBondsOn(false);

		String guessFoldingString = 
			outputPalette.getDrawingPane().getGrid().getPP().getDirectionSequence();
		
		System.out.println("guess=" + guessFoldingString);
		
		// see if it matches the target
		ShapeMatcher shapeMatcher = new ShapeMatcher(targetFoldingString, false);
		if (shapeMatcher.matchesTarget(guessFoldingString)) {
			System.out.println("match=Y");
		} else {
			System.out.println("match=N");
		}

	}
}
