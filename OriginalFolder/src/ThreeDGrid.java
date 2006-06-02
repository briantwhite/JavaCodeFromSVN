// ThreeDGrid.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota


import java.util.*;

/**
 * Model a three dimensional grid containing a polypeptide chain.
 */
public abstract class ThreeDGrid extends Grid
{

    protected AcidInChain[][][] cells;
    protected GridPoint[][][] points;

    public ThreeDGrid( )
    {
    }

    public ThreeDGrid( Polypeptide pp ) 
    {
	super(pp);
	cells    = new AcidInChain[size][size][size];
	points   = new GridPoint[size][size][size];
	for (int i = 0; i < size; i++) {
	    for (int j = 0; j < size; j++) {
		for (int k = 0; k < size; k++) {
		    cells[i][j][k]  = null;
		    points[i][j][k] = new GridPoint(i,j,k);
		}
	    }
	}
    }

    public int getSize()
    {
	return size;
    }

    public Polypeptide getPP()
    {
	return pp;
    }

    public GridPoint getCenter() 
    {
	return points[size/2][size/2][size/2];
    }
    
    public void set( int index, GridPoint p, Direction from )
    {
	AcidInChain a = acids[index];
	cells[p.y][p.x][p.z] = a;
	a.xyz = p;
	if (index > 0) {
	    acids[index-1].setNext(from);
	}
    }

    protected void unset( GridPoint p) 
    {
	this.unset(p.x, p.y, p.z);
    }

    protected void unset( GridPoint p, int index ) 
    {
	unset( cells[p.y][p.x][p.z], index, p.x, p.y, p.z); 
    }

    protected void unset( AcidInChain a )
    {
	if (( a != null ) && (a.xyz != null)) {
	    unset( a.xyz.x, a.xyz.y, a.xyz.z );
	}
    }
	    
    protected void unset( int x, int y, int z ) 
    {
	AcidInChain a = cells[y][x][z];
	if (a != null) {
	    unset( a, a.getIndex(), x, y, z);
	}
    }

    private void unset( AcidInChain a, int index, int x, int y, int z )
    {
	cells[y][x][z] = null;
	a.xyz = null;;
	if (index > 0) {
	    acids[index-1].setNext(Direction.none);
	}
    }
	
    protected AcidInChain get( GridPoint p )
    {
	return this.get(p.x, p.y, p.z);
    }

    protected AcidInChain get( int x, int y, int z )
    {
	return cells[y][x][z];
    }

    protected Direction getDirection(GridPoint p1, GridPoint p2, GridPoint p3)
    {
	return this.getDirection( p1, p2 );
    }

    protected abstract Direction[] getAllDirections();

    protected Direction[] allDirections = null;

    protected abstract GridPoint nextCell(Direction direction, GridPoint p );

    public abstract Direction[] getThirdPlacement();

    // statistics

    protected double energy;
    protected int freeEdges;

    // test killing this and seeing if all else works still
    public double getEnergy() 
    {
// 	System.out.println(pp);
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
