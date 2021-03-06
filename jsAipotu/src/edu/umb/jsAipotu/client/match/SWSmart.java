package edu.umb.jsAipotu.client.match;

//Local alignment with the Smith-Waterman algorithm (simple gap
//costs, smart linear space algorithm)

public class SWSmart extends AlignSmart {
	Traceback2[][] start; // Best alignment ending at (i,j) begins at start[i][j]
	int maxval;           // Score of best alignment
	int start1, start2;   // Best alignment begins at (start1, start2)
	int end1, end2;       // Best alignment ends at (end1, end2)
	
	public SWSmart(Substitution sub, int d, String sq1, String sq2) {
		super(sub, d, sq1, sq2);
		int n = this.n, m = this.m;
		int[][] score = sub.score;
		start = new Traceback2[2][m+1];
		maxval = NegInf;
		// Initialize first column (i=0):
		for (int j=0; j<=m; j++)
			start[1][j] = new Traceback2(0, j);
		for (int i=1; i<=n; i++) {
			swap01(F); swap01(start);
			// F[1] represents (new) column i and F[0] represents (old) column i-1
			// Initialize first row (j=0):
			start[1][0] = new Traceback2(i, 0);
			for (int j=1; j<=m; j++) {
				int s = score[seq1.charAt(i-1)][seq2.charAt(j-1)];
				int val = max(0, F[0][j-1]+s, F[0][j]-d, F[1][j-1]-d);
				F[1][j] = val;
				if (val == 0)           // Best alignment starts (and ends) here
					start[1][j] = new Traceback2(i, j);
				else if (val == F[0][j-1]+s)
					start[1][j] = start[0][j-1];
				else if (val == F[0][j]-d)
					start[1][j] = start[0][j];
				else if (val == F[1][j-1]-d)
					start[1][j] = start[1][j-1];
				else
					throw new Error("SWSmart 1");
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
		return (new NWSmart(sub, d, subseq1, subseq2)).getMatch();
	}
}
