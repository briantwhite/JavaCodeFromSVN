/*  File "gen_io.c" conatains source code for general input/output*/
/*  functions handling reading types of user input, safe opening  */
/*  of data streams and the most general types of output          */
/*  Functions in here are DELIBERATELY NOT PROGRAM SPECIFIC       */


/* A general function for the Safe opening of file streams:         */
/* accepts a string detailing the file to be opened, a string       */
/* detailing the type of opening (reading, writing etc.) and a      */
/* indicating whether the output stream should detail what is being */
/* attempted. If the opening is unsuccessful, the function warns    */
/* the user and prompts for an alternative path string.             */
/*  The function is safe in that it will not finish until a file has*/
/*  successfully been opened.                                       */
FILE*  Safe_Open(char*  file_string, char*  action_string, int warn)
{
 FILE* file_ptr;

 file_ptr = (fopen(file_string, action_string));

 if ((file_ptr!=NULL) && (warn==TRUE))
  {
   printf("\n\n '%s' File Opened Successfully For ", file_string);
   if (action_string[0]=='r') printf("Reading!");
   if (action_string[0]=='w') printf("Writing");
  }

 while (file_ptr==NULL)
  {
   printf("\n\n '%s' File Could Not Be Opened!", file_string);
   printf("\nPlease enter an alternative:");
   gets(file_string);
   file_ptr = (fopen(file_string, action_string));
  }

 return file_ptr;
}

/*    Function Get_Character gets a single character user input   */
/*    (from the stdin stream) which lies in a requested interval. */
/*    If the user input does not lie in the requested interval    */
/*    the the user is prompted for a new input, with permissable  */
/*    interval indicated. The function does not finish until a    */
/*    suitable character has been typed in                        */
char Get_Character(char lower, char upper)
{
 char command[80], buffer;
 fgets(command, 80, stdin);
 buffer=command[0];

 while ((buffer<lower)||(buffer>upper))
 {
  printf("\n    %cINVALID ENTRY!\n    Command?", 7);
  fgets(command, 80, stdin);
  buffer=command[0];
 }
 return buffer;
}

/*   Function Pause simply prevents further progress in the     */
/*   program until the user has typed the 'return' key          */
void Pause(void)
{
 char buffer;
 buffer = Get_Character(0, 127);
}

void Print_Break(void)
{
 printf("\n**********************************************************************");
}


/* safely reads an integer from a file specified by 'file_ptr', and returns as the arugment of */
/* the function. accepts '+ and '-' as prefixes, and ignores (reads through) other non-digit characters */
long Read_Integer(FILE* file_ptr)
{
 int  valid = FALSE;
 long integer=0, multiplier;
 char buffer='\0';
 multiplier = 1;

 while(((buffer<'0') || (buffer>'9')) && (feof(file_ptr)==FALSE))
  {
   buffer = fgetc(file_ptr);
   if(buffer == '-') multiplier = -1;
   if(buffer == '+') multiplier = +1;
  }

 while(((buffer>='0') && (buffer<='9')) && (!feof(file_ptr)))
  {
   valid = TRUE;
   integer = (integer*10) + (buffer - '0');
   buffer = fgetc(file_ptr);
   valid = FALSE;
  }
 return integer;
}

/* Function Get-User_Input accepts two arguments (both LONG), which define the */
/* minimum and maximum acceptable integer inputs. It reads a user entry, and   */
/* either accepts and returns this entry, or sounds a warning, specifies the   */
/* appropriate range and then reads in another number                          */
long Get_User_Input_Number(long min, long max)
{
 long entry_num;

 entry_num = (long) Read_Integer(stdin);

 while((entry_num<min)||(entry_num>max))
 {
  printf("\n    %cINVALID ENTRY: please reenter a value between %ld and %ld:", 7, min, max);
  entry_num = (short) Read_Integer(stdin);
 }
 return entry_num;
}

/*  The following functions are DOS specific functions which link */
/*  the application to a range of basic OS commands               */
void Display_Directory_Contents(void)
{
 system(" dir /p");
 Pause();
}

/* Function 'List_Files' list all files inthe current working directory which conform to a particular */
/* file extension(S) (specified by the string 'extension'). These are initally listed to a text file  */
/* (file system_text = 'system.txt'), and are then printed to stdout, with an associated number for   */
/* each file. The user is then asked to identify the file of interest by entering the associated      */
/* number, and this file is safely opened (for reading) and returned as the argment of the function   */
/* This function is the only system dependent code in the program, and should be modified by users    */
/* not working in an MS-DOS environment. In particular, it makes  use of the 'system' command to send */
/* a comand line instruction to the operating system. THIS INSTRUCTION IS MS-Dos SPECIFIC             */
FILE* List_Files(char* extension)
{
 char	system_command[100], buffer;
 short  counter, start=0, valid = TRUE;
 FILE*	system_text;

 system_text = Safe_Open("system.txt\0", "w\0", FALSE); /* creates an empty text file to contain the results of the OS iinstructions */
 fclose(system_text);  /* ensures accessibility for the OS instructions */
 strcpy(system_command, " dir *.*** >> system.txt /b \0");/* text instruction to the OS to list all files, placing results in a test file called 'system.txt'  */
 while(valid) /* loops through all file extensions passed in string 'extension', appending file 'system.txt' with the results of "dir [extension]" */
 {
  for(counter = 0; counter<5; counter +=1)  /* replaces the 5 characters *.*** with an extension specified in 'extension' */
   {
    buffer = extension[counter+(6*start)];
    system_command[5+counter]=buffer;
   }
  system(system_command); /* executes the system command */
  buffer = extension[5+start];
  start +=1;
  if(buffer!=',') valid = FALSE; /* 'extension' is a comma delimited list of file extensions */
 }

 system_text = Safe_Open("system.txt\0", "r\0", FALSE); /* opens 'system.txt' to read the filenames stored there */
 valid = TRUE; counter = 0; buffer = '\n'; /* sets variables ready for the following routine */

 while(valid)  /* this routine prints all 'dir' files to the screen as a numbered list */
  if(buffer=='\n')
   {
    buffer = fgetc(system_text);
    if(feof(system_text)) valid = FALSE;
    else
    {
     counter +=1;
     printf("\n %2hd: ", counter);
    }
    printf("%c", buffer); buffer = fgetc(system_text);
   }
  else
   { printf("%c", buffer); buffer = fgetc(system_text); }

 printf("\n ----------\n File for Input (1..%hd)?", counter);
 counter = (short)Get_User_Input_Number(1, counter); /* Requests listed number of file to opened */

 rewind(system_text);
 for(start=0; start<counter; start+=1)
  fgets(system_command, 50, system_text);

 system_command[strlen(system_command)-1]='\0';

 fclose(system_text);
 system("del system.txt");
 system_text = Safe_Open(system_command, "r\0", TRUE);

 return system_text;
}
