// Direction.java
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

package edu.umb.jsAipotu.client.biochem;

import java.io.Serializable;

/**
 * Model a direction on a grid.
 */
public class Direction implements Serializable {
	// for the hexagonal grid
	public static final Direction E = new Direction(" E", 1);

	public static final Direction SE = new Direction("SE", 2);

	public static final Direction SW = new Direction("SW", 3);

	public static final Direction W = new Direction(" W", -1);

	public static final Direction NW = new Direction("NW", -2);

	public static final Direction NE = new Direction("NE", -3);

	public static final Direction none = new Direction("none", 0);

	// two extra directions for the square grid
	public static final Direction N = new Direction(" N", 8);

	public static final Direction S = new Direction(" S", -8);

	// two more for the cubic grid - up and down
	public static final Direction U = new Direction(" U", 10);

	public static final Direction D = new Direction(" D", -10);

	// for the rhombic dodecahedral grid ...
	public static final Direction NU = new Direction("NU", -22);

	public static final Direction ND = new Direction("ND", -24);

	public static final Direction SU = new Direction("SU", -26);

	public static final Direction SD = new Direction("SD", -28);

	public static final Direction EU = new Direction("EU", -30);

	public static final Direction ED = new Direction("ED", -32);

	public static final Direction WU = new Direction("WU", -34);

	public static final Direction WD = new Direction("WD", -36);

	private int index;

	private String name;

	private Direction(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public String toString() {
		return name;
	}

	public static Direction getDirection(String s) {
		s = s.trim();
		if (s.equals("E"))
			return E;
		if (s.equals("NE"))
			return NE;
		if (s.equals("NW"))
			return NW;
		if (s.equals("W"))
			return W;
		if (s.equals("SW"))
			return SW;
		if (s.equals("SE"))
			return SE;
		if (s.equals("N"))
			return N;
		if (s.equals("S"))
			return S;
		if (s.equals("U"))
			return U;
		if (s.equals("D"))
			return D;
		return Direction.none;
	}

        /*
   	 * Method main for unit testing.
	 *
	 */
	public static void main(String[] args) {
		System.out.println(Direction.getDirection(args[0]));
	}
}
