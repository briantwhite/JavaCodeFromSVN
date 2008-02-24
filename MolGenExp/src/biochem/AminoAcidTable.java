// AminoAcidTable.java
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

package biochem;

import java.io.Serializable;
import java.util.Iterator;


/**
 * Model a table of AminoAcids. Serves too as a table factory.
 */
public abstract class AminoAcidTable implements Serializable {

	public static final String STANDARD = "standard";

	public static final String VIRTUAL = "virtual";

	private static StandardTable standardTable = null;

	private static VirtualTable virtualTable = null;

	/**
	 * The table factory.
	 * 
	 * @param tableName
	 *            the name of the table.
	 * 
	 * @return the table, null if no table with that name.
	 */
	public static AminoAcidTable makeTable(String tableName)
			throws FoldingException {
		if (tableName.equalsIgnoreCase("standard")) {
			if (standardTable == null) {
				standardTable = new StandardTable();
			}
			return standardTable;
		}
		if (tableName.equalsIgnoreCase("virtual")) {
			if (virtualTable == null) {
				virtualTable = new VirtualTable();
			}
			return virtualTable;
		}
		throw new FoldingException("no AminoAcid  named " + tableName);
	}

	/**
	 * Add an AminoAcid to the table, with its frequency.
	 * 
	 * @param a
	 *            the AminoAcid to add.
	 * @param probability
	 *            how likely is that AminoAcid?
	 * 
	 * @throws FoldingException
	 *             if operation not allowed.
	 */
	public abstract void add(AminoAcid a, double probability)
			throws FoldingException;

	/**
	 * Add an AminoAcid to the table.
	 * 
	 * @param a
	 *            the AminoAcid to add.
	 * 
	 * @throws FoldingException
	 *             if operation not allowed.
	 */
	public abstract void add(AminoAcid a) throws FoldingException;

	/**
	 * Choose a random sequence of AminoAcids from this table.
	 * 
	 * @param length
	 *            the length of the desired sequence.
	 * @param seed
	 *            a seed for the random number generator.
	 */
	public abstract AminoAcid[] getRandom(int length, int seed);

	//  public abstract AminoAcid[] getAllAcids();
	/**
	 * A bound on the absolute value of the energy of AminoAcids in the table.
	 * 
	 * @return the bound.
	 */
	public abstract double getMaxEnergy();

	/**
	 * Retrieve an acid from the table.
	 * 
	 * @param name
	 *            the name of the acid.
	 * 
	 * @return the acid, null if none.
	 */
	public abstract AminoAcid get(String name) throws FoldingException;
	
	public abstract AminoAcid getFromAbName(String name) throws FoldingException;

	/**
	 * A constant between 0 and 1 that leads to good contrast when normalized
	 * hydrophobic index determines color in a folding on a grid.
	 * 
	 * Default is 0.5;
	 * 
	 * @return the constant.
	 */
	public float getContrastScaler() {
		return (float) 0.5;
	}

	/**
	 * Enable iteration over the keys in the table.
	 * 
	 * @return an Iterator.
	 * 
	 * @throws FoldingException
	 *             if no Iterator available.
	 */
	public Iterator getIterator() throws FoldingException {
		throw new FoldingException("no iterator available");
	}

	/**
	 * Returns the name of this AminoAcidTable
	 * 
	 * @return a String
	 */
	public abstract String getName();

	/**
	 * Method main for unit testing
	 */
	public static void main(String[] args) {
		AminoAcidTable t = null;
		try {
			t = AminoAcidTable.makeTable("standard");
		} catch (FoldingException e) {
		}
		AminoAcid[] list = t.getRandom(15, 999);
		for (int i = 0; i < list.length; i++) {
			System.out.print(list[i] + " ");
		}
		System.out.println();
	}
}
