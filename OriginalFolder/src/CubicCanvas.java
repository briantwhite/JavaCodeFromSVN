// CubicCanvas.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota


/**
 * Display a Grid of cubes.
 * 
 * Use coordinate system to get neighbors:
 *
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
public class CubicCanvas extends GridCanvas
{
    private int cellHeight   = 2*cellRadius;

    private int xOffset    = (int)(0.6  * cellRadius);
    private int yOffset    = (int)(0.7  * cellRadius);
    private int acidRadius = (int)(0.8 * cellRadius);

    public CubicCanvas(int width, int height )
    {
	super(width, height);
    }

    protected void setGrid( Grid grid ) {
	super.setGrid(grid);
	GridPoint min = getMin();
	GridPoint max = getMax();
	// probably need to change this:
	this.setSize( cellRadius*2*(max.x - min.x + max.y - min.y + 3) ,
		      (1+cellHeight)*(max.y - min.y + 3));
    }

    protected int getAcidRadius() 
    {
	return acidRadius;
    }

    protected GridPoint project( GridPoint p ) 
    {
	return new GridPoint( (1+2*p.x)*cellRadius + p.z*xOffset, 
			      (1+2*p.y)*cellRadius + p.z*yOffset );
			      
    }
}




