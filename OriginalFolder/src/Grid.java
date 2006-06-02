// $folding/Grid.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota


// to do: rationalize the way the several statistics are computed

import java.util.*;

/**
 * Model a grid containing a polypeptide chain.
 */
public abstract class Grid
{
    public static final int STRAIGHT = 0;
    public static final int BENT = 1;

    protected int numAcids;
    protected int size;
    protected Polypeptide pp;
    
    private int tieBreaker = Grid.STRAIGHT;
    public Map directionMap  = null;
    public Map straightMap;
    public Map bentMap;

    
    // for efficiency: save function calls by creating an array here
    protected AcidInChain[] acids;
    
    public Grid( )
    {
    }

    public Grid( Polypeptide pp ) 
    {
	this.pp  = pp;
	numAcids = pp.getLength();
	acids    = pp.getAcidArray();
	size     = 2*numAcids + 1;

/// I don't understand why I can't put this here
/// instead of the subclass constructor
// 	if (pp.isFolded()) {
//// 	    GridPoint currentP = points[getCenter()][getCenter()];
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

    public int getSize()
    {
	return size;
    }

    public Polypeptide getPP()
    {
	return pp;
    }

    // override for dodecahedral grid.
    protected Direction getFirstDirection()
    {
	return Direction.E;
    }

    /**
     * A value guaranteed larger than the energy of any 
     * folding of any chain on this Grid.
     *
     * @return something large enough.
     */
    public double getInfiniteEnergy()
    {
	return Double.MAX_VALUE;
    }

    /**
     * Ring3 is an array of Directions that cycles through
     * all the Directions in this Grid three times.
     *
     * Insofar as possible, the subclasses should arrange
     * the cycle so that adjacent Directions are geometrically
     *  close.
     * 
     * @return the array of Directions.
     */
    protected abstract Direction[] getDirectionRing3();

    /**
     * How many Directions on this Grid?
     *
     * @return the number of Directions.
     */
    protected int getNumDirections()
    {
	return getDirectionRing3().length/3;
    }

    /**
     * Set the defaults for this Grid so that a folding 
     * algorithm knows in which Direction to place the
     * the next AminoAcid when there is no reason to prefer
     * one Direction.
     */
    public void setNextDirections( )
    {
	setTieBreaker(tieBreaker);
    }

    // needs error handling!
    public void setTieBreaker( int tieBreaker ) 
    {
	this.tieBreaker = tieBreaker;
	if (tieBreaker == Grid.STRAIGHT) {
	    directionMap = straightMap;
	}
	else {
	    directionMap = bentMap;
	}
    }

    private Direction[] reverse( Direction[] array )
    {
	int n = array.length;
	Direction[] rev = new Direction[n];
	for (int i = 0; i < n; i++ ) {
	    rev[i] = array[n-1-i];
	}
	return rev;
    }
		
    /**
     * When a folding algorithm needs to break a tie
     * it should continue in the previous Direction
     * insofar as possible.
     */


    // get rid of this - replace with old fashioned hard coded ...
    public void setNextDirectionsStraight()
    {
// 	System.out.println("straight");
	straightMap= new HashMap();
	int numDirections = getNumDirections();
	Direction[] ring = getDirectionRing3();
	for (int i=numDirections; i < 2*numDirections; i++) {
	    Direction[] next = new Direction[numDirections -1];
	    next[0] = ring[i];
	    for (int j = 1; j < numDirections/2 ; j++) {
		next[2*j-1] = ring[i+j];
		next[2*j]   = ring[i-j];
	    }
// 	    System.out.print(ring[i] + " straight:");
// 	    for (int k = 0; k < numDirections-1; k++ ) {
// 		System.out.print(" " + next[k]);
// 	    }
// 	    System.out.println("putting " + ring[i] + " " + next);
	    straightMap.put( ring[i], next);
// 	    nextDirection.put( ring[i], next);
	}
    }

    public void setNextDirectionsBent()
    {
	bentMap= new HashMap();
	Iterator i = straightMap.keySet().iterator();
	while (i.hasNext()) {
	    Object direction = i.next();
	    Direction[] stuff = 
		(Direction[])straightMap.get(direction);
	    bentMap.put(direction, reverse(stuff));
	}
// 	System.out.println("bent " + bentMap);
    }

    public abstract GridPoint getCenter() ;
    
    public abstract void set( int index, GridPoint p, Direction from );

    protected abstract void unset( GridPoint p);

    protected abstract void unset( GridPoint p, int index ); 

    protected void unsetAll() 
    {
	for (int i = 0; i < numAcids; i++) {
	    unset( acids[i] );
	}
    }
	    
    protected abstract void unset( AcidInChain a );
	
    protected abstract AcidInChain get( GridPoint p );
	
    protected abstract Direction getDirection(GridPoint p1, GridPoint p2);

    protected abstract Direction[] getAllDirections();

    protected Direction[] allDirections = null;

    protected abstract GridPoint nextCell(Direction direction, GridPoint p );

    public abstract Direction[] getThirdPlacement();

    // statistics

    protected double energy;
    protected int freeEdges;

    public double getEnergy() 
    {
	energy = 0;
	freeEdges = 0;
	for (int i = 0; i < numAcids; i++) {
	    AcidInChain a = acids[i];
	    if (a.xyz == null) { // a has not been placed on grid
		break;      // perhaps should be continue
	    }
	    int free = 0;
	    for (int d = 0; d < allDirections.length; d++ ) {
		if ( get(nextCell(allDirections[d], a.xyz)) == null ) {
		    free++;
		}
	    }
	    energy += free*a.hydrophobicIndex;
	    freeEdges += free;
	}
	return energy;
    }

    public double getFoldingIndex()
    {
	computeStatistics();
	return freeEdges/(double)(2+4*pp.getLength());
    }

    public int getFreeEdges()
    {
	computeStatistics();
	return freeEdges;
    }

    public void computeStatistics() 
    {
	getEnergy();
	setNeighbors();
    }

    public void setNeighbors()
    {
	pp.clearTopology();
	for (int i = 0; i < numAcids; i++) {
	    setNeighbors(acids[i]);
	}
    }

    public boolean isLastAcidPlaced()
    {
	return (acids[numAcids-1]).xyz != null;
    }

    protected void setNeighbors(AcidInChain to)  
    {
	GridPoint p = to.xyz;
	if (p == null) {
	    return;
	}
	for (int d = 0; d < allDirections.length; d++ ) {
	    setNeighbor( to, p, allDirections[d] );
	}
    }
    
    protected void setNeighbor(AcidInChain to, GridPoint p, Direction d)  
    {
	AcidInChain from = get(nextCell(d, p));
	if ( from != null ) {
	    pp.addNeighbor(to, from);
	}
    }


    public Direction[] getNextDirection( Direction d )
    {
	return (Direction[])directionMap.get(d);
    }
}    
