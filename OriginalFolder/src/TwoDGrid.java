// TwoDGrid.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota


import java.util.*;

/**
 * Model a grid containing a polypeptide chain.
 */
public abstract class TwoDGrid extends Grid
{
    protected AcidInChain[][] cells;

    // create one GridPoint for each cell, use as singletons
    protected GridPoint[][] points;

    public TwoDGrid( )
    {
    }

    public TwoDGrid( Polypeptide pp ) 
    {
	super(pp);
	cells    = new AcidInChain[size][size];
	points   = new GridPoint[size][size];
	for (int i = 0; i < size; i++) {
	    for (int j = 0; j < size; j++) {
		cells[i][j] = null;
		points[i][j] = new GridPoint(i,j);
	    }
	}
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

    public GridPoint getCenter() 
    {
	return new GridPoint(size/2, size/2);
    }
    
    public void set( int index, GridPoint p, Direction from )
    {
	AcidInChain a = acids[index];
	cells[p.y][p.x] = a;
	a.xyz = p;
	if (index > 0) {
	    acids[index-1].setNext(from);
	}
    }

    protected void unset( GridPoint p) 
    {
	this.unset(p.x, p.y);
    }

    protected void unset( GridPoint p, int index ) 
    {
	unset( cells[p.y][p.x], index, p.x, p.y); 
    }

    protected void unset( AcidInChain a )
    {
	if (( a != null ) && (a.xyz != null)) {
	    unset( a.xyz.x, a.xyz.y );
	}
    }

    protected void unset( int x, int y ) 
    {
	AcidInChain a = cells[y][x];
	if (a != null) {
	    unset( a, a.getIndex(), x, y);
	}
    }

    private void unset( AcidInChain a, int index, int x, int y )
    {
	cells[y][x] = null;
	a.xyz = null;;
	if (index > 0) {
	    acids[index-1].setNext(Direction.none);
	}
    }
	
    protected AcidInChain get( GridPoint p )
    {
	return this.get(p.x, p.y);
    }

    protected AcidInChain get( int x, int y )
    {
	return cells[y][x];
    }
	
    protected Direction getDirection(GridPoint p1, GridPoint p2) 
    {
	return getDirection(p1.x, p1.y, p2.x, p2.y);
    }

    protected Direction getDirection
	( int x1, int y1, int x2, int y2, int z1, int z2 )
    {
	return this.getDirection( x1, y1, x2, y2 );
    }

    protected abstract Direction getDirection(int x1, int y1, int x2, int y2);

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
// 	AcidInChain from = get(nextCell(d, x, y));
	AcidInChain from = get(nextCell(d, p));
	if ( from != null ) {
	    pp.addNeighbor(to, from);
	}
    }

    protected GridPoint getMin() 
    {
	int minX = size;
	int minY = size;
	for ( int i = 0; i < numAcids; i++ ) {
	    AcidInChain a = pp.getAminoAcid(i);
	    if (a.xyz.x < minX) minX = a.xyz.x;
	    if (a.xyz.y < minY) minY = a.xyz.y;
	}
	return new GridPoint(minX, minY);
    }

    protected GridPoint getMax() 
    {
	int maxX = 0;
	int maxY = 0;
	for ( int i = 0; i < numAcids; i++ ) {
	    AcidInChain a = pp.getAminoAcid(i);
	    if (a.xyz.x > maxX) maxX = a.xyz.x;
	    if (a.xyz.y > maxY) maxY = a.xyz.y;
	}
	return new GridPoint(maxX, maxY);
    }

}    
