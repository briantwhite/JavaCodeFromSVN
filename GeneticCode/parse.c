/*
 * parse.c
 *
 *  Created on: Feb 20, 2011
 *      Author: brian
 */

/* Function Transform_PAM_Matrix transforms PAM matrix values according to Ardell, 1998 */
void Transform_PAM_Matrix(SETTINGS_TYPE* settings_ptr)
{
 short counter, count2;
 float value;
 for(counter=0; counter<20; counter+=1)
  for(count2=0; count2<20; count2+=1)
   {
    value = settings_ptr->PAM_matrix[counter][count2];
    settings_ptr->PAM_matrix[counter][count2] = (float)pow(10, (value/10)*-1);
   }
 for(counter=0; counter<20; counter+=1)
  settings_ptr->PAM_matrix[counter][counter] = 0;
}

/* Function Is_Codon finds whether a character may be interpreted as  a nucleotide (returns 1) or not (returns 0) */
short Is_Codon(char codon[3])
{
 short base, valid = TRUE;
 for(base = 0; base<3; base+=1)
 {
  codon[base] = toupper(codon[base]);
  switch(codon[base])
  {
   case 'U': break;
   case 'C': break;
   case 'A': break;
   case 'G': break;
   case 'N': break;
   case 'R': break;
   case 'Y': break;
   default: valid = FALSE; break;
  };
 }
 return valid;
}

/* Accepts any nucleotide notation, and returns a range of values between 0 and 4, representing */
/* the nculeotides indicated by this notation  e.g. R=A or G, this corresponds to the range 2..3 */
void Base_to_Range(char base, short* lower_ptr, short* upper_ptr)
{
 base = toupper(base);
 switch(base)
 {
  case 'U': *lower_ptr =0; *upper_ptr =0; break;
  case 'C': *lower_ptr =1; *upper_ptr =1; break;
  case 'A': *lower_ptr =2; *upper_ptr =2; break;
  case 'G': *lower_ptr =3; *upper_ptr =3; break;
  case 'N': *lower_ptr =0; *upper_ptr =3; break;
  case 'R': *lower_ptr =2; *upper_ptr =3; break;
  case 'Y': *lower_ptr =0; *upper_ptr =1; break;
  default: break;
 };
}

/* Function Is_Acid compares a 3 character string ('name') with the 'name' fields held in the */
/* amino acid arra. I f a match is found, then the function returns 1; if no match, 0 is returned */
short Is_Acid(char name[3], ACID_ELEMENT_TYPE* acid_array)
{
 short count, valid = FALSE;
 for(count = 0; count<3; count+=1)
  name[count] = toupper(name[count]);
 for(count = 0; count<22; count+=1)
  if(strcmp(name, acid_array[count].name)==0) valid = TRUE;
 return valid;
}

/* Converts a numb er(0..3) to the corresponding nucleotide character. Useful for converting coordinates to codons   */
char Number_to_Base(int number)
{
 char base;
 switch (number)
   {
    case 0: base = 'U';  break;
    case 1: base = 'C';  break;
    case 2: base = 'A';  break;
    case 3: base = 'G';  break;
    default: base = '*'; break;
   }
 return base;
}

/* converts a nucleotide character to the appropriate number (0..3). useful for converting codons to coordinates       */
int Base_to_Number(char base)
{
 int number;
 switch (base)
  {
   case 'U': number = 0; break;
   case 'C': number = 1; break;
   case 'A': number = 2; break;
   case 'G': number = 3; break;
   default: number=99; break;
  }
 return number;
}

/* Converts an amino acid name (3 characters) to the appropriate number  as found in the amino acid array   */
int Acid_to_Number(NAME acid_name, ACID_ELEMENT_TYPE* acid_array)
{
 unsigned short int counter=0, acid_number = 0;
 while ((acid_number == 0) && (counter<22))
  {
   counter++;
   if(strcmp(acid_name, acid_array[counter].name)==0) acid_number = counter;
  }
  return acid_number;
}

/* Takes two coordinates (e.g. a the codons involved in a genetic error and compares them to
/* identify whether the change implied corresponds to a transition (function returns 1) or a transversion (returns 0)   */
short Is_Transition(COORD start_coord, COORD this_coord, short base_num)
{
 short	transition = FALSE;
 short	base1_num , base2_num;
 char	base1_char, base2_char;
 switch (base_num)
 {
  case 1:	base1_num = start_coord.x; base2_num = this_coord.x; break;
  case 2:	base1_num = start_coord.y; base2_num = this_coord.y; break;
  case 3:	base1_num = start_coord.z; base2_num = this_coord.z; break;
 }
 base1_char = Number_to_Base(base1_num);
 base2_char = Number_to_Base(base2_num);
 if((base1_char == 'A') && (base2_char == 'G')) transition = TRUE;
 if((base1_char == 'G') && (base2_char == 'A')) transition = TRUE;
 if((base1_char == 'C') && (base2_char == 'U')) transition = TRUE;
 if((base1_char == 'U') && (base2_char == 'C')) transition = TRUE;
 return transition;
}

/* identifies whether a particular genetic error is to 'A' or 'U'.  Used by the weighting model N->A   */
short Is_A(COORD this_coord, short base_num)
{
 short	is_A = FALSE;
 short	base1_num;
 char 	base1_char;
 switch (base_num)
 {
  case 1:	base1_num = this_coord.x; break;
  case 2:	base1_num = this_coord.y; break;
  case 3:	base1_num = this_coord.z; break;
 }
 base1_char = Number_to_Base(base1_num);
 if((base1_char == 'A')||(base1_char == 'U')) is_A = TRUE;
 return is_A;
}
