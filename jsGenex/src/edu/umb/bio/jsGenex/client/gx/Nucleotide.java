package edu.umb.bio.jsGenex.client.gx;
//Genes are made of an ArrayList of these
//they keep track of where they are in each strand
//- if they are in a strand & their position in the strand
//this info is needed by the selection logic to be sure that
//all corresponding nucleotides are selected.

public class Nucleotide {

 char base;
 int num;
 
 boolean isInPremRNA;
 int premRNABaseNum;
 
 boolean isInmRNA;
 int mRNABaseNum;
 
 boolean isInProtein;
 int aaNum;
 int positionInCodon;
 
 boolean selected;
 
 public Nucleotide (char newBase, int number) {
     num = number;
     base = newBase;
     isInmRNA = false;
     mRNABaseNum = -1;
     isInPremRNA = false;
     premRNABaseNum = -1;
     isInProtein = false;
     aaNum = -1;
     positionInCodon = -1;
     selected = false;
 }
 
 //get the DNA base
 public String getBase() {
     return String.valueOf(base);
 }
 
 //get the complementary DNA base
 public String getComplementBase() {
     switch (base) {
         case 'A': return "T";
         case 'G': return "C";
         case 'C': return "G";
         case 'T': return "A";  
     }
     return "";
 }
 
 //get the number in the DNA strand
 public int getDNABaseNum() {
     return num;
 }
 
 //get the base as it would occur in pre- & mature- mRNA
 public String getRNABase() {
     if (!isInPremRNA  && ! isInmRNA) return (" ");
     //if it's in the polyA tail, return A
     if (!isInPremRNA && isInmRNA) {
         return "A";
     }
     if (base == 'T')  return "U";
     return String.valueOf(base);
 }
 
 //methods for marking base as in pre- & mature- mRNA
 public void setInPremRNA() {
     isInPremRNA = true;
 }
 
 public boolean getInPremRNA() {
     return isInPremRNA;
 }
 
 public void setPremRNABaseNum(int newNum) {
     premRNABaseNum = newNum;
 }
 
 public int getPremRNABaseNum() {
     return premRNABaseNum;
 }

 public void setInmRNA() {
     isInmRNA = true;
 }
 
 public boolean getInmRNA() {
     return isInmRNA;
 }
 
 public void setmRNABaseNum(int newNum) {
     mRNABaseNum = newNum;
 }
 
 public int getmRNABaseNum() {
     return mRNABaseNum;
 }
 
 public void setInProtein() {
     isInProtein = true;
 }
 
 public void setAANum (int x) {
     aaNum = x;
 }
 
 public void setCodonPosition (int i) {
     positionInCodon = i;
 }
 
 public boolean getInProtein() {
     return isInProtein;
 }
 
 public int getAANum() {
     return aaNum;
 }
 
 public int getCodonPosition() {
     return positionInCodon;
 }
 
 public boolean getSelected() {
     return selected;
 }
 
 public void setSelected() {
     selected = true;
 }

}