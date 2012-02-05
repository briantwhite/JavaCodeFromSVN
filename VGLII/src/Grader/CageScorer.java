package Grader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import GeneticModels.Cage;
import GeneticModels.Organism;
import GeneticModels.OrganismList;
import GeneticModels.Phenotype;
import GeneticModels.PhenotypeProcessor;
import ModelBuilder.ModelBuilderUI;

/**
 * takes a set of Cages and determines if each shows:
 * 	-sex linkage or not
 * 		= if sex imbalance in offspring (eg red females but no red males)
 * 	-interaction evidence for one or more traits
 * 		= if offspring don't look like parents
 * 			A x B -> all A or all B
 * 			A x A -> any pheno other than A
 * 			A x B -> any pheno other than A or B
 * - linkage
 * @author brian
 *
 */
public class CageScorer {
	
	private ArrayList<Cage> cages;
	private ModelBuilderUI mbui;
	
	public CageScorer(ArrayList<Cage> cages, ModelBuilderUI mbui) {
		this.cages = cages;
		this.mbui = mbui;
	}

	public String getCageScores() {
		StringBuffer b = new StringBuffer();
		TreeSet<Integer> selectedCages = 
			mbui.getChosenRelevantCages();
		b.append("<hr>");
		b.append("<b>Selected Cages:</b><br>");
		
		if(selectedCages.size() == 0) {
			b.append("<b>No cages were selected.</b>");
		} else {
			Iterator<Integer> cageNumIt = selectedCages.iterator();
			while (cageNumIt.hasNext()) {
				int cageNum = cageNumIt.next();
				b.append(scoreCage(cages.get(cageNum - 1)));
			}
			
		}
		
		b.append("</ul>");
		return b.toString();
	}

	private String scoreCage(Cage cage) {
		StringBuffer b = new StringBuffer();

		b.append("<b>Cage ");
		b.append(cage.getId() + 1);
		b.append(" </b>");
		b.append("<ul>");

		// can't get data from the field pop
		if (cage.getId() == 0) {
			b.append("<li>You cannot get any information from the field Cage,</li>");
		} else {

			// get a token organism for reference purposes
			TreeMap<String, OrganismList> children = cage.getChildren();
			Iterator<String> phenoIt = children.keySet().iterator();
			phenoIt = children.keySet().iterator();
			Organism org = children.get(phenoIt.next()).get(0);
			ArrayList<Phenotype> phenotypes = org.getPhenotypes();

			// for now, can't deal with epistasis & complementation
			if (org.getGeneticModel().getPhenoTypeProcessor().getInteractionType() 
					== PhenotypeProcessor.NO_INTERACTION) {

				// iterate over the phenotypes
				for (int i = 0; i < phenotypes.size(); i++) {
					Phenotype currentPheno = org.getPhenotypes().get(i);

					b.append("<li><b>");
					b.append(currentPheno.getTrait().getBodyPart() + " ");
					b.append(currentPheno.getTrait().getType());
					b.append("</b></li>");
					b.append("<ul>");
					
					/**
					 * look for sex linkage first:
					 * - for each child phenotype of this trait, if you find
					 * 		males but no females
					 * 			or
					 * 		females but no males
					 * -> it shows evidence of sex linkage
					 * -> if not, it does not show evidence of sex linkage
					 * 
					 * note: you have to check all the olists with red eyes (eg)
					 * 	so, red eyes & six legs but also red eyes & 4 legs
					 */
					boolean showsSexLinkage = false;
					phenoIt = children.keySet().iterator();
					int males = 0;
					int females = 0;
					while (phenoIt.hasNext()) {
						String pheno = phenoIt.next();
						OrganismList oList = children.get(pheno);
						Organism o = oList.get(0);
						if (o.getPhenotypes().get(i).getTrait().toString().equals(
								currentPheno.getTrait().toString())) {
							males += oList.getNumberOfMales();
							females += oList.getNumberOfFemales();
						}
					}
					b.append("<li>");
					if (((males == 0) && (females > 0)) || ((males > 0) && (females == 0))) {
						showsSexLinkage = true;
						b.append("<font color=green>Shows ");
					} else {
						b.append("<font color=black>Does not show ");
					}
					b.append("evidence of <u>sex linkage</u></font></li> ");

					Phenotype p1Pheno = cage.getParents().get(0).getPhenotypes().get(i);
					Phenotype p2Pheno = cage.getParents().get(1).getPhenotypes().get(i);

					/**
					 * Then look for evidence of dominance, etc
					 * three ways it can be interesting
					 * 	Case 1) if p1Pheno not found in any kids: 
					 * 			A x B -> B only or -> C only
					 * 	Case 2) if p2Pheno not found in any kids: 
					 * 			A X B -> A only or -> C only
					 * 	Case 3) if kids have pheno that isnt p1 or p2: 
					 * 			A X B -> some C or A X A -> some B or some C
					 */
					boolean case1 = true;
					boolean case2 = true;
					boolean case3 = false;
					phenoIt = children.keySet().iterator();
					while (phenoIt.hasNext()) {
						Phenotype kidPheno = 
							children.get(phenoIt.next()).get(0).getPhenotypes().get(i);

						// if you ever find p1 in any type of kid, it can't be Case1
						if (kidPheno.toString().equals(p1Pheno.toString())) case1 = false;

						// if you ever find p2 in any type of kid, it can't be Case2
						if (kidPheno.toString().equals(p2Pheno.toString())) case2 = false;

						// if these kids have a neither p1 nor p2 pheno, its Case3
						if ((!kidPheno.toString().equals(p1Pheno.toString()))
								&& (!kidPheno.toString().equals(p2Pheno.toString()))) 
							case3 = true;
					}
					b.append("<li>");
					if (case1 || case2 || case3 || showsSexLinkage) {
						b.append("<font color = green>");
						b.append("Shows ");
					} else {
						b.append("<font color = black>");
						b.append("Does not show ");
					}
					b.append("evidence of <i>dominance</i></font></li>");
					b.append("</ul>");
				}

			} else {
				b.append("<li>Sorry, this has not been implemented "
						+ "for Complementation or Epistasis yet.</li>");
			}
			
			/*
			 * now, try for linkage
			 * only do this if more than one pheno
			 * try each pair of phenos
			 */
			if (phenotypes.size() > 1) {
				for (int i = 0; i < phenotypes.size(); i++) {
					for (int j = i + 1; j < phenotypes.size(); j++) {
						b.append(checkForEvidenceOfLinkageBetween(i, j));
					}
				}
			}
		}
		b.append("</ul>");
		return b.toString();
	}
	
	/*
	 * check for evidence of linkage
	 * to be evidentiary, 
	 * 		one parent must be heterozygous for both traits
	 * 	and: the other must have at least one recessive allele for both traits
	 * 
	 */
	private String checkForEvidenceOfLinkageBetween(int pheno1, int pheno2) {
		StringBuffer b = new StringBuffer();
		 
		System.out.println("cage scorer: looking for linkage between " + pheno1 + " and " + pheno2);
		return b.toString();
	}
}


