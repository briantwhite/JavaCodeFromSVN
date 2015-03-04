/*
 * 1_sample.c
 *
 *  Created on: Feb 20, 2011
 *      Author: brian
 */

/* Produces a sample of possible genetic codes (sample size, 'n'=settings_ptr->num_iterations)         */
/* according to either the haig & hurst shuffle method ('unrestricted') or the Taulor and Coates       */
/* shuffle method ('restricted') as dictated by settings_ptr->variation_model (1 or 2 respectively)    */

  /*
   * array to map the internal amino acid numbers - numbered by slots in code table where the
   * first is phe
   * need to map to order in MS0-calculating Excel sheet
   *  #in acid_array	amino acid 	# for excel sheet
   *   #0 = ALL 	0										(some sort of debug thing)
   *   #1 = PHE
   *   #2 = SER
   *   #3 = TYR
   *   #4 = CYS
   *   #5 = TRP
   *   #6 = LEU
   *   #7 = PRO
   *   #8 = HIS
   *   #9 = GLN
   *   #10 = ARG
   *   #11 = ILE
   *   #12 = MET
   *   #13 = THR
   *   #14 = ASN
   *   #15 = LYS
   *   #16 = VAL
   *   #17 = ALA
   *   #18 = ASP
   *   #19 = GLU
   *   #20 = GLY
   *   #21 = TER (who knows why)
   *
   *   want to read out in this order:
   *   aa0 aa17 aa10 aa18 aa14 aa4 aa19 aa9 aa20 aa8 aa11 aa6 aa15 aa12 aa1 aa7 aa2 aa13 aa5 aa3 aa16 aa21
   *   this array's index is the order in the list above (the desired print order)
   *   the values are the lookup into the acid_array
   *
   */
  int lookup[] = {0, 17, 10, 18, 14, 4, 19, 9, 20, 8, 11, 6, 15, 12, 1, 7, 2, 13, 5, 3, 16, 21};

/* Function Calculate_Code_MS  forms the heart of the MS calculations for a particular     */
/* genetic code's MS values. It accepts as arguments the codon matrix (representing the    */
/* particular gentic code under consideration) and the amino acid array (determining the   */
/* meanings and associated parameter values for each of the amino acid values. These are   */
/* used to calculate the 4 MS values for the 64 codons in a given genetic code. the MS     */
/* values are stored in the MS fields of varaible code_MS (struct ALL_MS) and then printed */
/* to a temporary (binary) results file by function Print_MA_Values                        */
void Calculate_Code_MS(MATRIX_TYPE* codon_matrix_ptr,
		ACID_ELEMENT_TYPE* acid_array,
		SETTINGS_TYPE* settings_ptr,
		FILE* codeFile)
{
 ALL_MS	code_MS;
 MUTANT	mutant;
 short	mut_a_num, stt_a_num, i, j;
 float 	weighting, this_SS, parameter_difference;
 int    counter;

 // array to save sums of squares for variance calc
 // just make it way bigger than needed
 float ss[64 * 27];

 code_MS = Clear_All_MS();
 for(counter = 0; counter<settings_ptr->num_mutants; counter +=1)
    {
     mutant = settings_ptr->mutant_array[counter];
     mut_a_num = (*codon_matrix_ptr)[mutant.mut_coord.x][mutant.mut_coord.y][mutant.mut_coord.z].acid_number;
     stt_a_num = (*codon_matrix_ptr)[mutant.stt_coord.x][mutant.stt_coord.y][mutant.stt_coord.z].acid_number;
// for debugging purposes
//  this prints all the mutants in the set - used to test for off-by-one error
//    see Square Evolution Log 03.docx page 28-33)
//     printf("Mutant # %d: %d %d %d (%s) -> %d %d %d (%s)\n",
//    		 counter,
//    		 mutant.stt_coord.x, mutant.stt_coord.y, mutant.stt_coord.z,
//   		 acid_array[stt_a_num].name,
//    		 mutant.mut_coord.x,mutant. mut_coord.y, mutant.mut_coord.z,
//    		 acid_array[mut_a_num].name);
     weighting = Find_Weighting(settings_ptr->MS_model, settings_ptr->start_weight, mutant);
     i = (short) acid_array[stt_a_num].parameter;
     j = (short) acid_array[mut_a_num].parameter;
    parameter_difference = (float) pow(settings_ptr->PAM_matrix[i][j], settings_ptr->power);
    this_SS = weighting * parameter_difference;
    code_MS.MS[0].num_obs  += weighting;
    code_MS.MS[0].total_SS += this_SS;
    code_MS.MS[mutant.base_number].num_obs  += weighting;
    code_MS.MS[mutant.base_number].total_SS += this_SS;
    ss[counter] = this_SS;
  }
  Store_MS_Values(settings_ptr, settings_ptr->tmp_file_ptr, code_MS);

  // calculate variance
  float variance = 0.0f;
  float mean = (code_MS.MS[0].total_SS)/(code_MS.MS[0].num_obs);
  for (counter = 0; counter<settings_ptr->num_mutants; counter +=1) {
	  variance = variance + (ss[counter] - mean)*(ss[counter] - mean);
  }
  variance = variance/(code_MS.MS[0].num_obs);

  // save the code and params to the codeFile
  int x;
  for (x = 0; x < 22; x++) {
	  fprintf(codeFile,"%s,", acid_array[lookup[x]].name);
  }
  fprintf(codeFile, "%3.5f,%3.5f\n",mean, variance);

}/* End of Function */

/* Function Produce_Single_Sample produces a sample for a  single set of parameter values (Tr/Tv bias, */
/* modular power function etc.), stores each sample value in a temporary binary file, then reads in    */
/* this binary file to form a distribution which is stored as an ASCII file, easily read by XL.        */
void Produce_Single_Sample (void (*Shuffle_Code)(ACID_ELEMENT_TYPE*), MATRIX_TYPE* codon_matrix_ptr, ACID_ELEMENT_TYPE* acid_array, SETTINGS_TYPE* settings_ptr)
{
 FILE* 				codeFile;
 ACID_ARRAY			ref_array;
 long				counter;
 short				count2;

 // set up the output file for the codes we generate
 codeFile = fopen("codes.csv", "w");
 int x;
 // print the header
 for (x = 0; x < 22; x++) {
	 fprintf(codeFile, "aa%d,",lookup[x]);
 }
 fprintf(codeFile, "MS0,Var0\n");


 for(count2 = 0; count2<22; count2 +=1)/* copies a reference copy of the amino acid array     */
  {                                    /* into array 'ref_array' (used to reset after shuffle)*/
   ref_array[count2].parameter = acid_array[count2].parameter;
   strcpy(ref_array[count2].name, acid_array[count2].name);
  }

 settings_ptr->tmp_file_ptr = tmpfile();
 printf("\n Analysing:[0000000]");
 settings_ptr->results_file_ptr = Safe_Open ("results.txt\0", "w\0", TRUE);
 Print_Results_Header(settings_ptr, codon_matrix_ptr, acid_array);

 for(counter = 0; counter<=settings_ptr->num_iterations; counter+=1)
 {
  Calculate_Code_MS(codon_matrix_ptr, acid_array, settings_ptr, codeFile);
  Shuffle_Code(acid_array);
  printf("%c%c%c%c%c%c%c%c%7ld]", 8,8,8,8,8,8,8,8,counter);
 }

 Read_Temp_File(settings_ptr, codon_matrix_ptr, ref_array);/* Reads in the temporary binary file of MS values, and forms a freq. distribution */
 fclose(settings_ptr->tmp_file_ptr);
 fclose(settings_ptr->results_file_ptr);
}
