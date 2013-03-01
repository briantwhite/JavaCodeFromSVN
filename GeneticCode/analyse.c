/*
 * analyse.c
 *
 *  Created on: Feb 20, 2011
 *      Author: brian
 */


void Print_Results_Header(SETTINGS_TYPE* settings_ptr, MATRIX_TYPE* codon_matrix_ptr, ACID_ELEMENT_TYPE* acid_array)
{
 fprintf(settings_ptr->results_file_ptr, "\n                 Analysis Type : ");  Print_Results_Model_String(settings_ptr->results_file_ptr, settings_ptr);
 fprintf(settings_ptr->results_file_ptr, "\n          Code Variation Model : ");  Print_Variation_Model_String(settings_ptr->results_file_ptr, settings_ptr);
 fprintf(settings_ptr->results_file_ptr, "\n              MS Measure Model : ");  Print_MS_Model_String(settings_ptr->results_file_ptr, settings_ptr);
  if(settings_ptr->results_model==1)
   {
    fprintf(settings_ptr->results_file_ptr, "\n                   Tr/Tv Value : w=%hd", settings_ptr->start_weight);
    fprintf(settings_ptr->results_file_ptr, "\n               Mod Power Value : p=%hd", settings_ptr->power);
    fprintf(settings_ptr->results_file_ptr, "\n Number of Variants per Sample : %ld", settings_ptr->num_iterations);
    fprintf(settings_ptr->results_file_ptr, "\n             Trap Better Codes :");
    if(settings_ptr->trap_codes==TRUE) printf("  ON"); else printf(" OFF");
   }
  else
   {
    fprintf(settings_ptr->results_file_ptr, "\n                  Tr/Tv Range  : %hd<=w<=%hd", settings_ptr->start_weight, settings_ptr->end_weight);
    fprintf(settings_ptr->results_file_ptr, "\n               Mod Power Range : 1<=p<=%hd", settings_ptr->power);
    if(settings_ptr->results_model==2)
     fprintf(settings_ptr->results_file_ptr, "\n Number of Variants per Sample : %ld", settings_ptr->num_iterations);
   }
  fprintf(settings_ptr->results_file_ptr, "\n Amino Acid Similarity Measure : ");  Print_Similarity_Measure(settings_ptr->results_file_ptr, settings_ptr);
 fprintf(settings_ptr->results_file_ptr, "\n\n Basic Genetic Code:");
 Print_Codon_Matrix(settings_ptr->results_file_ptr, codon_matrix_ptr, acid_array);
 fprintf(settings_ptr->results_file_ptr, "\n\n Amino Acid Parameters:\n");
 Print_PAM_Matrix(settings_ptr, acid_array, settings_ptr->results_file_ptr);
}

void Print_Results(SETTINGS_TYPE* settings_ptr,RESULTS_STRUCT data_structure)
{
 float		std_dev, mean;
 short		counter;

 fprintf(settings_ptr->results_file_ptr, "\n\n              :    nat   :    max   :    min   :num better:   mean   : variance :");
 for(counter = 0; counter<4; counter+=1)
   {
    mean = (float)(data_structure.MS.tot_MS[counter]/settings_ptr->num_iterations);
    std_dev = (float)pow(((float)(data_structure.MS.tsq_MS[counter]/settings_ptr->num_iterations)-(float)pow(mean, 2)),0.5);

    fprintf(settings_ptr->results_file_ptr, "\n Wgt[%2hd]", settings_ptr->start_weight);
    fprintf(settings_ptr->results_file_ptr, " MS[%1ld]:", counter);
    fprintf(settings_ptr->results_file_ptr, "%10.4f:", data_structure.MS.nat_MS[counter]);
    fprintf(settings_ptr->results_file_ptr, "%10.4f:", data_structure.max_MS[counter]);
    fprintf(settings_ptr->results_file_ptr, "%10.4f:", data_structure.min_MS[counter]);
    fprintf(settings_ptr->results_file_ptr, "%10ld:",  data_structure.MS.num_bt[counter]);
    fprintf(settings_ptr->results_file_ptr, "%10.4f:", mean);
    fprintf(settings_ptr->results_file_ptr, "%10.2f:", std_dev);
   }
}

void Fill_Freq_Arrays(SETTINGS_TYPE* settings_ptr, RESULTS_STRUCT data_structure, FREQ_ARRAY* freq_array_ptr)
{
 long		counter;
 short 		MS_cnt, this_class;
 float		MS_val;

 rewind(settings_ptr->tmp_file_ptr);
 printf("\n Reading observations into frequency arrays...");
 for(counter=0; counter<=100; counter+=1)
  for(MS_cnt = 0; MS_cnt<4; MS_cnt+=1)
   freq_array_ptr->freq[MS_cnt][counter] = 0;
 for(counter=0; counter<=settings_ptr->num_iterations; counter+=1)
  for(MS_cnt = 0; MS_cnt<4; MS_cnt+=1)
  {
   fread (&MS_val, sizeof(float), 1, settings_ptr->tmp_file_ptr) ;
   this_class = (short int)((MS_val - (floor)(data_structure.min_MS[MS_cnt]))/data_structure.interval[MS_cnt]);
   freq_array_ptr->freq[MS_cnt][this_class] +=1;
  }
}/* End of Function Fill_Freq_Arrays */
void Frequency_Distribution(SETTINGS_TYPE* settings_ptr, RESULTS_STRUCT data_structure)
{
 short int		counter, MS_cnt;
 FREQ_ARRAY 	freq_array;

 Fill_Freq_Arrays(settings_ptr, data_structure, &freq_array);
 fprintf(settings_ptr->results_file_ptr, "\n\n Frequency Distribution:\n\n");
 for(MS_cnt=0;MS_cnt<4; MS_cnt+=1)
  fprintf(settings_ptr->results_file_ptr, " Weight %2hd, MS[%hd];", settings_ptr->start_weight, MS_cnt);
 fprintf(settings_ptr->results_file_ptr, "\n");
 for(MS_cnt=0;MS_cnt<4; MS_cnt+=1)
    fprintf(settings_ptr->results_file_ptr, "  Class   , Freq ;");
 for(counter = 0; counter<100; counter+=1)
 {
  fprintf(settings_ptr->results_file_ptr, "\n");
  for(MS_cnt=0;MS_cnt<4; MS_cnt+=1)
   fprintf(settings_ptr->results_file_ptr, " %7.3f, %7ld;", floor(data_structure.min_MS[MS_cnt])+(counter*data_structure.interval[MS_cnt]), freq_array.freq[MS_cnt][counter]);
 }/* end of counter loop (number of freq. classes)*/
} /* end of function Frequency_Distribution */

void Read_Temp_File(SETTINGS_TYPE* settings_ptr, MATRIX_TYPE* codon_matrix_ptr, ACID_ELEMENT_TYPE* acid_array)
{
 short				MS_cnt;
 long				counter;
 float				MS_value;
 RESULTS_STRUCT		data_struct;

 printf("\n Summarising Data...");
 rewind(settings_ptr->tmp_file_ptr);
 for(MS_cnt = 0; MS_cnt<4; MS_cnt+=1)/* This loop reads in the MS values of the natural code, */
 {                                   /* other fields to zero, preparing the results matrix    */
  fread (&MS_value, sizeof(float), 1, settings_ptr->tmp_file_ptr) ;
  data_struct.MS.nat_MS[MS_cnt] = MS_value;
  data_struct.max_MS[MS_cnt] = MS_value;
  data_struct.min_MS[MS_cnt] = MS_value;
  data_struct.MS.num_bt[MS_cnt] = 0;
  data_struct.MS.tot_MS[MS_cnt] = 0;
  data_struct.MS.tsq_MS[MS_cnt] = 0;
 }/* End of MS_cnt loop */

 for(counter=0; counter<settings_ptr->num_iterations; counter+=1)
  for(MS_cnt = 0; MS_cnt<4; MS_cnt+=1)
  {
   fread (&MS_value, sizeof(float), 1, settings_ptr->tmp_file_ptr) ;
   if (MS_value == -1) {printf("\n Cock Up!"); Pause();}/* something has gone wrong! */
   if(MS_value>data_struct.max_MS[MS_cnt]) data_struct.max_MS[MS_cnt] = MS_value;
   if(MS_value<data_struct.min_MS[MS_cnt]) data_struct.min_MS[MS_cnt] = MS_value;
   if(MS_value<data_struct.MS.nat_MS[MS_cnt]) data_struct.MS.num_bt[MS_cnt] += 1;
   data_struct.MS.tot_MS[MS_cnt] += MS_value;
   data_struct.MS.tsq_MS[MS_cnt] += pow(MS_value, 2);
  }/* End of MS_cnt loop */

 for(MS_cnt = 0; MS_cnt<4; MS_cnt +=1)
  data_struct.interval[MS_cnt] = (float)(ceil(data_struct.max_MS[MS_cnt])-floor(data_struct.min_MS[MS_cnt]))/100;
 Print_Results(settings_ptr, data_struct);
 Frequency_Distribution(settings_ptr, data_struct);
} /* End of function Read_Temp_File*/



