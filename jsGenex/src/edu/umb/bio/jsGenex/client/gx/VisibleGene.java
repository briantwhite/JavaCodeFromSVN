package edu.umb.bio.jsGenex.client.gx;
//the object returned by the expressGene method of Genex
//it contains the HTML for displaying the gene
//as well as info that the selection logic needs in order
//to determine which base in which strand was selected

public class VisibleGene {

  public String HTML;
  public String currentDNA;
  public Gene theGene;
  
  VisibleGene(String HTML, Gene gene) {  // constructor
      theGene = gene;
      this.HTML = HTML;
      currentDNA = "";
  }

  public String getColorHTML() {
      return HTML;
  }
  
   
  public Gene getGene() {
      return theGene;
  }
  
}