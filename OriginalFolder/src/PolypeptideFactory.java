// PolypeptideFactory.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota


import java.util.*;

public class PolypeptideFactory
{

    private static PolypeptideFactory instance;

    private static final char[] DELIMITERS = { ':', ','};

    private PolypeptideFactory(){
    }

    public static PolypeptideFactory getInstance(){

	if (instance == null)
	    instance = new PolypeptideFactory();
	return instance;
    }
    /**
     *     Constructs a Polypeptide
     *
     * @param input        input containing amino acids, with directions or not;
     * @param isSolution   input represents a folded polypeptide
     * @param isRandom     generate a random polypeptide
     * @param length       length of random polypeptide
     * @param seed         seed of the random polypeptide
     * @param tableName    table of amino acids 
     */
    public Polypeptide createPolypeptide( String  input,
					  boolean isSolution,
					  boolean isRandom,
					  String  length,
					  String  seed,
					  String  tableName )
	throws FoldingException
    {
	if( isSolution){
	    return createSolution( tableName, input);
	}
	else if( isRandom){
	    return createRandom( tableName, length, seed );
	}
	else{	   
	    return createFromAcids( tableName, input);	    
	}
    }

    /**
     *  Creates a polypeptide from an array of strings. Used for 
     *   comand line modes, where the acids come from the args. 
     */
    public Polypeptide createPolypeptide(String[] args,
					 boolean  isSolution,
					 boolean  isRandom,
					 String   length,
					 String   seed,
					 String   tableName)
	throws FoldingException
    {
	return this.createPolypeptide(arrayToString( args),
				      isSolution,
				      isRandom,
				      length,
				      seed,
				      tableName);
    }

    // Helper method to construct a String from a String[]   
    private static String arrayToString( String[] listOfIndices )
    {
	String s = "";
    	for (int i = 0; i < listOfIndices.length; i++) {
    	    s += listOfIndices[i] + " ";
    	}
    	return s;
    }

    private AminoAcidTable createTable( String tableName)
	throws FoldingException
    {
	AminoAcidTable table;
	table = AminoAcidTable.makeTable( tableName);
	return table;
    }

    // create a polypeptide from a string containing a
    //     previously folded polypeptide
    public Polypeptide createSolution( String tableName, String input)
	throws FoldingException
    {
	AminoAcidTable table = createTable( tableName);
	
	// parse input into strings representing an acid or a direction
	ArrayList acidString = getTokens( input);
	
	
	// parsing each acid string into AminoAcids using AminoAcidTable.
	//     or each direction string into a Direction using Direction
	// From parsing, acids are on even positions, and directions on odd 
	//     positions. 
	int numberOfTokens = acidString.size();
	int acidIndex      = 0;
	int directionIndex = 0;
	AminoAcid[] acids      = new AminoAcid[ numberOfTokens / 2 ];
	Direction[] directions = new Direction[ numberOfTokens / 2 ];
	for( int i = 0; i< numberOfTokens; i = i + 2){

	    // parse acid string( found on even positions)	    
	    acids[ acidIndex++] = parseAcid( (String) acidString.get( i), table);
		
	    // parse direction string( found on odd positions)
	    directions[ directionIndex++] = parseDirection( (String) acidString.get( i+1));	    
	}
	
	// call constructor in Polypeptide
	return new Polypeptide( table, acids, directions);
    }

    public Polypeptide createRandom( String tableName, String length, String seed)
	throws FoldingException
    {
	
	int len = 0;
	int s = -1;
	try{
	    len = Integer.parseInt( length);
	}
	catch( NumberFormatException e){
	    throw new IntegerFormatFoldingException
		("Length: REQUIRED: integer GIVEN: "+length);
	}
	
	try{
	    s =  Integer.parseInt( seed);
	}
	catch( NumberFormatException e){
	    throw new IntegerFormatFoldingException
		("Seed:  REQUIRED: integer GIVEN: "+seed);
	}
	
	if( len < 1)
	    throw new IntegerFormatFoldingException
		("Length: REQUIRED: > 0 GIVEN: "+length);
	if( s < 0)
	    throw new IntegerFormatFoldingException
		("Seed: REQUIRED:  >=0 GIVEN: "+seed);

	return new Polypeptide( createTable( tableName), len, s );
    }

    public Polypeptide createFromAcids( String tableName, String input)
	throws FoldingException
    {
	AminoAcidTable table = createTable( tableName);

	// parse input into strings, each representing an acid
	ArrayList acidString = getTokens( input);
	
	// parsing each acid string into AminoAcids using the AminoAcidTable.
	AminoAcid[] acids = new AminoAcid[ acidString.size()];
	for( int i = 0; i< acids.length; i++){	 
	    acids[i] = parseAcid( (String) acidString.get( i), table);       	    	   
	}
	
	// call constructor in Polypeptide
	return new Polypeptide( table, acids);	
    }

    public ArrayList getTokens( String input)
    {
	// setting delimiters
	for( int i = 0;i < DELIMITERS.length; i++)
	    input = input.replace( DELIMITERS[i], ' ');

	// parsing
	ArrayList tokens = new ArrayList();
	StringTokenizer st = new StringTokenizer( input);
	while( st.hasMoreTokens())
	    tokens.add( st.nextToken());
	return tokens;
    }
    
    public AminoAcid parseAcid( String acidString, AminoAcidTable table)
	throws FoldingException
    {
	AminoAcid acid = table.get( acidString);
	if( acid == null)
	    throw new FoldingException
		("acid not found. ACID: "+acidString+" TABLE: "+table.getName());
	//System.out.println(" Acid is: "+acid.getName());
	return acid;
    }

    public Direction parseDirection( String directionString)
	throws FoldingException
    {
	if( directionString.trim().equalsIgnoreCase("none")){
	    //System.out.println("Direction is: none");
	    return Direction.none;	    
	}
	Direction direction = Direction.getDirection( directionString);
	//System.out.println("Direction is: "+direction);
	if( direction == Direction.none)       // direction not found
	    throw new FoldingException("direction not found. DIRECTION:  "+directionString); 
	return direction;
    }

}
