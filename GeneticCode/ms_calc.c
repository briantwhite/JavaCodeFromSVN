/*
 * ms_calc.c
 *
 *  Created on: Feb 20, 2011
 *      Author: brian
 */

/* Function Clear_All_MS clears the four fields of an MS_struct */
ALL_MS Clear_All_MS(void)
{
 ALL_MS all_MS;
 short  MS_counter;

 for(MS_counter=0; MS_counter<4; MS_counter+=1)
  {
   all_MS.MS[MS_counter].num_obs = 0;
   all_MS.MS[MS_counter].total_SS = 0;
  }

 return all_MS;
}

/* Function Find_tMS_Weighting finds the particular weighting of an MS MS calculation for one instance of a mutation (from starting*/
/* codon to mutant codon) for use under the MISTRANSLATION MODEL. Under this model, the base position of the mutation is */
/* important as  well as the transition/transversion nature of the mutation.It accepts the MUTANT struct descibing the mutation */
/* in question as its argument, and returns a (float) number representing the appropriate weighting                                              */
float Find_tMS_Weighting(MUTANT this_mutant)
{
 short base_number;
 float base_wgt, weighting;

 weighting = 1;
 base_number = (unsigned short int) this_mutant.base_number;

 switch(base_number)
 {
  case 1: base_wgt = (float) 0.5; break;
  case 2: base_wgt = (float) 0.1; break;
  case 3: base_wgt = (float) 1;   break;
  default: printf ("\n\n ERROR 1"); Pause(); break;
 };

 if(this_mutant.is_transition == TRUE)
  switch(base_number)
  {
   case 1: weighting = (float) 2; break;
   case 2: weighting = (float) 5; break;
   case 3: weighting = (float) 1; break;
   default: printf("\n\n ERROR 2"); Pause(); break;
  };

 weighting = weighting*base_wgt;
 return weighting;
}

/* Function find weighting calculates the appropriate weighting for a codon error, depending on */
/* whether it represents a transition or transversion error, and what the current analysis model is */
float Find_Weighting(short MS_model, short wgt_counter, MUTANT mutant)
{
 float weighting;

 switch(MS_model)
  {
   case 1: if(mutant.is_transition == TRUE) weighting = (float) (wgt_counter); else weighting = 1; break;
   case 2: weighting = Find_tMS_Weighting(mutant); break;
   case 3: if(mutant.is_A == TRUE) weighting = (float) (wgt_counter); else weighting = 1; break;
   default: printf("\n ERROR in model type"); Pause(); break;
  };

 return weighting;
}

