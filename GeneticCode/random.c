/*
 * random.c
 *
 *  Created on: Feb 20, 2011
 *      Author: brian
 */

/*  Taken from 'Numerical recipes in C', this is given as 'ran1', page 280*/
/*  and passes all statistical tests for randomness until called approx   */
/*  10^8 times                                                            */

#define IA 16807
#define IM 2147483647
#define AM (1.0/IM)
#define IQ 127773
#define IR 2836
#define NTAB 32
#define NDIV (1+(IM-1)/NTAB)
#define EPS 1.2e-7
#define RNMX (1.0-EPS)

long idum = -317;

float URandom(void)
/* Produces a random number between 0 and 1; call with 'idum', a negative no. */
{
 int j;
 long k;
 static long iy=0;
 static long iv[NTAB];
 float temp;


 if(idum <=0 || !iy)         	/* Initialise   */
 {
  if (-(idum) < 1) idum = 1;  /* Be sure to prevent idum = 0 */
  else idum = -(idum);
  for (j=NTAB+7; j>=0; j--)
   {
	k=(idum)/IQ;
	idum= IA*(idum-k*IQ)-IR*k;
	if (idum < 0) idum += IM;
	if (j < NTAB) iv[j] = idum;
   }
   iy = iv[0];
  }

  k = (idum)/IQ;
  idum=IA*(idum-k*IQ)-IR*k;	/* Starts here when not intialising */
  if (idum < 0) idum += IM;
  j = iy/NDIV;
  iy = iv[j];
  iv[j] = idum;
  if ((temp=(float)AM*iy) > RNMX) return (float) RNMX;
  else return temp;
}



int Random_Int (int min, int max)
/* Calls URandom to get a random real number between 0 and 1, */
/* and turns this into an integer between 'min' and 'max'     */
{
 float num, temp;
 int   return_value, interval;

 interval = max - min;

 num = URandom();

 temp = (num*interval)+min;

 return_value = (int)(temp+0.5);

 return return_value;
}
