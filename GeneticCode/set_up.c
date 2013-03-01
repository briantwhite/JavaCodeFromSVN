/*
 * set_up.c
 *
 *  Created on: Feb 20, 2011
 *      Author: brian
 */

/*  function Set_Acid_String sets up the array of 22 elements of type ACID_ELEMENT_TYPE. (each thus contains a 4 character    */
/*   field for the amino acid name, and a float field for the  corresponding parameter).                                       */

/*   This function simply fills in the name fields from an internal  (i.e. static) string contaning the 3 letter abreviations        */
/*   for all 20 amino acids, a field for TER codons, and an error  field termed ALL                                                */

void Set_Acid_String(ACID_ELEMENT_TYPE* acid_array)
{
 char base_string[67];
 int  counter, letter;

 strcpy(base_string, "ALLPHESERTYRCYSTRPLEUPROHISGLNARGILEMETTHRASNLYSVALALAASPGLUGLYTER\0");

 for(counter = 0; counter<22; counter++)
  {
   acid_array[counter].name[3] = '\0';
   for(letter = 0; letter<3; letter++)
	acid_array[counter].name[letter] = base_string[(counter*3)+letter];
  }
}



/*  Function Initial_Set_Up loads default values for all the  fundamental data structures: the codon matrix, the amino      */
/*  acid array and the settings data structure                    */
void Initial_Set_Up(MATRIX_TYPE* matrix_ptr, ACID_ELEMENT_TYPE* acid_array, SETTINGS_TYPE* settings_ptr)
{
 char  parameter_file_string[80];
 FILE* code_file_ptr;

 Set_Acid_String(acid_array);
 strcpy(parameter_file_string, "a_acid_f.txt\0"); /* These are the paths for the two crucial set                   */

 settings_ptr->similarity_measure = 1;
 settings_ptr->variation_model = 1;
 settings_ptr->MS_model = 1;
 settings_ptr->results_model = 1;
 settings_ptr->power = 2;
 settings_ptr->trap_codes = FALSE;
 settings_ptr->num_iterations = 1000;
 settings_ptr->start_weight = 1;
 settings_ptr->end_weight = 1;

 Read_Acid_Parameters(settings_ptr, acid_array, parameter_file_string);

 code_file_ptr = Safe_Open("matrix_f.cod\0", "r\0", FALSE);
 Read_Codon_Matrix(matrix_ptr, code_file_ptr, acid_array);

 settings_ptr->num_mutants = Count_Mutants(acid_array, matrix_ptr);
 settings_ptr->mutant_array = calloc(settings_ptr->num_mutants, sizeof(MUTANT));
}
