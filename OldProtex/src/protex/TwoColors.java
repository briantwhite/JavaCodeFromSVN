// TwoColors.java
//
//
// Copyright 2004-2005 MGX Team UMB.  All rights reserved.
/* 
 * License Information
 * 
 * This  program  is  free  software;  you can redistribute it and/or modify it 
 * under  the  terms  of  the GNU General Public License  as  published  by the 
 * Free  Software  Foundation; either version 2  of  the  License,  or (at your 
 * option) any later version.
 */

package protex;

import java.awt.Color;

/**
 * A class representing two colors to mix
 * 
 * @author Namita Singla
 */
public class TwoColors {

	private Color a;

	private Color b;

	private static final int HASH_PRIME = 1000003;

	/**
	 * Constructor
	 * 
	 * @param a
	 *            Color a
	 * @param b
	 *            Color b
	 */
	public TwoColors(Color a, Color b) {
		this.a = a;
		this.b = b;
	}

	/**
	 * Toggle colors
	 * 
	 * @return TwoColors
	 */
	public TwoColors toggleColors() {
		return new TwoColors(b, a);
	}

	/**
	 * Check if his TwoColors object has specific color
	 * 
	 * @param c
	 *            Color
	 * @return true if TwoColors object has specific color
	 */
	public boolean hasColor(Color c) {
		return (a.equals(c) || b.equals(c)) ? true : false;
	}

	/**
	 * String representation of TwoColors object
	 */
	public String toString() {
		return a.toString() + "\t" + b.toString();
	}

	/**
	 * Overriding default equals method
	 */
	public boolean equals(Object other) {
		if (other == this)
			return true;
		if (other == null)
			return false;
		if (getClass() != other.getClass())
			return false;
		TwoColors otherColor = (TwoColors) other;
		return ((a == otherColor.a || (a != null && a.equals(otherColor.a))) && (b == otherColor.b || (b != null && b
				.equals(otherColor.b))));
	}

	/**
	 * Overrides default hashCode method
	 */
	public int hashCode() {

		int result = 0;
		result = HASH_PRIME * result + a.hashCode();
		result = HASH_PRIME * result + b.hashCode();
		return result;

	}

}

