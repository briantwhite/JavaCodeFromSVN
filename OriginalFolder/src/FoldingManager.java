// FoldingManager.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota


import java.util.*;
import java.text.DecimalFormat;       // needed to format the energy

/**
 *
 *  A FoldingManager acts as a manager in the process of folding
 *   polypeptides, and also acts as a subject for FoldingObservers.
 *  FoldingManager is a singleton, having only one instance.
 *
 */

public class FoldingManager
{
    private static FoldingManager instance;
    private PolypeptideFactory    factory;
    private Vector                observers;

    // buffers
    
    private Attributes            currentAttrib;
    private Polypeptide           currentPP;
    private Folder                currentFolder;
    private Grid                  currentGrid;

    // flags

    private boolean               isPlotting = false;    // to report a folding or a plotting. 
    private boolean               DEBUG = false;         // flag for debug info
    private boolean               blackColoring = false; // flag for canvas 

    // Private Constructor
    private FoldingManager()
    {
	observers = new Vector();
	factory = PolypeptideFactory.getInstance();
	resetCurrent();                              // provides initialization
    }

    // initialize or resets current state for each folding.
    private void resetCurrent()
    {
	isPlotting     = false;
	currentAttrib  = null;
	currentPP      = null;
	currentFolder  = null;
	currentGrid    = null;
    }

    // sets the blackCloring flag;
    public void setBlackColoring( boolean flag)
    {
	this.blackColoring = flag;
    }
    public boolean getBlackColoring()
    {
	return this.blackColoring;
    }

    // sets the DEBUG flag, to provide debugging information
    public void setDEBUG( boolean flag)
    {
	this.DEBUG = flag;
    }
    public boolean getDEBUG()
    {
	return this.DEBUG;
    }

    // Gloabally accesible unique instance.
    public static FoldingManager getInstance()
    {
	if ( instance == null)
	    instance = new FoldingManager();
	return instance;
    }

    public void attach( FoldingObserver o)
    {
	if( observers.indexOf( o) < 0 ){
	    if( DEBUG)
		System.out.println(" Following object was attached as an observer: "+o);
	    observers.add( o);
	}
    }
    public void detach( FoldingObserver o)
    {
	observers.remove( o);	    
    }
    private void foldingDone()
    {
	Iterator iter = observers.iterator();
	while( iter.hasNext()){
	    FoldingObserver obs = (FoldingObserver) iter.next();
	    if( DEBUG)
		System.out.println("   A foldingDone() message sent to object: "+obs);
	    obs.foldingDone();

	}
    }

    ////////////////////////////////////
    //                                //
    //             FOLD               //
    //                                //
    ////////////////////////////////////

    public void fold( Attributes attrib)
	throws FoldingException
    {
	resetCurrent();
	currentAttrib = attrib;

	if ( attrib.getIsFolded())
	    {
		isPlotting = true;
		plotPP( attrib);
	    }
	else
	    {
		foldPP( attrib);
	    }
	foldingDone();
    }

    private void plotPP( Attributes attrib)
	throws FoldingException
    {
	createPP( attrib);
	createGrid( attrib);
    }

    private void foldPP( Attributes attrib)
	throws FoldingException
    {
	//System.out.println("DEBUG: Preparing to create a PP......");
	createPP( attrib);
	//System.out.println("DEBUG: PP Created. Creating Grid.....");
	createGrid( attrib);
	//System.out.println("DEBUG: Grid Created. Creating Folder....");
	createFolder( attrib);
	//System.out.println("DEBUG: Folder Created. Attepting to fold....");
	currentFolder.fold();
	//System.out.println("DEBUG: Folding completed. ");
    }

    private void createPP( Attributes attrib)
	throws FoldingException
    {
	try
	    {
		currentPP = factory.createPolypeptide( attrib.getInputString(),
						       attrib.getIsFolded(),
						       attrib.getIsRandom(),
						       attrib.getLength(),
						       attrib.getSeed(),
						       attrib.getTable());
	    }
	catch ( FoldingException ex)
	    {
		throw new FoldingException("Polypeptide Creation: "+ex.getMessage());
	    }
    } 
    private void createGrid( Attributes attrib)
	throws FoldingException
    {
	String grid = attrib.getGrid();
	if( grid.equalsIgnoreCase("hexagonal"))
	    {
		currentGrid   = new HexGrid( currentPP);
	    }
	else if( grid.equalsIgnoreCase("square"))
	    {
		currentGrid   = new SquareGrid( currentPP); 
	    }
	else if( grid.equalsIgnoreCase("cubic"))
	    {
		currentGrid   = new CubicGrid( currentPP);
	    }
	else if( grid.equalsIgnoreCase("dodecahedral"))
	    {
		currentGrid   = new DodecahedralGrid( currentPP);

		if( attrib.getBreakTies().equalsIgnoreCase("straight"))
		    {
			currentGrid.setTieBreaker( Grid.STRAIGHT);
		    }
		else //if( attrib.getBreakTies().equalsIgnoreCase("bent"))
		    {
			currentGrid.setTieBreaker( Grid.BENT);
		    }
		//else
		//    {
		//	throw new FoldingException
		//	    ("Grid Creation: DodecahedralGrid: Break Ties REQUIRED: straight or bent");
		//    }
	    }
	else
	    {
		throw new FoldingException("Grid Creation: REQUIRED:hexagonal OR square OR "+
					   "cubic OR dodecahedral. GIVEN: "+ grid);
	    }
    }
    
    private void createFolder( Attributes attrib)
	throws FoldingException
    {
	String folder = attrib.getFolder();
	if( folder.equalsIgnoreCase("bruteforce"))
	    {
		currentFolder = new BruteForceFolder( currentPP, currentGrid);
	    }
	else if( folder.equalsIgnoreCase("incremental"))
	    {
		currentFolder = new IncrementalFolder( currentPP, currentGrid);
		String lookupString = attrib.getLookup();
		String stepString   = attrib.getStep();
		int lookup = 0;
		int step = 0;
		try
		    {
			lookup = Integer.parseInt( lookupString);
		    }
		catch( NumberFormatException ex)
		    {
			throw new FoldingException
			    ("FolderCreation: look ahead: REQUIRED: integer GIVEN: "+lookupString);
		    }
		if( lookup <= 0)
		    throw new FoldingException
			    ("FolderCreation: look ahead: REQUIRED: positive no GIVEN: "+lookup);

		try
		    {
			step = Integer.parseInt( stepString);
		    }
		catch( NumberFormatException ex)
		    {
			throw new FoldingException
			    ("FolderCreation: step: REQUIRED: integer GIVEN: "+stepString);
		    }
		if( step <= 0)
		    throw new FoldingException
			    ("FolderCreation: step: REQUIRED: positive no GIVEN: "+step);

		( (IncrementalFolder) currentFolder).setLookAhead( lookup);
		( (IncrementalFolder) currentFolder).setStep( step);
	    }
	else
	    {
		throw new FoldingException
		    ("Folder creation: REQUIRED: bruteforce OR incremental. GIVEN: "+folder);
	    }
    }


    public GridCanvas createCanvas(int width, int height)
    {
	GridCanvas canvas;
	String grid = currentAttrib.getGrid();
	if( grid.equalsIgnoreCase("hexagonal"))
	    {
		canvas = new HexCanvas( width , height); 
	    }
	else if( grid.equalsIgnoreCase("square"))
	    {
		canvas = new SquareCanvas( width , height); 
	    }
	else if( grid.equalsIgnoreCase("cubic"))
	    {
		canvas = new CubicCanvas( width , height); 
	    }
	else if( grid.equalsIgnoreCase("dodecahedral"))
	    {
		canvas = new DodecahedralCanvas( width , height); 
	    }
	else
	    {                     // should not get here
		canvas = null;
		System.out.println("could not create canvas, grid argument failed: hex, ...");
	    }
	
	canvas.setGrid( currentGrid);
	return canvas;
    }
    
    public GridCanvas createCanvas()
    {
	return createCanvas( 400, 200);
    }
    
    public String getPolypeptideString()
    {
	return currentPP.toString();
    }
    public String getEnergyString()
    {
    	return String.valueOf( currentGrid.getEnergy());
    }
    public String getEnergyString( String pattern)
    {
	return getEnergy( pattern);
    }
    public String getEnergy( String pattern)
    {
	DecimalFormat formatter = new java.text.DecimalFormat( pattern);
	return ( formatter.format( getEnergy()));
    }
    public double getEnergy()
    {
	return currentGrid.getEnergy();
    }    
    public String getFoldingIndex( String pattern)
    {
	DecimalFormat formatter = new java.text.DecimalFormat( pattern);
	return ( formatter.format( getFoldingIndex()));
    }
    public String getFoldingIndexString()
    {
	return ""+getFoldingIndex();
    }
    public double getFoldingIndex()
    {
	return currentGrid.getFoldingIndex();
    }
    public String getTopology()
    {
	return currentPP.getTopology();
    }
    public long getTime()
    {
	if( isPlotting) return 0;
	return currentFolder.getTime();
    }
    public String report()
    {
	if( isPlotting)
	    {
		StringBuffer buf = new StringBuffer(); 
		buf.append( "\n " + "Polypeptide:   "+getPolypeptideString());
		buf.append( "\n " + "Energy:        "+getEnergy());
		buf.append( "\n " + "Folding index: "+getFoldingIndex());
		return buf.toString();
	    }
	else
	    {
		return "\n" + currentFolder.report();
	    }
    }
    public String actionReport()
    {
	StringBuffer buf = new StringBuffer();
	if( isPlotting)
	    {
		buf.append( "Plotted Solution");
	    }
	else
	    {
		buf.append( "\n");
		if( currentAttrib.getIsRandom())
		    {
			buf.append( "Randomly generated with length:   ");
			buf.append( currentAttrib.getLength());
			buf.append( "   and seed:  "+currentAttrib.getSeed());
		    }
		else
		    {
			buf.append(" Polypeptide was parsed from input");
		    }
		buf.append("\n");
		if( currentAttrib.getFolder().equalsIgnoreCase("bruteForce"))
		    {
			buf.append(" Brute Force Algorithm");
		    }
		else
		    {
			buf.append(" Incremental  ");
			buf.append("   look-ahead: "+currentAttrib.getLookup());
			buf.append("   step: "      +currentAttrib.getStep());
		    }
		buf.append("\n");
	    }
	return buf.toString();	
    }
}
