// GridCanvas.java
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

// .1 .2 .3 .4 .5 .6 .7 .8 

package edu.umb.jsAipotu.client.biochem;

import java.util.Arrays;
import java.util.Comparator;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.user.client.Window;

import edu.umb.jsAipotu.client.molGenExp.Dimension;
import edu.umb.jsAipotu.client.preferences.GlobalDefaults;

/**
 * Display a Grid.
 *  
 */
public abstract class GridCanvas {

	protected int cellDiameter = 2 * GlobalDefaults.aaRadius;

	protected int size; // 2*numAcids + 1

	protected Grid grid = null;

	protected int numAcids;

	protected Polypeptide pp;

	private Dimension requiredCanvasSize;

	private Canvas canvas;

	public GridCanvas(int width, int height) {
		canvas = Canvas.createIfSupported();
		if (canvas == null) {
			Window.alert("This application is not supported by this browser; please try a different browser.");
		}
		canvas.setWidth(width + "px");
		canvas.setCoordinateSpaceWidth(width);
		canvas.setHeight(height + "px");
		canvas.setCoordinateSpaceHeight(height);
		requiredCanvasSize = new Dimension(width, height);
	}

	public GridCanvas() {
		requiredCanvasSize = new Dimension(0,0);
	}

	public Dimension getRequiredCanvasSize() {
		calculateRequiredCanvasSize();
		return requiredCanvasSize;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
		this.size = grid.getSize();
		this.pp = grid.getPP();
		numAcids = pp.getLength();
	}

	/**
	 * Added by [DAP] on 3 Mar 2005
	 * 
	 * @return Grid
	 */
	public Grid getGrid() {
		return grid;
	}

	protected GridPoint getMin() {
		int minX = size;
		int minY = size;
		int minZ = size;
		for (int i = 0; i < numAcids; i++) {
			AcidInChain a = pp.getAminoAcid(i);
			if (a.xyz.x < minX)
				minX = a.xyz.x;
			if (a.xyz.y < minY)
				minY = a.xyz.y;
			if (a.xyz.z < minZ)
				minZ = a.xyz.z;
		}
		return new GridPoint(minX - 1, minY - 1, minZ - 1);
	}

	private GridPoint getMin(GridPoint[] points) {
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int minZ = Integer.MAX_VALUE;
		for (int i = 0; i < points.length; i++) {
			if (points[i].x < minX)
				minX = points[i].x;
			if (points[i].y < minY)
				minY = points[i].y;
			if (points[i].z < minZ)
				minZ = points[i].z;
		}
		return new GridPoint(minX - cellDiameter, minY - cellDiameter, minZ);
	}

	protected GridPoint getMax() {
		int maxX = 0;
		int maxY = 0;
		int maxZ = 0;
		for (int i = 0; i < numAcids; i++) {
			AcidInChain a = pp.getAminoAcid(i);
			if (a.xyz.x > maxX)
				maxX = a.xyz.x;
			if (a.xyz.y > maxY)
				maxY = a.xyz.y;
			if (a.xyz.z > maxZ)
				maxZ = a.xyz.z;
		}
		return new GridPoint(maxX, maxY, maxZ);
	}

	/**
	 * Compute a plane projection of a (possibly three dimensional) GridPoint to
	 * use to paint an AminoAcid on this GridCanvas.
	 * 
	 * @param p
	 *            the point to project.
	 * 
	 * @return the projection.
	 */
	protected abstract GridPoint project(GridPoint p);

	/**
	 * Returns constants used for center the name of the polypeptide
	 */
	protected int getStringIndentationConstant(String name, int r) {
		// the values returned are hardcoded with values that
		//   look best when the canvas is drawn. Their value
		//   was establish through trials, and best was picked.

		int length = name.trim().length();
		if (length == 1) // 1
			return 0;
		else if (length == 2) // -1
			return 0;
		else if (length == 3) // 0.x
			return (int) (1 / 8f * r);
		else if (length == 4) // -0.x
			return (int) (1 / 2f * r);
		else if (length == 5) // -0.xx
			return (int) (2 / 3f * r);
		else
			// length == 6. can't be longer. -0.xxx
			return (int) (3 / 4f * r);
	}

	public void calculateRequiredCanvasSize() {
		if (grid == null)
			return;

		GridPoint[] spots = new GridPoint[numAcids];
		AcidInChain[] acidsByZ = new AcidInChain[numAcids];
		for (int i = 0; i < numAcids; i++) {
			AcidInChain a = pp.getAminoAcid(i);
			spots[i] = project(a.xyz);
			acidsByZ[i] = a;
		}
		GridPoint min = getMin(spots);
		for (int i = 0; i < numAcids; i++) {
			spots[i] = spots[i].subtract(min);
		}
		Arrays.sort(acidsByZ, new SortByZ());
		GridPoint here = null;
		int maxX = 0;
		int maxY = 0;
		for (int i = 0; i < numAcids; i++) {
			AcidInChain a = acidsByZ[i];
			here = project(a.xyz).subtract(min);
			if (i == 0) {
				maxX = here.x;
				maxY = here.y;
			} else {
				if (here.x > maxX)
					maxX = here.x;
				if (here.y > maxY)
					maxY = here.y;
			}
		}
		requiredCanvasSize = new Dimension(maxX + 2 * cellDiameter, 
				maxY + 2 * cellDiameter);
	}

	public void paintProtein(Context2d g) {
		ColorCoder cc = null;

		cc = new ShadingColorCoder(GlobalDefaults.aaTable.getContrastScaler());
		g.setFillStyle(GlobalDefaults.PROTEIN_BACKGROUND_COLOR.toString());
		g.fillRect(0, 0, requiredCanvasSize.width, requiredCanvasSize.height);


		GridPoint[] spots = new GridPoint[numAcids];
		AcidInChain[] acidsByZ = new AcidInChain[numAcids];
		for (int i = 0; i < numAcids; i++) {
			AcidInChain a = pp.getAminoAcid(i);
			spots[i] = project(a.xyz);
			acidsByZ[i] = a;
		}
		GridPoint min = getMin(spots);
		for (int i = 0; i < numAcids; i++) {
			spots[i] = spots[i].subtract(min);
		}
		Arrays.sort(acidsByZ, new SortByZ());
		int r = GlobalDefaults.aaRadius;
		for (int i = 0; i < numAcids; i++) {
			AcidInChain a = acidsByZ[i];
			GridPoint here = project(a.xyz).subtract(min);	
			a.getAminoAcid().paint(g, cc, here.x - r, here.y - r);
		}

		// draw the backbone
		g.setStrokeStyle("magenta");
		g.beginPath();
		for (int i = 0; i < numAcids; i++) {
			AcidInChain a = pp.getAminoAcid(i);	
			// just move to first aa - don't start drawing yet
			if (i == 0) {
				g.moveTo(spots[i].x, spots[i].y);
			} else {
				g.lineTo(spots[i].x, spots[i].y);
			}
		}
		g.stroke();
	}

	private class SortByZ implements Comparator {
		public int compare(Object o1, Object o2) {
			AcidInChain a1 = (AcidInChain) o1;
			AcidInChain a2 = (AcidInChain) o2;
			return (a1.xyz.z - a2.xyz.z);
		}
	}
}

