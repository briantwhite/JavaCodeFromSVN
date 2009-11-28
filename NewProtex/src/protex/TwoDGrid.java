//TwoDGrid.java


//Copyright 2004, Ethan Bolker and Bogdan Calota
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
		coreColors = new ArrayList();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				cells[i][j] = null;
				points[i][j] = new GridPoint(i, j);
			}
		}
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

	public double getEnergy(boolean custom, double hpIndex, double hIndex, double iIndex, double sIndex) {
		//more energy is BAD
		energy = 0;
		freeEdges = 0;

		// see if using custom energy - just the table of interaction energies
		//  where lower is better
		if (custom) {
			for (int i = 0; i < numAcids; i++) {
				AcidInChain a = acids[i];
				if (a.xyz == null) {
					break;
				}
				
				for (int d = 0; d < allDirections.length; d++) {
					AcidInChain ac = get(nextCell(allDirections[d], a.xyz));
					if (ac != null) {
						energy += 
							a.getAminoAcid().getContactEnergy(
									ac.getAminoAcid().getIndexNum());
					}
				}
			}
		} else {

			//array to make sure that a given cys can only make one ssbond
			boolean[] availableForssBond = new boolean[numAcids];
			for (int i = 0; i < numAcids; i++) {
				availableForssBond[i] = true;
			}
			//set up the array list of ss bonds
			ssBondList = new ArrayList();

			for (int i = 0; i < numAcids; i++) {
				AcidInChain a = acids[i];
				if (a.xyz == null) { // a has not been placed on grid
					break; // perhaps should be continue
				}


				int free = 0;
				int hbondContacts = 0; // Added by NR
				int ionicInteractions = 0; //Added by NR
				int ssBonds = 0; // added by BW

				for (int d = 0; d < allDirections.length; d++) {
					AcidInChain ac = get(nextCell(allDirections[d], a.xyz));
					if (ac == null) {
						free++;
					} else {
						//add to H-bond contacts if both indices are 1
						hbondContacts = hbondContacts + 
						(a.gethydrogenbondIndex() * ac.gethydrogenbondIndex());

						//subtract from ionic bond index if both have opposite signs
						ionicInteractions = ionicInteractions - 
						(a.getionicIndex() * ac.getionicIndex());

						//count the ss bonds
						// be sure that each can only make one such bond
						if (a.getssIndex() 
								&& ac.getssIndex() 
								&& availableForssBond[i]
								                      && availableForssBond[ac.getIndex()]
								                                            && (Math.abs(i - ac.getIndex()) != 1)) {
							ssBonds++;
							availableForssBond[i] = false;
							availableForssBond[ac.getIndex()] = false;
							ssBondList.add(new SsBond(i, ac.getIndex()));
						}
					}
				}
				energy += free * a.hydrophobicIndex * hpIndex 
				- hbondContacts * hIndex 
				- ionicInteractions * iIndex
				- ssBonds * sIndex;
				freeEdges += free;
			}
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

	public double getFoldingIndex(boolean custom, double hpIndex, double hIndex, double iIndex, double sIndex) {
		computeStatistics(custom, hpIndex, hIndex, iIndex, sIndex);
		return freeEdges / (double) (2 + 4 * pp.getLength());
	}

	public int getFreeEdges(boolean custom, double hpIndex, double hIndex, double iIndex, double sIndex) {
		computeStatistics(custom, hpIndex, hIndex, iIndex, sIndex);
		return freeEdges;
	}

	public void computeStatistics(boolean custom, double hpIndex, double hIndex, double iIndex, double sIndex) {
		getEnergy(custom, hpIndex, hIndex, iIndex, sIndex);
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
