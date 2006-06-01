// GridPoint.java
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

package protex;

/**
 * Model a point in three dimensional space.
 */
public class GridPoint {
	public int x;

	public int y;

	public int z;

	public GridPoint(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public GridPoint(int x, int y) {
		this(x, y, 0);
	}

	public void translate(int deltaX, int deltaY) {
		translate(deltaX, deltaY, 0);
	}

	public void translate(int deltaX, int deltaY, int deltaZ) {
		x += deltaX;
		y += deltaY;
		z += deltaZ;
	}

	/**
	 * Add a GridPoint to this GridPoint. <br>
	 * (In C++ this would be done by overloading +)
	 * 
	 * @param p
	 *            the GridPoint to add.
	 * 
	 * @return the sum.
	 */
	public GridPoint add(GridPoint p) {
		return new GridPoint(x + p.x, y + p.y, z + p.z);
	}

	/**
	 * Subtract a GridPoint from this GridPoint. <br>
	 * (In C++ this would be done by overloading -)
	 * 
	 * @param p
	 *            the GridPoint to subtract.
	 * 
	 * @return the difference.
	 */
	public GridPoint subtract(GridPoint p) {
		return new GridPoint(x - p.x, y - p.y, z - p.z);
	}

	public String toString() {
		return "[" + x + "," + y + "," + z + "]";
	}

}
