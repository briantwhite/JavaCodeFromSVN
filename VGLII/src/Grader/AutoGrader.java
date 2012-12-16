package Grader;

import javax.swing.JComboBox;

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

	public static final String CORRECT = "CORRECT";
	public static final String INCORRECT = "INCORRECT";
	
	// allowable difference between
	private static final float ERROR_TOLERANCE = 0.2f;

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
				Element gmEl = new Element("Gene");

				gmEl.setAttribute("Index", String.valueOf(i));

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

				/*
				 * these are also the raw selected strings
				 * in the local language, so need to match with translated version
				 */
				String detailsGrade = CORRECT;
				ModelDetailsPanel mdp = modelPane.getModelDetailsPanel();
				// check each entry; if any mismatch, it's wrong
				if ((mdp.t1Choices != null) && (geneModel.t1 != null)) {
					if (!mdp.t1Choices.getSelectedItem().equals(
							Messages.getInstance().getTranslatedShortTraitName(geneModel.t1.getTraitName()))) detailsGrade = INCORRECT;
				}

				if ((mdp.t2Choices != null) && (geneModel.t2 != null)) {
					if (!mdp.t2Choices.getSelectedItem().equals(
							Messages.getInstance().getTranslatedShortTraitName(geneModel.t2.getTraitName()))) detailsGrade = INCORRECT;
				}

				if ((mdp.t3Choices != null) && (geneModel.t3 != null)) {
					if (!mdp.t3Choices.getSelectedItem().equals(
							Messages.getInstance().getTranslatedShortTraitName(geneModel.t3.getTraitName()))) detailsGrade = INCORRECT;
				}

				if ((mdp.t4Choices != null) && (geneModel.t4 != null)) {
					if (!mdp.t4Choices.getSelectedItem().equals(
							Messages.getInstance().getTranslatedShortTraitName(geneModel.t4.getTraitName()))) detailsGrade = INCORRECT;
				}

				if ((mdp.t5Choices != null) && (geneModel.t5 != null)) {
					if (!mdp.t5Choices.getSelectedItem().equals(
							Messages.getInstance().getTranslatedShortTraitName(geneModel.t5.getTraitName()))) detailsGrade = INCORRECT;
				}

				if ((mdp.t6Choices != null) && (geneModel.t6 != null)) {
					if (!mdp.t6Choices.getSelectedItem().equals(
							Messages.getInstance().getTranslatedShortTraitName(geneModel.t6.getTraitName()))) detailsGrade = INCORRECT;
				}

				gmEl.setAttribute("InteractionDetails", detailsGrade);

				e.addContent(gmEl);
			}

		} else {
			/*
			 * it's epistasis or complementation, so it must be treated differently
			 * there will only be one trait but two genes
			 * (technically, it is possible to have a third gene in these problems
			 *   but we have not made any problem types with that option; if we did this
			 *   the grading would break)
			 */
			Element gmEl = new Element("Character");

			gmEl.setAttribute("Name", gm.getPhenoTypeProcessor().getCharacter());

			String interactionTypeGrade = INCORRECT;
			ModelPane mp = mbui.getModelPanes()[0]; // only one model pane in these problems
			if (gm.getPhenoTypeProcessor().getInteractionType() == PhenotypeProcessor.COMPLEMENTATION) {
				if (mp.getInteractionTypeChoice().equals(Messages.getInstance().getString("VGLII.Complementation"))) interactionTypeGrade = CORRECT;
			} else {
				if (mp.getInteractionTypeChoice().equals(Messages.getInstance().getString("VGLII.Epistasis"))) interactionTypeGrade = CORRECT;
			}
			gmEl.setAttribute("InteractionType", interactionTypeGrade);

			/*
			 * these are also the raw selected strings
			 * in the local language, so need to match with translated version
			 */
			String detailsGrade = CORRECT;
			ModelDetailsPanel mdp = mp.getModelDetailsPanel();
			// check each entry; if any mismatch, it's wrong
			if ((mdp.t1Choices != null) && (gm.getPhenoTypeProcessor().getT1() != null)) {
				if (!mdp.t1Choices.getSelectedItem().equals(
						Messages.getInstance().getTranslatedShortTraitName(gm.getPhenoTypeProcessor().getT1().getTraitName()))) detailsGrade = INCORRECT;
			}

			if ((mdp.t2Choices != null) && (gm.getPhenoTypeProcessor().getT2() != null)) {
				if (!mdp.t2Choices.getSelectedItem().equals(
						Messages.getInstance().getTranslatedShortTraitName(gm.getPhenoTypeProcessor().getT2().getTraitName()))) detailsGrade = INCORRECT;
			}

			if ((mdp.t3Choices != null) && (gm.getPhenoTypeProcessor().getT3() != null)) {
				if (!mdp.t3Choices.getSelectedItem().equals(
						Messages.getInstance().getTranslatedShortTraitName(gm.getPhenoTypeProcessor().getT3().getTraitName()))) detailsGrade = INCORRECT;
			}

			gmEl.setAttribute("InteractionDetails", detailsGrade);

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
			float rf12 = 0.5f;
			float rf23 = 0.5f;
			float rf13 = 0.5f;

			// see if both on same chromosome
			// always have 2 genes here
			if (gm.isGeneModelSexLinkedByIndex(0) && gm.isGeneModelSexLinkedByIndex(1)) {
				rf12 = gm.getSexChromosomeModel().getRecombinationFrequencies().get(0);
			}
			if (!gm.isGeneModelSexLinkedByIndex(0) && gm.isGeneModelSexLinkedByIndex(1)) {
				rf12 = gm.getAutosomeModel().getRecombinationFrequencies().get(0);
			}

			// see if you need to check the other two
			if (gm.getNumberOfGeneModels() == 3) {
				// do gene 2 and 3
				if (gm.isGeneModelSexLinkedByIndex(1) && gm.isGeneModelSexLinkedByIndex(2)) {
					rf23 = gm.getSexChromosomeModel().getRecombinationFrequencies().get(1);
				}
				if (!gm.isGeneModelSexLinkedByIndex(1) && gm.isGeneModelSexLinkedByIndex(2)) {
					rf23 = gm.getAutosomeModel().getRecombinationFrequencies().get(1);
				}

				/*
				 * now gene 1 and 3
				 *   this depends on if 1 and 2 are linked
				 *     if they're not, it's the first rf in the list
				 *     otherwise, it's the second
				 */
				if (gm.isGeneModelSexLinkedByIndex(1) && gm.isGeneModelSexLinkedByIndex(2)) {
					if (rf12 == 0.5f) {
						rf13 = gm.getSexChromosomeModel().getRecombinationFrequencies().get(1);
					} else {
						rf13 = gm.getSexChromosomeModel().getRecombinationFrequencies().get(0);
					}
				}
				if (!gm.isGeneModelSexLinkedByIndex(1) && gm.isGeneModelSexLinkedByIndex(2)) {
					if (rf12 == 0.5f) {
						rf13 = gm.getAutosomeModel().getRecombinationFrequencies().get(1);
					} else {
						rf13 = gm.getAutosomeModel().getRecombinationFrequencies().get(0);
					}
				}

			}
			
			// now, see if they're right
			String linkageGrade = CORRECT;
			// always have 1-2
			if (Math.abs(rf12 - mbui.getLinkagePanel().getG1G2LinkageChoice()) > ERROR_TOLERANCE) linkageGrade = INCORRECT;
			
			if (gm.getNumberOfGeneModels() == 3) {
				if (Math.abs(rf23 - mbui.getLinkagePanel().getG2G3LinkageChoice()) > ERROR_TOLERANCE) linkageGrade = INCORRECT;
				if (Math.abs(rf13 - mbui.getLinkagePanel().getG1G3LinkageChoice()) > ERROR_TOLERANCE) linkageGrade = INCORRECT;				
			}
			Element le = new Element("Linkage");
			le.setAttribute("Linkage", linkageGrade);
			e.addContent(le);
		}

		// don't forget cage scoring

		return e;
	}
	
}
