package edu.umb.bio.jsGenex.client.gx;
//the object returned by the expressGene method of Genex
//it contains the HTML for displaying the gene
//as well as info that the selection logic needs in order
//to determine which base in which strand was selected

public class VisibleGene {

  public HTMLContainer HTMLstrings;
  public String currentDNA;
  public Gene theGene;
  
  VisibleGene(HTMLContainer HTMLinput, Gene gene) {  // constructor
      theGene = gene;
      HTMLstrings = HTMLinput;
      currentDNA = "";
  }

  public String getColorHTML() {
      return HTMLstrings.getColorHTML();
  }
  
  public String getBwHTML() {
  	return HTMLstrings.getBwHTML();
  }
  
  public Gene getGene() {
      return theGene;
  }
  
}