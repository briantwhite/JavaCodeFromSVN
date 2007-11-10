// AcidInChain.java
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
 * Modified: 02 Mar 2005 (D. A. Portman/MGX Team UMB)
 */

package protex;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

/**
 * An AcidInChain refers to an AminoAcid and has fields and methods to manage
 * the placement of that AminoAcid in a Polypeptide on a Grid.
 */
public class AcidInChain implements Serializable {

	/**
	 * Constructor.
	 * 
	 * @param a
	 * @param index
	 */
	public AcidInChain(AminoAcid a, int index) {
		this.a = a;
		next = Direction.none;
		neighbors = new TreeSet();
		this.index = index;
		name = a.getName();
		abName = a.getAbName();
		hydrophobicIndex = a.hydrophobicIndex;

	}

//	/**
//	 * Constructor.
//	 * 
//	 * @param hydroPhobicIndex
//	 * @param hydrogenbondIndex
//	 * @param ionicIndex
//	 * @param index
//	 */
//	public AcidInChain(double hydroPhobicIndex, double hydrogenbondIndex,
//			double ionicIndex, int index) {
//		this(new AminoAcid(hydroPhobicIndex, hydrogenbondIndex, ionicIndex),
//				index);
//	}

	// accessor methods

	public AminoAcid getAminoAcid() {
		return a;
	}

	public double getHydrophobicIndex() {
		return a.getHydrophobicIndex();
	}

	public int gethydrogenbondIndex() {
		return a.gethydrogenbondIndex();
	}

	public int getionicIndex() {
		return a.getionicIndex();
	}
	
	public boolean getssIndex() {
		return a.getssIndex();
	}

	public float getNormalizedHydrophobicIndex() {
		return a.getNormalizedHydrophobicIndex();
	}

	public String getName() {
		return a.getName();
	}

	public String getAbName() {
		return a.getAbName();
	}
	
	public int getIndex() {
		return index;
	}

	public Set getNeighbors() {
		return neighbors;
	}

	public GridPoint getPoint() {
		return xyz;
	}

	public Direction getNext() {
		return next;
	}

	public String toString() {
		return a.toString() + " : " + next;
	}

	// mutator methods

	/**
	 * Indicate that in a folded Polypeptide this acid is a neighbor of another.
	 * Don't record the obvious cases: every acid is a neighbor of its successor
	 * and its predecessor.
	 * 
	 * @param neighbor
	 *            an Integer wrapping the index of the neihboring acid.
	 */

	public void addNeighbor(Integer neighbor) {
		int i = neighbor.intValue();
		if ((i != index - 1) && (i != index + 1)) {
			neighbors.add(neighbor);
		}
	}

	public void setNext(Direction next) {
		this.next = next;
	}

	// non-public fields
	// These are protected so that some algorithms can get to them
	// without the overhead of a method call. They have some getters
	// and setters too, for normal use.

	protected Direction next; // Direction to next AminoAcid

	protected GridPoint xyz; // coordinates on Grid

	private int index; // location in Polypeptide

	protected AminoAcid a;

	protected String name;
	
	protected String abName;

	protected double hydrophobicIndex;

	private TreeSet neighbors; // AminoAcids touching this one

}
