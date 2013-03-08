/*
 * setting.c
 *
 *  Created on: Feb 20, 2011
 *      Author: brian
 */

void Print_MS_Model_String(FILE* file_ptr, SETTINGS_TYPE* settings_ptr)
{
 switch(settings_ptr->MS_model)
 {
  case 1: fprintf(file_ptr, "Transition/Transversion Bias"); break;
  case 2: fprintf(file_ptr, "MisTranslation              \0"); break;
  case 3: fprintf(file_ptr, "N->A Bias (mitochondrial)   \0"); break;
  default:fprintf(file_ptr, "MS MODEL TYPE ERROR\0"); break;
 }
}


void Print_Variation_Model_String(FILE* file_ptr, SETTINGS_TYPE* settings_ptr)
{
 switch(settings_ptr->variation_model)
 {
  case 1: fprintf(file_ptr, "Haig & Hurst ('unrestricted')"); break;
  case 2: fprintf(file_ptr, "Constrained shuffle ('restricted')"); break;
  case 3: fprintf(file_ptr, "Code optimisation"); break;
  default:fprintf(file_ptr, "ERROR: undefined variation model"); break;
 }
}


void Print_Similarity_Measure(FILE* file_ptr, SETTINGS_TYPE* settings_ptr)
{
 switch(settings_ptr->similarity_measure)
 {
  case 1: fprintf(file_ptr, "Physiochemical property"); break;
  case 2: fprintf(file_ptr, "PAM (similarity) matrix"); break;
  default:fprintf(file_ptr, "ERROR: undefined similarity measure"); break;
 }
}


void Print_Results_Model_String(FILE* file_ptr, SETTINGS_TYPE* settings_ptr)
{
 switch(settings_ptr->results_model)
 {
  case 1: fprintf(file_ptr, "Produce a single sample (freq. distribution)"); break;
  case 2: fprintf(file_ptr, "Produce many samples (explore parameter space)"); break;
  case 3: fprintf(file_ptr, "Optimize code structure"); break;
  default:fprintf(file_ptr, "ERROR: undefined results model"); break;
 }
}


void Change_Results_Model(SETTINGS_TYPE* settings_ptr, MATRIX_TYPE* matrix_ptr, ACID_ELEMENT_TYPE* acid_array)
{
 short model_num;

 Print_Break();
 printf("\n    Current Settings              ");
 printf("\n    ----------------              ");
 printf("\n    Results Model  : "); Print_Results_Model_String(stdout, settings_ptr);
 printf("\n                                  ");
 printf("\n    1: Produce a single sample (freq. distribution)");
 printf("\n    2: Produce many samples (explore parameter space)");
 printf("\n    3: Optimize code structure");
 printf("\n                                  ");
 printf("\n    Enter Desired Model Code (1..3):");

 model_num = (short) Get_User_Input_Number(1,3);
 settings_ptr->results_model = model_num;
}

void Change_Similarity_Measure(SETTINGS_TYPE* settings_ptr, ACID_ELEMENT_TYPE* acid_array)
{
 short measure;

 Print_Break();
 printf("\n    Current Settings              ");
 printf("\n    ----------------              ");
 printf("\n    Amino Acid Similarity Measure : ");  Print_Similarity_Measure(stdout, settings_ptr);
 printf("\n                                  ");
 printf("\n    1: Physiochemical property");
 printf("\n    2: PAM matrix");
 printf("\n                                  ");
 printf("\n    Enter Desired Model Code (1..2):");

 measure = (short) Get_User_Input_Number(1,2);
 switch(settings_ptr->similarity_measure)
 {
  case 1:  Read_PAM_Matrix(settings_ptr, acid_array, "PAM74100.txt\0"); break;
  case 2:  Read_Acid_Parameters(settings_ptr, acid_array, "a_acid_f.txt\0"); break;
  default: break;
 };
 settings_ptr->similarity_measure = measure;
}


void Change_Variation_Model(SETTINGS_TYPE* settings_ptr, MATRIX_TYPE* matrix_ptr, ACID_ELEMENT_TYPE* acid_array)
{
 short model_num;

 Print_Break();
 printf("\n    Current Settings              ");
 printf("\n    ----------------              ");
 printf("\n    Variation Model  : ");
 Print_Variation_Model_String(stdout, settings_ptr);
 printf("\n                                  ");
 printf("\n    1: Haig & Hurst model    ('unrestricted')");
 printf("\n    2: Taylor & Coates model ('restricted')");
 printf("\n                                  ");
 printf("\n    Enter Desired Model Code (1..2):");
 model_num = (short) Get_User_Input_Number(1,2);
 settings_ptr->variation_model = model_num;

 if( settings_ptr->variation_model ==2)
 {
  switch(settings_ptr->similarity_measure)
   {
    case 1:  Read_PAM_Matrix(settings_ptr, acid_array, "PAM74100.txt\0");	break;
    case 2:  Read_Acid_Parameters(settings_ptr, acid_array, "a_acid_f.txt\0"); break;
    default: printf("ERROR!: undefined simlarity measure"); Pause(); break;
   };
  Read_Codon_Matrix(matrix_ptr, Safe_Open("matrix_f.cod\0", "r\0", FALSE), acid_array);
  printf("\n %cWARNING! Code reset to standard genetic code ['ENTER' TO CONTINUE]", 7); Pause();
 }
}


void Change_Iterations(SETTINGS_TYPE* settings_ptr)
{
 long num_iterations;

 Print_Break();
 printf("\n    Current Settings");
 printf("\n    ----------------");
 printf("\n    Current Iterations:%ld", settings_ptr->num_iterations);
 printf("\n                ");
 printf("\n    Enter Desired Number of Iterations (<=2 million):");

 num_iterations = (long) Get_User_Input_Number(1,MAX_ITERATIONS);
 settings_ptr->num_iterations = num_iterations;
}



void Change_Weightings(SETTINGS_TYPE* settings_ptr)
{

 if(settings_ptr->results_model != 1)
 {
  Print_Break();
  printf("\n    Current Tr/Tv Weight Range Settings");
  printf("\n    -----------------------------------");
  printf("\n    Current Weighting Range              : %hd..%hd", settings_ptr->start_weight, settings_ptr->end_weight);
  printf("\n                ");
  printf("\n    Enter Desired Start Weighting (<=100):");
  settings_ptr->start_weight = (short) Get_User_Input_Number(1,100);
  printf("\n    Enter Desired End Weighting (<=%hd)  :", settings_ptr->start_weight+20);
  settings_ptr->end_weight = (short) Get_User_Input_Number(settings_ptr->start_weight,settings_ptr->start_weight+20);
 }
 else
 {
  printf("\n    Current Tr/Tv Weight Settings");
  printf("\n    -----------------------------");
  printf("\n    Current Tr/Tv Weight Value        : %hd", settings_ptr->start_weight);
  printf("\n                ");
  printf("\n    Enter Desired Weight Value (<=100):");
  settings_ptr->start_weight = (short) Get_User_Input_Number(1,120);
 }
}

void Change_MS_Model(SETTINGS_TYPE* settings_ptr)
{
 short model_num;

 Print_Break();
 printf("\n    Current Settings              ");
 printf("\n    ----------------              ");
 printf("\n    Model  : ");
 Print_MS_Model_String(stdout, settings_ptr);
 printf("\n                                              ");
 printf("\n    1: Transition/Transversion Bias   (wMS)   ");
 printf("\n    2: Mis-Translation mapping        (tMS)   ");
 printf("\n    3: N->A weighting (mitochondrial) (mMS)   ");
 printf("\n                                   ");
 printf("\n    Enter Desired Model Code (1..3):");

 model_num = (short) Get_User_Input_Number(1,3);
 settings_ptr->MS_model = model_num;
}

/* Function 'Choose_Code' allows the user to pick a genetic code file to read into memory. The function first lists all legitmate files */
/* (extension *.cod) to the screen (using PLATFORM DEPENDENT function List_Files). The user then */
/* enters the number which corresponds to the file of choice.                  */
void Choose_Code(MATRIX_TYPE* matrix_ptr, ACID_ELEMENT_TYPE* acid_array, SETTINGS_TYPE* settings_ptr)
{
 FILE*	in_file_ptr;

 printf("\n Select a Code File....");
 printf("\n ----------------------");
 in_file_ptr = List_Files("*.cod\0");
 Read_Codon_Matrix(matrix_ptr, in_file_ptr, acid_array);
 Print_Codon_Matrix(stdout, matrix_ptr, acid_array);
}

/*Function 'Save_Code' saves the current code structure as an ASCII file which may subsequently be read into the program    */
void Save_Code(MATRIX_TYPE* matrix_ptr, ACID_ELEMENT_TYPE* acid_array, SETTINGS_TYPE* settings_ptr)
{
 FILE*	out_file_ptr;
 COORD  coord;
 char	file_string[80], codon[3];

 printf("\n Enter a filename (NO extension!):");
 gets(file_string);
 strcat(file_string, ".cod\0");
 out_file_ptr = Safe_Open(file_string, "w\0", TRUE);

 for(coord.x=0; coord.x<4; coord.x+=1)
  for(coord.y=0; coord.y<4; coord.y+=1)
   for(coord.z=0; coord.z<4; coord.z+=1)
   {
    codon[0] = Number_to_Base(coord.x);
    codon[1] = Number_to_Base(coord.y);
    codon[2] = Number_to_Base(coord.z);
    fprintf(out_file_ptr, "\n %c%c%c %s", codon[0], codon[1], codon[2], acid_array[(*matrix_ptr)[coord.x][coord.y][coord.z].acid_number].name);
   }
 Pause();
 fclose(out_file_ptr);
}


/*  Function 'Reassign_Synonymous_Codons' allows the user to assign a new meaning to any group of   */
/*  codons within the code. The user first enters the coordinates of a family block, (the 4 bases   */
/*  AND letters 'R', 'Y' and 'N' are allowed), and then enters a new meaning                        */
void Reassign_Codons(MATRIX_TYPE* codon_matrix_ptr, ACID_ELEMENT_TYPE* acid_array)
{
 char string[80], codon[3];
 short count, lower[3], upper[3], acid_number;
 COORD coord;

 while(Is_Codon(codon)==FALSE)
 {
  printf("\n Enter Codon(s) (using U,C,A,G,R,Y,N):");
  gets(string);
  for(count = 0; count<3; count +=1) codon[count] = string[count];
 }

 Base_to_Range(codon[0], &lower[0], &upper[0]);
 Base_to_Range(codon[1], &lower[1], &upper[1]);
 Base_to_Range(codon[2], &lower[2], &upper[2]);
 while(Is_Acid(codon, acid_array)==FALSE)
 {
  printf("\n New meaning for %s?", codon);
  gets(string);
  for(count = 0; count<3; count +=1) codon[count] = string[count];
 }
 for(count = 0; count<22; count+=1)
  if(strcmp(codon, acid_array[count].name)==0) acid_number = count;
 for(coord.x=lower[0]; coord.x<=upper[0]; coord.x+=1)
  for(coord.y=lower[1]; coord.y<=upper[1]; coord.y+=1)
   for(coord.z=lower[2]; coord.z<=upper[2]; coord.z+=1)
    (*codon_matrix_ptr)[coord.x][coord.y][coord.z].acid_number = acid_number;
}

void Change_Code(SETTINGS_TYPE* settings_ptr, MATRIX_TYPE* codon_matrix_ptr, ACID_ELEMENT_TYPE* acid_array)
{
 char command='a';
 while(command!='d')
 {
  Print_Break();
  Print_Codon_Matrix(stdout, codon_matrix_ptr, acid_array);
  printf("\n    Options");
  printf("\n    -------              ");
  printf("\n a: Change codon assignment(s)      b: Load code from a file");
  printf("\n c: Save code to file               d: RETURN TO MAIN MENU");
  printf("\n    Command?");
  command=Get_Character('a','d');
  switch(command)
  {
   case 'a': Reassign_Codons(codon_matrix_ptr, acid_array); break;
   case 'b': Choose_Code(codon_matrix_ptr, acid_array, settings_ptr); break;
   case 'c': Save_Code(codon_matrix_ptr, acid_array, settings_ptr); break;
   case 'd': break;
   default: break;
  }
 }/* end of while loop */
}

void Change_Modular_Power_Function(SETTINGS_TYPE* settings_ptr)
{
 Print_Break();
 printf("\n    Current Power Function");
 printf("\n    ----------------------");
 printf("\n");
 printf("\n    E = |a1 - a2|^");
 if(settings_ptr->results_model==1)
 {
  printf("%hd", settings_ptr->power);
  printf("\n");
  printf("\n    Desired power function?(1..5)");
 }
 else
 {
  printf("p\n");
  printf("\n    1 <= p <= %hd", settings_ptr->power);
  printf("\n                ");
  printf("\n    Enter Desired Upper Power Limit (<=5):");
 }
 settings_ptr->power = (short) Get_User_Input_Number(1,5);
}


/* Function Display_Pre_Run_Options is the major controlling function which allows the user to reset parameters */
/* unitl the user elects to start an analysis */
void Display_Pre_Run_Options(MATRIX_TYPE* codon_matrix_ptr, ACID_ELEMENT_TYPE* acid_array, SETTINGS_TYPE* settings_ptr)
{
 char  command;

/* The major loop: presents current parameters, and allows user to specify which are to be changed */
while(command!='i')
 {
  Print_Break();
  printf("\n                                Current Settings");
  printf("\n                                ----------------");
  printf("\n                 Analysis Type : ");  Print_Results_Model_String(stdout, settings_ptr);
  printf("\n          Code Variation Model : ");  Print_Variation_Model_String(stdout, settings_ptr);
  printf("\n              MS Measure Model : ");  Print_MS_Model_String(stdout, settings_ptr);
  if(settings_ptr->results_model==1)
   {
    printf("\n                   Tr/Tv Value : w=%hd", settings_ptr->start_weight);
    printf("\n               Mod Power Value : p=%hd", settings_ptr->power);
    printf("\n Number of Variants per Sample : %ld", settings_ptr->num_iterations);
   }
  else
   {
    printf("\n                  Tr/Tv Range  : %hd<=w<=%hd", settings_ptr->start_weight, settings_ptr->end_weight);
    printf("\n               Mod Power Range : 1<=p<=%hd", settings_ptr->power);
    if(settings_ptr->results_model==2)
     printf("\n Number of Variants per Sample : %ld", settings_ptr->num_iterations);
   }
  printf("\n Amino Acid Similarity Measure : ");  Print_Similarity_Measure(stdout, settings_ptr);

  printf("\n                ");
  printf("\n    Options");
  printf("\n    -------");

  printf("\n a) Change analysis type             (single sample, many samples or optimise)");
  printf("\n b) Change code variation model      (restricted or unrestricted)");
  printf("\n c) Change MS measurement model      (Tr/Tv, mistranslation OR N->A)");
  printf("\n d) Change w                         (Tr/Tv weighting)");
  printf("\n e) Change p                         (modular power function)");
  if(settings_ptr->results_model!=3) printf("\n f) Change sample size");
	else printf("\n f) DISALLOWED UNDER CURRENT RESULTS MODEL");
  if(settings_ptr->variation_model == 1) printf("\n g) Display/Change current genetic code");
	else printf("\n g) DISALLOWED UNDER CURRENT VARIATION MODEL");
  printf("\n h) Change measure of amino acid similarity (physiochem. property/PAM matrix)");
  printf("\n i) RUN SIMULATION");
  printf("\n    Command?");

  command=Get_Character('a','j');

  switch(command)
  {
   case 'a': Change_Results_Model(settings_ptr, codon_matrix_ptr, acid_array); 	break;
   case 'b': Change_Variation_Model(settings_ptr, codon_matrix_ptr, acid_array); 	break;
   case 'c': Change_MS_Model(settings_ptr);				break;
   case 'd': Change_Weightings(settings_ptr);				break;
   case 'e': Change_Modular_Power_Function(settings_ptr);		break;
   case 'f': Change_Iterations(settings_ptr);				break;
   case 'g': Change_Code(settings_ptr, codon_matrix_ptr, acid_array);	break;
   case 'h': Change_Similarity_Measure(settings_ptr, acid_array);		break;
   default: break;
  }

 }/* End of while statement */
}/* End of function Display_Settings */
