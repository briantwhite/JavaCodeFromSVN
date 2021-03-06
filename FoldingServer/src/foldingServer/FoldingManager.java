// FoldingManager.java
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

package foldingServer;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Vector;

/**
 * Manages the process of folding the polypeptide chains; serves as a subject
 * for FoldingObservers. FoldingManager is a singleton class. Therefore, it has
 * just one instance; there is no public constructor. See getInstance() method.
 */
public class FoldingManager {

	// accessor methods

	/**
	 * 
	 * @return FoldingManager instance.
	 */
	public static FoldingManager getInstance() {
		if (instance == null)
			instance = new FoldingManager();
		return instance;
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

	/**
	 * 
	 * @return String.
	 */
	public String getEnergyString() {
		return String.valueOf(currentGrid.getEnergy(currentFolder.hpIndex,
				currentFolder.hIndex, currentFolder.iIndex, currentFolder.sIndex));
	}

	/**
	 * 
	 * @param pattern
	 *            String.
	 * @return String.
	 */
	public String getEnergyString(String pattern) {
		return getEnergy(pattern);
	}

	/**
	 * 
	 * @param pattern
	 *            String.
	 * @return String.
	 */
	public String getEnergy(String pattern) {
		DecimalFormat formatter = new java.text.DecimalFormat(pattern);
		return (formatter.format(getEnergy()));
	}

	/**
	 * 
	 * @return double.
	 */
	public double getEnergy() {
		return currentGrid.getEnergy(currentFolder.hpIndex,
				currentFolder.hIndex, currentFolder.iIndex, currentFolder.sIndex);
	}


	/**
	 * 
	 * @param pattern
	 *            String.
	 * @return String.
	 */
	public String getFoldingIndex(String pattern) {
		DecimalFormat formatter = new java.text.DecimalFormat(pattern);
		return (formatter.format(getFoldingIndex()));
	}

	/**
	 * 
	 * @return String.
	 */
	public String getFoldingIndexString() {
		return "" + getFoldingIndex();
	}

	/**
	 * 
	 * @return double.
	 */
	public double getFoldingIndex() {
		return currentGrid.getFoldingIndex(currentFolder.hpIndex,
				currentFolder.hIndex, currentFolder.iIndex, currentFolder.sIndex);
	}

	/**
	 * 
	 * @return String.
	 */
	public String getTopology() {
		return currentPP.getTopology();
	}

	/**
	 * 
	 * @return long.
	 */
	public long getTime() {
		if (isPlotting)
			return 0;
		return currentFolder.getTime();
	}

	/**
	 * 
	 * @return String.
	 */
	public String report() {
		if (isPlotting) {
			StringBuffer buf = new StringBuffer();
			buf.append("\n " + "Polypeptide:   " + getPolypeptide().toString());
			buf.append("\n " + "Energy:        " + getEnergy());
			buf.append("\n " + "Folding index: " + getFoldingIndex());
			return buf.toString();
		} else {
			return "\n" + currentFolder.report();
		}
	}
	
	public Grid getGrid() {
		return currentGrid;
	}

	/**
	 * 
	 * @return String.
	 */
	public String actionReport() {
		StringBuffer buf = new StringBuffer();
		if (isPlotting) {
			buf.append("Plotted Solution");
		} else {
			buf.append("\n");
			if (currentAttrib.getIsRandom()) {
				buf.append("Randomly generated with length:   ");
				buf.append(currentAttrib.getLength());
				buf.append("   and seed:  " + currentAttrib.getSeed());
			} else {
				buf.append(" Polypeptide was parsed from input");
			}
			buf.append("\n");
			if (currentAttrib.getFolder().equalsIgnoreCase("bruteForce")) {
				buf.append(" Brute Force Algorithm");
			} else {
				buf.append(" Incremental  ");
				buf.append("   look-ahead: " + currentAttrib.getLookup());
				buf.append("   step: " + currentAttrib.getStep());
			}
			buf.append("\n");
		}
		return buf.toString();
	}

	// mutator methods


	/**
	 * 
	 * @param id
	 *            int
	 */
	public void setLastPPId(int id) {
		this.lastPPId = id;
	}

	/**
	 * Assign a string ID (a "ppId") to a Polypeptide chain.
	 * 
	 * @param id
	 *            int
	 * @return String Polypeptide string ID
	 */
	public static String assignPPId(int id) {
		String stringId;

		if (id > 100)
			stringId = "PP" + new Integer(id).toString();
		else if (id > 10)
			stringId = "PP0" + new Integer(id).toString();
		else
			stringId = "PP00" + new Integer(id).toString();

		if (FoldingManager.getInstance().DEBUG) {
			System.out.println("\nHistoryCellRenderer: "
					+ "Value assigned, stringId = " + stringId);
		}

		return stringId;
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
	 * Folding happens here.
	 * 
	 * @param attrib
	 *            Attributes.
	 * @throws FoldingException
	 */
	public void fold(Attributes attrib) throws FoldingException {
		resetCurrent();
		currentAttrib = attrib;
		foldPP(attrib);
	}
	

	// non-public fields

	private int lastPPId;
	private static FoldingManager instance;
	private PolypeptideFactory factory;

	// buffers

	private Attributes currentAttrib;
	private Polypeptide currentPP;
	private Folder currentFolder;
	private Grid currentGrid;

	// flags

	private boolean isPlotting = false; // to report a folding or a plotting.
	protected boolean DEBUG = false; // flag for debug info

	/**
	 * Private constructor.
	 *  
	 */
	private FoldingManager() {
		factory = PolypeptideFactory.getInstance();
		resetCurrent(); // provides initialization
	}

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
	private void foldPP(Attributes attrib) 
		throws FoldingException {
		createPP(attrib);
		createGrid(attrib);
		createFolder(attrib);
		currentFolder.fold();
	}
	

	/**
	 * Create a polypeptide chain.  Maintain references to ppFromHistory.
	 * 
	 * @param attrib Attributes.
	 * @throws FoldingException.
	 */
	private void createPP(Attributes attrib) 
		throws FoldingException {
		try {
			currentPP = factory.createPolypeptide(
				attrib.getInputString(),
				attrib.getIsFolded(), 
				attrib.getIsRandom(), 
				attrib.getLength(), 
				attrib.getSeed(), 
				attrib.getTable(),
				attrib.getNumAALetterCode(),
				attrib.getPpId());
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
	private void createGrid(Attributes attrib) throws FoldingException {
		String grid = attrib.getGrid();
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
	private void createFolder(Attributes attrib) throws FoldingException {
		String folder = attrib.getFolder();
		if (folder.equalsIgnoreCase("bruteforce")) {
			currentFolder = new BruteForceFolder(currentPP, currentGrid);
		} else if (folder.equalsIgnoreCase("incremental")) {
			currentFolder = new IncrementalFolder(currentPP, currentGrid);
			String lookupString = attrib.getLookup();
			String stepString = attrib.getStep();
			String hpIndexString = attrib.getHydroPhobicIndex();
			String hIndexString = attrib.getHydrogenIndex();
			String iIndexString = attrib.getIonicIndex();
			String sIndexString = attrib.getssBondIndex();

			int lookup = 0;
			int step = 0;
			double hpIndex = 0;
			double hIndex = 0;
			double iIndex = 0;
			double sIndex = 0;
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
			try {
				sIndex = Double.parseDouble(sIndexString);
			} catch (NumberFormatException ex) {
				throw new FoldingException(
						"FolderCreation: ssBond Index: REQUIRED: double GIVEN: "
								+ sIndexString);
			}

			((IncrementalFolder) currentFolder).setLookAhead(lookup);
			((IncrementalFolder) currentFolder).setStep(step);
			currentFolder.setHydroPhobicIndex(hpIndex);
			currentFolder.setHydrogenIndex(hIndex);
			currentFolder.setIonicIndex(iIndex);
			currentFolder.setssBondIndex(sIndex);
		} else {
			throw new FoldingException(
					"Folder creation: REQUIRED: bruteforce OR incremental. GIVEN: "
							+ folder);
		}
	}
}
