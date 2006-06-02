// DodecahedralGrid.java
//
// 
// Copyright 2004, Ethan Bolker and Bogdan Calota


import java.util.*;

/**
 * Model a grid of rhombic dodecahedra containing a polypeptide chain.
 * 
 * In standard coordinate system, occupiable cells are those for which
 * the sum of the coordinates is even.
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
public class DodecahedralGrid extends ThreeDGrid
{
    public DodecahedralGrid( Polypeptide pp ) 
    {
	super(pp);
	allDirections = getAllDirections();
	setNextDirectionsStraight();
	setNextDirectionsBent();
	setNextDirections();
// 
// 	this.pp  = pp;
// 	numAcids = pp.getLength();
// 	acids    = pp.getAcidArray();
	size     = 4*numAcids + 1;
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

    // tools for Directions
    public final Direction NW = Direction.NW;
    public final Direction NE = Direction.NE;
    public final Direction SW = Direction.SW;
    public final Direction SE = Direction.SE;

    public final Direction NU =  Direction.NU;
    public final Direction ND =  Direction.ND;
    public final Direction SU =  Direction.SU;
    public final Direction SD =  Direction.SD;

    public final Direction EU =  Direction.EU;
    public final Direction ED =  Direction.ED;
    public final Direction WU =  Direction.WU;
    public final Direction WD =  Direction.WD;


    protected Direction[] getAllDirections() 
    {
	Direction[] all = {NE,NW,SE,SW,NU,ND,SU,SD,EU,ED,WU,WD};
	return all;
    }

    protected Direction getFirstDirection()
    {
	return NE;
    }

    public Direction[] getThirdPlacement()
    {
	Direction[] directions = { NE, ND, NW, NU, EU, WU };
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
	if (z1 == z2 ) {
	    if (y1+1 == y2) {
		if (x1+1 == x2) return NE;
		return NW;
	    }
	    if (x1+1 == x2) return SE;
	    return SW;
	}
	if (y1 == y2) {
	    if (z1+1 == z2) {
		if (x1+1 == x2) return EU;		
		return WU;
	    }
	    if (x1+1 == x2) return ED;		
	    return WD;	    
	}
	if (x1 == x2) {
	    if (y1+1 == y2) {
		if (z1+1 == z2) return NU;
		return ND;
	    }
	    if (z1+1 == z2) return SU;
	    return SD;
	}
	return Direction.none;
    }

    protected GridPoint nextCell(Direction direction, GridPoint p)
    {
// 	System.out.println(p);
	int x = p.x;
	int y = p.y;
	int z = p.z;
	//                                 EW   NS   UD
	if (direction == NE) return points[x+1][y+1][z];
	if (direction == NW) return points[x-1][y+1][z];
	if (direction == SE) return points[x+1][y-1][z];
	if (direction == SW) return points[x-1][y-1][z];

	if (direction == NU) return points[x][y+1][z+1];
	if (direction == ND) return points[x][y+1][z-1];
	if (direction == SU) return points[x][y-1][z+1];
	if (direction == SD) return points[x][y-1][z-1];

	if (direction == EU) return points[x+1][y][z+1];
	if (direction == ED) return points[x+1][y][z-1];
	if (direction == WU) return points[x-1][y][z+1];
	if (direction == WD) return points[x-1][y][z-1];

	if (direction == Direction.none) return p;
	return null;
    }


    // coming from some Direction, try to continue in the same
    // Direction. Make right handed coordinate turn if you must turn.

    protected Direction[] getDirectionRing3()
    {
	return null;
    }

    protected int getNumDirections()
    {
	return 12;
    }

    public void setNextDirectionsStraight()
    {
	straightMap = new HashMap();
	Direction[] nextNE = {NE,NU,EU,ED,ND,NW,SE,SD,WD,WU,SU}; // 1
	straightMap.put(NE, nextNE);
	Direction[] nextNU = {NU,EU,NE,NW,WU,SU,ND,WD,SW,SE,ED}; // 2
	straightMap.put(NU, nextNU);	
	Direction[] nextNW = {NW,ND,WD,WU,NU,NE,SW,SU,EU,ED,SD}; // 9
	straightMap.put(NW, nextNW);	
	Direction[] nextND = {ND,NE,ED,WD,NW,NU,SD,SW,WU,EU,SE}; // 10
	straightMap.put(ND, nextND);	

	Direction[] nextSE = {SE,EU,SU,SD,ED,NE,SW,WD,ND,NU,WU}; // 4
	straightMap.put(SE, nextSE);	
	Direction[] nextSU = {SU,WU,SW,SE,EU,NU,SD,ED,NE,NW,WD}; // 5
	straightMap.put(SU, nextSU);	
	Direction[] nextSW = {SW,SU,WU,WD,SD,SE,NW,ND,ED,EU,NU}; // 7
	straightMap.put(SW, nextSW);	
	Direction[] nextSD = {SD,SW,WD,ED,SE,SU,ND,NE,EU,WU,NW}; // 11
	straightMap.put(SD, nextSD);	

	Direction[] nextEU = {EU,NE,NU,SU,SE,ED,WU,SW,SD,ND,NW}; // 3
	straightMap.put(EU, nextEU);	
	Direction[] nextED = {ED,ND,NE,SE,SD,WD,EU,SU,SW,NW,NU}; // 12
	straightMap.put(ED, nextED);	
	Direction[] nextWU = {WU,SW,SU,NU,NW,WD,EU,NE,ND,SD,SE}; // 6
	straightMap.put(WU, nextWU);	
	Direction[] nextWD = {WD,NW,ND,SD,SW,WU,ED,SE,SU,NU,NE}; // 8
	straightMap.put(WD, nextWD);	
    }

    public static void main( String[] args )
    {
// 	System.out.println("on hex grid");
// 	Polypeptide pp = new Polypeptide( args );
// 	System.out.println(pp);
// 	Grid g         = new HexGrid(pp);
//  	Folder f       = new BruteForceFolder(pp, g);
// 	Folder f       = new IncrementalFolder(pp, g);
// 	f.fold();
// 	System.out.println(f.report());
// 	System.out.println(g);

//	System.out.println("on Dodecahedral grid");
//	Polypeptide pp; // = new Polypeptide( args );
//	pp = PolypeptideFactory.getInstance().createPolypeptide
//		(args,
//		 false,
//		 true,
//		 "",
//		 "",
//		 "virtual");
//	System.out.println(pp);
//	Grid g  = new DodecahedralGrid(pp);
//	Folder f  = new BruteForceFolder(pp, g);
//	f.fold();
//	System.out.println(f.report());
//	System.out.println(g);
    }
}    
