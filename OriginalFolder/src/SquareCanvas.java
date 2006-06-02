// SquareCanvas.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota


import java.awt.*;

/**
 * Display a Grid of squares.
 * 
 * Use coordinate system to get neighbors:
 *
 * 0-0 1-0 2-0 3-0
 * 0-1 1-1 2-1 3-1
 * 0-2 1-2 2-2 3-2
 */
public class SquareCanvas extends GridCanvas
{
    private int cellHeight   = 2*cellRadius;
    private Polygon square;

    public SquareCanvas(int width, int height )
    {
	super(width, height);

	// this creates a square of the proper size
	// NOT TESTED
	square = new Polygon();
	square.addPoint(0,0);	
	square.addPoint(cellDiameter, 0);
	square.addPoint(cellDiameter, cellDiameter);
	square.addPoint(0, cellDiameter);
    }

    protected void setGrid( Grid grid ) {
	super.setGrid(grid);
// 	GridPoint min = grid.getMin();
	GridPoint min = getMin();
// 	GridPoint max = grid.getMax();
	GridPoint max = getMax();
	// probably need to change this:
	this.setSize( cellRadius*2*(max.x - min.x + max.y - min.y + 3) ,
		      (1+cellHeight)*(max.y - min.y + 3));
    }

    protected int getAcidRadius() 
    {
	return cellRadius;
    }

    protected GridPoint project( GridPoint p ) 
    {
	return new GridPoint( (1+2*p.x)*cellRadius, 
			      (1+2*p.y)*cellRadius );
			      
    }
}

