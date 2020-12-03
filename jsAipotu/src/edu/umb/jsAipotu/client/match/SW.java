package edu.umb.jsAipotu.client.match;

//Local alignment with the Smith-Waterman algorithm (simple gap costs)

public class SW extends AlignSimple {
	public SW(Substitution sub, int d, String sq1, String sq2) {
		super(sub, d, sq1, sq2);
		int n = this.n, m = this.m;
		int[][] score = sub.score;
		int maxi = n, maxj = m;
		int maxval = NegInf;
		for (int i=1; i<=n; i++)
			for (int j=1; j<=m; j++) {
				int s = score[seq1.charAt(i-1)][seq2.charAt(j-1)];
				int val = max(0, F[i-1][j-1]+s, F[i-1][j]-d, F[i][j-1]-d);
				F[i][j] = val;
				if (val == 0)
					B[i][j] = null;
				else if (val == F[i-1][j-1]+s)
					B[i][j] = new Traceback2(i-1, j-1);
				else if (val == F[i-1][j]-d)
					B[i][j] = new Traceback2(i-1, j);
				else if (val == F[i][j-1]-d)
					B[i][j] = new Traceback2(i, j-1);
				else
					throw new Error("SW 1");
				if (val > maxval) {
					maxval = val;
					maxi = i; maxj = j;
				}
			}
		B0 = new Traceback2(maxi, maxj);
	}
}
