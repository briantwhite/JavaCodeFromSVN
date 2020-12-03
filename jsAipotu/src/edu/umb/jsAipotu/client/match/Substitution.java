package edu.umb.jsAipotu.client.match;

//The class of substitution (scoring) matrices
abstract class Substitution {
	public int[][] score;
	
	void buildscore(String residues, int[][] residuescores) {
		// Allow lowercase and uppercase residues (ASCII code <= 127):
		score = new int[127][127];
		for (int i=0; i<residues.length(); i++) {
			char res1 = residues.charAt(i);
			for (int j=0; j<=i; j++) {
				char res2 = residues.charAt(j);
				score[res1][res2] = score[res2][res1] 
				                                = score[res1][res2+32] = score[res2+32][res1] 
				                                                                        = score[res1+32][res2] = score[res2][res1+32] 
				                                                                                                             = score[res1+32][res2+32] = score[res2+32][res1+32] 
				                                                                                                                                                        = residuescores[i][j];
			}
		}
	}
	
	abstract public String getResidues();
}
