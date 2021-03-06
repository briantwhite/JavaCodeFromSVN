// TwoDGrid.java
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

import java.awt.Color;
import java.util.ArrayList;

/**
 * Model a grid containing a polypeptide chain.
 */
public abstract class TwoDGrid extends Grid {
	protected AcidInChain[][] cells;

	// create one GridPoint for each cell, use as singletons
	protected GridPoint[][] points;

	protected ArrayList hydrophobics;

	protected ArrayList hydrophilics;

	protected RYBColorChart chart;

	protected Color proteinColor = Color.white;

	protected ArrayList coreColors;

	public TwoDGrid() {
	}

	public TwoDGrid(Polypeptide pp) {
		super(pp);
		hydrophobics = new ArrayList();
		hydrophilics = new ArrayList();
		cells = new AcidInChain[size][size];
		points = new GridPoint[size][size];
//		chart = new RYBColorChart();
		chart = RYBColorChart.getRYBColorChart();
		coreColors = new ArrayList();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				cells[i][j] = null;
				points[i][j] = new GridPoint(i, j);
			}
		}
		/// I don't understand why I can't put this here
		/// instead of the subclass constructor
		// 	if (pp.isFolded()) {
		//// GridPoint currentP = points[getCenter()][getCenter()];
		// 	    GridPoint currentP = getCenter();
		// 	    set(0, currentP, Direction.none);
		// 
		// 	    Direction d = pp.getNextDirection(0);
		// 	    for (int i = 1; i < numAcids; i++ ) {
		// 		currentP = nextCell( d, currentP );
		// 		set(i, currentP, d);
		// 		d = pp.getNextDirection(i);
		// 	    }
		// 	}
	}

	public GridPoint getCenter() {
		return new GridPoint(size / 2, size / 2);
	}

	public void set(int index, GridPoint p, Direction from) {
		AcidInChain a = acids[index];
		cells[p.y][p.x] = a;
		a.xyz = p;
		if (index > 0) {
			acids[index - 1].setNext(from);
		}
	}

	protected void unset(GridPoint p) {
		this.unset(p.x, p.y);
	}

	protected void unset(GridPoint p, int index) {
		unset(cells[p.y][p.x], index, p.x, p.y);
	}

	protected void unset(AcidInChain a) {
		if ((a != null) && (a.xyz != null)) {
			unset(a.xyz.x, a.xyz.y);
		}
	}

	protected void unset(int x, int y) {
		AcidInChain a = cells[y][x];
		if (a != null) {
			unset(a, a.getIndex(), x, y);
		}
	}

	private void unset(AcidInChain a, int index, int x, int y) {
		cells[y][x] = null;
		a.xyz = null;
		if (index > 0) {
			acids[index - 1].setNext(Direction.none);
		}
	}

	protected AcidInChain get(GridPoint p) {
		return this.get(p.x, p.y);
	}

	protected AcidInChain get(int x, int y) {
		return cells[y][x];
	}

	protected Direction getDirection(GridPoint p1, GridPoint p2) {
		return getDirection(p1.x, p1.y, p2.x, p2.y);
	}

	protected Direction getDirection(int x1, int y1, int x2, int y2, int z1,
			int z2) {
		return this.getDirection(x1, y1, x2, y2);
	}

	protected abstract Direction getDirection(int x1, int y1, int x2, int y2);

	protected abstract Direction[] getAllDirections();

	protected Direction[] allDirections = null;

	protected abstract GridPoint nextCell(Direction direction, GridPoint p);

	public abstract Direction[] getThirdPlacement();

	// statistics

	protected double energy;

	protected int freeEdges;

	public double getEnergy(double hpIndex, double hIndex, double iIndex) {
		energy = 0;
		freeEdges = 0;
		for (int i = 0; i < numAcids; i++) {
			AcidInChain a = acids[i];
			//	System.out.println("protein:"+a.getName());
			if (a.xyz == null) { // a has not been placed on grid
				break; // perhaps should be continue
			}
			int free = 0;
			int hbondContacts = 0; // Added by NR
			int ionicInteractions = 0; //Added by NR
			for (int d = 0; d < allDirections.length; d++) {
				AcidInChain ac = get(nextCell(allDirections[d], a.xyz));
				if (ac == null) {
					free++;
				}
				//	if(ac!=null)
				//	System.out.println("protein:"+ac.getName());
				if ((int) acids[i].gethydrogenbondIndex() == 1) {
					if (ac != null) {
						//	System.out.println("protein:"+ac.getName());
						if ((int) ac.gethydrogenbondIndex() == 1) {
							hbondContacts++;
							//		System.out.println("HBond:" + hbondContacts);
						}
					}
				}
				if ((int) acids[i].getionicIndex() == 1) {
					if (ac != null) {
						//	System.out.println("protein:"+ac.getName());
						if ((int) ac.getionicIndex() == 1) {
							ionicInteractions++;
							//	System.out.println("Ionic Bond:" +
							// ionicInteractions);
						} else if ((int) ac.getionicIndex() == -1) {
							ionicInteractions--;
							//	System.out.println("Ionic Bond:" +
							// ionicInteractions);
						}
					}
				}
				if ((int) acids[i].getionicIndex() == -1) {
					if (ac != null) {
						if ((int) ac.getionicIndex() == -1) {
							ionicInteractions++;
						} else if ((int) ac.getionicIndex() == 1) {
							ionicInteractions--;
						}
					}
				}
			}
			energy += free * a.hydrophobicIndex * hpIndex - hbondContacts
					* hIndex + ionicInteractions * iIndex;
			freeEdges += free;
		}
		return energy;
	}

	public void categorizeAcids() {
		for (int i = 0; i < numAcids; i++) {
			AcidInChain a = acids[i];
			if (a.getHydrophobicIndex() >= 0
					|| a.getName().equalsIgnoreCase("tyr"))
				hydrophobics.add(a);
			else
				hydrophilics.add(a);
		}
	}

	public Color getProteinColor() {
		if (numAcids < 13)
			return Color.white;
		categorizeAcids();
		if (hydrophobics.size() < 7 || hydrophilics.size() < 6)
			return Color.white;
		Color c = Color.white;
		for (int i = 0; i < hydrophobics.size(); i++) {
			c = Color.white;
			AcidInChain a = (AcidInChain) hydrophobics.get(i);
			int d;
			for (d = 0; d < allDirections.length; d++) {
				AcidInChain ac = get(nextCell(allDirections[d], a.xyz));

				if (ac == null || !hydrophobics.contains(ac))
					break;
				else if (hydrophobics.contains(ac)) {
					c = primaryColors(c, ac);
				}
			}
			if (d == allDirections.length) {
				c = primaryColors(c, a);
				coreColors.add(c);
			}
		}
		if (coreColors.size() > 0)
			proteinColor = mixHexagonalCores();
		return proteinColor;
	}

	/**
	 *  
	 */
	private Color mixHexagonalCores() {

		proteinColor = (Color) coreColors.get(0);
		for (int i = 1; i < coreColors.size(); i++)
			proteinColor = chart.mixTwoColors(proteinColor, (Color) coreColors
					.get(i));
		return proteinColor;
	}

	/**
	 * @param c
	 * @param a
	 * @return
	 */
	private Color primaryColors(Color c, AcidInChain a) {
		if (a.getName().equalsIgnoreCase("phe"))
			c = chart.mixTwoColors(c, Color.red);
		if (a.getName().equalsIgnoreCase("tyr"))
			c = chart.mixTwoColors(c, Color.blue);
		if (a.getName().equalsIgnoreCase("trp"))
			c = chart.mixTwoColors(c, Color.yellow);
		return c;
	}

	public double getFoldingIndex(double hpIndex, double hIndex, double iIndex) {
		computeStatistics(hpIndex, hIndex, iIndex);
		return freeEdges / (double) (2 + 4 * pp.getLength());
	}

	public int getFreeEdges(double hpIndex, double hIndex, double iIndex) {
		computeStatistics(hpIndex, hIndex, iIndex);
		return freeEdges;
	}

	public void computeStatistics(double hpIndex, double hIndex, double iIndex) {
		getEnergy(hpIndex, hIndex, iIndex);
		setNeighbors();
	}

	public void setNeighbors() {
		pp.clearTopology();
		for (int i = 0; i < numAcids; i++) {
			setNeighbors(acids[i]);
		}
	}

	public boolean isLastAcidPlaced() {
		return (acids[numAcids - 1]).xyz != null;
	}

	protected void setNeighbors(AcidInChain to) {
		GridPoint p = to.xyz;
		if (p == null) {
			return;
		}
		for (int d = 0; d < allDirections.length; d++) {
			setNeighbor(to, p, allDirections[d]);
		}
	}

	protected void setNeighbor(AcidInChain to, GridPoint p, Direction d) {
		AcidInChain from = get(nextCell(d, p));
		if (from != null) {
			pp.addNeighbor(to, from);
		}
	}

	protected GridPoint getMin() {
		int minX = size;
		int minY = size;
		for (int i = 0; i < numAcids; i++) {
			AcidInChain a = pp.getAminoAcid(i);
			if (a.xyz.x < minX)
				minX = a.xyz.x;
			if (a.xyz.y < minY)
				minY = a.xyz.y;
		}
		return new GridPoint(minX, minY);
	}

	protected GridPoint getMax() {
		int maxX = 0;
		int maxY = 0;
		for (int i = 0; i < numAcids; i++) {
			AcidInChain a = pp.getAminoAcid(i);
			if (a.xyz.x > maxX)
				maxX = a.xyz.x;
			if (a.xyz.y > maxY)
				maxY = a.xyz.y;
		}
		return new GridPoint(maxX, maxY);
	}

}
