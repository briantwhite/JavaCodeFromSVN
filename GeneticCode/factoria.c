/*
 * factoria.c
 *
 *  Created on: Feb 20, 2011
 *      Author: brian
 */

/* Function Factorial calculates the factorial (n!) for a (short) integer */
int Factorial(short num_elements)
{
 int counter, factorial=1;
 for(counter = 1; counter<=num_elements; counter+=1)
   factorial = factorial*counter;
 return factorial;
}

/* Function Make_Linked_List makes a linked list of 'num_elements' items containing 'next and 'last' fields and sequential integer */
/* values. The list is padded at each end by a dummy element (i.e. contains a total of (num_elements+2) items, to ease list */
/* handling routines      */
INT_LIST_ELEMENT* Make_Linked_List (short num_elements)
{
 short				counter;
 INT_LIST_ELEMENT	*head, *tail;

 head = malloc(sizeof(INT_LIST_ELEMENT));
 head->last_element = NULL;
 head->number  = -1;
 tail = head;
 for (counter = 0; counter <= num_elements; counter+=1)
  {
   tail->next_element = malloc(sizeof(INT_LIST_ELEMENT));
   tail->next_element->next_element = NULL;
   tail->next_element->last_element = tail;
   tail = tail->next_element;
   tail->number = counter;
  }
 tail->number = -1;
 return head;
}


/* Loads an array of x elements with the numbers 1..x) The order of numbers in the loaded array reflects the */
/* n'th possible permutation of the x! possibilities Where:    */
/* 	'x' is passed as argument (short) 'num_elements'      */
/* 	'n' is passed as argument (int)   'this_permutation'  */
/*  the array to be loaded is passed as (short*) list    */
void Find_Permutation(short num_elements, int this_permutation, short* list)
{
 short* ref_list;
 int    counter, temp;
 INT_LIST_ELEMENT	*head, *tail;

 ref_list = calloc(num_elements, sizeof(short));
 for(counter=0; counter<num_elements; counter+=1)
  ref_list[counter] = counter;
 head=Make_Linked_List(num_elements);  /* creates the basic linked list of num_elements */
 while(num_elements > 0)
 {
 temp = 1+ (int) (this_permutation/Factorial((short)(num_elements-1)));
 this_permutation -= (int) ((temp-1)*Factorial((short)(num_elements-1)));
 tail = head;
 for(counter = 0; counter<temp; counter +=1)
  tail=tail->next_element;
 (tail->last_element)->next_element = tail->next_element;
 (tail->next_element)->last_element = tail->last_element;
 list[num_elements-1] = (short) tail->number;
 free(tail);
 num_elements -=1;
 }

 free(head->next_element); /* Frees the dummy element at the tail of the linked list*/
 free(head);               /* Frees the dummy element at the head of the linked list*/
}

