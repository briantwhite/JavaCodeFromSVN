// CubicGrid.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota


import java.util.*;

/**
 * Model a grid of cubes containing a polypeptide chain.
 * 
 * Use coordinate system to get cubic neighbors:
 *
 * 0-0-0 1-0-0 2-0-0 3-0-0
 * 0-1-0 1-1-0 2-1-0 3-1-0
 * 0-2-0 1-2-0 2-2-0 3-2-0
 *
 * ...
 * 0-0-3 1-0-3 2-0-3 3-0-3
 * 0-1-3 1-1-3 2-1-3 3-1-3
 * 0-2-3 1-2-3 2-2-3 3-2-3
 */
public class CubicGrid extends ThreeDGrid
{
    public CubicGrid( Polypeptide pp ) 
    {
	super(pp);
	allDirections = getAllDirections();
	setNextDirectionsStraight();
	setNextDirectionsBent();
	setNextDirections();

// 	this.pp  = pp;
// 	numAcids = pp.getLength();
// 	acids    = pp.getAcidArray();
// 	size     = 2*numAcids + 1;
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
	allDirections = getAllDirections();
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
			   Direction.S,
			   Direction.U,
			   Direction.D};
	return all;
    }

    public Direction[] getThirdPlacement()
    {
	Direction[] directions = {
	    Direction.E, 
	    Direction.N,
	    Direction.U};
	return directions;
    }

    public String toString()
    {
	StringBuffer buf = new StringBuffer();
	for (int lyr = 0; lyr < size; lyr++) {
	    for (int row = 0; row < size; row++) {
		for (int col = 0; col < size; col++) {
		    buf.append( cells[row][col][lyr] == null ? "---" :
				cells[row][col][lyr].name );
		    buf.append(' ');
		}
		buf.append('\n');
	    }
	    buf.append('\n');
	}
	return buf.toString();
    }

    protected Direction getDirection(GridPoint p1, GridPoint p2)
    {
	int x1 = p1.x; 
	int y1 = p1.y; 
	int z1 = p1.z;
	int x2 = p2.x; 
	int y2 = p2.y; 
	int z2 = p2.z;

	if (( x1 == x2) && (y1 == y2)) {
	    if (z1+1 == z2) return U;
	    return D;
	}
	if (( y1 == y2) && (z1 == z2)) {
	    if (x1+1 == x2) return E;
	    return W;
	}
	if (( z1 == z2) && (x1 == x2)) {	
	    if (y1+1 == y2) return S;
	    return N;
	}
	return Direction.none;
    }

//     protected Direction getDirection(int x1, int y1, int x2, int y2)
//     {
// 	if (y1 == y2) {
// 	    if (x1+1 == x2) return E;
// 	    return W;
// 	}
// 	if (x1 == x2) {
// 	    if (y1+1 == y2) return S;
// 	    return N;
// 	}
// 	return Direction.none;
//     }

    protected GridPoint nextCell(Direction direction, GridPoint p)
    {
// 	System.out.println(p);
	int x = p.x;
	int y = p.y;
	int z = p.z;
	if (direction == E) return points[x+1][y][z];
	if (direction == W) return points[x-1][y][z];
	if (direction == N) return points[x][y-1][z];
	if (direction == S) return points[x][y+1][z];
	if (direction == U) return points[x][y][z+1];
	if (direction == D) return points[x][y][z-1];
	if (direction == Direction.none) return p;
// 	return points[x][y][z];
	return null;
    }

    // tools for Directions
    public final Direction E = Direction.E;
    public final Direction W = Direction.W;
    public final Direction N = Direction.N;
    public final Direction S = Direction.S;
    public final Direction U = Direction.U;
    public final Direction D = Direction.D;

    // coming from some Direction, try to continue in the same
    // Direction. Make right handed coordinate turn if you must turn.

    protected Direction[] getDirectionRing3()
    {  Direction[] ring = 
	    { E, N, U, W, S, D, 
	      E, N, U, W, S, D, 
	      E, N, U, W, S, D };
	return ring;
    }

//     public void setNextDirections()
//     {
// 	Direction[] next = new Direction[5];
// 	next[0]=E; next[1]=N; next[2]=U; next[3]=S; next[4]=D;
// 	nextDirection.put( E, next);
// 
// 	next = new Direction[5];
// 	next[0]=S; next[1]=E; next[2]=U; next[3]=W; next[4]=D;
// 	nextDirection.put(S, next);
// 
// 	next = new Direction[5];
// 	next[0]=W; next[1]=S; next[2]=U; next[3]=N; next[4]=D;
// 	nextDirection.put(W, next);
// 
// 	next = new Direction[5];
// 	next[0]=N; next[1]=W; next[2]=U; next[3]=E; next[4]=D; 
// 	nextDirection.put( N, next);
// 
// 	next = new Direction[5];
// 	next[0]=U; next[1]=E; next[2]=N; next[3]=W; next[4]=S; 
// 	nextDirection.put( U, next);
// 
// 	next = new Direction[5];
// 	next[0]=D; next[1]=W; next[2]=N; next[3]=E; next[4]=S;
// 	nextDirection.put( D, next);
//     }

    public static void main( String[] args )
    {
// 	System.out.println("on hex grid");
// 	Polypeptide pp = new Polypeptide( new String(args) );
// 	System.out.println(pp);
// 	Grid g         = new HexGrid(pp);
//  	Folder f       = new BruteForceFolder(pp, g);
// 	Folder f       = new IncrementalFolder(pp, g);
// 	f.fold();
// 	System.out.println(f.report());
// 	System.out.println(g);

//	System.out.println("on cubic grid");
//	Polypeptide pp;          //  = new Polypeptide( args );
//	pp = PolypeptideFactory.getInstance().createPolypeptide
//		(new String(args),
//		 false,
//		 false,
//		 "",
//		 "",
//		 "virtual");
//	System.out.println(pp);
//	Grid g  = new CubicGrid(pp);
//	Folder f  = new BruteForceFolder(pp, g);
//	f.fold();
//	System.out.println(f.report());
//	System.out.println(g);
    }
}    
