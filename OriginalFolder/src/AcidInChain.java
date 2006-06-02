// AcidInChain.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota


import java.util.*;

/**
 * An AcidInChain refers to an AminoAcid and has fields 
 * and methods to manage the placement of that AminoAcid
 * in a Polypeptide on a Grid.
 */

public class AcidInChain 
{

    // These are protected so that some algorithms can get to them 
    // without the overhead of a method call. They have some getters 
    // and setters too, for normal use.

    protected  Direction next; // Direction to next AminoAcid

    protected GridPoint xyz;   // coordinates on Grid
//     protected int x;          // coordinates on Grid - remove
//     protected int y;

    private   int index;      // location in Polypeptide

    protected AminoAcid a;

    /// replace these with calls to getters ...
    protected String name;             // a.name, a hack
    protected double hydrophobicIndex; // a.hydrophobicIndex, a hack

    private TreeSet neighbors; // AminoAcids touching this one

    public AcidInChain( AminoAcid a, int index ) 
    {
	this.a = a;
	next = Direction.none;
	neighbors = new TreeSet();
	this.index = index;
	name = a.name;
        hydrophobicIndex = a.hydrophobicIndex;

    }
    
    public AcidInChain( double hydroPhobicIndex, int index )
    {
	this (new AminoAcid(hydroPhobicIndex), index);
// 	a = new AminoAcid(hydroPhobicIndex);
// 	next = Direction.none;
// 	neighbors = new TreeSet();
// 	this.index = index;
// 	name = a.name;
//         hydrophobicIndex = a.hydrophobicIndex;
    }

    /**
     * Indicate that in a folded Polypeptide this acid is a neighbor
     * of another. Don't record the obvious cases: every acid is a neighbor
     * of its successor and its predecessor.
     *
     * @param neighbor an Integer wrapping the index of the neihboring acid.
     */

    public void addNeighbor( Integer neighbor ) 
    {
	int i = neighbor.intValue();
	if ( (i != index-1) && (i != index+1) ) {
	    neighbors.add( neighbor );
	}
    }

    public double getHydrophobicIndex()
    {
	return a.getHydrophobicIndex();
    }

    public float getNormalizedHydrophobicIndex()
    {
	return a.getNormalizedHydrophobicIndex();
    }

    public String getName()
    {
	return a.name;
    }
    
    public int getIndex()
    {
	return index;
    }

    public Set getNeighbors()
    {
	return neighbors;
    } 
	
//     public void set( int x, int y ) 
//     {
// 	this.x = x;
// 	this.y = y;
//     }

    public GridPoint getPoint() 
    {
	return xyz;
// 	return new GridPoint(x,y);
    }

    public void setNext( Direction next) 
    {
	this.next = next;
    }

    public Direction getNext()
    {
	return next;
    }

    public String toString() 
    {
// 	return a.toString() + xyz + " : " + next;
	return a.toString() + " : " + next;
    }
}
