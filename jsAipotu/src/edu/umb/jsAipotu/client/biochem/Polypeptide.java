// Polypeptide.java
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
 * Modified: 26 Apr Mar 2005 (D. A. Portman/MGX Team UMB)
 */

package edu.umb.jsAipotu.client.biochem;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.canvas.dom.client.CssColor;

import edu.umb.jsAipotu.client.preferences.GlobalDefaults;

/**
 * Model a polypeptide as a List of AminoAcids.
 *  
 */
public class Polypeptide {

	// public accessors

	public boolean isFolded() {
		return folded;
	}


	/**
	 * @return Returns the color.
	 */
	public CssColor getColor() {
		return color;
	}

	public int getLength() {
		return acids.size();
	}

	public Iterator<AcidInChain> iterator() {
		return acids.iterator();
	}

	public AcidInChain[] getAcidArray() {
		return acidArray;
	}

	public String getTopology() {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < numAcids; i++) {
			buf.append("\n" + i + ": " + acidArray[i].getNeighbors());
		}
		return buf.toString();
	}

	public Direction getNextDirection(int index) {
		return acidArray[index].next;
	}

	public AcidInChain getAminoAcid(int i) {
		return (AcidInChain) acids.get(i);
	}

	public String toString() {
		Iterator<AcidInChain> i = iterator();
		StringBuffer buf = new StringBuffer();
		while (i.hasNext()) {
			buf.append(i.next() + " : ");
		}
		return buf.toString();
	}
	
	public String getSingleLetterAASequence() {
		StringBuffer b = new StringBuffer();
		Iterator<AcidInChain> i = iterator();
		while (i.hasNext()) {
			b.append((i.next()).getAbName());
		}
		return b.toString();
	}
	
	// string representation for use in FoldedProteinArchive
	//   aa:direction:aa:direction
	public String getProteinString() {
		StringBuffer b = new StringBuffer();
		Iterator<AcidInChain> i = iterator();
		while (i.hasNext()) {
			AcidInChain a = i.next();
			b.append(a.getAbName() + ":");
			b.append(a.getNext() + ":");
		}
		return b.toString();
	}

	public String toCSV() {
		Iterator<AcidInChain> i = iterator();
		StringBuffer buf = new StringBuffer();
		while (i.hasNext()) {
			buf.append((i.next()).getHydrophobicIndex() + ", ");
		}
		return buf.toString();
	}

	/**
	 * 
	 * @return String report for debugging.
	 */
	public String toReport() {
		StringBuffer buf = new StringBuffer();
		buf.append("\n\tPolpeptide pp:         " + this.toString());
		buf.append("\n\tpp.getColor():         " + this.getColor());
		return buf.toString();
	}


	public double getMaxEnergy() {
		return maxEnergy;
	}

	public String getDirectionSequence() {
		Iterator<AcidInChain> i = iterator();
		StringBuffer buf = new StringBuffer();
		while (i.hasNext()) {
			buf.append((i.next()).getNext() + " : ");
		}
		return buf.toString();
	}

	// public mutators

	public void setFolded() {
		folded = true;
	}

	/**
	 * @param color
	 *            The color to set.
	 */
	public void setColor(CssColor color) {
		this.color = color;
	}


	/**
	 * Set the Direction of the next AminoAcid in the chain.
	 * 
	 * @param index
	 *            the index of an AminoAcid.
	 * @param d
	 *            the Direction of the following AminoAcid.
	 */
	public void setNext(int index, Direction d) {
		acidArray[index].setNext(d);
	}

	public void addNeighbor(AcidInChain to, AcidInChain from) {
		to.addNeighbor(wrappers[acids.indexOf(from)]);
	}

	public void clearTopology() {
		for (int i = 0; i < numAcids; i++) {
			acidArray[i].getNeighbors().clear();
		}
	}

	// non-public fields

	private int numAcids;

	private boolean folded = false;

	private CssColor color;

	private double maxEnergy = 1.0; // to scale histogram

	private ArrayList<AcidInChain> acids;

	// for efficiency, mirror the ArrayList as an array,
	// and mirror the next Directions (dynamic)
	private AcidInChain[] acidArray;

	private Integer[] wrappers;


	// non-public constructors

	/**
	 * Constructor: Creates a Polypeptide with random acids and with the
	 * prescribed seed from a table.
	 * 
	 * @param table
	 *            the name of the table.
	 * @param length
	 *            the length of the chain.
	 * @param seed
	 *            the seed.
	 * @param id
	 *            the assigned string id for this Polypeptide
	 */
	protected Polypeptide(int length, int seed) {

		// ASSUMES: length >0, seed >=0, table != null
		this(GlobalDefaults.aaTable.getRandom(length, seed));
	}

	/**
	 * Constructor: Creates a polypeptide from the acids with corresponding
	 * directions.
	 * 
	 * @param table
	 *            the name of the table
	 * @param realAcids
	 *            the array of amino acids
	 * @param directions
	 *            the array of directions
	 * @param id
	 *            the assigned string id for this Polypeptide
	 */
	protected Polypeptide(AminoAcid[] acids, Direction[] directions) {
		this(acids);
		setDirections(directions);
	}

	/**
	 * Constructor: Creates a polypeptide from the prescribed acids, from a
	 * table.
	 * 
	 * @param table
	 *            the name of the table
	 * @param realAcids
	 *            the array of amino acids
	 * @param id
	 *            the assigned string id for this Polypeptide
	 */
	protected Polypeptide(AminoAcid[] realAcids) {

		// ASSUMES: all acids in realAcids are from table.
		maxEnergy = GlobalDefaults.aaTable.getMaxEnergy();
		acids = new ArrayList<AcidInChain>(); // duplicated code
		for (int i = 0; i < realAcids.length; i++) {
			acids.add(new AcidInChain(realAcids[i], i));
		}

		// duplicated code below
		numAcids = acids.size();
		wrappers = new Integer[acids.size()];
		for (int i = 0; i < wrappers.length; i++) {
			wrappers[i] = new Integer(i);
		}
		acidArray = (AcidInChain[]) acids.toArray(new AcidInChain[0]);
	}

	// other non-public methods


	/*
	 * Private helper method that sets the direction for each acid.
	 * 
	 * @param directions
	 */
	private void setDirections(Direction[] directions) {
		for (int i = 0; i < directions.length; i++) {
			setNext(i, directions[i]);
		}
		folded = true;
	}
}
