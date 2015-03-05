/* Useful for storing 3 letter strings: amino acid names and codons*/
typedef char NAME[4];

/* The fundamental unit of the amino acid array */
struct Acid_Struct
{
 NAME  name;
 float parameter;
 float PR;
};
typedef struct Acid_Struct ACID_ELEMENT_TYPE;
typedef ACID_ELEMENT_TYPE ACID_ARRAY[22];

/* COORD is a handy little number for moving through the matrix */
struct Coordinate
{
 short x;
 short y;
 short z;
};
typedef struct Coordinate COORD;

/* The following compound structure stores data necessary to calculate MS */
/* values for any given codon: the structure looks like this:             */
/*                                                                        */
/*       {ALL_MS}   variable.MS[n].num_obs  {INT}                         */
/*                                .total_SS {FLOAT}                       */
/*                                                                        */
/*                  where n = 0..3                                        */
/*                                                                        */

struct Single_MS /* contains two fields: one for the total sum of squares        */
                 /* (total_SS), and one for the number of observations (num_obs) */
{
 float num_obs;
 float total_SS;
};
typedef struct Single_MS SINGLE_MS;

struct All_MS /* Stores all MS measurements for a particular amino acid */
{
 SINGLE_MS MS[4];
};
typedef struct All_MS ALL_MS;


/*  type MUTANT contains all information pertaining to a particular point mutation */
/*                                                                                 */
/*     .stt_coord     [a struct of type COORD which collectively describes         */
/*                     the codon in MATRIX_TYPE from which a that particular       */
/*                     point mutation has occurred]                                */
/*                     mutation represents a transition mutation]                  */
/*     .mut_coord     [a struct of type COORD which collectively describes         */
/*                     the new codon in MATRIX_TYPE which that particular          */
/*                     point mutation would lead to]                               */
/*                     mutation represents a transition mutation]                  */
/*     .is_transition [a 0 or 1 value indicating whether that partiular            */
/*                     mutation represents a transition mutation]                  */
/*     .base_number   [a 0..3 value indicating which codon base number that        */
/*                     mutation represents]                                        */
/*     .is_A          [a 0 or 1 value indicating whether that particular           */
/*                     mutation represents a mutation N->A]                        */
/*                                                                                 */
struct Mutant_Element
{
 short is_transition;
 short base_number;
 short is_A ;
 COORD mut_coord;
 COORD stt_coord;
};

typedef struct Mutant_Element MUTANT;



/*   THE CODON MATRIX: the basic 4x4x4 matrix, each element referring to one of the 64 codons, */
/*   and contains a SHORT indentifier to indicate which amino acid that element refers to      */
/*   (i.e.a number <22) referring to an element of the amino acid array "acid_array"           */
struct matrix_element
{
 short 	acid_number;
};
typedef struct matrix_element MATRIX_ELEMENT;
typedef MATRIX_ELEMENT MATRIX_TYPE[4][4][4];


/* A data structure used to create linked lists of integers: used in code shuffling routines */
struct Int_List_Element
{
 short  number;
 struct Int_List_Element* next_element;
 struct Int_List_Element* last_element;
};
typedef struct Int_List_Element INT_LIST_ELEMENT;


/* MS struct contains fields for the data pertaining to a a sample's error values */
struct MS_Struct
 {
  long   num_bt[4];
  float	 num_obs[4];
  float  tot_SS[4];
  float  min_MS[4];
  double nat_MS[4];
  double tot_MS[4];
  double tsq_MS[4];
 };
typedef struct MS_Struct MS_STRUCT;


/* Results struct is used in creating a frequency array from a data file */
/* of individual error values for a sample                               */
struct Results_Struct
 {
  MS_STRUCT	MS;
  float		max_MS[4];
  float		min_MS[4];
  float		interval[4];
 };
typedef struct Results_Struct RESULTS_STRUCT;


/* contains fields for a frequency distribution of 100 classes for */
/* 4 error value types: each base position and all bases combined  */
struct Freq_Array
 {
  long		freq[4][100];
 };
typedef struct Freq_Array FREQ_ARRAY;


struct Settings_Struct      	/* stores details describing a particular simulation run */
{
 long    num_iterations;	/* number of members to produce in a sample */
 short   start_weight;		/* lower limit of Tr/Tv weighting (w)*/
 short   end_weight;        	/* upper limit of Tr/Tv weighting (w)*/
 short   MS_model;		/* Tr/Tv, n->A, OR mistranslation */
 short   variation_model;   	/* Haig and Hurst or Taylor and Coates */
 short   results_model;     	/* produce a sample or optimise   */
 short   similarity_measure;	/* PAM matrix or Physiochemcial Properties: method by which amino acid similarity is measured */
 short   trap_codes;        	/* create results file of better code structures? */
 short   power;		/* modular power function (p) can be used as a range upper limit, or a single value*/
 int     num_mutants;       	/* the number of legitimate mutations in a particular code (<9*64) */
 float** PAM_matrix;        	/* a pointer to the matrix of modular error values for a particular amino acid substitution */
 MUTANT* mutant_array;	/* an array of all the possible mutations in a particular code */
 FILE*   results_file_ptr;	/* a pointer to the ASCII results file */
 FILE*   tmp_file_ptr;		/* a pointer to the temporary (binary) file used to store individual error values for a sample */
};
typedef struct Settings_Struct SETTINGS_TYPE;


