// BruteForceFolder.java
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

// histogram is broken - need to get right value for offset

package protex.client;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


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
	
	ProtexGWT protexGWT;

	public String getName() {
		return "Brute force folding";
	}
	
	// for progress bar - not properly implemented
	public int getCurrent() {
		return 0;
	}

	// should get rid of this one - requires rewriting some other
	// stuff that needs rewriting anyway

	public BruteForceFolder(boolean custom, Polypeptide pp, Grid grid) {
		super(custom, pp, grid);
		infiniteEnergy = grid.getInfiniteEnergy();
	}

	public void realFold() {
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

		if (numAcids > 0) {
			grid.set(0, grid.getCenter(), Direction.none);
		}

		if (numAcids == 1) { // just one AminoAcid
			energy = grid.getEnergy(custom, hpIndex, hIndex, iIndex, sIndex);
			protexGWT.drawProtein();
			return;
		}

		// else place second AminoAcid a (to the East)
		grid.set(1, grid.nextCell(grid.getFirstDirection(), grid.getCenter()),
				grid.getFirstDirection());

		if (numAcids == 2) { // just two AminoAcids
			energy = grid.getEnergy(custom, hpIndex, hIndex, iIndex, sIndex);
			protexGWT.drawProtein();
			return;
		}
		placeRestOfAcids();
		restore();
	}

	// else place the third AminoAcid E,NE,NW
	// (symmetry ==> no need to try SE or SW)
	protected void placeRestOfAcids() {
		Direction[] thirdPlacement = grid.getThirdPlacement();
		for (int i = 0; i < thirdPlacement.length; i++) {
			tryDirection(thirdPlacement[i], 2, numAcids);
		}
	}

	protected void resetEnergy() {
		energy = infiniteEnergy;
	}

	public void recurse(Direction[] next, int current, int stop) {
        recurseAsync(next, current, stop);
	}
    
	public native void recurseAsync(Direction[] next, int current, int stop)
	/*-{
       	var that = this;
       	
       	var tryDirection = function(direction, currenttt, stoppp) {
			var lastA = that.@protex.client.Folder::acids[currenttt - 1];
 			var lastAxyz = lastA.@protex.client.AcidInChain::xyz;
 			var grid = that.@protex.client.Folder::grid;
 			var p = grid.@protex.client.HexGrid::nextCell(Lprotex/client/Direction;Lprotex/client/GridPoint;)(direction, lastAxyz);
 			if (grid.@protex.client.Grid::get(Lprotex/client/GridPoint;)(p) === null) {
				grid.@protex.client.Grid::set(ILprotex/client/GridPoint;Lprotex/client/Direction;)(currenttt, p, direction);
				if (++currenttt == stoppp) {
					that.@protex.client.BruteForceFolder::saveIfNecessary()();
				} else {
					var nextDir = grid.@protex.client.Grid::getNextDirection(Lprotex/client/Direction;)(direction);
					for (var i = 0; i < nextDir.length; i++) {
   						tryDirection(nextDir[i], currenttt, stoppp);
					}	
				}
				grid.@protex.client.Grid::unset(Lprotex/client/GridPoint;I)(p, currenttt);
			}
       	};
       	
       	var recurse = function(nextt, currentt, stopp) {
			var callback = $entry(function() {
    			that.@protex.client.IncrementalFolder::done()();
  			});
  			
  			var progress = $entry(function(val) {
    			that.@protex.client.IncrementalFolder::progress(I)(val);
  			});
  			
			//The following doesn't work!!!
			//for (var i = 0; i < nextt.length; i++)
			//{
				//$wnd.setTimeout(function(){return tryDirection(nextt[i], currentt, stopp);}, i*50);
			//}
			
			//But this does!!! There are always 5 directions
			$wnd.setTimeout(function(){return progress(0);}, 0);
			$wnd.setTimeout(function(){return tryDirection(nextt[0], currentt, stopp);}, 50);
			$wnd.setTimeout(function(){return progress(1);}, 75);
			$wnd.setTimeout(function(){return tryDirection(nextt[1], currentt, stopp);}, 100);
			$wnd.setTimeout(function(){return progress(2);}, 125);
			$wnd.setTimeout(function(){return tryDirection(nextt[2], currentt, stopp);}, 150);
			$wnd.setTimeout(function(){return progress(3);}, 175);
			$wnd.setTimeout(function(){return tryDirection(nextt[3], currentt, stopp);}, 200);
			$wnd.setTimeout(function(){return progress(4);}, 225);
			$wnd.setTimeout(function(){return tryDirection(nextt[4], currentt, stopp);}, 250);
			$wnd.setTimeout(function(){return progress(5);}, 275);
			$wnd.setTimeout(function(){return callback();}, 300);
   		}
   		
   		recurse(next, current, stop);
    }-*/;

	
	//For debugging
	public static native void log(String msg)
	/*-{
		console.log(msg);
	}-*/;
	
	public static native void alert(String msg)
	/*-{
		$wnd.alert(msg);
	}-*/;
	
	//Original recursion
	/*function recurse(nextt, currentt, stopp) {
			for (var i = 0; i < nextt.length; i++) {
   			tryDirection(nextt[i], currentt, stopp);
   		}
   	}*/
	 
	protected void tryDirection(Direction direction, int current, int stop) {
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

	protected void saveIfNecessary() {
		chainCount++;
		double localEnergy = grid.getEnergy(custom, hpIndex, hIndex, iIndex, sIndex);

		if (localEnergy > energy) {
			return;
		}
		if (localEnergy == energy) {
			energyTies++;
			grid.computeStatistics(custom, hpIndex, hIndex, iIndex, sIndex);
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
		grid.computeStatistics(custom, hpIndex, hIndex, iIndex, sIndex);
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

	public String report() {
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
