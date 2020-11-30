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

package edu.umb.jsAipotu.biochem;

import java.awt.Polygon;

import edu.umb.jsAipotu.preferences.GlobalDefaults;



/**
 * Display a Grid of hexagons.
 * 
 * Use coordinate system to get hex neighbors:
 * 
 * 0-0 1-0 2-0 3-0 0-1 1-1 2-1 3-1 0-2 1-2 2-2 3-2
 */
public class HexCanvas extends GridCanvas {
	private static final int sqrt3 = (int) Math.sqrt(3);

	private int cellHeight = (int) (GlobalDefaults.aaRadius * Math.sqrt(3));

	private Polygon hexagon;

	public HexCanvas(int width, int height) {
		super(width, height);
		createHexagon();
	}

	public HexCanvas() {
		super();
		createHexagon();
	}

	/**
	 * This creates a hexagon of the proper size
	 */
	private void createHexagon() {
		hexagon = new Polygon();
		hexagon.addPoint(GlobalDefaults.aaRadius / 2, 
				0);
		hexagon.addPoint(3 * GlobalDefaults.aaRadius / 2, 
				0);
		hexagon.addPoint(2 * GlobalDefaults.aaRadius, 
				sqrt3 * GlobalDefaults.aaRadius);
		hexagon.addPoint(3 * GlobalDefaults.aaRadius / 2, 
				2 * sqrt3 * GlobalDefaults.aaRadius);
		hexagon.addPoint(GlobalDefaults.aaRadius / 2, 
				2 * sqrt3 * GlobalDefaults.aaRadius);
		hexagon.addPoint(0, 
				sqrt3 * GlobalDefaults.aaRadius);
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

