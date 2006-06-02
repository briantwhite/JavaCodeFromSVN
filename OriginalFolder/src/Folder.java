// Folder.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota


import java.util.*;

/** 
 * Abstract base class for folding algorithms.
 *
 */

public abstract class Folder
{
    protected Grid grid;
    protected Polypeptide pp;
    private long time;
    protected java.text.DecimalFormat formatter 
	    = new java.text.DecimalFormat("####.####");

    // for efficiency: save function calls by creating an array here
    protected AcidInChain[] acids;
    protected int numAcids;

//     public Folder( Polypeptide pp )
//     {
// 	this(new Grid(pp), pp);
// 	this(new HexGrid(pp), pp);
// 	this(new SquareGrid(pp), pp);
//     }

    public Folder( Polypeptide pp, Grid grid ) 
    {
	this.grid = grid;
	this.pp   = pp;
	numAcids  = pp.getLength();
	acids     = pp.getAcidArray();
    }

    public void fold( ) 
    {
	time = System.currentTimeMillis();
	realFold();
	time = System.currentTimeMillis() - time;
	grid.computeStatistics();
	pp.setFolded();
    }

    public abstract void realFold();

    public abstract String getStatistics();

    public abstract String getName();

    public String report()
    {
	StringBuffer buf = new StringBuffer(getName());
	buf.append("\n" + pp.toString());
	buf.append("\nenergy " + 
			   formatter.format(grid.getEnergy()));
	buf.append("\nfolding index " + 
			   formatter.format(grid.getFoldingIndex()));
	buf.append("\ntime   " + getTime() + " seconds");
	buf.append("\ntopology " + pp.getTopology());
	return buf.toString();
    }

    public String csvReport()
    {
	StringBuffer buf = new StringBuffer();
	buf.append(""+formatter.format(grid.getEnergy()));
	buf.append(", " + formatter.format(grid.getFoldingIndex()));
	buf.append(", " + pp.getDirectionSequence());
	buf.append(", " + pp.toCSV());
	return buf.toString();
    }

    public String getTopology()
    {
	return pp.getTopology();
    }

    public long getTime() 
    {
	return time/1000;
    }

    public double getEnergy() 
    {
	return grid.getEnergy();
    }
}    
