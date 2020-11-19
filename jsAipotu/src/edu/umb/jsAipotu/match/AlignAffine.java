package edu.umb.jsAipotu.match;

//Alignment with affine gap costs

abstract class AlignAffine extends Align {
	int e;                        // gap extension cost
	int[][][] F;                  // the matrices used to compute the alignment
	Traceback3[][][] B;           // the traceback matrix
	
	public AlignAffine(Substitution sub, int d, int e, String sq1, String sq2)
	{
		super(sub, d, sq1, sq2);
		this.e = e;
		F = new int[3][n+1][m+1];
		B = new Traceback3[3][n+1][m+1];
	}
	
	public Traceback next(Traceback tb) {
		Traceback3 tb3 = (Traceback3)tb;
		return B[tb3.k][tb3.i][tb3.j];
	}
	
	public int getScore() 
	{ return F[((Traceback3)B0).k][B0.i][B0.j]; }
	
	public void printf(Output out) {
		for (int k=0; k<3; k++) {
			out.println("F[" + k + "]:");
			for (int j=0; j<=m; j++) {
				for (int i=0; i<F[k].length; i++) 
					out.print(padLeft(fmtscore(F[k][i][j]), 5)); 
				out.println();
			}
		}
	}
}
