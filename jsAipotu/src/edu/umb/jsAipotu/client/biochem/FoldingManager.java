//FoldingManager.java


//Copyright 2004, Ethan Bolker and Bogdan Calota
/* 
 * License Information
 * 
 * This  program  is  free  software;  you can redistribute it and/or modify it 
 * under  the  terms  of  the GNU General Public License  as  published  by the 
 * Free  Software  Foundation; either version 2  of  the  License,  or (at your 
 * option) any later version.
 */
/*
 * The following organization of a public class is recommended by X. Jia [2004: 
 * Object Oriented Software Development Using Java(TM). Addison Wesley, Boston, 
 * 677 pp.]
 *
 *     public class AClass {
 *         (public constants)
 *         (public constructors)
 *         (public accessors)
 *         (public mutators)
 *         (nonpublic fields)
 *         (nonpublic auxiliary methods or nested classes)
 *     }
 *
 * Jia also recommends the following design guidelines.
 *
 *     1. Avoid public fields.  There should be no nonfinal public fields, 
 *        except when a class is final and the field is unconstrained.
 *     2. Ensure completeness of the public interface.  The set of public 
 *        methods defined in the class should provide full and convenient 
 *        access to the functionality of the class.
 *     3. Separate interface from implementation.  When the functionality 
 *        supported by a class can be implemented in different ways, it is 
 *        advisable to separate the interface from the implementation.
 * 
 * Modified:  10 May 2005 (D. A. Portman/MGX Team UMB)
 */

package edu.umb.jsAipotu.client.biochem;


import java.util.ArrayList;

import com.google.gwt.canvas.dom.client.CssColor;

import edu.umb.jsAipotu.client.JsAipotu;

/**
 * Manages the process of folding the polypeptide chains; serves as a subject
 * for FoldingObservers. FoldingManager was originally a singleton class. 
 * BW in 2008, when making Aipotu needed to make it a regular class
 * in order to accommodate multiple simultaneous folding threads.
 */
public class FoldingManager {

	private BiochemAttributes attributes;

	/**
	 *  constructor.
	 *  
	 */
	public FoldingManager() {
		factory = PolypeptideFactory.getInstance();
		resetCurrent(); // provides initialization
		attributes = new BiochemAttributes();
	}

	/**
	 * 
	 * @return boolean.
	 */
	public boolean getBlackColoring() {
		return this.blackColoring;
	}

	/**
	 * 
	 * @return Polypeptide
	 */
	public Polypeptide getPolypeptide() {
		return currentPP;
	}

	/**
	 * 
	 * @return int
	 */
	public int getLastPPId() {
		return lastPPId;
	}


	public Grid getGrid() {
		return currentGrid;
	}

	/**
	 * 
	 * @return CssColor.
	 * @throws PaintedInACornerFoldingException 
	 */
	public CssColor getProteinColor() throws PaintedInACornerFoldingException {
		return currentGrid.getProteinColor();
	}

	/**
	 * 
	 * @return String.
	 * @throws PaintedInACornerFoldingException 
	 */
	public String getFoldingIndexString() throws PaintedInACornerFoldingException {
		return "" + getFoldingIndex();
	}

	/**
	 * 
	 * @return double.
	 * @throws PaintedInACornerFoldingException 
	 */
	public double getFoldingIndex() throws PaintedInACornerFoldingException {
		return currentGrid.getFoldingIndex(currentFolder.hpIndex,
				currentFolder.hIndex, currentFolder.iIndex);
	}

	/**
	 * 
	 * @return String.
	 */
	public String getTopology() {
		return currentPP.getTopology();
	}


	// mutator methods

	/**
	 * 
	 * @param flag
	 *            boolean.
	 */
	public void setBlackColoring(boolean flag) {
		this.blackColoring = flag;
	}

	/**
	 * 
	 * @param id
	 *            int
	 */
	public void setLastPPId(int id) {
		this.lastPPId = id;
	}

	/**
	 * 
	 * @param flag
	 *            boolean.
	 */
	public void setDEBUG(boolean flag) {
		this.DEBUG = flag;
	}

	// other methods

	/**
	 * folding for evolution 
	 * 	- just folds it and returns shape sequence and color
	 * 	- no images of the protein
	 */
	public FoldedAndColoredProtein foldAndColor(String sequence) {
		String aaSeq = parseAASeq(sequence);
		
		//see if it's in the archive
		FoldedProteinArchive foldedProteinArchive = 
			FoldedProteinArchive.getInstance();
		if (foldedProteinArchive.isInArchive(aaSeq)) {
			return foldedProteinArchive.getEntry(aaSeq);
		}
		
		// since it isn't, fold it the regular way
		resetCurrent();
		currentAttrib = attributes;
		try {
			foldPP(aaSeq);
			
			// add it to archive
			foldedProteinArchive.add(
					aaSeq, 
					currentGrid.getPP().getProteinString(), 
					currentGrid.getProteinColor());
			
			return new FoldedAndColoredProtein(
					currentGrid.getPP().getProteinString(), 
					currentGrid.getProteinColor());
			
		} catch (FoldingException e) {
			// folded in a corner, so return a null fold and color
			foldedProteinArchive.add(aaSeq, null, null);
			return new FoldedAndColoredProtein(null, null);
		}
	}
	
	/**
	 * folding for biochem
	 * 	- creates both big and small images
	 *  - must specify how big the big image needs to be
	 */
	public FoldedProteinWithImages foldWithPix(String sequence) {
		// whatever form it's in, make it single letter code
		String aaSeq = parseAASeq(sequence);
		
		// see if it's in the archive
		FoldedProteinArchive foldedProteinArchive = 
			FoldedProteinArchive.getInstance();
		
		if (foldedProteinArchive.isInArchive(aaSeq)) {
			// need to create pix, but don't have to actually fold it
			
			// first, place the amino acids according to the proteinString 
			//   (list of directions)
			HexCanvas hexCanvas = new HexCanvas();
			FoldedAndColoredProtein foldedAndColoredProtein = 
				foldedProteinArchive.getEntry(aaSeq);

			Polypeptide polypeptide;
			try {
				polypeptide = PolypeptideFactory.getInstance().createFromProteinString(
						foldedAndColoredProtein.getProteinString());

				hexCanvas.setGrid(new HexGrid(polypeptide));
			} catch (FoldingException e1) {
				return null;
			}

			//now, make the pix
			ProteinImageSet imageSet = 
				ProteinImageFactory.generateImages(hexCanvas);
			
			return new FoldedProteinWithImages(
					aaSeq,
					imageSet.getFullScaleImage(),
					imageSet.getThumbnailImage(),
					foldedAndColoredProtein.getColor());
		}
		
		// since it isn't, fold it the regular way
		resetCurrent();
		currentAttrib = attributes;
		try {
			foldPP(aaSeq);
			
			// add it to archive
			foldedProteinArchive.add(
					aaSeq, 
					currentGrid.getPP().getProteinString(), 
					currentGrid.getProteinColor());

			// make the pix
			HexCanvas hexCanvas = new HexCanvas();
			hexCanvas.setGrid(currentGrid);
			ProteinImageSet imageSet = 
				ProteinImageFactory.generateImages(hexCanvas);		
			
			return new FoldedProteinWithImages(
					aaSeq,
					imageSet.getFullScaleImage(),
					imageSet.getThumbnailImage(),
					currentGrid.getProteinColor());
		} catch (FoldingException e) {
			// folded in a corner, so return a null images and color
			return new FoldedProteinWithImages(aaSeq, null, null, null);
		}

	}

	public String parseAASeq(String sequence) {
		//get the aa seq as a single letter string with no separators
		AminoAcid[] acids;
		try {
			acids = factory.parseInputStringToAmAcArray(sequence);
		} catch (FoldingException e) {
			return null;
		}
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < acids.length; i++) {
			buf.append(acids[i].getAbName());
		}
		return buf.toString();
	}
	
	private Direction[] directionStringToDirectionArray(String directionString) {
		//if there are colons, its aa:direction;aa:direction; etc
		// if not, it's just directions
		ArrayList<Direction> directionList = new ArrayList<Direction>();
		String[] parts = directionString.split(";");
		if (directionString.indexOf(":") != -1) {
			for (int i = 0; i < parts.length; i++) {
				String[] pieces = parts[i].split(":");
				directionList.add(Direction.getDirection(pieces[1]));
			}
		} else {
			for (int i = 0; i < parts.length; i++) {
				directionList.add(Direction.getDirection(parts[i]));
			}
		}
		Direction[] directionArray = new Direction[directionList.size()];
		for (int i = 0; i < directionList.size(); i++) {
			directionArray[i] = (Direction)directionList.get(i);
		}
		
		return directionArray;
	}


	/**
	 * 
	 * @param width
	 *            int.
	 * @param height
	 *            int.
	 * @return GridCanvas
	 */
	public GridCanvas createCanvas(int width, int height) {
		GridCanvas canvas;
		String grid = currentAttrib.getGrid();

		if (grid.equalsIgnoreCase("hexagonal")) {
			canvas = new HexCanvas();
		} else { // should not get here
			canvas = null;
			System.out.print("\nFoldingManager.createCanvas(): ");
			System.out
			.println("Could not create canvas. Grid argument failed.");
		}

		canvas.setGrid(currentGrid);
		return canvas;
	}

	/**
	 * Another createCanvas method.
	 * 
	 * @param outputPanel
	 *            OutputPalette
	 */
//	public void createCanvas(OutputPalette outputPanel) {
//		outputPanel.getDrawingPane().repaint();
//		outputPanel.getDrawingPane().setGrid(currentGrid);
//	}

	// non-public fields

	private int lastPPId;
	private PolypeptideFactory factory;

	// buffers

	private BiochemAttributes currentAttrib;
	private Polypeptide currentPP;
	private Folder currentFolder;
	private Grid currentGrid;
	private TwoDGrid hexagonalCore;

	// flags

	private boolean isPlotting = false; // to report a folding or a plotting.
	private boolean blackColoring = false; // flag for canvas
	protected boolean DEBUG = false; // flag for debug info


	/**
	 * Initialize or resets current state for each folding.
	 *  
	 */
	private void resetCurrent() {
		isPlotting = false;
		currentAttrib = null;
		currentPP = null;
		currentFolder = null;
		currentGrid = null;
	}

	/**
	 * Fold a single polypeptide chain.
	 * 
	 * @param attrib
	 *            Attributes object.
	 * @throws FoldingException
	 */
	private void foldPP(String aaSeq) 
	throws FoldingException {
		createPP(aaSeq);
		createGrid();
		createFolder();
		currentFolder.fold();
	}


	/**
	 * Create a polypeptide chain.  Maintain references to ppFromHistory.
	 * 
	 * @param attrib Attributes.
	 * @throws FoldingException.
	 */
	private void createPP(String aaSeq) 
	throws FoldingException {
		try {
			currentPP = factory.createPolypeptide(
					aaSeq,
					attributes.getIsFolded(), 
					attributes.getIsRandom(), 
					attributes.getLength(), 
					attributes.getSeed());
		} 
		catch (FoldingException ex) {
			throw new FoldingException("Polypeptide Creation: "
					+ ex.getMessage());
		}
	}

	/**
	 * Create a Grid.
	 * 
	 * @param attrib
	 *            Attributes object.
	 * @throws FoldingException
	 */
	private void createGrid() throws FoldingException {
		String grid = attributes.getGrid();
		if (grid.equalsIgnoreCase("hexagonal")) {
			currentGrid = new HexGrid(currentPP);
		} else {
			throw new FoldingException("FoldingManager.createGrid(): "
					+ "REQUIRED: hexagonal. GIVEN: " + grid);
		}
	}

	/**
	 * 
	 * @param attrib
	 *            Attributes object.
	 * @throws FoldingException
	 */
	private void createFolder() throws FoldingException {
		String folder = attributes.getFolder();
		if (folder.equalsIgnoreCase("bruteforce")) {
			currentFolder = new BruteForceFolder(currentPP, currentGrid);
		} else if (folder.equalsIgnoreCase("incremental")) {
			currentFolder = new IncrementalFolder(currentPP, currentGrid);
			String lookupString = attributes.getLookup();
			String stepString = attributes.getStep();
			String hpIndexString = attributes.getHydroPhobicIndex();
			String hIndexString = attributes.getHydrogenIndex();
			String iIndexString = attributes.getIonicIndex();
			int lookup = 0;
			int step = 0;
			double hpIndex = 0;
			double hIndex = 0;
			double iIndex = 0;
			try {
				lookup = Integer.parseInt(lookupString);
			} catch (NumberFormatException ex) {
				throw new FoldingException(
						"FolderCreation: look ahead: REQUIRED: integer GIVEN: "
						+ lookupString);
			}
			if (lookup <= 0)
				throw new FoldingException(
						"FolderCreation: look ahead: REQUIRED: positive no GIVEN: "
						+ lookup);

			try {
				step = Integer.parseInt(stepString);
			} catch (NumberFormatException ex) {
				throw new FoldingException(
						"FolderCreation: step: REQUIRED: integer GIVEN: "
						+ stepString);
			}
			if (step <= 0)
				throw new FoldingException(
						"FolderCreation: step: REQUIRED: positive no GIVEN: "
						+ step);
			try {
				hpIndex = Double.parseDouble(hpIndexString);
			} catch (NumberFormatException ex) {
				throw new FoldingException(
						"FolderCreation: HydroutputPanelhobic Index: REQUIRED: double GIVEN: "
						+ hpIndexString);
			}
			try {
				hIndex = Double.parseDouble(hIndexString);
			} catch (NumberFormatException ex) {
				throw new FoldingException(
						"FolderCreation: HydrogenBond Index: REQUIRED: double GIVEN: "
						+ hIndexString);
			}
			try {
				iIndex = Double.parseDouble(iIndexString);
			} catch (NumberFormatException ex) {
				throw new FoldingException(
						"FolderCreation: Ionic Index: REQUIRED: double GIVEN: "
						+ iIndexString);
			}
			((IncrementalFolder) currentFolder).setLookAhead(lookup);
			((IncrementalFolder) currentFolder).setStep(step);
			currentFolder.setHydroPhobicIndex(hpIndex);
			currentFolder.setHydrogenIndex(hIndex);
			currentFolder.setIonicIndex(iIndex);
		} else {
			throw new FoldingException(
					"Folder creation: REQUIRED: bruteforce OR incremental. GIVEN: "
					+ folder);
		}
	}
}
