/*
 * in_out.c
 *
 *  Created on: Feb 20, 2011
 *      Author: brian
 */

/*  File "in_out.c" contains source code for PROGRAM SPECIFIC     */
/*  input/output functions: output of data structures (the codon  */
/*  matrix, mutant matrix and amino acid array); reading in of    */
/*  particular data files (amino acid parameter files, genetic code files) etc.  */

/*  Function Print_Codon_Matrix prints the contents of the codon  */
/*  matrix to a file specified by a pointer passed as an argument */
/*  to the function. The matrix is printed as each of the codons  */
/*  (in the form of nucleotide labels), and the current associated  meaning */
void Print_Codon_Matrix(FILE* file_ptr, MATRIX_TYPE* codon_matrix_ptr, ACID_ELEMENT_TYPE* acid_array)
{
 char  char1, char2, char3;
 COORD coord;
 NAME  this_name;

 for(coord.x=0; coord.x<4; coord.x+=1)
  {
   fprintf(file_ptr, "\n");
   for (coord.z=0; coord.z<4; coord.z+=1)
   {
    fprintf(file_ptr, "\n");
    for (coord.y=0; coord.y<4; coord.y+=1)
     {
      char1 = Number_to_Base(coord.x);
      char2 = Number_to_Base(coord.y);
      char3 = Number_to_Base(coord.z);
      strcpy (this_name, acid_array[(*codon_matrix_ptr)[coord.x][coord.y][coord.z].acid_number].name);
      fprintf(file_ptr, " %c%c%c:%c%c%c", char1, char2, char3, tolower(this_name[0]), tolower(this_name[1]), tolower(this_name[2]));
     }
   }
  }
}

/*    Function Print_Acid_Array prints the 22 elements of the amino acid array to a file specified by a pointer passed    */
/*    as an argument to the function. Each amino acid (plus TER and ALL) are printed with their current associated parameter values  */
void Print_Acid_Array(FILE* file_ptr, ACID_ELEMENT_TYPE* acid_array)
{
 short counter[2];

 for(counter[0] = 0; counter[0]<7; counter[0]+=1)
 {
  fprintf(file_ptr, "\n");
  for(counter[1]=1; counter[1]<4; counter[1]+=1)
    fprintf(file_ptr, " %3s: %5.2f  ", acid_array[counter[1]+(3*counter[0])].name, acid_array[counter[1]+(3*counter[0])].parameter);
 }
}


/*   Function Read_Acid_Parameters takes a path for a text file (containing the 3 letter abbrevations of each amino acid,      */
/*   together with a numerical value for each) and reads in the  file, matching parameter values to the amino acid names        */
/*   already stored in amino_acid_array   */
void Read_Acid_Parameters(SETTINGS_TYPE* settings_ptr, ACID_ELEMENT_TYPE*  acid_array, char*  parameter_file_string)
{
 short 	counter, count2, acid_number;
 float 	parameter;
 NAME 	acid_name;
 FILE*  file_ptr;

 file_ptr = Safe_Open(parameter_file_string, "r\0", FALSE);

 settings_ptr->PAM_matrix = calloc(20, sizeof(float*));
 for(counter = 0; counter<=20; counter+=1)
  settings_ptr->PAM_matrix[counter] = calloc(20, sizeof(float));

 if (file_ptr!=NULL)                     /* Reads amino acid names, and associated         */
  for (counter=0; counter<22; counter++) /* physiochemcial property values into acid array */
{
 fscanf(file_ptr, "%3s", acid_name);
 acid_name [3] = '\0';
 fscanf(file_ptr, "%f", &parameter);
 acid_number = Acid_to_Number(acid_name, acid_array);
 acid_array[acid_number].parameter = parameter;
}

 fclose(file_ptr);

 for(counter = 0; counter<20; counter +=1)  /* Updates the parameter difference matrix */
  for(count2 = 0; count2<20; count2+=1)     /* based on these properties               */
   {
    parameter = acid_array[counter+1].parameter - acid_array[count2+1].parameter;
    if(parameter<0) parameter *= -1;
    settings_ptr->PAM_matrix[counter][count2] = parameter;
   }
 for(counter = 0; counter<20; counter +=1)  /* updates the amino acid array to refer to the */
    acid_array[counter+1].parameter = counter;/* element of the parameter difference matrix   */
}


/*   Function Read_PAM_matrix takes a path for a text file          */
/*   (containing the PAM matrix) and reads the data into a matrix   */
void Read_PAM_Matrix(SETTINGS_TYPE* settings_ptr, ACID_ELEMENT_TYPE*  acid_array, char*  parameter_file_string)
{
 short 	counter, count2, acid_number;
 float 	parameter;
 NAME 	acid_name;
 FILE*  PAM_file_ptr;

 PAM_file_ptr = Safe_Open(parameter_file_string, "r\0", FALSE);
 settings_ptr->PAM_matrix = calloc(20, sizeof(float*));
 for(counter = 0; counter<=20; counter+=1)
  settings_ptr->PAM_matrix[counter] = calloc(20, sizeof(float));
  for (counter=0; counter<20; counter++)	/* assigns links from the amino acid array to the PAM matrix */
	{                                       	/* such that each amino acid (field parameter) contains the  */
	 fscanf(PAM_file_ptr, "%3s", acid_name);/* number of the correct column(row) of the PAM matrix       */
	 acid_name [3] = '\0';
    	 acid_number = Acid_to_Number(acid_name, acid_array);
     	 acid_array[acid_number].parameter = counter;
	}

  for (counter=0; counter<20; counter++)
   for (count2=0; count2<1+counter; count2++)
	{
	fscanf(PAM_file_ptr, "%f", &parameter);
      	settings_ptr->PAM_matrix[counter][count2] = parameter;
     	}

  for (counter=0; counter<20; counter++)
   for (count2=0; count2<20; count2++)
    if(settings_ptr->PAM_matrix[counter][count2]==0)
     settings_ptr->PAM_matrix[counter][count2] = settings_ptr->PAM_matrix[count2][counter];

 fclose(PAM_file_ptr);
 Transform_PAM_Matrix(settings_ptr);
}


void Print_PAM_Matrix(SETTINGS_TYPE* settings_ptr, ACID_ELEMENT_TYPE*  acid_array, FILE* out_file_ptr)
{
 short 	counter, count2;

  fprintf(out_file_ptr, "\n    :");
  for (counter=1; counter<21; counter++)
   fprintf(out_file_ptr, " %3s,", acid_array[counter].name);

  for (counter=1; counter<21; counter++)
   {
    fprintf(out_file_ptr, "\n %3s:", acid_array[counter].name);
    for (count2=1; count2<21; count2++)
     fprintf(out_file_ptr, "%4.1f,",
	settings_ptr->PAM_matrix[(short)(acid_array[counter].parameter)][(short)(acid_array[count2].parameter)]);
   }
}


/*  Function Read_Codon_Matrix takes the 4x4x4 matrix (representing the codons in a genetic code) and fills each element with a     */
/*  representing the meaning of each codon in terms of amino acids  */
void Read_Codon_Matrix(MATRIX_TYPE*  matrix_ptr, FILE* file_ptr, ACID_ELEMENT_TYPE* acid_array)
{
 int	counter, acid_number;
 NAME	acid_name, codon_name;
 COORD	coord;


 if (file_ptr!=NULL)
 {
  for (counter=0; counter<64; counter++)
	{
	 fscanf(file_ptr, "%3s", codon_name);
     fscanf(file_ptr, "%3s", acid_name);
	 codon_name[3] = '\0';
	 acid_name [3] = '\0';

	 coord.x = (short) Base_to_Number(codon_name[0]);
	 coord.y = (short) Base_to_Number(codon_name[1]);
     coord.z = (short) Base_to_Number(codon_name[2]);

     acid_number = Acid_to_Number(acid_name, acid_array);

	 (*matrix_ptr)[coord.x][coord.y][coord.z].acid_number = acid_number;
    }

  fclose(file_ptr);
 }
}

/* Function Print_MS_Values calculates the 4 MS values in an instance of ALL_MS  (from the two constituent */
/* values: 4 x num_obs and 4 x total_SS. It then writes these 4 values to a BINARY file  */
void Store_MS_Values(SETTINGS_TYPE* settings_ptr, FILE* file_ptr, ALL_MS code_MS)
{
 short			MS_counter;
 float			MS_value;

 for(MS_counter = 0; MS_counter<4; MS_counter+=1)
 {
  if((code_MS.MS[MS_counter].total_SS==0)||(code_MS.MS[MS_counter].num_obs==0)) MS_value = -1;
   else MS_value = code_MS.MS[MS_counter].total_SS/code_MS.MS[MS_counter].num_obs;
  fwrite(&MS_value, sizeof(float), 1, file_ptr);
 }
}


/* Function Print_MS_Matrix prints the results matrices for each of the four base positions over the range of modular
/* power functions and tr/tv weightings for which the analysis has been performed.*/
void Print_MS_Matrix(MS_STRUCT **results_matrix, FILE* file_ptr, short weightings, short top_power, long num_iterations)
{
 short	wgt_counter, power, base;

 for(base=0; base<4; base+=1)
 {
  fprintf(file_ptr, "\n\n Base[%hd]: MS values of start code configuration", base);
  fprintf(file_ptr, "\n p\\w:", power);
  for(wgt_counter = 0; wgt_counter<=weightings; wgt_counter+=1) fprintf(file_ptr, "%10hd, ", wgt_counter+1);

  for(power = 0; power<top_power; power+=1)
  {
   fprintf(file_ptr, "\n  %2hd:", power+1);
   for(wgt_counter = 0; wgt_counter<=weightings; wgt_counter+=1)
    fprintf(file_ptr, "%10.3f, ", results_matrix[power][wgt_counter].nat_MS[base]);
  }
 }

 for(base=0; base<4; base+=1)
 {
  fprintf(file_ptr, "\n\n Base[%hd]: number of better codes found (n=%ld)", base, num_iterations);
  fprintf(file_ptr, "\n p\\w:", power);
  for(wgt_counter = 0; wgt_counter<=weightings; wgt_counter+=1) fprintf(file_ptr, "%10hd, ", wgt_counter+1);

  for(power = 0; power<top_power; power+=1)
  {
   fprintf(file_ptr, "\n  %2hd:", power+1);
   for(wgt_counter = 0; wgt_counter<=weightings; wgt_counter+=1)
    fprintf(file_ptr, "%10ld, ", results_matrix[power][wgt_counter].num_bt[base]);
  }
 }

 for(base=0; base<4; base+=1)
 {
  fprintf(file_ptr, "\n\n Base[%hd]: Minimum MS values within sample (n=%ld)", base, num_iterations);
  fprintf(file_ptr, "\n p\\w:", power);
  for(wgt_counter = 0; wgt_counter<=weightings; wgt_counter+=1) fprintf(file_ptr, "%10hd, ", wgt_counter+1);

  for(power = 0; power<top_power; power+=1)
  {
   fprintf(file_ptr, "\n  %2hd:", power+1);
   for(wgt_counter = 0; wgt_counter<=weightings; wgt_counter+=1)
    fprintf(file_ptr, "%10.3f, ", results_matrix[power][wgt_counter].min_MS[base]);
  }
 }

 for(base=0; base<4; base+=1)
 {
  fprintf(file_ptr, "\n\n Base[%hd]: Mean MS values of sample (n=%ld)", base, num_iterations);
  fprintf(file_ptr, "\n p\\w:", power);
  for(wgt_counter = 0; wgt_counter<=weightings; wgt_counter+=1) fprintf(file_ptr, "%10hd, ", wgt_counter+1);

  for(power = 0; power<top_power; power+=1)
  {
   fprintf(file_ptr, "\n  %2hd:", power+1);
   for(wgt_counter = 0; wgt_counter<=weightings; wgt_counter+=1)
    fprintf(file_ptr, "%10.3f, ", (float)(results_matrix[power][wgt_counter].tot_MS[base]/num_iterations));
  }
 }

 for(base=0; base<4; base+=1)
 {
  fprintf(file_ptr, "\n\n Base[%hd]: Std. Dev. of sample MS values (n=%ld)", base, num_iterations);
  fprintf(file_ptr, "\n p\\w:", power);
  for(wgt_counter = 0; wgt_counter<=weightings; wgt_counter+=1) fprintf(file_ptr, "%10hd, ", wgt_counter+1);

  for(power = 0; power<top_power; power+=1)
  {
   fprintf(file_ptr, "\n  %2hd:", power+1);
   for(wgt_counter = 0; wgt_counter<=weightings; wgt_counter+=1)
    fprintf(file_ptr, "%10.3f, ", (float)(pow((results_matrix[power][wgt_counter].tsq_MS[base]/num_iterations)-pow(results_matrix[power][wgt_counter].tot_MS[base]/num_iterations, 2), 0.5)));
  }
 }


}/* End of Function */

