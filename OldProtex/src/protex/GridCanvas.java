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

package protex;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.Comparator;
//import java.util.Iterator;
//import java.util.Set;

import javax.swing.JPanel;

/**
 * Display a Grid.
 *  
 */
public abstract class GridCanvas extends JPanel {
	protected int cellRadius = 20;

	protected int cellDiameter = 2 * cellRadius;

	protected int size; // 2*numAcids + 1

	protected Grid grid = null;

	protected int numAcids;

	protected Polypeptide pp;

	private static final Color COLOR_BACKGROUND = new Color((float) 0.7,
			(float) 0.7, (float) 1.0);

	private static final Color BLACK_BACKGROUND = new Color((int) 175,
			(int) 175, (int) 175);

	// careful here. 175 is half of 100-250

	private boolean blackColoring; // flag for black / color coloring

	private JPanel parentPanel;

	public GridCanvas(int width, int height) {
		this();
		this.setSize(width, height);
	}

	public GridCanvas() {
		blackColoring = FoldingManager.getInstance().getBlackColoring();
	}

	protected void setGrid(Grid grid) {
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
	protected Grid getGrid() {
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

	protected int getAcidRadius() {
		return cellRadius;
	}

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

	public void setParentPanel(JPanel parentPanel) {
		this.parentPanel = parentPanel;
	}

	public void paint(Graphics g) {
		// badly design. Code of two block of statements is almost entirely the
		// same.
		//  fix: either redesign ColorCoder and use polymorphism
		//       or just split the ColorCoder creation and usage
		//          i.e. when the disk is drawn in the adequate color.
		super.paintComponent(g);
		if (blackColoring) {
			setBackground(BLACK_BACKGROUND);
			//////////////////////////////
			//    COLORING IN BLACK //
			//////////////////////////////

			BlackColorCoder cc = new BlackColorCoder(pp.getTable()
					.getContrastScaler()); /// changed
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
			int r = getAcidRadius();
			for (int i = 0; i < numAcids; i++) {
				AcidInChain a = acidsByZ[i];
				GridPoint here = project(a.xyz).subtract(min);

				// fills the circle
				g.setColor(cc.getCellColor(a.getNormalizedHydrophobicIndex()));
				g.fillOval(here.x - r, here.y - r, 2 * r, 2 * r);

				// draws the circle, on top on the color disk
				g.setColor(Color.black);
				g.drawOval(here.x - r, here.y - r, 2 * r, 2 * r);
			}
			// connect neighbors here
			//if (grid instanceof ThreeDGrid) {
			//	g.setColor(Color.white);
			//	for (int i = 0; i < numAcids; i++) {
			//		AcidInChain a = pp.getAminoAcid(i);
			//		Set nbrs = a.getNeighbors();
			//		Iterator iter = nbrs.iterator();
			//		while (iter.hasNext()) {
			//			int j = ((Integer) iter.next()).intValue();
			//			g.drawLine(spots[i].x, spots[i].y, spots[j].x,
			//					spots[j].y);
			//		}
			//	}
			//}

			// draw the backbone
			g.setColor(Color.black);
			for (int i = 0; i < numAcids; i++) {
				AcidInChain a = pp.getAminoAcid(i);
				int offset = getStringIndentationConstant(a.name, r);
				g.drawString(a.name, spots[i].x - offset, spots[i].y);
				// string is drawn to an left offset from center of disk.;
				if (i < numAcids - 1) {
					g.drawLine(spots[i].x, spots[i].y, spots[i + 1].x,
							spots[i + 1].y);
				}
			}
		} else {
			setBackground(COLOR_BACKGROUND);
			try {
				boolean changed = false;
				/////////////////////////////
				//    COLORING IN COLOR //
				/////////////////////////////

				ColorCoder cc = new ColorCoder(pp.getTable()
						.getContrastScaler()); ///
				// changed
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
				int r = getAcidRadius();
				for (int i = 0; i < numAcids; i++) {
					AcidInChain a = acidsByZ[i];
					g.setColor(cc.getCellColor(a
							.getNormalizedHydrophobicIndex()));
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
					g.fillOval(here.x - r, here.y - r, 2 * r, 2 * r);
				}

				int this_width = maxX + 2 * cellDiameter;
				if (this_width > ((OutputPalette) parentPanel).getDimension().width) {
					((OutputPalette) parentPanel).getDimension().width = this_width;
					changed = true;
				}
				int this_height = maxY + 2 * cellDiameter;
				if (this_height > ((OutputPalette) parentPanel).getDimension().height) {
					((OutputPalette) parentPanel).getDimension().height = this_height;
					changed = true;
				}
				if (changed) {
					//Update client's preferred size because
					//the area taken up by the graphics has
					//gotten larger or smaller (if cleared).
					setPreferredSize(((OutputPalette) parentPanel)
							.getDimension());

					//Let the scroll pane know to update itself
					//and its scrollbars.
					revalidate();
				}
				// connect neighbors here
				//if (grid instanceof ThreeDGrid) {
				//	g.setColor(Color.white);
				//	for (int i = 0; i < numAcids; i++) {
				//		AcidInChain a = pp.getAminoAcid(i);
				//		Set nbrs = a.getNeighbors();
				//		Iterator iter = nbrs.iterator();
				//		while (iter.hasNext()) {
				//			int j = ((Integer) iter.next()).intValue();
				//			g.drawLine(spots[i].x, spots[i].y, spots[j].x,
				//					spots[j].y);
				//		}
				//	}
				//}

				// draw the backbone
				g.setColor(Color.black);
				for (int i = 0; i < numAcids; i++) {
					AcidInChain a = pp.getAminoAcid(i);
					int offset = getStringIndentationConstant(a.name, r);
					g.drawString(a.name, spots[i].x - offset, spots[i].y);
					// string is drawn to an left offset from center of disk.;
					if (i < numAcids - 1) {
						g.drawLine(spots[i].x, spots[i].y, spots[i + 1].x,
								spots[i + 1].y);
					}
				}
			} catch (Exception ex) {

			}
		}
	}

	private class SortByZ implements Comparator {
		public int compare(Object o1, Object o2) {
			AcidInChain a1 = (AcidInChain) o1;
			AcidInChain a2 = (AcidInChain) o2;
			return (a1.xyz.z - a2.xyz.z);
		}
	}

	private void drawDottedLine(Graphics g, int x1, int y1, int x2, int y2) {
	}

	// for hex grid - never called
	private void paintEmpties(Graphics g) {
		g.setColor(Color.black);
		for (int row = 0; row < size; row++) {
			int rowstart = (row <= numAcids) ? numAcids - row : 0;
			int rowend = (row <= numAcids) ? size : size - row + numAcids;
			for (int col = rowstart; col < rowend; col++) {
				GridPoint here = new GridPoint(row, col);
				// redo if needed g.drawOval(
				// 			   getCenterX(here) - cellRadius,
				// 			   getCenterY(here) - cellRadius,
				// 			   cellDiameter, cellDiameter);
				// commented out code draws the grid
				// 		hexagon.translate(getCornerX(row, col),
				// 				  getCornerY(row,col));
				// 		g.drawPolygon(hexagon);
				// 		hexagon.translate(-getCornerX(row, col),
				// 				  -getCornerY(row,col));
			}
		}
	}

}

