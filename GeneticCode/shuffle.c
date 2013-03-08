/*
 * shuffle.c
 *
 *  Created on: Feb 20, 2011
 *      Author: brian
 */

/*  File Shuffle.c conatins all routines necessary to execute EITHER a complete (UNCONSTRAINED) reassignment of */
/* synonymous codon sets  (i.e. a Haig & Hurst model for varying the genetic code) OR a 'restricted' reassignment of  synonymous*/
/* codon sets (i.e. a Taylor and Coates model for varying the genetic code. This is achieved by creating  a new incidence of the*/
/*  amino acid  such that the meaning of an amino acid number has now changed Thus the codon matrix, including mutants and */
/* details pertaining to mutants REMAIN UNCHANGED, maximising the speed of simulation runs.  */

/*  Function 'Full Code Shuffle' effects a change in the genetic code consistent with Haig and Hurst's model: i.e. any synonymous */
/* codon set can swap with any other. This is effected by generating a linked list of the numbers 1 though 21, then randomly */
/* removing the elements one at a time, storing the value of the cut element sequentially in a new amino acid array. E.G. if the */
/* first element  cut from the linked list is '15', then what was previously the 15th amino acid in the array becomes element 1 of */
/* the new array Elements 0 and 21 remain unchanged (i.e. the dummy value ALL and the TER value are never */
/* shuffled...just the 20 amino acids         */
void Full_Code_Shuffle(ACID_ELEMENT_TYPE* acid_array)
{
 short			counter, count2, rand_num=0, new_array[22];
 ACID_ARRAY		new_acid_array;
 INT_LIST_ELEMENT	*head, *tail;
 new_array[0] = 0; new_array[21]= 21;
 head = malloc(sizeof(INT_LIST_ELEMENT));
 head->last_element = NULL;  head->number  = 0;  tail = head;
 for (counter = 1; counter <22; counter++)
  {
   tail->next_element = malloc(sizeof(INT_LIST_ELEMENT));
   tail->next_element->next_element = NULL;  tail->next_element->last_element = tail;
   tail = tail->next_element;  tail->number = counter;
  }
 for (counter = 0; counter <20; counter++)
  {
   rand_num = (short) Random_Int(1, 20-counter);
   tail = head->next_element;
   for(count2 = 1; count2 < rand_num; count2+=1) tail = tail->next_element;
   (tail->next_element)->last_element = tail->last_element;
   (tail->last_element)->next_element = tail->next_element;
   new_array[counter+1] = tail->number;    free(tail);
  }
  free(head->next_element);  free(head);
  for(counter = 0; counter<22; counter+=1)
  {
   new_acid_array[counter].parameter=acid_array[new_array[counter]].parameter;
   strcpy(new_acid_array[counter].name, acid_array[new_array[counter]].name);
  }
  for(counter = 0; counter<22; counter+=1)
  {
   acid_array[counter].parameter=new_acid_array[counter].parameter;
   strcpy(acid_array[counter].name, new_acid_array[counter].name);
  }
}

/* Function Restricted_Code_Shuflle forms 4 x 5-elements arrays of random numbers, and implements changes to the code*/
/* structure on the basis of this list. For the restricted set of codes, this means that each Taylor and Coates      */
/* subset of amino acids is randomly shuffled in turn (there are 4 subsets of 5 amino acids each...)                 */
void Restricted_Code_Shuffle(ACID_ELEMENT_TYPE* ref_array)
{
 ACID_ARRAY			acid_array;
 short				counter, count2, permute_num, start, *list;
 list = calloc(5, sizeof(short));
 for(counter = 0; counter <4; counter+=1) /* this 'count' loop takes the four groups of the restricted code, and alters each in turn */
 {
  permute_num = Random_Int(0,119);        /* chooses a random permutation from 5! possibilities */
  Find_Permutation(5, permute_num, list); /* obtains a linked list of numbers 1..5 correpsonding to the random permutation number*/
  start = (short)(1+(5*counter)); /* finds the correct starting place in the acid array for the reshuffle of 5 amino acids */

  /*changes 5 elements of 'acid_array' with respect to 'ref_array': the variable 'start' defines which code subset is under consideration*/
  for(count2=0; count2<5; count2+=1)
  {
   acid_array[count2+start].parameter = ref_array[list[count2]+start].parameter;
   strcpy(acid_array[count2+start].name, ref_array[list[count2]+start].name);
  }
 }
 for(counter = 0; counter<21; counter+=1)
  {
   ref_array[counter].parameter = acid_array[counter].parameter;
   strcpy(ref_array[counter].name, acid_array[counter].name);
  }
}
