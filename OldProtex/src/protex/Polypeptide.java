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

package protex;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

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
	 * 
	 * @return true if this Polypeptide is in HistoryList.
	 */
	public boolean isInHistory() {
		return inHistory;
	}

	/**
	 * 
	 * @return true if this Polypeptide is in UpperIOPanel.
	 */
	public boolean isInUpperIOPanel() {
		return inUpperIOPanel;
	}

	/**
	 * 
	 * @return true if this Polypeptide is in LowerIOPanel.
	 */
	public boolean isInLowerIOPanel() {
		return inLowerIOPanel;
	}

	/**
	 * 
	 * @param iOP
	 *            IOPanel the current (active) IOPanel
	 * @return true if this Polypeptide is also displayed in the other IOPanel.
	 */
	public boolean isInOtherIOPanel(IOPanel iOP) {
		if (iOP.equals(ProtexMainApp.upperIOPanel)) {
			if (this.isInLowerIOPanel())
				return true;
			else
				return false;
		} else if (iOP.equals(ProtexMainApp.lowerIOPanel)) {
			if (this.isInUpperIOPanel())
				return true;
			else
				return false;
		} else {
			System.out.println("\nPolypeptide.isInOtherIOPanel: "
					+ "ERROR - You should never get here.");
			return false;
		}
	}

	/**
	 * 
	 * @return ppId String.
	 */
	public String getId() {
		return stringId;
	}

	/**
	 * @return Returns the color.
	 */
	public Color getColor() {
		return color;
	}

	public int getLength() {
		return acids.size();
	}

	public Iterator iterator() {
		return acids.iterator();
	}

	public AminoAcidTable getTable() {
		return table;
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
		Iterator i = iterator();
		StringBuffer buf = new StringBuffer();
		while (i.hasNext()) {
			buf.append((AcidInChain) i.next() + " : ");
		}
		return buf.toString();
	}

	public String toCSV() {
		Iterator i = iterator();
		StringBuffer buf = new StringBuffer();
		while (i.hasNext()) {
			buf.append(((AcidInChain) i.next()).getHydrophobicIndex() + ", ");
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
		buf.append("\n\tpp.getId():            " + this.getId());
		buf.append("\n\tpp.getColor():         " + this.getColor());
		buf.append("\n\tpp.isInUpperIOPanel(): " + this.isInUpperIOPanel());
		buf.append("\n\tpp.isInHistory():      " + this.isInHistory());
		buf.append("\n\tpp.isInLowerIOPanel(): " + this.isInLowerIOPanel());
		for (int i = 0; i < 3; i++) {
			buf.append("\n\tpp.UHL[" + i + "] = " + UHL[i]);
		}
		return buf.toString();
	}

	/**
	 * @return Return the uHL.
	 */
	public Polypeptide[] getUHL() {
		return UHL;
	}

	public double getMaxEnergy() {
		return maxEnergy;
	}

	public String getDirectionSequence() {
		Iterator i = iterator();
		StringBuffer buf = new StringBuffer();
		while (i.hasNext()) {
			buf.append(((AcidInChain) i.next()).getNext() + " : ");
		}
		return buf.toString();
	}

	// public mutators

	public void setFolded() {
		folded = true;
	}

	/**
	 * 
	 * @param b
	 *            Boolean.
	 */
	public void setInHistoryTo(boolean b) {
		inHistory = b;
	}

	/**
	 * 
	 * @param b
	 *            Boolean.
	 */
	public void setInUpperIOPanelTo(boolean b) {
		inUpperIOPanel = b;
	}

	/**
	 * 
	 * @param b
	 *            Boolean.
	 */
	public void setInLowerIOPanelTo(boolean b) {
		inLowerIOPanel = b;
	}

	/**
	 * 
	 * @param String
	 *            ppId.
	 */
	public void setId(String ppId) {
		stringId = ppId;
	}

	/**
	 * @param color
	 *            The color to set.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Set references for one Polypeptide. This method is overloads 
	 * the public method setUHL(Polypeptide). 
	 * 
	 * @param pp 
	 * 			Polypeptide[] vector holding three Polypeptide objects.
	 */
	public void setUHL(Polypeptide[] UHL) {
		Polypeptide ppFromHistory = UHL[1];
		setUHL(ppFromHistory);
	}

	/**
	 * Set references for one Polypeptide. A Polypeptide needs to know its
	 * locations: Upper work panel, History and/or Lower work panel.  This
	 * method overloads the private method 
	 * 		setUHL(Polypeptide, Polypeptide Polypeptide)
	 * 
	 * @param pp 
	 * 			Polypeptide.
	 */
	public void setUHL(Polypeptide ppFromHistory) {
		Polypeptide ppInOtherPanel;
		IOPanel activeIOPanel = ProtexMainApp.activeIOPanel;

		// if the Upper work panel is active, assign references to
		//	Polypeptides in History and in Lower work panel
		if (activeIOPanel.equals(ProtexMainApp.upperIOPanel)) {
			ppInOtherPanel = ppFromHistory.getUHL()[2];
			this.setUHL(this, ppFromHistory, ppInOtherPanel);
			ppFromHistory.setUHL(this, ppFromHistory, ppInOtherPanel);
		}

		// otherwise, Lower work panel is active; assign references to
		//	Polypeptides in History and Upper work panel
		else if (activeIOPanel.equals(ProtexMainApp.lowerIOPanel)) {
			ppInOtherPanel = ppFromHistory.getUHL()[0];
			this.setUHL(ppInOtherPanel, ppFromHistory, this);
			ppFromHistory.setUHL(ppInOtherPanel, ppFromHistory, this);
		}

		else {
			System.out.println("\nPolypeptide.setUHL(): "
					+ "ERROR - You should never get here.");
		}
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

	private int id, numAcids;

	private boolean folded = false;

	private boolean inHistory = false;

	private boolean inUpperIOPanel;

	private boolean inLowerIOPanel;

	private Color color;

	private String stringId = null;

	private IOPanel iOP = null;

	private double maxEnergy = 1.0; // to scale histogram

	private AminoAcidTable table;

	private ArrayList acids;

	// for efficiency, mirror the ArrayList as an array,
	// and mirror the next Directions (dynamic)
	private AcidInChain[] acidArray;

	private Integer[] wrappers;

	private Polypeptide ppFromHistory;
	
	private Polypeptide[] UHL = { null, null, null };

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
	protected Polypeptide(AminoAcidTable table, int length, int seed, String id) {

		// ASSUMES: length >0, seed >=0, table != null
		this(table, table.getRandom(length, seed), id);
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
	protected Polypeptide(AminoAcidTable table, AminoAcid[] acids,
			Direction[] directions, String id) {
		this(table, acids, id);
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
	protected Polypeptide(AminoAcidTable table, AminoAcid[] realAcids, String id) {
		this.stringId = id;

		// ASSUMES: all acids in realAcids are from table.
		this.table = table;
		maxEnergy = table.getMaxEnergy();
		acids = new ArrayList(); // duplicated code
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
	 * Set references to Polypeptides in Upper work Panel, History and Lower
	 * work panel. This array ensures that all related Polypeptides are always
	 * accessible to one another.
	 * 
	 * NOTE: This method is overloaded by the public method setUHL().
	 * 
	 * @param ppInUpperPanel Polypeptide in upper work panel. @param ppInHistory
	 * Polypeptide in history. @param ppInLowerPanel Polypeptide in lower work
	 * panel.
	 */
	protected void setUHL(Polypeptide ppInUpperPanel, Polypeptide ppInHistory,
			Polypeptide ppInLowerPanel) {
		UHL[0] = ppInUpperPanel;
		UHL[1] = ppInHistory;
		UHL[2] = ppInLowerPanel;

		if (ppInUpperPanel != null) {
			ppInUpperPanel.setInUpperIOPanelTo(true);
			if (ppInHistory != null)
				ppInHistory.setInUpperIOPanelTo(true);
			if (ppInLowerPanel != null)
				ppInLowerPanel.setInUpperIOPanelTo(true);
		}

		if (ppInHistory != null) {
			if (ppInUpperPanel != null)
				ppInUpperPanel.setInHistoryTo(true);
			ppInHistory.setInHistoryTo(true);
			if (ppInLowerPanel != null)
				ppInLowerPanel.setInHistoryTo(true);
		}

		if (ppInLowerPanel != null) {
			if (ppInUpperPanel != null)
				ppInUpperPanel.setInLowerIOPanelTo(true);
			if (ppInHistory != null)
				ppInHistory.setInLowerIOPanelTo(true);
			ppInLowerPanel.setInLowerIOPanelTo(true);
		}
	}

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
