package edu.umb.jsAipotu.client.match;

//Alignment with affine gap costs; smart linear-space algorithm

abstract class AlignSmartAffine extends Align {
	int e;                        // gap extension cost
	int[][][] F;                  // the matrices used to compute the alignment
	
	public AlignSmartAffine(Substitution sub, int d, int e, 
			String sq1, String sq2) {
		super(sub, d, sq1, sq2);
		this.e = e;
		F = new int[3][2][m+1];
	}
	
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
	
	static void swap01(Object[] A) 
	{ Object tmp = A[1]; A[1] = A[0]; A[0] = tmp; }
}
