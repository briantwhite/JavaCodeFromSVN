//BruteForceFolder.java


//Copyright 2004, Ethan Bolker and Bogdan Calota
/* 
 * License Information
 * 
 * This  program  is  free  software;  you can redistribute it and/or modify it 
 * under  the  terms  of  the GNU General Public License  as  published  by the 
 * Free  Software  Foundation; either version 2  of  the  License,  or (at your 
 * option) any later version.
 */

//histogram is broken - need to get right value for offset

package edu.umb.jsAipotu.biochem;

import java.util.HashSet;
import java.util.Set;



/**
 * Recursive backtracking search for minimum energy folding. Assumes the grid is
 * allocated and large enough so that we won't run over if we start in the
 * middle.
 */
public class BruteForceFolder extends Folder {
	protected double energy;

	protected GridPoint[] points;

	protected int chainCount = 0;

	protected int energyTies = 1;

	protected double infiniteEnergy;


	// For a histogram.
	protected long[] buckets = new long[201];

	protected double scale;

	protected double offset;

	protected Set topologies;

	public String getName() {
		return "Brute force folding";
	}

	// for progress bar - not properly implemented
	public int getCurrent() {
		return 0;
	}

	// should get rid of this one - requires rewriting some other
	// stuff that needs rewriting anyway

	public BruteForceFolder(Polypeptide pp, Grid grid) {
		super(pp, grid);
		infiniteEnergy = grid.getInfiniteEnergy();
	}

	public void realFold() throws PaintedInACornerFoldingException {
		for (int i = 0; i < buckets.length; i++) {
			buckets[i] = 0;
		}
		topologies = new HashSet();

		scale = 100.0 / (pp.getMaxEnergy() * (4 * numAcids + 2));
		offset = 100.0 / pp.getMaxEnergy();

		points = new GridPoint[grid.getSize()];

		// place first two AminoAcids near center, loop on just
		// a few vectors for the third (depending on symmetry of grid
		for (int i = 0; i < numAcids; i++) {
			grid.unset(acids[i]);
		}
		resetEnergy();

		//if nothing to fold
		if (numAcids == 0) {
			return;
		}

		if (numAcids > 0) {
			grid.set(0, grid.getCenter(), Direction.none);
		}

		if (numAcids == 1) { // just one AminoAcid
			energy = grid.getEnergy(hpIndex, hIndex, iIndex);
			return;
		}

		// else place second AminoAcid a (to the East)
		grid.set(1, grid.nextCell(grid.getFirstDirection(), grid.getCenter()),
				grid.getFirstDirection());

		if (numAcids == 2) { // just two AminoAcids
			energy = grid.getEnergy(hpIndex, hIndex, iIndex);
			return;
		}

		placeRestOfAcids();
		restore();
	}

	// else place the third AminoAcid E,NE,NW
	// (symmetry ==> no need to try SE or SW)
	protected void placeRestOfAcids() throws 
	PaintedInACornerFoldingException {
		Direction[] thirdPlacement = grid.getThirdPlacement();
		for (int i = 0; i < thirdPlacement.length; i++) {
			tryDirection(thirdPlacement[i], 2, numAcids);
		}
	}

	protected void resetEnergy() {
		energy = infiniteEnergy;
	}

	public void recurse(Direction[] next, int current, int stop) 
	throws PaintedInACornerFoldingException {
		//if the next is null, it means there's no open places
		//  for the next aa, so we're 'painted into a corner'
		//  so need to abort this branch here (or it'll get a
		//  NullPointerException) AND fire an exception.
		if (next == null) {
			throw new PaintedInACornerFoldingException(
					grid.getPP().getSingleLetterAASequence());
		}
		for (int i = 0; i < next.length; i++) {
			tryDirection(next[i], current, stop);
		}
	}

	protected void tryDirection(Direction direction, int current, int stop) 
	throws PaintedInACornerFoldingException {
		AcidInChain lastA = acids[current - 1];
		GridPoint p = grid.nextCell(direction, lastA.xyz);
		if (grid.get(p) == null) {
			grid.set(current, p, direction);
			if (++current == stop) {
				saveIfNecessary();
			} else {
				recurse(grid.getNextDirection(direction), current, stop);
			}
			grid.unset(p, current);
		}
	}

	protected void saveIfNecessary() throws PaintedInACornerFoldingException {
		chainCount++;
		double localEnergy = grid.getEnergy(hpIndex, hIndex, iIndex);

		if (localEnergy > energy) {
			return;
		}
		if (localEnergy == energy) {
			energyTies++;
			grid.computeStatistics(hpIndex, hIndex, iIndex);
			if (grid.isLastAcidPlaced()) {
				topologies.add(pp.getTopology() + "\n"
						+ pp.getDirectionSequence());
			}
			return;
		}

		// else new minumum
		energy = localEnergy;
		energyTies = 1;
		topologies.clear();
		grid.computeStatistics(hpIndex, hIndex, iIndex);
		if (grid.isLastAcidPlaced()) {
			topologies.add(pp.getTopology() + "\n" + pp.getDirectionSequence());
		}
		for (int i = 0; i < numAcids; i++) {
			points[i] = acids[i].getPoint();
		}
	}

	public String getTopologies() {
		return topologies.toString();
	}

	public String getStatistics() {
		return "explored " + chainCount + " chains in " + getTime()
		+ " seconds";
	}

	public String getEnergyHistogram() {
		StringBuffer buf = new StringBuffer(400);
		for (int i = 0; i < buckets.length; i++) {
			buf.append(buckets[i] + ", ");
		}
		return buf.toString();
	}

	public String report() throws PaintedInACornerFoldingException {
		StringBuffer buf = new StringBuffer(super.report());
		buf.append("\nexplored " + chainCount + " chains");
		buf.append("\nminimum occurred " + getEnergyTies() + " times");
		return buf.toString();
	}

	public int getEnergyTies() {
		return energyTies;
	}

	protected void restore() {
		for (int i = 0; i < numAcids; i++) {
			Direction d = Direction.none;
			if (points[i] == null) {
				break;
			}
			if (i > 0) {
				d = grid.getDirection(points[i - 1], points[i]);
			}
			grid.set(i, points[i], d);
		}
	}
}
