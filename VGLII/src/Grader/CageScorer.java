package Grader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import GeneticModels.Cage;
import GeneticModels.Organism;
import GeneticModels.OrganismList;
import GeneticModels.Phenotype;
import GeneticModels.PhenotypeProcessor;

/**
 * takes a Cage and determines if it shows:
 * 	-sex linkage or not
 * 		= if sex imbalance in offspring (eg red females but no red males)
 * 	-interaction evidence for one or more traits
 * 		= if offspring don't look like parents
 * 			A x B -> all A or all B
 * 			A x A -> any pheno other than A
 * 			A x B -> any pheno other than A or B
 * @author brian
 *
 */
public class CageScorer {

	public static String scoreCage(Cage cage) {
		StringBuffer b = new StringBuffer();

		b.append("<li><b>Cage ");
		b.append(cage.getId() + 1);
		b.append(" </b></li>");
		b.append("<ul>");

		// can't get data from the field pop
		if (cage.getId() == 0) {
			b.append("<li>You cannot get any information from the field Cage,</li>");
		} else {
			/**
			 * look for sex linkage first:
			 * - for each child phenotype, if you find
			 * 		males but no females
			 * 			or
			 * 		females but no males
			 * -> it shows evidence of sex linkage
			 * -> if not, it does not show evidence of sex linkage
			 */
			boolean showsSexLinkage = false;
			TreeMap<String, OrganismList> children = cage.getChildren();
			Iterator<String> phenoIt = children.keySet().iterator();
			while (phenoIt.hasNext()) {
				String pheno = phenoIt.next();
				OrganismList oList = children.get(pheno);
				int males = oList.getNumberOfMales();
				int females = oList.getNumberOfFemales();
				if (((males == 0) && (females > 0)) || ((males > 0) && (females == 0))) {
					showsSexLinkage = true;
				}
			}
			b.append("<li>");
			if (showsSexLinkage) {
				b.append("Shows ");
			} else {
				b.append("Does not show ");
			}
			b.append("evidence of sex linkage.</li>");

			/**
			 * now look for evidence of dominance, etc
			 * go phenotype by phenotype - unless there's epistasis or compl
			 */
			// get a token organism for reference purposes
			phenoIt = children.keySet().iterator();
			Organism org = children.get(phenoIt.next()).get(0);
			if (org.getGeneticModel().getPhenoTypeProcessor().getInteractionType() 
					== PhenotypeProcessor.NO_INTERACTION) {
				// iterate over the phenotypes
				ArrayList<Phenotype> phenotypes = org.getPhenotypes();
				for (int i = 0; i < phenotypes.size(); i++) {
					Phenotype currentPheno = org.getPhenotypes().get(i);
					Phenotype p1Pheno = cage.getParents().get(0).getPhenotypes().get(i);
					Phenotype p2Pheno = cage.getParents().get(1).getPhenotypes().get(i);

					/**
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
					if (case1 || case2 || case3) {
						b.append("Shows evidence of dominance for ");
					} else {
						b.append("Shows  no evidence of dominance for ");
					}
					b.append(currentPheno.getTrait().getBodyPart() + " ");
					b.append(currentPheno.getTrait().getType());
					b.append("</li>");
				}

			} else {
				b.append("<li>Sorry, this has not been implemented "
						+ "for Complementation or Epistasis yet.</li>");
			}
		}
		b.append("</ul>");
		return b.toString();
	}
}


