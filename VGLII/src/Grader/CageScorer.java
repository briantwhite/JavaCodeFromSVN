package Grader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import GeneticModels.Cage;
import GeneticModels.GeneticModel;
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


			/* 
			 * iterate over the phenotypes
			 * 		actually, over the body part:type (eg Wing:Color) - the character
			 * 		not the individual traits (Wing:Color:Red etc)
			 * 	
			 */
			for (int i = 0; i < phenotypes.size(); i++) {
				Phenotype currentPheno = org.getPhenotypes().get(i);
				b.append("<li><b>");
				b.append(currentPheno.getTrait().getBodyPart() + " ");
				b.append(currentPheno.getTrait().getType());
				b.append("</b></li>");
				b.append("<ul>");

				/**
				 * look for sex linkage first:
				 * - for each child phenotype (character) of this trait, if you find
				 * 		males but no females
				 * 			or
				 * 		females but no males
				 * -> it shows evidence of sex linkage
				 * -> if not, it does not show evidence of sex linkage
				 * 
				 * note: you have to check all the olists with red eyes (eg)
				 * 	so, red eyes & six legs but also red eyes & 4 legs
				 */
				String character = 
					currentPheno.getTrait().getBodyPart() 
					+ ":" 
					+ currentPheno.getTrait().getType();
				boolean showsSexLinkage = false;
				phenoIt = children.keySet().iterator();
				while (phenoIt.hasNext()) {
					String pheno = phenoIt.next();
					OrganismList oList = children.get(pheno);
					Organism o = oList.get(0);
					String testCharacter = 
						o.getPhenotypes().get(i).getTrait().getBodyPart() 
						+ ":" 
						+ o.getPhenotypes().get(i).getTrait().getType();
					if (testCharacter.equals(character)) {
						int males = oList.getNumberOfMales();
						int females = oList.getNumberOfFemales();
						if (((males == 0) && (females > 0)) || 
								((males > 0) && (females == 0))) showsSexLinkage = true;
					}
				}
				b.append("<li>");

				if (showsSexLinkage) {
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
				
				if (org.getGeneticModel().getPhenoTypeProcessor().getInteractionType() 
						== PhenotypeProcessor.NO_INTERACTION) {
					b.append("evidence of <i>dominance</i></font></li>");
				} else {
					// must be EPISTASIS or COMPLEMENTATION
					b.append("evidence of <i>dominance</i> or <i>interaction</i></font></li>");
				}
				b.append("</ul>");
			}


			/*
			 * now, look at linkage
			 * only do this if more than one pheno and linkage is possible
			 * determining if a cross shows linkage between 2 traits is hard
			 * 		need to show one parent heterozygous for both traits
			 * 			and all 4 traits present in offspring
			 * 
			 * so, for now, just give the parent's genotypes
			 * 		and leave it up to the instructor
			 */
			if (mbui.hasLinkagePanel()) {
				b.append("<li><b>Linkage:</b></li>");
				b.append("<ul><li>Parent 1 Genotype:</li><ul>");
				Organism p0 = cage.getParents().get(0);
				b.append(getHTMLforGenotype(p0, p0.getMaternalAutosome().toString()));
				b.append(getHTMLforGenotype(p0, p0.getPaternalAutosome().toString()));
				b.append(getHTMLforGenotype(p0, p0.getMaternalSexChromosome().toString()));
				b.append(getHTMLforGenotype(p0, p0.getPaternalSexChromosome().toString()));
				b.append("</ul></ul>");

				b.append("<ul><li>Parent 2 Genotype:</li><ul>");				
				Organism p1 = cage.getParents().get(1);
				b.append(getHTMLforGenotype(p1, p1.getMaternalAutosome().toString()));
				b.append(getHTMLforGenotype(p1, p1.getPaternalAutosome().toString()));
				b.append(getHTMLforGenotype(p1, p1.getMaternalSexChromosome().toString()));
				b.append(getHTMLforGenotype(p1, p1.getPaternalSexChromosome().toString()));
				b.append("</ul></ul>");
			}
		}
		b.append("</ul>");
		return b.toString();
	}

	private String getHTMLforGenotype(Organism org, String chromosome) {
		if (chromosome.indexOf(";") == -1) {
			return "";
		} else {
			return "<li>" + chromosome + "</li>";
		}
	}

}


