package edu.umb.jsAipotu.client.match;

//The BLOSUM50 substitution matrix for amino acids (Durbin et al, p 16)

public class Blosum50 extends Substitution {
	
	private String residues = "ARNDCQEGHILKMFPSTWYV";
	
	public String getResidues() 
	{ return residues; }
	
	private int[][] residuescores = 
		/* A  R  N  D  C  Q  E  G  H  I  L  K  M  F  P  S  T  W  Y  V */
	{ /* A */ {  5                                                          },
			/* R */ { -2, 7                                                       },
			/* N */ { -1,-1, 7                                                    },
			/* D */ { -2,-2, 2, 8                                                 },
			/* C */ { -1,-4,-2,-4,13                                              },
			/* Q */ { -1, 1, 0, 0,-3, 7                                           },
			/* E */ { -1, 0, 0, 2,-3, 2, 6                                        },
			/* G */ {  0,-3, 0,-1,-3,-2,-3, 8                                     },
			/* H */ { -2, 0, 1,-1,-3, 1, 0,-2,10                                  },
			/* I */ { -1,-4,-3,-4,-2,-3,-4,-4,-4, 5                               },
			/* L */ { -2,-3,-4,-4,-2,-2,-3,-4,-3, 2, 5                            },
			/* K */ { -1, 3, 0,-1,-3, 2, 1,-2, 0,-3,-3, 6                         },
			/* M */ { -1,-2,-2,-4,-2, 0,-2,-3,-1, 2, 3,-2, 7                      },
			/* F */ { -3,-3,-4,-5,-2,-4,-3,-4,-1, 0, 1,-4, 0, 8                   },
			/* P */ { -1,-3,-2,-1,-4,-1,-1,-2,-2,-3,-4,-1,-3,-4,10                },
			/* S */ {  1,-1, 1, 0,-1, 0,-1, 0,-1,-3,-3, 0,-2,-3,-1, 5             },
			/* T */ {  0,-1, 0,-1,-1,-1,-1,-2,-2,-1,-1,-1,-1,-2,-1, 2, 5          },
			/* W */ { -3,-3,-4,-5,-5,-1,-3,-3,-3,-3,-2,-3,-1, 1,-4,-4,-3,15       },
			/* Y */ { -2,-1,-2,-3,-3,-1,-2,-3, 2,-1,-1,-2, 0, 4,-3,-2,-2, 2, 8    },
			/* V */ {  0,-3,-3,-4,-1,-3,-3,-4,-4, 4, 1,-3, 1,-1,-3,-2, 0,-3,-1, 5 } 
			/* A  R  N  D  C  Q  E  G  H  I  L  K  M  F  P  S  T  W  Y  V */
	};
	
	public Blosum50() 
	{ buildscore(residues, residuescores); }
}