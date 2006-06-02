// $folding/GridCanvas.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota


// .1 .2 .3 .4 .5 .6 .7 .8 

import java.awt.*;
import java.util.*;

/**
 * Display a Grid.
 * 
 */
public abstract class GridCanvas extends Canvas
{
    protected int cellRadius = 20;
    protected int cellDiameter = 2*cellRadius;
    protected int size; // 2*numAcids + 1

    protected Grid grid = null;
    protected int numAcids;
    protected Polypeptide pp;

    private static final Color COLOR_BACKGROUND = 
	new Color( (float)0.7, (float)0.7, (float)1.0);
    private static final Color BLACK_BACKGROUND = 
	new Color( (int) 175, (int) 175, (int) 175);
          // careful here. 175 is half of 100-250

    private boolean blackColoring;              // flag for black / color coloring

    public GridCanvas(int width, int height )
    {
	blackColoring = FoldingManager.getInstance().getBlackColoring();
	if( blackColoring)
	    this.setBackground( BLACK_BACKGROUND);
	else
	    this.setBackground( COLOR_BACKGROUND);

	this.setSize(width, height);
    }
	
    protected void setGrid( Grid grid ) {
	this.grid = grid;
	this.size = grid.getSize();
	this.pp   = grid.getPP();
// 	numAcids  = (size-1)/2;
	numAcids  = pp.getLength();
    }

    protected GridPoint getMin() 
    {
	int minX = size;
	int minY = size;
	int minZ = size;
	for ( int i = 0; i < numAcids; i++ ) {
	    AcidInChain a = pp.getAminoAcid(i);
	    if (a.xyz.x < minX) minX = a.xyz.x;
	    if (a.xyz.y < minY) minY = a.xyz.y;
	    if (a.xyz.z < minZ) minZ = a.xyz.z;
	}
	return new GridPoint(minX-1, minY-1, minZ-1);
    }

    private GridPoint getMin( GridPoint[] points ) 
    {
	int minX = Integer.MAX_VALUE;
	int minY = Integer.MAX_VALUE;
	int minZ = Integer.MAX_VALUE;
	for ( int i = 0; i < points.length; i++ ) {
	    if (points[i].x < minX) minX = points[i].x;
	    if (points[i].y < minY) minY = points[i].y;
	    if (points[i].z < minZ) minZ = points[i].z;
	}
	return new GridPoint(minX - cellDiameter, minY - cellDiameter, minZ);
    }

    protected GridPoint getMax() 
    {
	int maxX = 0;
	int maxY = 0;
	int maxZ = 0;
	for ( int i = 0; i < numAcids; i++ ) {
	    AcidInChain a = pp.getAminoAcid(i);
	    if (a.xyz.x > maxX) maxX = a.xyz.x;
	    if (a.xyz.y > maxY) maxY = a.xyz.y;
	    if (a.xyz.z > maxZ) maxZ = a.xyz.z;
	}
	return new GridPoint(maxX, maxY, maxZ);
    }

    /**
     * Compute a plane projection of a (possibly
     * three dimensional) GridPoint to use to paint an
     * AminoAcid on this GridCanvas.
     *
     * @param p the point to project.
     *
     * @return the projection.
     */
    protected abstract GridPoint project( GridPoint p );

    protected int getAcidRadius() 
    {
	return cellRadius;
    }

    /**
     *   Returns constants used for center the name of the polypeptide
     */
    protected int getStringIndentationConstant( String name, int r)
    {
	// the values returned are hardcoded with values that 
	//   look best when the canvas is drawn. Their value
	//   was establish through trials, and best was picked.

	int length = name.trim().length();
	if( length == 1)                      // 1
	    return 0;
	else if( length == 2)                 // -1
	    return 0;
	else if( length == 3)                 // 0.x
	    return (int) (1/8f * r);
	else if( length == 4)                 // -0.x
	    return (int) (1/2f * r);      
	else if( length == 5)                 // -0.xx
	    return (int) (2/3f * r);
	else // length == 6. can't be longer.    -0.xxx
	    return (int) (3/4f * r);
    }

    public void paint( Graphics g )
    {
	// badly design. Code of two block of statements is almost entirely the same.
	//  fix: either redesign ColorCoder and use polymorphism
	//       or just split the ColorCoder creation and usage 
	//          i.e. when the disk is drawn in the adequate color. 

	if( blackColoring){
	    
	    //////////////////////////////
	    //    COLORING IN BLACK     //
	    //////////////////////////////

	    BlackColorCoder cc = new BlackColorCoder(pp.getTable().getContrastScaler()); /// changed
	    if (grid == null) return;
	    GridPoint[] spots    = new GridPoint[ numAcids ];
	    AcidInChain[] acidsByZ = new AcidInChain[ numAcids ];
	    for (int i = 0; i < numAcids; i++) {
		AcidInChain a = pp.getAminoAcid(i);
		spots[i]    =  project(a.xyz);
		acidsByZ[i] = a;
	    }
	    GridPoint min = getMin(spots);
	    for (int i = 0; i < numAcids; i++) {
		spots[i] = spots[i].subtract(min);
	    }
	    Arrays.sort( acidsByZ, new SortByZ() );
	    // 	paintEmpties(g);
	    int r = getAcidRadius();
	    for ( int i = 0; i < numAcids; i++ ) {
		AcidInChain a = acidsByZ[i];
		GridPoint here = project(a.xyz).subtract(min);
		
		// fills the circle
		g.setColor( cc.getCellColor( a.getNormalizedHydrophobicIndex() ) );		
		g.fillOval( here.x - r, here.y - r, 2*r, 2*r );

		// draws the circle, on top on the color disk
		g.setColor( Color.black);		
		g.drawOval( here.x - r, here.y - r, 2*r, 2*r );
	    }
	    // connect neighbors here
	    if ( grid instanceof ThreeDGrid ) {
		g.setColor( Color.white );
		for ( int i = 0; i < numAcids; i++ ) {
		    AcidInChain a = pp.getAminoAcid(i);
		    Set nbrs = a.getNeighbors();
		    Iterator iter = nbrs.iterator();
		    while (iter.hasNext()) {
			int j = ((Integer)iter.next()).intValue();
			g.drawLine(spots[i].x, spots[i].y, spots[j].x, spots[j].y);
			           
		    }
		}
	    }

	    // draw the backbone
	    g.setColor( Color.black );
	    for ( int i = 0; i < numAcids; i++ ) {
		AcidInChain a = pp.getAminoAcid(i);
		int offset =  getStringIndentationConstant( a.name, r);
		//System.out.println("Name is:     "+a.name);
		//System.out.println("Radius is:   "+r);
		//System.out.println("Constant is: "+getStringIndentationConstant( a.name, r));
		g.drawString(a.name, spots[i].x - offset, spots[i].y);
		                 // string is drawn to an left offset from center of disk.;
		if ( i < numAcids -1 ) {
		    g.drawLine(spots[i].x, spots[i].y, spots[i+1].x, spots[i+1].y);
		}
	    }
	}
	else{

	    /////////////////////////////
	    //    COLORING IN COLOR    //
	    /////////////////////////////

	    ColorCoder cc = new ColorCoder(pp.getTable().getContrastScaler()); /// changed
	    if (grid == null) return;
	    GridPoint[] spots    = new GridPoint[ numAcids ];
	    AcidInChain[] acidsByZ = new AcidInChain[ numAcids ];
	    for (int i = 0; i < numAcids; i++) {
		AcidInChain a = pp.getAminoAcid(i);
		spots[i]    =  project(a.xyz);
		acidsByZ[i] = a;
	    }
	    GridPoint min = getMin(spots);
	    for (int i = 0; i < numAcids; i++) {
		spots[i] = spots[i].subtract(min);
	    }
	    Arrays.sort( acidsByZ, new SortByZ() );
	    // 	paintEmpties(g);
	    int r = getAcidRadius();
	    for ( int i = 0; i < numAcids; i++ ) {
		AcidInChain a = acidsByZ[i];
		g.setColor( cc.getCellColor( a.getNormalizedHydrophobicIndex() ) );
		GridPoint here = project(a.xyz).subtract(min);
		g.fillOval( here.x - r, here.y - r, 2*r, 2*r );
	    }
	    // connect neighbors here
	    if ( grid instanceof ThreeDGrid ) {
		g.setColor( Color.white );
		for ( int i = 0; i < numAcids; i++ ) {
		    AcidInChain a = pp.getAminoAcid(i);
		    Set nbrs = a.getNeighbors();
		    Iterator iter = nbrs.iterator();
		    while (iter.hasNext()) {
			int j = ((Integer)iter.next()).intValue();
			g.drawLine(spots[i].x, spots[i].y, spots[j].x, spots[j].y);
		    }
		}
	    }

	    // draw the backbone
	    g.setColor( Color.black );
	    for ( int i = 0; i < numAcids; i++ ) {
		AcidInChain a = pp.getAminoAcid(i);
		int offset = getStringIndentationConstant( a.name, r);
		g.drawString(a.name, spots[i].x - offset, spots[i].y);
		          // string is drawn to an left offset from center of disk.; 
		if ( i < numAcids -1 ) {
		    g.drawLine(spots[i].x, spots[i].y, spots[i+1].x, spots[i+1].y);
		}
	    }

	}
    }

    private class SortByZ implements Comparator 
    {
	public int compare( Object o1, Object o2 )
	{
	    AcidInChain a1 = (AcidInChain)o1;
	    AcidInChain a2 = (AcidInChain)o2;
	    return (a1.xyz.z - a2.xyz.z);
	}
    }

    private void drawDottedLine( Graphics g, int x1, int y1, int x2, int y2 )
    {
    }

    // for hex grid - never called
    private void paintEmpties(Graphics g)
    {
	g.setColor( Color.black );
	for (int row = 0; row < size; row++) {
	    int rowstart = (row <= numAcids) ?
		numAcids - row :
		0 ;
	    int rowend = (row <= numAcids) ?
		size :
		size - row + numAcids;
	    for (int col = rowstart; col < rowend; col++) {
		GridPoint here = new GridPoint(row, col);
// redo if needed		g.drawOval(
// 			   getCenterX(here) - cellRadius, 
// 			   getCenterY(here) - cellRadius,
// 			   cellDiameter, cellDiameter);
		// commented out code draws the grid
// 		hexagon.translate(getCornerX(row, col),
// 				  getCornerY(row,col));
// 		g.drawPolygon(hexagon);
// 		hexagon.translate(-getCornerX(row, col),
// 				  -getCornerY(row,col));		
	    }
	}
    }

//     private float[] redHSB = Color.RGBtoHSB(
// 					    Color.RED.getRed(),
// 					    Color.RED.getGreen(),
// 					    Color.RED.getBlue(),
// 					    new float[3]);
//     private float[] greenHSB = Color.RGBtoHSB(
// 					    Color.GREEN.getRed(),
// 					    Color.GREEN.getGreen(),
// 					    Color.GREEN.getBlue(),
// 					    new float[3]);
// 
    // Map the range -1.0 .. 1.0 of hydrophobic indices h to
    // a continuum of colors between RED and GREEN.
    //
    // In HSB space halfway from RED to GREEN is YELLOW, which
    // makes sense. 
    //
    // First map h linearly to x between 0.0 and 1.1 so that we
    // can form convex combinations. But using that x to weight
    // the colors changes them too shallowly near the ends and
    // too steeply in the middle. I get better visual effect 
    // when I replace x by 
    //
    //                 f(x) = ax^3 + bx^2 + cx
    //
    // f(0) = 0 puts GREEN at one end. Then adjust a, b and c so that
    // f(1) = 1 (RED), f(1/2) = 1/2 (YELLOW), and f'(1/2) = k. 
    // Then k determines the slope on the whole interval.
    //
    // k = 0 is horizontal in the middle, which is too shallow there.
    // k = 1 is just the linear function we started with. 
    // k = 1/2 seems about right.

//     private float k = (float)0.5;

    // It's easy to solve for a, b and c in terms of k:

//     private float a =  4 - 4*k;
//     private float b = -6 + 6*k;
//     private float c =  3 - 2*k;
// 
//     protected Color getCellColor( float hydrophobicIndex ) 
//     {
// 	float x = (hydrophobicIndex + 1)/2; // map [-1,1] to [0,1]
// 	float alpha = x*(c+x*(b+a*x)); // polynomial evaluation trick
// 	float oneMinusAlpha = (float)1.0 - alpha;
// 	return Color.getHSBColor
// 	    ( alpha*redHSB[0] + oneMinusAlpha*greenHSB[0],
// 	      alpha*redHSB[1] + oneMinusAlpha*greenHSB[1],
// 	      alpha*redHSB[2] + oneMinusAlpha*greenHSB[2] );
//     }
}




