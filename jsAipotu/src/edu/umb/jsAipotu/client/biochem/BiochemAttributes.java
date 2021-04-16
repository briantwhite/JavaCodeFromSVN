// Attributes.java
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

package edu.umb.jsAipotu.client.biochem;


/**
 * Class Attributes contains the attributes needed to fold or plot a
 * polypeptide. 
 */
public class BiochemAttributes {

	/**
	 * A complete default constructor.
	 *  
	 */
	public BiochemAttributes() {
		this("straight");
	}

	/**
	 * Constructor implementing default values set by customer (and useful for
	 * debugging).
	 * 
	 * @param inputString
	 *            String. Polypeptide chain (amino acids).
	 * @param breakTies
	 *            String.
	 */
	public BiochemAttributes(String breakTies) {
		
		this(breakTies, "hexagonal", "incremental",
				"8", "4", "0.1", "0.5", "1.0");
	}

	/**
	 * Constructor. 
	 * 
	 * @param inputString
	 *            String. Polypeptide chain (a list of amino acids separated by
	 *            ":").
	 * @param table
	 *            String.
	 * @param breakTies
	 *            String.
	 * @param grid
	 *            String.
	 * @param folder
	 *            String.
	 * @param lookup
	 *            String.
	 * @param step
	 *            String.
	 * @param hpindx
	 *            String.
	 * @param h2indx
	 *            String.
	 * @param ionindx
	 *            String.
	 * @param ppId
	 *            String.
	 */
	public BiochemAttributes(String breakTies,
			String grid, 
			String folder, 
			String lookup, 
			String step,
			String hpindx, 
			String h2indx, 
			String ionindx) {
		
		this.breakTies = breakTies;
		this.grid = grid;
		this.folder = folder;
		this.lookup = lookup;
		this.step = step;
		this.hydroPhobicIndex = hpindx;
		this.hydrogenIndex = h2indx;
		this.ionicIndex = ionindx;
	}

	// accessor methods

	public boolean getIsFolded() {
		return isFolded;
	}

	public boolean getIsRandom() {
		return isRandom;
	}

	public String getLength() {
		return length;
	}

	public String getSeed() {
		return seed;
	}

	public String getGrid() {
		return grid;
	}

	public String getBreakTies() {
		return breakTies;
	}

	public String getFolder() {
		return folder;
	}

	public String getLookup() {
		return lookup;
	}

	public String getStep() {
		return step;
	}

	public String getHydroPhobicIndex() {
		return hydroPhobicIndex;
	}

	public String getHydrogenIndex() {
		return hydrogenIndex;
	}

	public String getIonicIndex() {
		return ionicIndex;
	}

	// mutator methods

	public void setIsFolded(boolean b) {
		isFolded = b;
	}

	public void setIsRandom(boolean b) {
		isRandom = b;
	}
	
	public void setLength(String s) {
		length = s;
	}

	public void setSeed(String s) {
		seed = s;
	}

	/**
	 * Save the current state as of this object (Attributes).
	 * 
	 * @param filename
	 *            String.
	 */
	public void saveState(String filename) {
		// define a new output stream.
		// print the following text Strings to output.
		//	pp chain consisting of amino acids: String
		//  breakTies: String
		//	hydrophobic index value: String.
		//	hydrogen index value: String.
		//	ionic index value: String.
	}

	/**
	 * Load an existing state as defined by an Attribute object.
	 * 
	 * @param filename
	 *            String filename holding the existing state parameters.
	 * @return Attributes object.
	 */
	public BiochemAttributes loadState(String filename) {
		return new BiochemAttributes();
	}

	private boolean isFolded = false;
	private boolean isRandom = false;

	private String length = "";
	private String seed = "";
	private String grid = "";
	private String breakTies = "";
	private String folder = "";
	private String lookup = "";
	private String step = "";
	private String hydroPhobicIndex = "";
	private String hydrogenIndex = "";
	private String ionicIndex = "";
}
