// HybridFolder.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota


// This algorithm is no longer in use ...

import java.awt.Point;
import java.util.*;

/**
 * Hybrid approximation algorithm searching for minimal energy.
 *
 * Combination of local greed and backtracking search:
 * 
 * while there are acids to place
 *    recursively explore all ways to place the next
 *    acid to minimize energy of the intial segment
 */

public class HybridFolder extends BruteForceFolder
{
    public String getName() 
    {
	return "Hybrid folding";
    }

    public HybridFolder( Polypeptide pp, Grid g  )
    {
	super(pp, g);
    }

    public void recurse( Direction[] next, int current, int stop )
    {
	Set minDirections = new HashSet();
	double minSoFar = infiniteEnergy;
	for (int i = 0; i < 5; i++) {
	    double energy = place( current, next[i] );
	    if ( energy < minSoFar ) {
		minDirections.clear();
		minSoFar = energy;
	    }
	    if ( energy == minSoFar ) {
		minDirections.add( next[i] );
	    }
	}
	for (int i = 0; i < 5; i++) {
	    if ( minDirections.contains( next[i] ) ) {
		tryDirection(next[i], current, stop );
	    }
	}
    }

    // Place next acid in given direction and return energy 
    // so far.
    //
    // Return infinity if placement impossible.

    private double place( int current, Direction direction )
    {
	AcidInChain lastA = acids[current-1];
// 	GridPoint p = grid.nextCell(direction, lastA.x, lastA.y);
	GridPoint p = grid.nextCell(direction, lastA.xyz);
	if ( grid.get(p) != null ) { // cell is occupied
	    return grid.getInfiniteEnergy();
	}
	grid.set( current, p, direction );
	double energy = grid.getEnergy();
	grid.unset(p, current);	
	return energy;
    }

    public static void main( String[] args )
    {
	//	Polypeptide pp = null;
	//	Grid grid = null;
	//	Folder folder = null;
	//	double bfenergy, ienergy;
	//	if (args.length == 2) {
	//	    int length = Integer.parseInt( args[0] );
	//	    int seed   = Integer.parseInt( args[1] );
	//	    try {
	//		pp = new Polypeptide( "virtual", length, seed );
	//		grid = new HexGrid(pp);
	//		folder = new BruteForceFolder( pp, grid );
	//	    }
	//	    catch( FoldingException e ) {
	//		System.out.println(e);
	//		System.exit(0);
	//	    }
	//	    folder.fold();
	//	    System.out.println(folder.report());
	//	    bfenergy = folder.getEnergy();
	//	    try {
	//		pp = new Polypeptide( "virtual", length, seed );
	//		grid = new HexGrid(pp);
	//		folder = new HybridFolder( pp, grid );
	//	    }
	//	    catch( FoldingException e ) {
	//		System.out.println(e);
	//		System.exit(0);
	//	    }
	//	    folder.fold();
	//	    ienergy = folder.getEnergy();
	//	    System.out.println(folder.report());
	//	}
	//	else {
	//	    pp = new Polypeptide( args );
	//	    grid = new HexGrid(pp);
	//	    folder = new BruteForceFolder(pp, grid);
	//	    folder.fold();
	//	    bfenergy = folder.getEnergy();
	//	    System.out.println(folder.report());
	//	    pp = new Polypeptide( args );
	//	    grid = new HexGrid(pp);
	//	    folder = new HybridFolder( pp, grid );
	//	    folder.fold();
	//	    ienergy = folder.getEnergy();
	//	    System.out.println(folder.report());
	//	}
	//	System.out.println("diff/TrueMinimum : " + (bfenergy-ienergy)/bfenergy);
    }
}    
