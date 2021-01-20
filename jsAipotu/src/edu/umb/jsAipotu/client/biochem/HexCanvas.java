// HexCanvas.java
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

import edu.umb.jsAipotu.client.preferences.GlobalDefaults;



/**
 * Display a Grid of hexagons.
 * 
 * Use coordinate system to get hex neighbors:
 * 
 * 0-0 1-0 2-0 3-0 0-1 1-1 2-1 3-1 0-2 1-2 2-2 3-2
 */
public class HexCanvas extends GridCanvas {

	private int cellHeight = (int) (GlobalDefaults.aaRadius * Math.sqrt(3));

	public HexCanvas(int width, int height) {
		super(width, height);
	}

	public HexCanvas() {
		super();
	}

	public void setGrid(Grid grid) {
		super.setGrid(grid);
	}

	protected GridPoint project(GridPoint p) {
		GridPoint spot = new GridPoint((1 + 2 * p.x + p.y) * GlobalDefaults.aaRadius, 
				p.y * cellHeight + GlobalDefaults.aaRadius);
		return spot;
	}
}

