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

	public static Element grade(GeneticModel gm, ModelBuilderUI mbui) {

		Element e = new Element("Grade");  // root element

		// some basic info
		Element p = new Element("Problem");
		p.setAttribute("ProblemFileName", gm.getProblemFileName());
		p.setAttribute("PracticeMode", String.valueOf(gm.isBeginnerMode()));
		e.addContent(p);
		
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

				String sexLinkageGrade = INCORRECT;
				if (gm.isGeneModelSexLinkedByIndex(i)) {
					if (gm.getSexLinkageType()) {
						// XX/XY
						if (modelPane.getSexLinkageChoice().equals(
								"XX " 
										+ Messages.getInstance().getString("VGLII.Female")
										+ "/XY "
										+ Messages.getInstance().getString("VGLII.Male"))) sexLinkageGrade = CORRECT;
					} else {
						// ZZ/ZW
						if (modelPane.getSexLinkageChoice().equals(
								"ZZ " 
										+ Messages.getInstance().getString("VGLII.Male")
										+ "/ZW "
										+ Messages.getInstance().getString("VGLII.Female"))) sexLinkageGrade = CORRECT;
					}
				} else {
					// not sex-linked
					if (modelPane.getSexLinkageChoice().equals(
							Messages.getInstance().getString("VGLII.NotSexLinked"))) sexLinkageGrade = CORRECT;
				}
				gmEl.setAttribute("SexLinkage", sexLinkageGrade);

				/*
				 * number of alleles
				 */
				if ((modelPane.getAlleleNumberChoice()) == geneModel.getNumAlleles()) {
					gmEl.setAttribute("NumberOfAlleles", CORRECT);
				} else {
					gmEl.setAttribute("NumberOfAlleles", INCORRECT);
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
				//				b.append("<ul>" + gm.getInteractionHTML() + "</ul>");

				// end it
				//				b.append("</ul>");
				//				b.append("<hr>");
				e.addContent(gmEl);
			}

		} else {

		}


		// linkage, if present
		//		b.append(autosomeModel.getHTMLForGrading());
		//		b.append(sexChromosomeModel.getHTMLForGrading());


		return e;
	}

}
