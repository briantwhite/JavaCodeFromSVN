// BatchMode.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota



//
// Compute foldings from the command line. 
// 
// Reads either a list of hydrophobicIndices, or instructions
// to generate several random chains.
// 
// Very badly designed. Rewrite, perhaps as two separate classes.
// Allow choice of algorithm (one, both, many ...) from command line.
//
// Allow choice of report (csv for Excel or text for viewing).

import java.util.*;

public class BatchMode
{
    private Polypeptide pp;
    private Grid grid;
    private BruteForceFolder folder;

    public BatchMode( String[] listOfIndices )
	throws FoldingException
     {
	 //pp = new Polypeptide(listOfIndices);
	pp = PolypeptideFactory.getInstance().createPolypeptide
	    ( listOfIndices,
	      false,
	      false,
	      "",
	      "",
	      "virtual");
     }

    public BatchMode( String listOfIndices )
	throws FoldingException
    {
	//	pp = new Polypeptide(listOfIndices);
	pp = PolypeptideFactory.getInstance().createPolypeptide
	    ( listOfIndices,
	      false,
	      false,
	      "",
	      "",
	      "virtual");
    }

    public BatchMode( Polypeptide pp )
    {
	this.pp = pp;
    }

    /**
     * Parse input to create AminoAcid chain, 
     * create grid, call BruteForceFolder.
     */
    public void fold()
    {
	grid   = new HexGrid( pp );
	folder = new BruteForceFolder(pp, grid);
	folder.fold(  );     
    }

    private void report()
    {
	System.out.println(folder.report());
	System.out.println("\ntopology " + folder.getTopology());
	System.out.println("\nenergies " + folder.getEnergyHistogram());
	System.out.println("\n" + folder.getTopologies() + "\n");
// 	System.out.println("\n\n" + csvReport());
    }

    private String csvReport()
    {
	StringBuffer buf = new StringBuffer(folder.csvReport());
	buf.append(", " + folder.getEnergyHistogram());
	buf.append("\n" + folder.getTopologies() + "\n");
	return buf.toString();
    }

    private static void foldMany( String table, int length, int number )
	throws FoldingException
    {
	for (int i = 0; i < number; i++) {
	    BatchMode folder = new BatchMode
		( PolypeptideFactory.getInstance().
		  createPolypeptide("",false, true,""+length,"0",table));
	    folder.fold();
	    System.out.println(folder.csvReport());
	}
    }

    public static void main( String[] args )
    {
	Options opts = new Options
	    ("random",
	     "algorithm:incremental step:8 lookahead:4 table:standard length:10 seed:0",
	     args);
	if (opts.isOpt("random")) {
	    int length = Integer.parseInt(args[1]);
	    int number = Integer.parseInt(args[2]);
	    try {
		foldMany(opts.getOpt("table"), 
			 opts.getIntOpt("length"),
			 opts.getIntOpt("seed"));
	    }
	    catch( FoldingException e ) {
		System.out.println(e);
	    }
	    return;
	}
	try{
	    BatchMode folder = new BatchMode(args);
	    folder.fold();
	    folder.report();
	}
	catch( Exception e){
	    System.out.println("ERROR: "+e);
	}

    }
}    


