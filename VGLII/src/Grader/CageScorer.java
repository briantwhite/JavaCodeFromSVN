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
			System.out.println("analyzing linkage");
			if (phenotypes.size() > 1) {
				for (int i = 0; i < phenotypes.size(); i++) {
					for (int j = i + 1; j < phenotypes.size(); j++) {
						b.append(checkForEvidenceOfLinkageBetween(cage, phenotypes, i, j));
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
	 *    (but that's a pain to tell)
	 *    easier = find both phenos of both traits in offspring
	 * 
	 */
	private String checkForEvidenceOfLinkageBetween(Cage cage, 
			ArrayList<Phenotype> phenotypes, 
			int pheno1, int pheno2) {

		// check for heterozygosity first
		Organism p1 = cage.getParents().get(0);
		String p1ma = p1.getMaternalAutosome().toString();
		String p1pa = p1.getPaternalAutosome().toString();
		String p1ms = p1.getMaternalSexChromosome().toString();
		String p1ps = p1.getPaternalSexChromosome().toString();
		boolean p1heterozygous = (isHeterozygous(p1ma, p1pa, pheno1, pheno2) 
				|| isHeterozygous(p1ms, p1ps, pheno1, pheno2));
		if (p1heterozygous) {
			System.out.println("p1: is heterozygous for " + phenotypes.get(pheno1) + " and " + phenotypes.get(pheno2));
			System.out.println("\t" + p1ma);
			System.out.println("\t" + p1pa);
			System.out.println("\t" + p1ms);
			System.out.println("\t" + p1ps);
		}
		
		Organism p2 = cage.getParents().get(1);
		String p2ma = p2.getMaternalAutosome().toString();
		String p2pa = p2.getPaternalAutosome().toString();
		String p2ms = p2.getMaternalSexChromosome().toString();
		String p2ps = p2.getPaternalSexChromosome().toString();
		boolean p2heterozygous = (isHeterozygous(p2ma, p2pa, pheno1, pheno2) 
				|| isHeterozygous(p2ms, p2ps, pheno1, pheno2));
		if (p2heterozygous) {
			System.out.println("p2: is heterozygous for " + phenotypes.get(pheno1) + " and " + phenotypes.get(pheno2));
			System.out.println("\t" + p2ma);
			System.out.println("\t" + p2pa);
			System.out.println("\t" + p2ms);
			System.out.println("\t" + p2ps);
		}
		
		// then for all four types of offpsring
		
		StringBuffer b = new StringBuffer();
		 
		return b.toString();
	}
	
	/*
	 * check for heterozygosity at two traits from pheno string
	 * like: Gray-Eye;Three-Wing;Forked-Body
	 * could also be "null sex chromosome" - then can't tell
	 * 
	 * if has trait, look at the two traits specified by the indices
	 * only true if heterozygous at BOTH
	 * 
	 */
	private boolean isHeterozygous(String chromo1, String chromo2, int index1, int index2) {
		// if either is "null sex chromo" can't tell
		if ((chromo1.indexOf(";") == -1) || (chromo2.indexOf(";") == -1)) return false;
		
		//break up into each pheno
		String[] pheno1parts = chromo1.split(";");
		String[] pheno2parts = chromo2.split(";");
		
		return (!pheno1parts[index1].equals(pheno2parts[index1]) 
				&& !pheno1parts[index2].equals(pheno2parts[index2]));
	}
}


