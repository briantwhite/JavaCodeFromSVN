package edu.umb.jsAipotu.match;

//Alignment with simple gap costs

abstract class AlignSimple extends Align {
	int[][] F;                    // the matrix used to compute the alignment
	Traceback2[][] B;             // the traceback matrix
	
	public AlignSimple(Substitution sub, int d, String seq1, String seq2) {
		super(sub, d, seq1, seq2);
		F = new int[n+1][m+1];
		B = new Traceback2[n+1][m+1];
	}
	
	public Traceback next(Traceback tb) {
		Traceback2 tb2 = (Traceback2)tb;
		return B[tb2.i][tb2.j];
	}
	
	public int getScore() 
	{ return F[B0.i][B0.j]; }
	
	public void printf(Output out) {
		for (int j=0; j<=m; j++) {
			for (int i=0; i<F.length; i++)
				out.print(padLeft(fmtscore(F[i][j]), 5));
			out.println();
		}
	}
}
