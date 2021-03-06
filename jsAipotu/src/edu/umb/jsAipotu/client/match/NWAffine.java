package edu.umb.jsAipotu.client.match;

//Global alignment using the Needleman-Wunsch algorithm (affine gap costs)

public class NWAffine extends AlignAffine {
	
	public NWAffine(Substitution sub, int d, int e, String sq1, String sq2) {
		super(sub, d, e, sq1, sq2);
		int n = this.n, m = this.m;
		int[][] score = sub.score;
		int[][] M = F[0], Ix = F[1], Iy = F[2];
		for (int i=1; i<=n; i++) {
			Ix[i][0] = -d - e * (i-1);
			B[1][i][0] = new Traceback3(1, i-1, 0);
		}
		for (int i=1; i<=n; i++)
			Iy[i][0] = M[i][0] = NegInf;
		for (int j=1; j<=m; j++) {
			Iy[0][j] = -d - e * (j-1);
			B[2][0][j] = new Traceback3(2, 0, j-1);
		}
		for (int j=1; j<=m; j++)
			Ix[0][j] = M[0][j] = NegInf;
		for (int i=1; i<=n; i++)
			for (int j=1; j<=m; j++) {
				int val;
				int s = score[seq1.charAt(i-1)][seq2.charAt(j-1)];
				val = M[i][j] = max(M[i-1][j-1]+s, Ix[i-1][j-1]+s, Iy[i-1][j-1]+s);
				if (val == M[i-1][j-1]+s)
					B[0][i][j] = new Traceback3(0, i-1, j-1);
				else if (val == Ix[i-1][j-1]+s)
					B[0][i][j] = new Traceback3(1, i-1, j-1);
				else if (val == Iy[i-1][j-1]+s)
					B[0][i][j] = new Traceback3(2, i-1, j-1);
				else 
					throw new Error("NWAffine 1");
				val = Ix[i][j] = max(M[i-1][j]-d, Ix[i-1][j]-e, Iy[i-1][j]-d);
				if (val == M[i-1][j]-d)
					B[1][i][j] = new Traceback3(0, i-1, j);
				else if (val == Ix[i-1][j]-e)
					B[1][i][j] = new Traceback3(1, i-1, j);
				else if (val == Iy[i-1][j]-d)
					B[1][i][j] = new Traceback3(2, i-1, j);
				else 
					throw new Error("NWAffine 2");
				val = Iy[i][j] = max(M[i][j-1]-d, Iy[i][j-1]-e, Ix[i][j-1]-d);
				if (val == M[i][j-1]-d)
					B[2][i][j] = new Traceback3(0, i, j-1);
				else if (val == Iy[i][j-1]-e)
					B[2][i][j] = new Traceback3(2, i, j-1);
				else if (val == Ix[i][j-1]-d)
					B[2][i][j] = new Traceback3(1, i, j-1);
				else 
					throw new Error("NWAffine 3");
			}
		// Find maximal score
		int maxk = 0;
		int maxval = F[0][n][m];
		for (int k=1; k<3; k++)
			if (maxval < F[k][n][m]) {
				maxval = F[k][n][m];
				maxk = k;
			}
		B0 = new Traceback3(maxk, n, m);
	}
}
