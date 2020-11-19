package edu.umb.jsAipotu.match;

//The BLOSUM50 substitution matrix for amino acids (Durbin et al, p 16)

public class DNAidentity extends Substitution {
	
	private String residues = "AGCT";
	
	public String getResidues() 
	{ return residues; }
	
	private int[][] residuescores = 
		      /* A     G     C     T */
	{ /* A */ {  1                   },
	  /* G */ { -1000,  1            },
	  /* C */ { -1000,-1000, 1       },
	  /* T */ { -1000,-1000,-1000, 1 } 
			  /* A     G     C     T */
	};
	
	public DNAidentity() 
	{ buildscore(residues, residuescores); }
}
