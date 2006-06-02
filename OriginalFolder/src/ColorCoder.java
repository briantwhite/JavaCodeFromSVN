// ColorCoder.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota


import java.awt.Color;
import java.util.Iterator;

/**
 *  Interpolate between colors in HSB model.
 */
public class ColorCoder
{
    private float[] redHSB = Color.RGBtoHSB(
					    Color.RED.getRed(),
					    Color.RED.getGreen(),
					    Color.RED.getBlue(),
					    new float[3]);
    private float[] greenHSB = Color.RGBtoHSB(
					    Color.GREEN.getRed(),
					    Color.GREEN.getGreen(),
					    Color.GREEN.getBlue(),
					    new float[3]);

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

    private float k = (float)0.5;

    // It's easy to solve for a, b and c in terms of k:

    private float a;
    private float b;
    private float c;

    public ColorCoder() 
    {
	this( (float)0.5 );
    }

    public ColorCoder( float k )
    {
	this.k = k;
	a =  4 - 4*k;
	b = -6 + 6*k;
	c =  3 - 2*k;
    }

    protected Color getCellColor( double hydrophobicIndex ) 
    {
	return getCellColor( (float)hydrophobicIndex );
    }

    protected Color getCellColor( float hydrophobicIndex ) 
    {
	float x = (hydrophobicIndex + 1)/2; // map [-1,1] to [0,1]
	float alpha = x*(c+x*(b+a*x)); // polynomial evaluation trick
	float oneMinusAlpha = (float)1.0 - alpha;
	return Color.getHSBColor
	    ( alpha*redHSB[0] + oneMinusAlpha*greenHSB[0],
	      alpha*redHSB[1] + oneMinusAlpha*greenHSB[1],
	      alpha*redHSB[2] + oneMinusAlpha*greenHSB[2] );
    }

    public static void main( String[] args ) 
    {
	try {
	    ColorCoder cc = new ColorCoder();
	    AminoAcidTable standard = AminoAcidTable.makeTable("standard");
	    Iterator i = standard.getIterator();
	    while( i.hasNext() ) {
		AminoAcid a = standard.get((String)i.next());
		Color color = cc.getCellColor(a.getHydrophobicIndex());
		Color opp = new Color( -color.getRGB() );
		System.out.println(a + " " + 
				   color + " " +
				   opp + " " +
				   Integer.toHexString(color.getRGB()) + " " +
				   Integer.toHexString(-color.getRGB()));
	    }
	}
	catch( Exception e ) {
	    System.out.println(e);
	}
    }
}




