package edu.umb.jsAipotu.client.match;
//Implementation of some algorithms for pairwise alignment from
//Durbin et al: Biological Sequence Analysis, CUP 1998, chapter 2.
//Peter Sestoft, sestoft@dina.kvl.dk 1999-09-25, 2003-04-20 version 1.4
//Reference:  http://www.dina.kvl.dk/~sestoft/bsa.html

//License: Anybody can use this code for any purpose, including
//teaching, research, and commercial purposes, provided proper
//reference is made to its origin.  Neither the author nor the Royal
//Veterinary and Agricultural University, Copenhagen, Denmark, can
//take any responsibility for the consequences of using this code.

//Compile with:
//javac Match2.java
//Run with:
//java Match2 HEAGAWGHEE PAWHEAE

//Class hierarchies
//-----------------
//Align                   general pairwise alignment
//AlignSimple          alignment with simple gap costs
//NW                global alignment with simple gap costs
//SW                local alignment with simple gap costs
//RM                repeated matches with simple gap costs
//OM                overlap matches with simple gap costs 
//AlignAffine          alignment with affine gap costs (FSA model)
//NWAffine          global alignment with affine gap costs
//AlignSmart           alignment using smart linear-space algorithm
//NWSmart           global alignment using linear space
//SWSmart           local alignment using linear space
//AlignSmartAffine     alignment w affine gap costs in linear space
//SWSmartAffine     local alignment w affine gap costs in linear space
//Traceback               traceback pointers
//Traceback2           traceback for simple gap costs
//Traceback3           traceback for affine gap costs
//Substitution            substitution matrices with fast lookup
//Blosum50             the BLOSUM50 substitution matrix
//Output                  general text output
//SystemOut            output to the console (in the application)
//TextAreaOut          output to a TextArea (in the applet)

//Notational conventions: 
//i in {0..n} indexes columns and sequence seq1
//j in {0..m} indexes rows    and sequence seq2
//k in {0..2} indexes states (in affine alignment)

//Pairwise sequence alignment 

abstract class Align {
	Substitution sub;             // substitution matrix
	int d;                        // gap cost
	String seq1, seq2;            // the sequences
	int n, m;                     // their lengths
	Traceback B0;                 // the starting point of the traceback
	
	final static int NegInf = Integer.MIN_VALUE/2; // negative infinity
	
	public Align(Substitution sub, int d, String seq1, String seq2) {
		this.sub = sub;
		this.seq1 = strip(seq1); this.seq2 = strip(seq2);
		this.d = d;
		this.n = this.seq1.length(); this.m = this.seq2.length();
	}
	
	public String strip(String s) {
		boolean[] valid = new boolean[127];
		String residues = sub.getResidues();
		for (int i=0; i<residues.length(); i++) {
			char c = residues.charAt(i);
			if (c < 96) 
				valid[c] = valid[c+32] = true;
			else
				valid[c-32] = valid[c] = true;
		}
		StringBuffer res = new StringBuffer(s.length());
		for (int i=0; i<s.length(); i++)
			if (valid[s.charAt(i)])
				res.append(s.charAt(i));
		return res.toString();
	}
	
	// Return two-element array containing an alignment with maximal score
	
	public String[] getMatch() {
		StringBuffer res1 = new StringBuffer();
		StringBuffer res2 = new StringBuffer();
		Traceback tb = B0;
		int i = tb.i, j = tb.j;
		while ((tb = next(tb)) != null) {
			if (i == tb.i) 
				res1.append('-');
			else
				res1.append(seq1.charAt(i-1)); 
			if (j == tb.j) 
				res2.append('-');
			else
				res2.append(seq2.charAt(j-1)); 
			i = tb.i; j = tb.j;
		}        
		String[] res = { res1.reverse().toString(), res2.reverse().toString() };
		return res;
	}
	
	public String fmtscore(int val) {
		if (val < NegInf/2) 
			return "-Inf";
		else
			return Integer.toString(val);
	}
	
	// Print the score, the F matrix, and the alignment
	public void domatch(Output out, String msg, boolean udskrivF) {
		out.println(msg + ":"); 
		out.println("Score = " + getScore());
		if (udskrivF) {
			out.println("The F matrix:");
			printf(out);
		}
		out.println("An optimal alignment:");
		String[] match = getMatch();
		out.println(match[0]);
		out.println(match[1]);
		out.println();
	}
	
	public void domatch(Output out, String msg) 
	{ domatch(out, msg, true); }    
	
	// Get the next state in the traceback
	public Traceback next(Traceback tb)
	{ return tb; }                // dummy implementation for the `smart' algs.
	
	// Return the score of the best alignment
	public abstract int getScore();
	
	// Print the matrix (matrices) used to compute the alignment
	public abstract void printf(Output out);
	
	// Auxiliary functions
	static int max(int x1, int x2) 
	{ return (x1 > x2 ? x1 : x2); }
	
	static int max(int x1, int x2, int x3) 
	{ return max(x1, max(x2, x3)); }
	
	static int max(int x1, int x2, int x3, int x4) 
	{ return max(max(x1, x2), max(x3, x4)); }
	
	static String padLeft(String s, int width) {
		int filler = width - s.length();
		if (filler > 0) {           // and therefore width > 0
			StringBuffer res = new StringBuffer(width);
			for (int i=0; i<filler; i++)
				res.append(' ');
			return res.append(s).toString();
		} else
			return s;
	}
}
