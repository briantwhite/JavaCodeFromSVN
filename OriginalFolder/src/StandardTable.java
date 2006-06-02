// StandardTable.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota


import java.util.*;

/**
 * Model the standard table of amino acids.
 */
public class StandardTable extends AminoAcidTable
{
    private Map table;
    private double maxEnergy;

    public StandardTable()
    {
	table = new TreeMap();
	try {
	    add( new AminoAcid( "Arg", -15.86), 0.057 );
	    add( new AminoAcid( "Asp",  -9.66), 0.053 );
	    add( new AminoAcid( "Glu",  -7.75), 0.062 );
	    add( new AminoAcid( "Asn",  -7.58), 0.044 );
	    add( new AminoAcid( "Lys",  -6.49), 0.057 );
	    add( new AminoAcid( "Gln",  -6.48), 0.040 );
	    add( new AminoAcid( "His",  -5.60), 0.022 );
	    add( new AminoAcid( "Ser",  -4.34), 0.069 );
	    add( new AminoAcid( "Thr",  -3.51), 0.058 );
	    add( new AminoAcid( "Tyr",  -1.08), 0.032 );
	    add( new AminoAcid( "Gly",   0.00), 0.072 );
	    add( new AminoAcid( "Pro",   0.01), 0.051 ); // check value
	    add( new AminoAcid( "Cys",   0.34), 0.017 );
	    add( new AminoAcid( "Ala",   0.87), 0.083 );
	    add( new AminoAcid( "Trp",   1.39), 0.013 );
	    add( new AminoAcid( "Met",   1.41), 0.024 );
	    add( new AminoAcid( "Phe",   2.04), 0.039 );
	    add( new AminoAcid( "Val",   3.10), 0.066 );
	    add( new AminoAcid( "Ile",   3.98), 0.052 );
	    add( new AminoAcid( "Leu",   3.98), 0.090 );
	}
	catch (FoldingException e) {
	    System.err.println("shouldn't get here");
	    e.printStackTrace();
	}
	normalize();
    }

    public Iterator getIterator() 
	throws FoldingException
    {
	return table.keySet().iterator();
    }

    /**
     * Add an AminoAcid to the table, with its frequency.
     *
     * @param a the AminoAcid to add.
     * @param probability how likely is that AminoAcid?
     */
    public void add( AminoAcid a, double probability )
	throws FoldingException
    {
	table.put(a.getName().trim().toUpperCase(), 
		  new AcidInTable(a, probability));
    }

    /**
     * Add an AminoAcid to the table.
     *
     * @param a the AminoAcid to add.
     *
     * @throws FoldingException if operation not allowed.
     */
    public void add( AminoAcid a )
	throws FoldingException 
    {
	throw new FoldingException
	    ("can't add to standard table without probability");
    }

    /**
     * Retrieve an acid from the table.
     *
     * @param name the name of the acid.
     * 
     * @return the acid, null if none.
     */
    public AminoAcid get( String name ) 
    {
	AcidInTable a = (AcidInTable)table.get(name.trim().toUpperCase());
	if (a == null) {
	    return null;
	}
	return a.a;
    }

    /**
     * A constant between 0 and 1 that leads to good contrast
     * when normalized hydrophobic index determines color in a 
     * folding on a grid.
     *
     * Default is 0.5;
     *
     * @return the constant.
     */
    public float getContrastScaler()
    {
	return (float)1.0;
    }

    /**
     * Returns the name of this table
     */
    public String getName()
    {
	return AminoAcidTable.STANDARD;
    }
    /**
     * Choose a random sequence of AminoAcids from this table.
     *
     * @param length the length of the desired sequence.
     * @param seed a seed for the random number generator.
     */
    public AminoAcid[] getRandom( int length, int seed )
    {
	AminoAcid[] sequence = new AminoAcid[ length ];
	Random r = new Random(seed);
	for (int i = 0; i < sequence.length; i++ ) {
	    double d = r.nextDouble();
	    double ptotal = 0;
	    Iterator iter = table.values().iterator();
	    while (iter.hasNext()) {
		AcidInTable a = (AcidInTable)iter.next();	    
		ptotal += a.probability;
		if (d < ptotal) {
		    sequence[i] = a.a;
		    break; // back to for loop
		}
	    }
	}
	return sequence;
    }

    private void normalize()
    {
	double maxHI = Double.MIN_VALUE;
	double minHI = Double.MAX_VALUE;
	double ptotal = 0.0;
	Iterator i = table.values().iterator();
	while (i.hasNext()) {
	    AcidInTable a = (AcidInTable)i.next();
	    ptotal += a.probability;
	    if (a.a.getHydrophobicIndex() > maxHI) {
		maxHI = a.a.getHydrophobicIndex();
	    }
	    if (a.a.getHydrophobicIndex() < minHI) {
		minHI = a.a.getHydrophobicIndex();
	    }
	}

	// assume maxHI > 0 and minHI < 0
	maxEnergy = Math.max( maxHI, -minHI );

	i = table.values().iterator();
	while (i.hasNext()) {
	    AcidInTable a = (AcidInTable)i.next();
	    a.probability /= ptotal;
	    a.a.setNormalizedHydrophobicIndex
		( a.a.getHydrophobicIndex() / maxEnergy );
	}
    }

    private class AcidInTable
    {
	protected AminoAcid a;
	protected double probability;

	public AcidInTable( AminoAcid a, double probability )
	{
	    this.a = a;
	    this.probability = probability;
	}
	
	public String toString() 
	{
	    return a.toString() + '\t' +
		a.getHydrophobicIndex() + '\t' +
		a.getNormalizedHydrophobicIndex() + "\t\t" +
		probability + '\n';
	}
    }
    
    public String toString()
    {
	return "name\t\thi\tnormal\tprob\n" + table.toString();
    }

    /**
     * A bound on the absolute value of the energy of
     * AminoAcids in the table.
     *
     * @return the bound.
     */
    public double getMaxEnergy() 
    {
	return maxEnergy;
    }

    public static void main( String[] args )
    {
	AminoAcidTable t = new StandardTable();
	System.out.println(t);
	AminoAcid[] list = t.getRandom(15, 999);
	for (int i = 0; i < list.length; i++ ) {
	    System.out.println(list[i]);
	}
    }
}

// 	    add( new AminoAcid( "", 15.86), 0.057 );
// 	    add( new AminoAcid( "",  9.66), 0.053 );
// 	    add( new AminoAcid( "",  7.75), 0.062 );
// 	    add( new AminoAcid( "",  7.58), 0.044 );
// 	    add( new AminoAcid( "",  6.49), 0.057 );
// 	    add( new AminoAcid( "",  6.48), 0.040 );
// 	    add( new AminoAcid( "",  5.60), 0.022 );
// 	    add( new AminoAcid( "",  4.34), 0.069 );
// 	    add( new AminoAcid( "",  3.51), 0.058 );
// 	    add( new AminoAcid( "",  1.08), 0.032 );
// 	    add( new AminoAcid( "",  0.00), 0.072 );
// 	    add( new AminoAcid( "", -0.01), 0.051 ); // check value
// 	    add( new AminoAcid( "", -0.34), 0.017 );
// 	    add( new AminoAcid( "", -0.87), 0.083 );
// 	    add( new AminoAcid( "", -1.39), 0.013 );
// 	    add( new AminoAcid( "", -1.41), 0.024 );
// 	    add( new AminoAcid( "", -2.04), 0.039 );
// 	    add( new AminoAcid( "", -3.10), 0.066 );
// 	    add( new AminoAcid( "", -3.98), 0.052 );
// 	    add( new AminoAcid( "

// Arg Asp Glu Asn Lys Gln His Ser Thr Tyr Gly Pro Cys Ala Trp Met Phe Val Ile Leu
