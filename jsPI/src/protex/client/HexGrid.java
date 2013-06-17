// HexGrid.java
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

package protex.client;

import java.util.HashMap;

/**
 * Model a grid of hexagons containing a polypeptide chain.
 * 
 * Use coordinate system to get hex neighbors:
 * 
 * 0-0 1-0 2-0 3-0 0-1 1-1 2-1 3-1 0-2 1-2 2-2 3-2
 */
public class HexGrid extends TwoDGrid {

	// tools for Directions
	public final Direction E = Direction.E;

	public final Direction SE = Direction.SE;

	public final Direction SW = Direction.SW;

	public final Direction W = Direction.W;

	public final Direction NW = Direction.NW;

	public final Direction NE = Direction.NE;

	public HexGrid(Polypeptide pp) {
		super(pp);
		allDirections = getAllDirections();
		setNextDirectionsStraight();
		setNextDirectionsBent();
		setNextDirections();

		// following should go in superclass constructor but fails there
		if (pp.isFolded()) {
			GridPoint currentP = getCenter();
			set(0, currentP, Direction.none);
			Direction d = pp.getNextDirection(0);
			for (int i = 1; i < numAcids; i++) {
				currentP = nextCell(d, currentP);
				set(i, currentP, d);
				d = pp.getNextDirection(i);
			}
		}

	}

	protected Direction[] getAllDirections() {
		Direction[] all = { Direction.E, Direction.NE, Direction.NW,
				Direction.W, Direction.SW, Direction.SE };
		return all;
	}

	public Direction[] getThirdPlacement() {
		Direction[] directions = { E, NE, NW };
		return directions;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (int row = 0; row < size; row++) {
			for (int indent = 0; indent < row; indent++) {
				buf.append("  ");
			}
			for (int col = 0; col < size; col++) {
				buf.append(cells[row][col] == null ? "---"
						: cells[row][col].name);
				buf.append(' ');
			}
			buf.append('\n');
		}
		return buf.toString();
	}

	protected Direction getDirection(int x1, int y1, int x2, int y2) {
		if (y1 == y2) {
			if (x1 + 1 == x2)
				return E;
			return W;
		}
		if (x1 == x2) {
			if (y1 + 1 == y2)
				return SE;
			return NW;
		}
		if (y1 + 1 == y2)
			return SW;
		return NE;
	}
	public static native void log(String msg)
	/*-{
		console.log(msg);
	}-*/;
	protected GridPoint nextCell(Direction direction, GridPoint p) {
		int x = p.x;
		int y = p.y;
		if (direction == E)
			return points[x + 1][y];
		if (direction == W)
			return points[x - 1][y];
		if (direction == SE)
			return points[x][y + 1];
		if (direction == NW)
			return points[x][y - 1];
		if (direction == SW)
			return points[x - 1][y + 1];
		if (direction == NE)
			return points[x + 1][y - 1];
		if (direction == Direction.none)
			return p;
		return null;
	}

	// coming from some Direction, try to continue in the same
	// Direction. Make counterclockwise turn if you must turn.

	protected Direction[] getDirectionRing3() {
		Direction[] ring = { E, NE, NW, W, SW, SE, E, NE, NW, W, SW, SE, E, NE,
				NW, W, SW, SE };
		return ring;
	}

	protected int getNumDirections() {
		return 6;
	}

	public void setNextDirectionsStraight() {
		straightMap = new HashMap();
		Direction[] nextE = { E, NE, SE, NW, SW };
		straightMap.put(E, nextE);
		Direction[] nextNE = { NE, NW, E, W, SE };
		straightMap.put(NE, nextNE);
		Direction[] nextNW = { NW, W, NE, SW, E };
		straightMap.put(NW, nextNW);
		Direction[] nextW = { W, SW, NW, SE, NE };
		straightMap.put(W, nextW);
		Direction[] nextSW = { SW, W, SE, NW, E };
		straightMap.put(SW, nextSW);
		Direction[] nextSE = { SE, SW, E, W, NE };
		straightMap.put(SE, nextSE);

		//  HERE WAS THE PROBLEM OF GETTING THE OBJECT REFERENCES
		//     PRINTED TO STDOUT. COMMENTED IT OUT.

		//System.out.println(straightMap);
	}
}

