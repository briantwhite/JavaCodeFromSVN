# define TRUE  1
# define FALSE 0
#define  MAX_ITERATIONS 2000000

#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <math.h>
#include <stdlib.h>

#include "codon.h"
#include "gen_io.c"
#include "random.c"
#include "parse.c"
#include "in_out.c"
#include "mut_calc.c"
#include "set_up.c"
#include "setting.c"
#include "ms_calc.c"
#include "factoria.c"
#include "shuffle.c"
#include "analyse.c"
#include "1_sample.c"
#include "n_samples.c"
#include "optimize.c"




void main()
{
 SETTINGS_TYPE	settings;      /* the three fundamental data structures */
 MATRIX_TYPE	codon_matrix;
 ACID_ARRAY	acid_array;

 Initial_Set_Up(&codon_matrix, acid_array, &settings); /* Loads defaults */

 Display_Pre_Run_Options(&codon_matrix, acid_array, &settings); /* allows user manipulationof parameters */

 Build_Mutant_Array(&settings, acid_array, &codon_matrix); /* calculates all errors possible within the code under consideration */

 switch(settings.results_model) /* Executes a particular type of analysis */
 {
  case 1: if(settings.variation_model == 1) Produce_Single_Sample (Full_Code_Shuffle, &codon_matrix, acid_array, &settings);
          else Produce_Single_Sample(Restricted_Code_Shuffle, &codon_matrix, acid_array, &settings); break;
  case 2: if(settings.variation_model == 1) Produce_Multi_Samples(Full_Code_Shuffle, &codon_matrix, acid_array, &settings);
          else Produce_Multi_Samples(Restricted_Code_Shuffle, &codon_matrix, acid_array, &settings); break;
  case 3: if(settings.variation_model == 1) Optimise_Code(Full_Code_Shuffle, &codon_matrix, acid_array, &settings);
          else Optimise_Code(Restricted_Code_Shuffle, &codon_matrix, acid_array, &settings); break;
  default: printf("\n ERROR in 'main.c': I don't know what to do!"); Pause(); break;
 }

 printf("\n Analysis Complete: review contents of RESULTS.TXT");
 Pause();
}



