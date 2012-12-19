package Grader;

import java.util.List;

import org.jdom.Element;

import GeneticModels.GeneModel;
import GeneticModels.GeneticModel;
import GeneticModels.PhenotypeProcessor;
import ModelBuilder.ModelBuilderUI;
import ModelBuilder.ModelDetailsPanel;
import ModelBuilder.ModelPane;
import VGL.Messages;

/*
 * Compares correct answer (GeneticModel) with student answer (ModelBuilderUI)
 * returns info for direct on-line grading
 * = Success or failure in matching - not details of their answer
 * this is meant to be machine readable, not human readable
 */
public class AutoGrader {

	// allowable difference between
	private static final float ERROR_TOLERANCE = 0.2f;

	public static Element grade(GeneticModel gm, ModelBuilderUI mbui) {

		Element e = new Element("Grade");  // root element

		// some basic info
		Element p = new Element("Problem");
		p.addContent((new Element("ProblemFileName")).addContent(gm.getProblemFileName()));
		p.addContent((new Element("PracticeMode")).addContent(String.valueOf(gm.isBeginnerMode())));
		e.addContent(p);

		/*
		 * Then, see if there's epistasis or complementation
		 * if there is, then the #geneModels > #characters
		 * so you have to be careful and process it differently
		 */
		if (gm.getPhenoTypeProcessor().getInteractionType() == PhenotypeProcessor.NO_INTERACTION) {
			for (int i = 0; i < gm.getNumberOfGeneModels(); i++) {
				Element geneEl = new Element("Gene");

				geneEl.addContent((new Element("Index")).addContent(String.valueOf(i)));

				// get right answer and student answer
				GeneModel geneModel = gm.getGeneModelByIndex(i);
				ModelPane modelPane = mbui.getModelPanes()[i];

				// the character
				geneEl.addContent((new Element("Character")).addContent(geneModel.getCharacter()));

				boolean sexLinkageCorrect = false;
				if (gm.isGeneModelSexLinkedByIndex(i)) {
					if (gm.getSexLinkageType()) {
						// XX/XY
						if (modelPane.getSexLinkageChoice().equals(
								"XX " 
										+ Messages.getInstance().getString("VGLII.Female")
										+ "/XY "
										+ Messages.getInstance().getString("VGLII.Male"))) sexLinkageCorrect = true;
					} else {
						// ZZ/ZW
						if (modelPane.getSexLinkageChoice().equals(
								"ZZ " 
										+ Messages.getInstance().getString("VGLII.Male")
										+ "/ZW "
										+ Messages.getInstance().getString("VGLII.Female"))) sexLinkageCorrect = true;
					}
				} else {
					// not sex-linked
					if (modelPane.getSexLinkageChoice().equals(
							Messages.getInstance().getString("VGLII.NotSexLinked"))) sexLinkageCorrect = true;
				}
				Element slEl = new Element("SexLinkage");
				slEl.addContent((new Element("Correct")).addContent(String.valueOf(sexLinkageCorrect)));
				geneEl.addContent(slEl);

				/*
				 * number of alleles
				 */
				Element naEl = new Element("NumberOfAlleles");
				naEl.addContent((
						new Element("Correct")).addContent(String.valueOf(
								(modelPane.getAlleleNumberChoice()) == geneModel.getNumAlleles())));
				geneEl.addContent(naEl);
				
				/*
				 * these are the raw selected strings (eg "Simple dominance") 
				 * in the LOCAL language, so you have to match with translated version
				 */
				boolean interactionTypeCorrect = false;
				String studentDomTypeText = modelPane.getInteractionTypeChoice();
				if (geneModel.getDomTypeText().equals("Simple") 
						&& studentDomTypeText.equals(Messages.getInstance().getString("VGLII.SimpleDominance"))) interactionTypeCorrect = true;
				if (geneModel.getDomTypeText().equals("Circular")
						&& studentDomTypeText.equals(Messages.getInstance().getString("VGLII.CircularDominance"))) interactionTypeCorrect = true;
				if (geneModel.getDomTypeText().equals("Hierarchical")
						&& studentDomTypeText.equals(Messages.getInstance().getString("VGLII.HierarchicalDominance"))) interactionTypeCorrect = true;
				if (geneModel.getDomTypeText().equals("Incomplete")
						&& studentDomTypeText.equals(Messages.getInstance().getString("VGLII.IncompleteDominance"))) interactionTypeCorrect = true;
				Element itEl = new Element("InteractionType");
				itEl.addContent((new Element("Correct")).addContent(String.valueOf(interactionTypeCorrect)));
				geneEl.addContent(itEl);

				/*
				 * these are also the raw selected strings
				 * in the local language, so need to match with translated version
				 * 
				 * first, see if type is OK
				 * 	if not, then the details can't be right
				 */
				boolean detailsCorrect = true;
				if (!interactionTypeCorrect) {
					detailsCorrect = false;
				} else {
					ModelDetailsPanel mdp = modelPane.getModelDetailsPanel();
					// check each entry; if any mismatch, it's wrong
					if ((mdp.t1Choices != null) && (geneModel.t1 != null)) {
						if (!mdp.t1Choices.getSelectedItem().equals(
								Messages.getInstance().getTranslatedShortTraitName(geneModel.t1.getTraitName()))) detailsCorrect = false;
					}

					if ((mdp.t2Choices != null) && (geneModel.t2 != null)) {
						if (!mdp.t2Choices.getSelectedItem().equals(
								Messages.getInstance().getTranslatedShortTraitName(geneModel.t2.getTraitName()))) detailsCorrect = false;
					}

					if ((mdp.t3Choices != null) && (geneModel.t3 != null)) {
						if (!mdp.t3Choices.getSelectedItem().equals(
								Messages.getInstance().getTranslatedShortTraitName(geneModel.t3.getTraitName()))) detailsCorrect = false;
					}

					if ((mdp.t4Choices != null) && (geneModel.t4 != null)) {
						if (!mdp.t4Choices.getSelectedItem().equals(
								Messages.getInstance().getTranslatedShortTraitName(geneModel.t4.getTraitName()))) detailsCorrect = false;
					}

					if ((mdp.t5Choices != null) && (geneModel.t5 != null)) {
						if (!mdp.t5Choices.getSelectedItem().equals(
								Messages.getInstance().getTranslatedShortTraitName(geneModel.t5.getTraitName()))) detailsCorrect = false;
					}

					if ((mdp.t6Choices != null) && (geneModel.t6 != null)) {
						if (!mdp.t6Choices.getSelectedItem().equals(
								Messages.getInstance().getTranslatedShortTraitName(geneModel.t6.getTraitName()))) detailsCorrect = false;
					}
				}
				Element idEl = new Element("InteractionDetails");
				idEl.addContent((new Element("Correct")).addContent(String.valueOf(detailsCorrect)));
				geneEl.addContent(idEl);

				e.addContent(geneEl);
			}

		} else {
			/*
			 * it's epistasis or complementation, so it must be treated differently
			 * there will only be one trait but two genes
			 * (technically, it is possible to have a third gene in these problems
			 *   but we have not made any problem types with that option; if we did this
			 *   the grading would break)
			 *   
			 */
			Element gmEl = new Element("Character");

			gmEl.addContent((new Element("Name")).addContent(gm.getPhenoTypeProcessor().getCharacter()));

			boolean interactionTypeGrade = false;
			ModelPane mp = mbui.getModelPanes()[0]; // only one model pane in these problems
			if (gm.getPhenoTypeProcessor().getInteractionType() == PhenotypeProcessor.COMPLEMENTATION) {
				if (mp.getInteractionTypeChoice().equals(Messages.getInstance().getString("VGLII.Complementation"))) interactionTypeGrade = false;
			} else {
				if (mp.getInteractionTypeChoice().equals(Messages.getInstance().getString("VGLII.Epistasis"))) interactionTypeGrade = false;
			}
			Element itEl = new Element("InteractionType");
			itEl.addContent((new Element("Correct")).addContent(String.valueOf(interactionTypeGrade)));
			gmEl.addContent(itEl);

			/*
			 * these are also the raw selected strings
			 * in the local language, so need to match with translated version
			 * 
			 * but, if the type is wrong, the details CAN'T be right
			 */
			boolean detailsCorrect = true;
			if (!interactionTypeGrade) {
				detailsCorrect = false;
			} else {
				ModelDetailsPanel mdp = mp.getModelDetailsPanel();
				// check each entry; if any mismatch, it's wrong
				if ((mdp.t1Choices != null) && (gm.getPhenoTypeProcessor().getT1() != null)) {
					if (!mdp.t1Choices.getSelectedItem().equals(
							Messages.getInstance().getTranslatedShortTraitName(gm.getPhenoTypeProcessor().getT1().getTraitName()))) detailsCorrect = false;
				}

				if ((mdp.t2Choices != null) && (gm.getPhenoTypeProcessor().getT2() != null)) {
					if (!mdp.t2Choices.getSelectedItem().equals(
							Messages.getInstance().getTranslatedShortTraitName(gm.getPhenoTypeProcessor().getT2().getTraitName()))) detailsCorrect = false;
				}

				if ((mdp.t3Choices != null) && (gm.getPhenoTypeProcessor().getT3() != null)) {
					if (!mdp.t3Choices.getSelectedItem().equals(
							Messages.getInstance().getTranslatedShortTraitName(gm.getPhenoTypeProcessor().getT3().getTraitName()))) detailsCorrect = false;
				}
			}
			Element dIt = new Element("InteractionDetails");
			dIt.addContent((new Element("Correct")).addContent(String.valueOf(detailsCorrect)));
			gmEl.addContent(dIt);

			e.addContent(gmEl);
		}

		/*
		 * linkage, if present
		 */
		if (mbui.getLinkagePanel() != null) {
			/*
			 * find data for each gene pair
			 *  12, 23, 13
			 * encode like this:
			 * 	rf = 0.5 => unlinked
			 *  rf < 0.5 => linked with given rf
			 */
			double rf12 = 0.5f;
			double rf23 = 0.5f;
			double rf13 = 0.5f;

			// see if both on same chromosome
			// always have 2 genes here
			if (gm.isGeneModelSexLinkedByIndex(0) && gm.isGeneModelSexLinkedByIndex(1)) {
				rf12 = gm.getSexChromosomeModel().getRecombinationFrequencies().get(0);
			}
			if (!gm.isGeneModelSexLinkedByIndex(0) && !gm.isGeneModelSexLinkedByIndex(1)) {
				rf12 = gm.getAutosomeModel().getRecombinationFrequencies().get(0);
			}

			// see if you need to check the other two
			if (gm.getNumberOfGeneModels() == 3) {
				// do gene 2 and 3
				if (gm.isGeneModelSexLinkedByIndex(1) && gm.isGeneModelSexLinkedByIndex(2)) {
					rf23 = gm.getSexChromosomeModel().getRecombinationFrequencies().get(1);
				}
				if (!gm.isGeneModelSexLinkedByIndex(1) && !gm.isGeneModelSexLinkedByIndex(2)) {
					rf23 = gm.getAutosomeModel().getRecombinationFrequencies().get(1);
				}

				/*
				 * now gene 1 and 3
				 *   this depends on if 1 and 2 are linked
				 *     if they're not, it's the first rf in the list
				 *     otherwise, you have to calculate it using the Kosambi formula
				 *       for adding rfs
				 */
				if (gm.isGeneModelSexLinkedByIndex(1) && gm.isGeneModelSexLinkedByIndex(2)) {
					if (rf12 == 0.5f) {
						rf13 = gm.getSexChromosomeModel().getRecombinationFrequencies().get(0);
					} else {
						rf13 = 0.5 * Math.tanh(2 *(rf12 + rf23));
					}
				}
				if (!gm.isGeneModelSexLinkedByIndex(1) && !gm.isGeneModelSexLinkedByIndex(2)) {
					if (rf12 == 0.5f) {
						rf13 = gm.getAutosomeModel().getRecombinationFrequencies().get(0);
					} else {
						rf13 = 0.5 * Math.tanh(2 *(rf12 + rf23));

					}
				}

			}

			// now, see if they're right
			boolean linkageCorrect = true;
			// always have 1-2
			double student_rf12 = mbui.getLinkagePanel().getG1G2LinkageChoice();
			if (Math.abs(rf12 - student_rf12) > ERROR_TOLERANCE) linkageCorrect = false;

			if (gm.getNumberOfGeneModels() == 3) {
				double student_rf23 = mbui.getLinkagePanel().getG2G3LinkageChoice();
				double student_rf13 = mbui.getLinkagePanel().getG1G3LinkageChoice();
				if (Math.abs(rf23 - student_rf23) > ERROR_TOLERANCE) linkageCorrect = false;
				if (Math.abs(rf13 - student_rf13) > ERROR_TOLERANCE) linkageCorrect = false;				
			}
			Element le = new Element("Linkage");
			le.addContent((new Element("Correct")).addContent(String.valueOf(linkageCorrect)));
			e.addContent(le);
		}

		// don't forget cage scoring

		return e;
	}

}
