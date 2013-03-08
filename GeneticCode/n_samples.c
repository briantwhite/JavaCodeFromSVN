/*
 * n_samples.c
 *
 *  Created on: Feb 20, 2011
 *      Author: brian
 */

/* Produces a sample of possible genetic codes (sample size, 'n'=settings_ptr->num_iterations)  according to either the */
/* Haig & Hurst shuffle method ('unrestricted') or the Taulor and Coates shuffle method ('restricted') as dictated */
/* by settings_ptr->variation_model (1 or 2 respectively). It contains the necessary functions to produce this sample over a range of  */
/* parameter settings correpsonding to a range of transition/transversion weightings and a range of modular power     */
/* functions: it may thus be used to explore parameter space.                                      */

/* Function Store_Nat_MS  is used to load the MS values of the starting code configuration into the appropriate fields of the */
/* results matrix. these values will be used to log the number of 'better' codes (lower MS values) when variant codes */
/* are produced In addition, this function clears the MS values of the natural code from the running  total of MS values */
/* (field tot_MS) and from the running total of squared MS values (field tsq_MS). These fields are used at the end of a */
/* simulation run to calculate the mean and variance of the random sample's MS values.  */
void Store_Nat_MS(MS_STRUCT **results_matrix, short num_weightings, short top_power)
{
 double	MS_value;
 short	wgt_counter, power, base;
 for(power = 0; power<top_power; power+=1)
  for(wgt_counter = 0; wgt_counter<=num_weightings; wgt_counter+=1)
   for(base=0; base<4; base+=1)
    {
     MS_value = results_matrix[power][wgt_counter].tot_MS[base];
     results_matrix[power][wgt_counter].nat_MS[base] = MS_value;  /* Loads the MS value of the start code into the appropriate field */
     results_matrix[power][wgt_counter].min_MS[base] = MS_value;  /* Loads the MS value of the start code as the min MS value */
     results_matrix[power][wgt_counter].tot_MS[base] = 0;         /* clears this initial MS value from the summed total */
     results_matrix[power][wgt_counter].tsq_MS[base] = 0;         /* and from the sum of squared totals                 */
    }
}/* End of Function */

/* Function Update_MS_Matrix  forms the heart of the MS calculations for a particular exploration of parameter space. */
/* A dynamically allocated matrix is formed, with members for each value of the modular power function and each value */
/* of the tr/tv bias Each matrix element contains fields for the sum of amino acid parameter differences and the corresponding */
/* number of observations for each MS value [0..3] for a particular code configuration. The former is then divided by the latter */
/* to provide an MS value and this value is (i) tested against the value of the natural code, to update te number of better */
/* codes (field num_bt). (ii) is added to a running total of MS values for the sample of random variant codes and (iii) */
/* is squared and added to the total of squared MS values of the sample. Values (ii) and (iii) are used at the end of a simulation */
/* run to  calculate the mean and variance of the MS values of the sample                          */
void Update_MS_Matrix(SETTINGS_TYPE* settings_ptr, MATRIX_TYPE* codon_matrix_ptr, ACID_ELEMENT_TYPE* acid_array, MS_STRUCT **results_matrix)
{
 MUTANT	mutant;
 short		wgt_counter, power, mut_a_num, stt_a_num, base, i, j;
 float 		weighting, this_SS, parameter_difference;
 double MS_value;
 int    counter, weight;
 for(counter = 0; counter<settings_ptr->num_mutants; counter +=1)
    {
     mutant = settings_ptr->mutant_array[counter];
     mut_a_num = (*codon_matrix_ptr)[mutant.mut_coord.x][mutant.mut_coord.y][mutant.mut_coord.z].acid_number;
     stt_a_num = (*codon_matrix_ptr)[mutant.stt_coord.x][mutant.stt_coord.y][mutant.stt_coord.z].acid_number;
     i = (short) acid_array[stt_a_num].parameter;     j = (short) acid_array[mut_a_num].parameter;
     parameter_difference = settings_ptr->PAM_matrix[i][j];
     for(power = 0; power<settings_ptr->power; power+=1)
      for(wgt_counter = settings_ptr->start_weight; wgt_counter<=settings_ptr->end_weight; wgt_counter+=1)
  	 {
	   weighting = Find_Weighting(settings_ptr->MS_model, wgt_counter, mutant);
        	   this_SS = (float)(weighting * pow(parameter_difference, power+1));
        	  results_matrix[power][wgt_counter-settings_ptr->start_weight].num_obs[0]  += weighting;
        	  results_matrix[power][wgt_counter-settings_ptr->start_weight].num_obs[mutant.base_number]  += weighting;
        	  results_matrix[power][wgt_counter-settings_ptr->start_weight].tot_SS[0] += this_SS;
        	  results_matrix[power][wgt_counter-settings_ptr->start_weight].tot_SS[mutant.base_number] += this_SS;
   	}/* end of modular power & tr/tv weightings loop */
     }/* end of mutant array loop */
   for(power = 0; power<settings_ptr->power; power+=1)
    for(wgt_counter = settings_ptr->start_weight; wgt_counter<=settings_ptr->end_weight; wgt_counter+=1)
     for(base = 0; base<4; base +=1)
     {
      weight = wgt_counter - settings_ptr->start_weight;
      MS_value = results_matrix[power][weight].tot_SS[base]/results_matrix[power][weight].num_obs[base];
      if(MS_value < results_matrix[power][weight].nat_MS[base]) results_matrix[power][weight].num_bt[base] += 1;
      if(MS_value < results_matrix[power][weight].min_MS[base]) results_matrix[power][weight].min_MS[base] = (float)MS_value;
      results_matrix[power][weight].tot_MS[base] += MS_value;
      results_matrix[power][weight].tsq_MS[base] += pow(MS_value, 2);
      results_matrix[power][weight].tot_SS[base]  = 0;
      results_matrix[power][weight].num_obs[base] = 0;
     }
}/* End of Function */


/* Function Produce_Multi_Samples produces samples for each point over a variety of parameter setttings*/
/* (tr/tv biases and modular power functions; individual code values are NOT stored, but instead are   */
/* used to update a matrix of values (number of better codes found, total MS for the sample etc. etc.) */
void Produce_Multi_Samples(void (*Shuffle_Code)(ACID_ELEMENT_TYPE*), MATRIX_TYPE* codon_matrix_ptr, ACID_ELEMENT_TYPE* acid_array, SETTINGS_TYPE* settings_ptr)
{
 MS_STRUCT			**results_ptr;
 ACID_ARRAY			ref_array;
 long				counter;
 short				count2;

 /* The following 2 statements create a matrix of (i x j) elements to store results */
 /* where i = number of modular powers to be tested, and j is the number of tr/tv   */
 /* weightings to be tested. Each element of the matrix is a struct of type RESULTS_*/
 /* _STRUCT, and thus contains 4 fileds each (for each MS position) of natrual code */
 /* MS, number of better codes found, the total MS value and the total^2 MS value   */
 results_ptr = calloc(settings_ptr->power, sizeof(MS_STRUCT*));
 for(counter = 0; counter<=settings_ptr->power; counter+=1)
  results_ptr[counter] = calloc((settings_ptr->end_weight-settings_ptr->start_weight)+1, sizeof(MS_STRUCT));

 settings_ptr->results_file_ptr = Safe_Open ("results.txt\0", "w\0", TRUE);
 Print_Results_Header(settings_ptr, codon_matrix_ptr, acid_array);

 for(count2 = 0; count2<22; count2 +=1)/* creates a reference copy of the amino acid array     */
  {                                    /* into array 'ref_array' (used to reset after shuffle) */
   ref_array[count2].parameter = acid_array[count2].parameter;
   strcpy(ref_array[count2].name, acid_array[count2].name);
  }

 Update_MS_Matrix(settings_ptr, codon_matrix_ptr, acid_array, results_ptr);
 Store_Nat_MS(results_ptr, (short)(settings_ptr->end_weight-settings_ptr->start_weight), (short)(settings_ptr->power));
 printf("\n Analysing:[0000000]");
 for(counter = 0; counter<settings_ptr->num_iterations; counter+=1)
  {
   Shuffle_Code(acid_array);

   Update_MS_Matrix(settings_ptr, codon_matrix_ptr, acid_array, results_ptr);

   printf("%c%c%c%c%c%c%c%c%7ld]", 8,8,8,8,8,8,8,8,counter+1);
  }

 Print_MS_Matrix(results_ptr, settings_ptr->results_file_ptr, (short)(settings_ptr->end_weight-settings_ptr->start_weight), (short)(settings_ptr->power), settings_ptr->num_iterations);
}
