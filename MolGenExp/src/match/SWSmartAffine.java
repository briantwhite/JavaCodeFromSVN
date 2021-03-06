package match;

//Local alignment with the Smith-Waterman algorithm (affine gap
//costs, smart linear space algorithm)

public class SWSmartAffine extends AlignSmartAffine {
	Traceback2[][] start;	// Best alignment ending at (i,j) begins at start[i][j]
	int maxval;           // Score of best alignment
	int start1, start2;   // Best alignment begins at (start1, start2)
	int end1, end2;       // Best alignment ends at (end1, end2)
	
	public SWSmartAffine(Substitution sub, int d, int e, 
			String sq1, String sq2) {
		super(sub, d, e, sq1, sq2);
		int n = this.n, m = this.m;
		int[][] score = sub.score;
		int[][] M = F[0], Ix = F[1], Iy = F[2];
		start = new Traceback2[2][m+1];
		maxval = NegInf;
		// Initialize first column (i=0); score is zero:
		for (int j=0; j<=m; j++) 
			start[1][j] = new Traceback2(0, j);
		for (int i=1; i<=n; i++) {
			swap01(M); swap01(Ix); swap01(Iy); swap01(start);
			// F[k][1] represents (new) col i and F[k][0] represents (old) col i-1
			// Initialize first row (j=0):
			start[1][0] = new Traceback2(i, 0);
			for (int j=1; j<=m; j++) {
				int s = score[seq1.charAt(i-1)][seq2.charAt(j-1)];
				int val, valm, valix, valiy;
				valm  = M[1][j]  = max(0, M[0][j-1]+s, Ix[0][j-1]+s, Iy[0][j-1]+s);
				valix = Ix[1][j] = max(M[0][j]-d, Ix[0][j]-e);
				valiy = Iy[1][j] = max(M[1][j-1]-d, Iy[1][j-1]-e);
				
				val = max(valm, valix, valiy);
				if (val == 0)
					start[1][j] = new Traceback2(i, j);
				else if (val == valm)
					start[1][j] = start[0][j-1];
				else if (val == valix)
					start[1][j] = start[0][j];
				else if (val == valiy)
					start[1][j] = start[1][j-1];
				else 
					throw new Error("SWSmartAffine 1");
				if (val > maxval) {
					maxval = val;
					Traceback2 sij = start[1][j];
					start1 = sij.i; start2 = sij.j;
					end1 = i; end2 = j;
				}
			}
		}
	}
	
	public int getScore() 
	{ return maxval; }
	
	public String[] getMatch() {
		String subseq1 = seq1.substring(start1, end1);
		String subseq2 = seq2.substring(start2, end2);
		// The optimal local alignment between seq1 and seq2 is the
		// optimal global alignment between subseq1 and subseq2:
		return (new NWAffine(sub, d, e, subseq1, subseq2)).getMatch();
	}
}
