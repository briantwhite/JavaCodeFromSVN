// SquareGrid.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota


import java.util.*;

/**
 * Model a grid of squares containing a polypeptide chain.
 * 
 * Use coordinate system to get square neighbors:
 *
 * 0-0 1-0 2-0 3-0
 * 0-1 1-1 2-1 3-1
 * 0-2 1-2 2-2 3-2
 */
public class SquareGrid extends TwoDGrid
{
    public SquareGrid( Polypeptide pp ) 
    {
	super(pp);
	allDirections = getAllDirections();
	setNextDirectionsStraight();
	setNextDirectionsBent();
	setNextDirections();
	// following should go in superclass constructor but fails there
	if (pp.isFolded()) {
	    GridPoint currentP = getCenter();
	    set(0, currentP, Direction.none);

	    Direction d = pp.getNextDirection(0);
	    for (int i = 1; i < numAcids; i++ ) {
		currentP = nextCell( d, currentP );
		set(i, currentP, d);
		d = pp.getNextDirection(i);
	    }
	}
    }

    protected Direction[] getAllDirections() 
    {
	Direction[] all = {Direction.E, 
			   Direction.N, 
			   Direction.W, 
			   Direction.S};
	return all;
    }

    public Direction[] getThirdPlacement()
    {
	Direction[] directions = {Direction.E, Direction.N};
	return directions;
    }

    public String toString()
    {
	StringBuffer buf = new StringBuffer();
	for (int row = 0; row < size; row++) {
	    for (int col = 0; col < size; col++) {
		buf.append( cells[row][col] == null ? "---" :
			    cells[row][col].name );
		buf.append(' ');
	    }
	    buf.append('\n');
	}
	return buf.toString();
    }

    protected Direction getDirection(int x1, int y1, int x2, int y2)
    {
	if (y1 == y2) {
	    if (x1+1 == x2) return E;
	    return W;
	}
	if (x1 == x2) {
	    if (y1+1 == y2) return S;
	    return N;
	}
	return Direction.none;
    }

    protected GridPoint nextCell(Direction direction, GridPoint p)
    {
	int x = p.x;
	int y = p.y;
	if (direction == E) return points[x+1][y];
	if (direction == W) return points[x-1][y];
	if (direction == N) return points[x][y-1];
	if (direction == S) return points[x][y+1];
	if (direction == Direction.none) return p;
	return null;
    }

    // tools for Directions
    public final Direction E = Direction.E;
    public final Direction W = Direction.W;
    public final Direction N = Direction.N;
    public final Direction S = Direction.S;

    // coming from some Direction, try to continue in the same
    // Direction. Make counterclockwise turn if you must turn.

    protected Direction[] getDirectionRing3()
    {  Direction[] ring = 
	    { E, N, W, S,
	      E, N, W, S,
	      E, N, W, S };
	return ring;
    }

//     public void setNextDirections()
//     {
// 	for (int i=numDirections; i < 2*numDirections; i++) {
// 	    Direction[] next = new Direction[numDirections -1];
// 	    next[0] = ring[i];
// 	    for (int j = 1; j <= numDirections/2 ; j+=2) {
// 		next[j]   = ring[i+j];
// 		next[j+1] = ring[i-j];
// 	    }
// 	    System.out.print(ring[i] + ":");
// 	    for (int k = 0; k < numDirections-1; k++ ) {
// 		System.out.print(" " + next[k]);
// 	    }
// 	    System.out.println();
// 	    nextDirection.put( ring[i], next);
// 	}
//     }
// 
//     public void oldsetNextDirections()
//     {
// 	Direction[] next = new Direction[3];
// 	next[0]=E; next[1]=N; next[2]=S;
// 	nextDirection.put( E, next);
// 
// 	next = new Direction[3];
// 	next[0]=S; next[1]=E; next[2]=W;
// 	nextDirection.put(S, next);
// 
// 	next = new Direction[3];
// 	next[0]=W; next[1]=S; next[2]=N;
// 	nextDirection.put(W, next);
// 
// 	next = new Direction[3];
// 	next[0]=N; next[1]=W; next[2]=E; 
// 	nextDirection.put( N, next);
//     }
// 
//    public static void main( String[] args )
//    {
///	System.out.println("on hex grid");
//	Polypeptide pp = new Polypeptide( args );
//             pp has to be created from manager.
//	System.out.println(pp);
//	Grid g         = new HexGrid(pp);
// 	Folder f       = new BruteForceFolder(pp, g);
// 	Folder f       = new IncrementalFolder(pp, g);
//	f.fold();
//	System.out.println(f.report());
//	System.out.println(g);
//
//	System.out.println("on square grid");
//	pp = new Polypeptide( args );
//	g  = new SquareGrid(pp);
//	f  = new BruteForceFolder(pp, g);
//	f.fold();
//	System.out.println(f.report());
//	System.out.println(g);
//    }
}    
