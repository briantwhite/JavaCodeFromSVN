/*
 * optimize.c
 *
 *  Created on: Feb 20, 2011
 *      Author: brian
 */

/* Function Calculate_MS  forms the heart of the MS calculations for a code optimisation  search algorithm. It accepts as */
/* arguments the codon matrix (representing the particular genetic code under consideration), the amino acid array */
/* (determining the meanings and   associated parameter values for each of the amino acid values), and the settings struct */
/* (which contains the actual modular difference values for particular amino acid errors)   used to calculate the overall MS value */
/* (MS[0]) for the 64 codons in a given genetic code The MS[0] value is returned as the return-value of the function                         */
float Calculate_MS(MATRIX_TYPE* codon_matrix_ptr, ACID_ELEMENT_TYPE* acid_array, SETTINGS_TYPE* settings_ptr, short power, short weight)
{
 MUTANT	mutant;
 short	mut_a_num, stt_a_num, i, j;
 float 	MS_value=0, total_SS=0, num_obs=0, weighting, this_SS, parameter_difference;
 int    counter;

 for(counter = 0; counter<settings_ptr->num_mutants; counter +=1)
    {
     mutant = settings_ptr->mutant_array[counter];
     mut_a_num = (*codon_matrix_ptr)[mutant.mut_coord.x][mutant.mut_coord.y][mutant.mut_coord.z].acid_number;
     stt_a_num = (*codon_matrix_ptr)[mutant.stt_coord.x][mutant.stt_coord.y][mutant.stt_coord.z].acid_number;
     weighting = Find_Weighting(settings_ptr->MS_model, weight, mutant);
     i = (short) acid_array[stt_a_num].parameter;
     j = (short) acid_array[mut_a_num].parameter;
    parameter_difference = (float) pow(settings_ptr->PAM_matrix[i][j], power);
    this_SS = weighting * parameter_difference;
    num_obs  += weighting;
    total_SS += this_SS;
   }
  if((total_SS>0)&&(num_obs>0)) MS_value = total_SS/num_obs;
   else MS_value = -99;
  return MS_value;
}/* End of Function */


/* Function Local_Peturb performs a local peturbation of a genetic code configuration, swapping the amino acid meanings */
/* of two synonymous codon blocks. This swap is between any two codon blocks if the Haig and Hurst shuffle model is being */
/*considered, or between two blocks within a biosynthetic pathway if the Taylor and Coates shuffle method is being considered           */
void Local_Peturb(SETTINGS_TYPE* settings_ptr, short* choice_1, short* choice_2)
{
 short multiplier;

 if (settings_ptr->variation_model==2) /* Taylor and Coates peturbation */
  {
   multiplier= Random_Int(0,3);
   *choice_1 = (multiplier*5)+Random_Int(1, 5);
   *choice_2 = (multiplier*5)+Random_Int(1, 5);
  }
 else
  {
   *choice_1 = Random_Int(1, 20); /* Haig and Hurst peturbation */
   *choice_2 = Random_Int(1, 20);
  }
}



/* Function Optimise_Code uses a minimisation version of the Great_Deluge_Algorithm in     */
/* order to estimate the optimum code MS value possible under any particular set of rules  */
/* (tr/tv weight etc) see Dueck, G., "New Optimization Heuristics",                        */
/* Journal of Computational Physics, 103,  1992 for formal testing of this algorithmg      */
void Optimise_Code(void (*Shuffle_Code)(ACID_ELEMENT_TYPE*), MATRIX_TYPE* codon_matrix_ptr, ACID_ELEMENT_TYPE* acid_array, SETTINGS_TYPE* settings_ptr)
{
 ACID_ARRAY         		ref_array;
 ACID_ELEMENT_TYPE	acid_1, acid_2;
 long			tries;
 short			choice_1, choice_2, count2, weight, power, opt_runs;
 float			best_code, water_level, evapouration = (float)0.001, current_MS_value, variant_MS_value, code_MS;

 for(count2 = 0; count2<22; count2 +=1)    /* stores a copy of the natural genetic code*/
  ref_array[count2] = acid_array[count2];  /* in acid array ref_array                  */

 settings_ptr->results_file_ptr = Safe_Open("results.txt\0", "w\0", FALSE);
 Print_Results_Header(settings_ptr, codon_matrix_ptr, acid_array);
 fprintf(settings_ptr->results_file_ptr, "\n\n :   power  :tr/tv wgt :best MS :code MS\n");

 /* MAIN FUNCTION LOOPS: optimizations for weight x power x no. runs per setting */
 for(weight=settings_ptr->start_weight; weight<=settings_ptr->end_weight; weight+=1)
  for(power=1; power<=settings_ptr->power; power+=1)
   {
    for(count2 = 0; count2<22; count2 +=1)    /* resets genetic code to natural code*/
    acid_array[count2] = ref_array[count2];
    code_MS = Calculate_MS(codon_matrix_ptr, acid_array, settings_ptr, power, weight);
    best_code = code_MS;
    printf("\n Currently looking at...power = %hd; tr/tv wgt= %hd;(code MS=%8.3f)", power, weight, code_MS);
    printf("\n [current ][water lv][best MS ][evap. ][RUN]\n");
    evapouration *= 5; if (power == 1) evapouration = (float)0.001; /* sets evapouration to an appropriate level */
    for(opt_runs = 0; opt_runs < 50; opt_runs+=1)
    {
     Shuffle_Code(acid_array);
     current_MS_value = Calculate_MS(codon_matrix_ptr, acid_array, settings_ptr, power, weight);
     water_level = (float)(current_MS_value *1.5);
     tries = 0;
     while(tries<1000)
     {
      tries +=1;
      Local_Peturb(settings_ptr, &choice_1, &choice_2);
      acid_1 = acid_array[choice_1];
      acid_2 = acid_array[choice_2];
      acid_array[choice_1] = acid_2;
      acid_array[choice_2] = acid_1;
      variant_MS_value = Calculate_MS(codon_matrix_ptr, acid_array, settings_ptr, power, weight);
      if(variant_MS_value < water_level)
       {
        if(variant_MS_value<best_code) best_code = variant_MS_value;
		current_MS_value = variant_MS_value;
        water_level -= evapouration;
        for(count2=0; count2<46; count2+=1) printf("%c", 8);
        printf(" [%8.3f][%8.3f][%8.3f][%6.3f][%3hd]", current_MS_value, water_level, best_code, evapouration, opt_runs);
        tries = 0;
       }
      else
       {
        acid_array[choice_1] = acid_1; /* discards variation */
        acid_array[choice_2] = acid_2;
       }

     }/* end of 'tries' loop*/
    } /*end of 'opt_runs' loop */
   fprintf(settings_ptr->results_file_ptr, "\n :%10hd:%10hd:%8.3f:%8.3f", power, weight, best_code, code_MS);
   }/* end of for...next loops */

 fclose(settings_ptr->results_file_ptr);
}
