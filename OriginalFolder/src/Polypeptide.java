// Polypeptide.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota


// This class has too many convoluted constructors. Clean them up.

import java.util.*;

/**
 * Model a polypeptide as a List of AminoAcids
 */

public class Polypeptide
{
    private AminoAcidTable table;
    private ArrayList acids;
    private Integer[] wrappers;
    private boolean folded = false;
    private int numAcids;
    private double maxEnergy = 1.0; // to scale histogram

    // for efficiency, mirror the ArrayList as an array,
    // and mirror the next Directions (dynamic)
    private AcidInChain[] acidArray;

    /**
     *  Creates a Polypeptide with random acids and with the prescribed seed
     *    from the table provided.
     * 
     * @param table  the name of the table.
     * @param length the length of the chain.
     * @param seed   the seed.
     */    
    protected Polypeptide( AminoAcidTable table, int length, int seed)
    {
	// ASSUMES: length >0, seed >=0, table != null

	this( table, table.getRandom( length, seed));
    }

    /**
     *  Creates a polypeptide from the prescribed acids, 
     *    from the table provided
     *
     * @param table      the name of the table
     * @param realAcids  the array of amino acids
     */
    protected Polypeptide( AminoAcidTable table, AminoAcid[] realAcids)
    {
	// ASSUMES: all acids in realAcids are from table.

	this.table = table;
	maxEnergy = table.getMaxEnergy();
	acids = new ArrayList(); // duplicated code
	for (int i=0; i < realAcids.length; i++) {
	    acids.add (new AcidInChain(realAcids[i], i));
	}
	// duplicated code below
	numAcids = acids.size();
	wrappers = new Integer[ acids.size() ];
	for (int i = 0; i < wrappers.length; i++ ) {
	    wrappers[i] = new Integer(i);
	}
	acidArray = (AcidInChain[]) acids.toArray(new AcidInChain[0]);
    }

    /**
     *  Creates a polypeptide from the acids with corresponding directions.
     *
     * @param table      the name of the table
     * @param realAcids  the array of amino acids
     * @param directions the array of directions 
     */
    protected Polypeptide( AminoAcidTable table, AminoAcid[] acids, Direction[] directions)
    {
	this( table, acids);
	setDirections( directions);
    }
    
    // Helper method to set the direction for each acid
    private void setDirections ( Direction[] directions)
    {
	for( int i = 0; i< directions.length; i++){
	    setNext(i, directions[i]);
	}
	folded = true;
    }
    
    public AminoAcidTable getTable()
    {
	return table;
    }

    public AcidInChain[] getAcidArray()
    {
	return acidArray;
    }

    public void setFolded()
    {
	folded = true;
    }

    public boolean isFolded()
    {
	return folded;
    }

    public Iterator iterator()
    {
	return acids.iterator();
    }

    /**
     * Set the Direction of the next AminoAcid in the chain.
     *
     * @param index the index of an AminoAcid.
     * @param d the Direction of the following AminoAcid.
     */
    public void setNext( int index, Direction d ) {
	acidArray[index].setNext(d);
    }

    public void addNeighbor( AcidInChain to, AcidInChain from )
    {
	to.addNeighbor( wrappers[ acids.indexOf(from) ] );
    }

    public void clearTopology()
    {
	for( int i = 0; i < numAcids; i++ ) {
	    acidArray[i].getNeighbors().clear();
	}
    }

    public String getTopology() 
    {
	StringBuffer buf = new StringBuffer();
	for( int i = 0; i < numAcids; i++ ) {
	    buf.append("\n" + i + ": " + acidArray[i].getNeighbors());
	}
	return buf.toString();
    }

    public Direction getNextDirection( int index )
    {
	return acidArray[index].next;
    }    

    public AcidInChain getAminoAcid( int i )
    {
	return (AcidInChain)acids.get(i);
    }

    public int getLength() 
    {
	return acids.size();
    }

    public String toString()
    {
	Iterator i = iterator();
	StringBuffer buf = new StringBuffer();
	while (i.hasNext()) {
	    buf.append( (AcidInChain)i.next() + " : " );
	}
	return buf.toString();
    }

    public String toCSV()
    {
	Iterator i = iterator();
	StringBuffer buf = new StringBuffer();
	while (i.hasNext()) {
	    buf.append( ((AcidInChain)i.next()).getHydrophobicIndex() + ", ");
	}
	return buf.toString();
    }

    public double getMaxEnergy() 
    {
	return maxEnergy;
    }

    public String getDirectionSequence()
     {
	Iterator i = iterator();
	StringBuffer buf = new StringBuffer();
	while (i.hasNext()) {
	    buf.append( ((AcidInChain)i.next()).getNext() + " : ");
	}
	return buf.toString();
     }


    //    public static void main( String[] args ) 
    ///    {
    //	try {
    //	    Polypeptide pp = new Polypeptide("standard", 10, 999);
    //	    System.out.println( pp );
    //	}
    //	catch( FoldingException e ) {
    //	    System.out.println(e);
    //	}
    // 	Polypeptide pp = new Polypeptide(5, 1000);
    // 	Polypeptide pp = new Polypeptide(args[0], args[1]);
    // 	System.out.println( pp.toCSV() );
    // 	System.out.println( pp.getDirectionSequence() );
    // 	Grid grid = new Grid(pp);
    // 	System.out.println(grid.getEnergy());
    //  }

    /////////////////////////////////////////////////
    // THIS CODE (from now on) IS NO LONGER NEEDED //
    /////////////////////////////////////////////////


    /** 
     * Construct a Polypeptide with random acids chosen from a table, 
     * with a prescribed seed.
     *
     * @param tableName the name of the table.
     * @param n the length of the chain.
     * @param seed the seed.
     */
    //protected Polypeptide( String tableName, int n, int seed )
    //	throws FoldingException
    //  {
    //	table = AminoAcidTable.makeTable(tableName);
    //	AminoAcid[] realAcids = table.getRandom( n, seed );
    //	maxEnergy = table.getMaxEnergy();
    //	acids = new ArrayList(); // duplicated code
    //	for (int i=0; i < realAcids.length; i++) {
    //	    acids.add (new AcidInChain(realAcids[i], i));
    //	}
    //	// duplicated code below
    //	numAcids = acids.size();
    //	wrappers = new Integer[ acids.size() ];
    //	for (int i = 0; i < wrappers.length; i++ ) {
    //	    wrappers[i] = new Integer(i);
    //	}
    //	acidArray = (AcidInChain[]) acids.toArray(new AcidInChain[0]);
    //    }

    /**
     * Construct a Polypeptide, perhaps with a folding.
     *
     * Input argument is a String to be parsed.
     *
     * @param listOfIndices the hydrophobic indices.
     */
    //   protected Polypeptide(String listOfIndices)
    //   {
    //	try {
    //	    table = AminoAcidTable.makeTable("standard"); // ugly default
    //	    init(listOfIndices);
    //	}
    //	catch ( FoldingException e ) {
    //	    System.out.println(e);
    //	    System.out.println("should never get here");
    //	}
    //   }

    // this is really ugly ... rewrite now that we have the tables
    // in place - no longer need usingTable ...
    //    private void init( String listOfIndices )
    //    {
    //// 	AminoAcidTable table = new StandardTable(); // just in case
    //	boolean usingTable = false;
    //	boolean listWithDirections = (listOfIndices.indexOf(":") >= 0);
    //	String directionList = "";
    //	listOfIndices = listOfIndices.replace(',', ' ');
    //	listOfIndices = listOfIndices.replace(':', ' ');
    //	StringTokenizer st = new StringTokenizer(listOfIndices);
    //	acids = new ArrayList();
    //	int index = 0;
    //	while( st.hasMoreTokens() ) {
    //	    String s = st.nextToken();
    //	    if (index == 0) {
    //		try {
    //		    usingTable = (table.get(s) != null);
    //		}
    //		catch( FoldingException e ) {}
    //	    }
    //	    if (s.equals("directions")){
    //		break;
    //	    }
    //	    if ( usingTable ) {
    //		try {
    //		    acids.add(new AcidInChain(table.get(s), index++));
    //		}
    //		catch( NullPointerException e ) {
    //		    directionList += " " + s;
    //		}
    //		catch( FoldingException e) {}
    //	    }
    //	    else {
    //		try {
    //		    double hydrophobicIndex = (new Double(s)).doubleValue();
    //		    acids.add (new AcidInChain(hydrophobicIndex, index++));
    //		}
    //		catch( NumberFormatException e ) {
    //		    directionList += " " + s;
    //		}
    //	    }
    //	}
    //	numAcids = acids.size();
    //	wrappers = new Integer[ acids.size() ];
    //	for (int i = 0; i < wrappers.length; i++ ) {
    //	    wrappers[i] = new Integer(i);
    //	}
    //	acidArray = (AcidInChain[]) acids.toArray(new AcidInChain[0]);
    //	// now parse directions, if any
    //	if (listWithDirections) {
    //	    st = new StringTokenizer(directionList);
    //	}
    //	if (st.hasMoreTokens()) {
    //	    setDirections(st);
    //	}
    //   }

    /**
     * Construct a Polypeptide.
     *
     * Input argument is an array of doubles to be parsed.
     *
     * @param listOfIndices the hydrophobic indices.
     */
    //  protected Polypeptide (String[] listOfIndices)
    //   {
    //	this( arrayToString( listOfIndices ) );
    //   }

    /** 
     * Construct a Polypeptide with random acids chosen from a table,
     *
     * @param tableName the name of the table.
     * @param n the length of the chain.
     */
    //   protected Polypeptide( String tableName, int n )
    //	throws FoldingException
    //   {
    //	this( tableName, n, 0);
    //  }

        /**
     * Construct a Polypeptide with specific folding.
     *
     * Input arguments are Strings to be parsed.
     *
     * @param listOfIndices the hydrophobic indices.
     * @param listOfDirections Directions from each acid to the next.
     */
    //   protected Polypeptide(String listOfIndices, String listOfDirections)
    //  {
    //	this(listOfIndices);
    //	StringTokenizer st = new StringTokenizer( listOfDirections, " :");
    //	setDirections( st );
    // }

    //  private void setDirections( StringTokenizer st )
    // {
    //	for (int i = 0; i < numAcids; i++) {
    //	    setNext(i, Direction.getDirection(st.nextToken(":, ")));
    //	}
    //	folded = true;
    //   }

    // Helper method to construct a String from a String[]
    //
    //  private static String arrayToString( String[] listOfIndices )
    ///  {
    //	String s = "";
    //	for (int i = 0; i < listOfIndices.length; i++) {
    //	    s += listOfIndices[i] + " ";
    //	}
    //	return s;
    //  }

    // Helper method to construct a random list of 
    // hydrophobic indices.
    //
    //  private static String createRandomList( int n, long seed ) 
    //  {
    //	java.util.Random random = new java.util.Random(seed);
    //	StringBuffer buf = new StringBuffer();
    //	for( int j = 0; j < n; j++ ) {
    //	    double d = ((double)random.nextInt(1000))/1000;
    //	    if (random.nextBoolean()) {
    //		d = -d;
    //	    }
    //	    buf.append(" " + d);
    //	}
    //	return buf.toString();
    //  }


}
