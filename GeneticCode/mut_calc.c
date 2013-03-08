/*
 * mut_calc.c
 *
 *  Created on: Feb 20, 2011
 *      Author: brian
 */

/* The 4x4x4 codon matrix represents a particular genetic code,    This code is used to produce a mutant matrix, a list of all      */
/* possible single base mutations possible within a particular code. Each element of this mutant array comprises fields describing    */
/* whether the mutation in question...                              */
/*  	(i)   represents a transition (as opposed to transversion)      */
/*  	(ii)  represents a mutation to A or U                           */
/* 	 (iii) which base the mutation is in (1st,2nd or 3rd)            */
/*  	(iv)  the coordinates detailing which codon the mutation is to  */
/*        	       (i.e. mutant codon coordinates in terms of the 4x4x4 code */

/* Functions in this file are responsible for building an instance  of the mutant matrix for a given genetic code. This is done ONCE */
/* at the start of the program in order to maximise speed of   simulation runs.                                                 */

/* Functions in this file are responsible for building an instance  of the mutant matrix for a given genetic code. This is done ONCE */
/* at the start of the program in order to maximise speed of  simulation runs.                                                 */



/* Function Check_Legitimacy checks the legitimacy of a particular  point mutation for a particular codon (i.e. whether it is to be  */
/* included as part of the error value for that codon). If it is a mutation from a STOP codon or to a STOP codon, it is not       */
/* legitimate. The function makes this comparison by taking the appropriate COORD structs of the mutant codon and starting codon */
/* it compares their meanings by use of the codon matrix and amino array. The result of the comparison is used to set a single flag */
/* which represents the legitimacy of the mutation and is returned as the result of the function. [1=legitimate, 0=NOT legitimate]  */
short Check_Legitimacy(COORD mutant_coord, COORD start_coord, MATRIX_TYPE* codon_matrix_ptr, ACID_ELEMENT_TYPE* acid_array)
{
 short valid = TRUE;

 if((mutant_coord.x == start_coord.x)&&(mutant_coord.y==start_coord.y)&&(mutant_coord.z==start_coord.z)) valid = FALSE;
 if(strcmp(acid_array[(*codon_matrix_ptr)[mutant_coord.x][mutant_coord.y][mutant_coord.z].acid_number].name, "TER\0")==0) valid = FALSE;
 if(strcmp(acid_array[(*codon_matrix_ptr)[start_coord.x][start_coord.y][start_coord.z].acid_number].name, "TER\0")==0) valid = FALSE;

 return valid;
}

/*  Function Load_Mutant loads the pertinent values (transition,   n->A, base number and mutant codon coordinates) into an instance */
/*  of a MUTANT struct, and returns this as the value of the functionThis is achieved by comparison of the starting codon coordinates */
/*  (accepted asparameter start_coord) with the mutant codon coordinates (this_coord. Comparisons are via small sub-functions */
MUTANT Load_Mutant(int* num_stored_ptr, COORD start_coord, COORD this_coord, short int base_num)
{
 MUTANT mutant;

 mutant.base_number   = base_num;
 mutant.is_transition = Is_Transition(start_coord, this_coord, base_num);
 mutant.is_A = Is_A(this_coord, base_num);
 mutant.mut_coord = this_coord;
 mutant.stt_coord = start_coord;
 (*num_stored_ptr)+=1;
 return mutant;
}

/* Function Count_Mutants finds the number of single point mutants  applicable to a particular genetic code. This is 9 x 64 - n       */
/* where 'n' is the number of point mutations to and from stop codons*/
int Count_Mutants(ACID_ELEMENT_TYPE* acid_array, MATRIX_TYPE* codon_matrix_ptr)
{
 int    num_mutants = 0;
 COORD  this_coord, start_coord;

 for(start_coord.x=0; start_coord.x<4; start_coord.x+=1)
  for(start_coord.y=0; start_coord.y<4; start_coord.y+=1)
   for(start_coord.z=0; start_coord.z<4; start_coord.z+=1)
   {
    this_coord = start_coord;
    for(this_coord.x=0; this_coord.x<4; this_coord.x+= 1)
	 if(this_coord.x!=start_coord.x)
	  if(Check_Legitimacy(this_coord, start_coord, codon_matrix_ptr, acid_array)==TRUE) num_mutants +=1;

    this_coord = start_coord;
    for(this_coord.y=0; this_coord.y<4; this_coord.y+= 1)
	 if(this_coord.y!=start_coord.y)
	  if(Check_Legitimacy(this_coord, start_coord, codon_matrix_ptr, acid_array)==TRUE) num_mutants +=1;

    this_coord = start_coord;
    for(this_coord.z=0; this_coord.z<4; this_coord.z+= 1)
	 if(this_coord.z!=start_coord.z)
	  if(Check_Legitimacy(this_coord, start_coord, codon_matrix_ptr, acid_array)==TRUE) num_mutants +=1;
   }

 return num_mutants;
}

/* Function Find_Possible_Mutants finds the 9 mutants applicable to  any one starting codon. It is passed the 9 element array of       */
/* MUTANT (structs) which are found in each element of the 4x4x4   mutant matrixto store the pertinent values. The function directly */
/* finds the coordinates of the 9 mutants for a single codons (in  batches of 3 per base position) and for each mutant, it calls     */
/* function Load_Mutant to do the nitty-gritty of storing information*/
void Load_Mutant_Array(MATRIX_TYPE* codon_matrix_ptr, ACID_ELEMENT_TYPE* acid_array, MUTANT* mutant_array, COORD start_coord, int* num_stored_ptr)
{
	COORD this_coord;
	
	this_coord = start_coord;
	for(this_coord.x=0; this_coord.x<4; this_coord.x+= 1) {
		if(Check_Legitimacy(this_coord, start_coord, codon_matrix_ptr, acid_array)==TRUE) {
			MUTANT mutant = Load_Mutant(num_stored_ptr, start_coord, this_coord, 1);
			mutant_array[*num_stored_ptr] = mutant;
		}
	}
	
	this_coord = start_coord;
	for(this_coord.y=0; this_coord.y<4; this_coord.y+= 1) {
		if(Check_Legitimacy(this_coord, start_coord, codon_matrix_ptr, acid_array)==TRUE) {
			MUTANT mutant = Load_Mutant(num_stored_ptr, start_coord, this_coord, 2);
			mutant_array[*num_stored_ptr] = mutant;
		}
	}
	
	this_coord = start_coord;
	for(this_coord.z=0; this_coord.z<4; this_coord.z+= 1) {
		if(Check_Legitimacy(this_coord, start_coord, codon_matrix_ptr, acid_array)==TRUE) {
			MUTANT mutant = Load_Mutant(num_stored_ptr, start_coord, this_coord, 3);
			mutant_array[*num_stored_ptr] = mutant;
		}
	}
}


/*  Function Build_Mutant_Matrix oversees the building of a mutant matrix by simply cycling through the 64 elements of the genetic*/
/*  code, and passing each as a set of coordinates, and a 9 element array of MUTANT (structs) to function Find_Possible_Mutants    */
void Build_Mutant_Array(SETTINGS_TYPE* settings_ptr, ACID_ELEMENT_TYPE* acid_array, MATRIX_TYPE* codon_matrix_ptr)
{
 int	 num_mutants=0, num_stored = -1;
 COORD   coord;

 for(coord.x=0; coord.x<4; coord.x+=1)
  for(coord.y=0; coord.y<4; coord.y+=1)
   for(coord.z=0; coord.z<4; coord.z+=1)
    Load_Mutant_Array(codon_matrix_ptr, acid_array, settings_ptr->mutant_array, coord, &num_stored);
}
