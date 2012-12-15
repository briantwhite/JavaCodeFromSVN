package Grader;

import org.jdom.Element;

import GeneticModels.GeneModel;
import GeneticModels.GeneticModel;
import GeneticModels.PhenotypeProcessor;
import ModelBuilder.ModelBuilderUI;
import ModelBuilder.ModelPane;
import VGL.Messages;

/*
 * Compares correct answer (GeneticModel) with student answer (ModelBuilderUI)
 * returns info for direct on-line grading
 * = Success or failure in matching - not details of their answer
 * this is meant to be machine readable, not human readable
 */
public class AutoGrader {
	
	public static final String CORRECT = "CORRECT";
	public static final String INCORRECT = "INCORRECT";
	
	public Element grade(GeneticModel gm, ModelBuilderUI mbui) {
		
		Element e = new Element("Grade");  // root element
		
		// some basic info
		e.setAttribute("ProblemFileName", gm.getProblemFileName());
		e.setAttribute("PracticeMode", String.valueOf(gm.isBeginnerMode()));
		
		/*
		 * Then, see if there's epistasis or complementation
		 * if there is, then the #geneModels > #characters
		 * so you have to be careful and process it differently
		 */
		if (gm.getPhenoTypeProcessor().getInteractionType() == PhenotypeProcessor.NO_INTERACTION) {
			for (int i = 0; i < gm.getNumberOfGeneModels(); i++) {
				Element gmEl = new Element("FirstGene");
				
				// get right answer and student answer
				GeneModel geneModel = gm.getGeneModelByIndex(i);
				ModelPane modelPane = mbui.getModelPanes()[i];
				
				// the character
				gmEl.setAttribute("Character", geneModel.getCharacter());

				/*
				 *  sex linked or not
				 *    note that this is set once in GeneticModel for all individual gene models
				 *    but we have to check each one
				 *  the indices are from the sex linkage choice JComboBox
				 *  	0 = unknown
				 *  	1 = not sex-linked
				 *  	2 = XX/XY
				 *  	3 = ZZ/ZW
				 */
				String sexLinkageGrade = INCORRECT;
				if (gm.isGeneModelSexLinkedByIndex(i)) {
					if (gm.getSexLinkageType()) {
						// XX/XY
						if (modelPane.getSexLinkageChoice() == 2) sexLinkageGrade = CORRECT;
					} else {
						// ZZ/ZW
						if (modelPane.getSexLinkageChoice() == 3) sexLinkageGrade = CORRECT;
					}
				} else {
					// not sex-linked
					if (modelPane.getSexLinkageChoice() == 1) sexLinkageGrade = CORRECT;
				}
				gmEl.setAttribute("SexLinkage", sexLinkageGrade);
 
				/*
				 * number of alleles
				 *  0 = unknown
				 *  1 = 2-allele
				 *  2 = 3-allele
				 */
				if ((modelPane.getAlleleNumberChoice() + 1) == geneModel.getNumAlleles()) {
					gmEl.setAttribute("NumberOfAlleles", CORRECT);
				}
	
				/*
				 * these are the raw selected strings (eg "Simple dominance") 
				 * in the LOCAL language, so you have to match with translated version
				 */
				String interactionTypeGrade = INCORRECT;
				String studentDomTypeText = modelPane.getInteractionTypeChoice();
				if (geneModel.getDomTypeText().equals("Simple") 
						&& studentDomTypeText.equals(Messages.getInstance().getString("VGLII.SimpleDominance"))) interactionTypeGrade = CORRECT;
				if (geneModel.getDomTypeText().equals("Circular")
						&& studentDomTypeText.equals(Messages.getInstance().getString("VGLII.CircularDominance"))) interactionTypeGrade = CORRECT;
				if (geneModel.getDomTypeText().equals("Hierarchical")
						&& studentDomTypeText.equals(Messages.getInstance().getString("VGLII.HierarchicalDominance"))) interactionTypeGrade = CORRECT;
				if (geneModel.getDomTypeText().equals("Incomplete")
						&& studentDomTypeText.equals(Messages.getInstance().getString("VGLII.IncompleteDominance"))) interactionTypeGrade = CORRECT;
				gmEl.setAttribute("InteractionType", interactionTypeGrade);
				
				//details
				b.append("<ul>" + gm.getInteractionHTML() + "</ul>");

				// end it
				b.append("</ul>");
				b.append("<hr>");
				e.addContent(gmEl);
			}

		} else {
			
		}
		
		
		// linkage, if present
		b.append(autosomeModel.getHTMLForGrading());
		b.append(sexChromosomeModel.getHTMLForGrading());

		
		return e;
	}

}
