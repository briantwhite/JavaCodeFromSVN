package edu.umb.jsVGL.client.Grader;

import java.util.ArrayList;
import java.util.Iterator;

import org.jdom.Element;

import GeneticModels.Cage;
import GeneticModels.GeneModel;
import GeneticModels.GeneticModel;
import GeneticModels.PhenotypeProcessor;
import ModelBuilder.ModelBuilderUI;
import ModelBuilder.ModelDetailsPanel;
import ModelBuilder.ModelPane;
import VGL.CageUI;
import VGL.Messages;

/*
 * Compares correct answer (GeneticModel) with student answer (ModelBuilderUI)
 * returns info for direct on-line grading
 * = Success or failure in matching - not details of their answer
 * this is meant to be machine readable, not human readable
 */
public class AutoGrader {

	// allowable difference between
	private static final float ERROR_TOLERANCE = 0.1f;

	public static Element grade(ArrayList<CageUI> cageCollection, GeneticModel gm, ModelBuilderUI mbui) {

		// set up to score cages
		Iterator<CageUI> it = cageCollection.iterator();
		ArrayList<Cage> cages = new ArrayList<Cage>();
		while (it.hasNext()) {
			CageUI cui = it.next();
			Cage c = cui.getCage();
			cages.add(c);
		}
		CageScorer cageScorer = new CageScorer(cages, mbui);


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

				/*
				 * if this is null, it means that there was no choice with this
				 * menu - that is, it was certain to be sex-linked or autosomal
				 * so it's not appropriate to give a grade here
				 */
				if (modelPane.getSexLinkageChoice() != null) {
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

					CageScoreResult slCsr = cageScorer.scoreCage(modelPane.getSexLinkageCageChoice());
					if (slCsr == null) {
						slEl.addContent((
								new Element("Justified")).addContent(
										String.valueOf(false)));
					} else {
						// sex-linkage choice must match correct type
						if(gm.isGeneModelSexLinkedByIndex(i)) {
							// sex-linked, so cage must show sex-linkage
							slEl.addContent((
									new Element("Justified")).addContent(
											String.valueOf(slCsr.getCageScoreForCharacter(i).showsSexLinkage)));
						} else {
							// not sex-linked, so cage must not show sex-linkage
							slEl.addContent((
									new Element("Justified")).addContent(
											String.valueOf(!slCsr.getCageScoreForCharacter(i).showsSexLinkage)));
						}
					}
					geneEl.addContent(slEl);
				}

				/*
				 * number of alleles
				 * 
				 * first see if there was really a choice for the student here
				 */
				boolean numAllelesCorrect = true;
				// see if they really had a choice or not
				if (modelPane.getAlleleNumberChoice() != 0) {
					numAllelesCorrect = (modelPane.getAlleleNumberChoice() == geneModel.getNumAlleles());
					Element naEl = new Element("NumberOfAlleles");
					naEl.addContent((
							new Element("Correct")).addContent(String.valueOf(numAllelesCorrect)));
					geneEl.addContent(naEl);
				}


				/*
				 * these are the raw selected strings (eg "Simple dominance") 
				 * in the LOCAL language, so you have to match with translated version
				 * 
				 * also, can't get type right if number of alleles is wrong
				 */
				boolean interactionTypeCorrect = false;
				if (numAllelesCorrect) {
					if (modelPane.getInteractionTypeChoice() != null) {
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
					} else {
						interactionTypeCorrect = true;	// if they didn't have to enter it, it's OK 
					}
				}

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
					/*
					 */
					ModelDetailsPanel mdp = modelPane.getModelDetailsPanel();
					/*
					 * first, deal with special cases
					 * 	in inc dom, the homozygote traits are exchangable
					 * 		(they don't have to match exactly, as long as the pair is right)
					 * 		2-allele: t1 and t3 can be swapped as long as both are right
					 * 		3-allele: 3 circular permutations are all OK
					 * in circ dom, the circular permutations are all ok
					 * 
					 */				
					if ((geneModel.getNumAlleles() == 2) && (geneModel.getDomTypeText().equals("Incomplete"))) {
						detailsCorrect = false;
						// check to be sure they've instantiated the choices
						if (mdp.t1Choices != null) {
							// check het pheno first - if it's wrong, give up
							if (mdp.t3Choices.getSelectedItem().equals(
									Messages.getInstance().getTranslatedShortTraitName(geneModel.t3.getTraitName()))) {

								// try either permutation of the homozygotes
								// direct match
								if (
										(mdp.t1Choices.getSelectedItem().equals(
												Messages.getInstance().getTranslatedShortTraitName(geneModel.t1.getTraitName()))) &&
												(mdp.t2Choices.getSelectedItem().equals(
														Messages.getInstance().getTranslatedShortTraitName(geneModel.t2.getTraitName()))) 
								){
									detailsCorrect = true;
								}
								// swapped
								if (
										(mdp.t1Choices.getSelectedItem().equals(
												Messages.getInstance().getTranslatedShortTraitName(geneModel.t2.getTraitName()))) &&
												(mdp.t2Choices.getSelectedItem().equals(
														Messages.getInstance().getTranslatedShortTraitName(geneModel.t1.getTraitName()))) 
								){
									detailsCorrect = true;
								}
							} 
						}

					} else if ((geneModel.getNumAlleles() == 3) && (geneModel.getDomTypeText().equals("Incomplete"))) {
						detailsCorrect = false;
						/* only 3 possibilities to be correct
						 * 	normal slot		alternatives
						 *		1			1	3	2
						 *		2			2	1	3
						 *		3			3	2	1
						 *		4			4	6	5
						 *		5			5	4	6
						 *		6			6	5	4
						 * use the one found at slot 1 to decide which case it is, then go into detail
						 */
						// check to be sure they've instantiated the choices
						if (mdp.t1Choices != null) {
							// then, see which permutation it is
							if (mdp.t1Choices.getSelectedItem().equals(
									Messages.getInstance().getTranslatedShortTraitName(geneModel.t1.getTraitName()))) {
								// first alternative; provisionally right unless a mismatch
								detailsCorrect = true;
								if (!mdp.t2Choices.getSelectedItem().equals(
										Messages.getInstance().getTranslatedShortTraitName(geneModel.t2.getTraitName()))) detailsCorrect = false;
								if (!mdp.t3Choices.getSelectedItem().equals(
										Messages.getInstance().getTranslatedShortTraitName(geneModel.t3.getTraitName()))) detailsCorrect = false;
								if (!mdp.t4Choices.getSelectedItem().equals(
										Messages.getInstance().getTranslatedShortTraitName(geneModel.t4.getTraitName()))) detailsCorrect = false;
								if (!mdp.t5Choices.getSelectedItem().equals(
										Messages.getInstance().getTranslatedShortTraitName(geneModel.t5.getTraitName()))) detailsCorrect = false;
								if (!mdp.t6Choices.getSelectedItem().equals(
										Messages.getInstance().getTranslatedShortTraitName(geneModel.t6.getTraitName()))) detailsCorrect = false;

							} else if (mdp.t1Choices.getSelectedItem().equals(
									Messages.getInstance().getTranslatedShortTraitName(geneModel.t3.getTraitName()))){
								// second alternative; provisionally right unless a mismatch
								detailsCorrect = true;
								if (!mdp.t2Choices.getSelectedItem().equals(
										Messages.getInstance().getTranslatedShortTraitName(geneModel.t1.getTraitName()))) detailsCorrect = false;
								if (!mdp.t3Choices.getSelectedItem().equals(
										Messages.getInstance().getTranslatedShortTraitName(geneModel.t2.getTraitName()))) detailsCorrect = false;
								if (!mdp.t4Choices.getSelectedItem().equals(
										Messages.getInstance().getTranslatedShortTraitName(geneModel.t6.getTraitName()))) detailsCorrect = false;
								if (!mdp.t5Choices.getSelectedItem().equals(
										Messages.getInstance().getTranslatedShortTraitName(geneModel.t4.getTraitName()))) detailsCorrect = false;
								if (!mdp.t6Choices.getSelectedItem().equals(
										Messages.getInstance().getTranslatedShortTraitName(geneModel.t5.getTraitName()))) detailsCorrect = false;

							} else if (mdp.t1Choices.getSelectedItem().equals(
									Messages.getInstance().getTranslatedShortTraitName(geneModel.t2.getTraitName()))) {
								// third alternative; provisionally right unless a mismatch
								detailsCorrect = true;
								if (!mdp.t2Choices.getSelectedItem().equals(
										Messages.getInstance().getTranslatedShortTraitName(geneModel.t3.getTraitName()))) detailsCorrect = false;
								if (!mdp.t3Choices.getSelectedItem().equals(
										Messages.getInstance().getTranslatedShortTraitName(geneModel.t1.getTraitName()))) detailsCorrect = false;
								if (!mdp.t4Choices.getSelectedItem().equals(
										Messages.getInstance().getTranslatedShortTraitName(geneModel.t5.getTraitName()))) detailsCorrect = false;
								if (!mdp.t5Choices.getSelectedItem().equals(
										Messages.getInstance().getTranslatedShortTraitName(geneModel.t6.getTraitName()))) detailsCorrect = false;
								if (!mdp.t6Choices.getSelectedItem().equals(
										Messages.getInstance().getTranslatedShortTraitName(geneModel.t4.getTraitName()))) detailsCorrect = false;
							}
							// none of the alternatives, so details aren't correct
						}
					} else if ((geneModel.getNumAlleles() == 3) && (geneModel.getDomTypeText().equals("Circular"))) {
						detailsCorrect = false;
						/*
						 * 		normal slot		alternatives
						 *			1			1	3	2
						 *			2			2	1	3
						 *			3			3	2	1
						 */
						// check to be sure they've instantiated the choices
						if (mdp.t1Choices != null) {
							// then, see which permutation it is
							if (mdp.t1Choices.getSelectedItem().equals(
									Messages.getInstance().getTranslatedShortTraitName(geneModel.t1.getTraitName()))) {
								// first alternative; provisionally right unless a mismatch
								detailsCorrect = true;
								if (!mdp.t2Choices.getSelectedItem().equals(
										Messages.getInstance().getTranslatedShortTraitName(geneModel.t2.getTraitName()))) detailsCorrect = false;
								if (!mdp.t3Choices.getSelectedItem().equals(
										Messages.getInstance().getTranslatedShortTraitName(geneModel.t3.getTraitName()))) detailsCorrect = false;

							} else if (mdp.t1Choices.getSelectedItem().equals(
									Messages.getInstance().getTranslatedShortTraitName(geneModel.t3.getTraitName()))){
								// second alternative; provisionally right unless a mismatch
								detailsCorrect = true;
								if (!mdp.t2Choices.getSelectedItem().equals(
										Messages.getInstance().getTranslatedShortTraitName(geneModel.t1.getTraitName()))) detailsCorrect = false;
								if (!mdp.t3Choices.getSelectedItem().equals(
										Messages.getInstance().getTranslatedShortTraitName(geneModel.t2.getTraitName()))) detailsCorrect = false;

							} else if (mdp.t1Choices.getSelectedItem().equals(
									Messages.getInstance().getTranslatedShortTraitName(geneModel.t2.getTraitName()))) {
								// third alternative; provisionally right unless a mismatch
								detailsCorrect = true;
								if (!mdp.t2Choices.getSelectedItem().equals(
										Messages.getInstance().getTranslatedShortTraitName(geneModel.t3.getTraitName()))) detailsCorrect = false;
								if (!mdp.t3Choices.getSelectedItem().equals(
										Messages.getInstance().getTranslatedShortTraitName(geneModel.t1.getTraitName()))) detailsCorrect = false;

							}
							// none of the alternatives, so details aren't correct
						}
					} else {
						/*
						 * it's not one of the exceptions with multiple possibilities
						 * there's only one right answer here, so:
						 * check each entry; if any mismatch, it's wrong
						 */
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
				}
				Element idEl = new Element("InteractionDetails");
				idEl.addContent((new Element("Correct")).addContent(String.valueOf(detailsCorrect)));
				int interactionCageChoice = modelPane.getInteractionCageChoice();
				CageScoreResult inCsr = cageScorer.scoreCage(interactionCageChoice);
				if (inCsr == null) {
					idEl.addContent((
							new Element("Justified")).addContent(
									String.valueOf(false)));
				} else {
					idEl.addContent((
							new Element("Justified")).addContent(
									String.valueOf(inCsr.getCageScoreForCharacter(i).showsInteraction)));
				}
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
				if (mp.getInteractionTypeChoice().equals(Messages.getInstance().getString("VGLII.Complementation"))) interactionTypeGrade = true;
			} else {
				if (mp.getInteractionTypeChoice().equals(Messages.getInstance().getString("VGLII.Epistasis"))) interactionTypeGrade = true;
			}
			Element itEl = new Element("InteractionType");
			itEl.addContent((new Element("Correct")).addContent(String.valueOf(interactionTypeGrade)));
			CageScoreResult inCsr = cageScorer.scoreCage(mp.getInteractionCageChoice());
			if (inCsr == null) {
				itEl.addContent((
						new Element("Justified")).addContent(
								String.valueOf(false)));
			} else {
				itEl.addContent((
						new Element("Justified")).addContent(
								String.valueOf(inCsr.getCageScoreForCharacter(0).showsInteraction)));
			}
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
			double totalError = 0.0f;
			double maxError = Double.MIN_VALUE;
			// always have 1-2
			double student_rf12 = mbui.getLinkagePanel().getG1G2LinkageChoice();
			if (student_rf12 != -1.0f) {
				double error12 = Math.abs(rf12 - student_rf12);
				totalError = totalError + error12;
				if (error12 > maxError) maxError = error12;
				if (error12 > ERROR_TOLERANCE) linkageCorrect = false;
			} else {
				linkageCorrect = false;
			}

			if (gm.getNumberOfGeneModels() == 3) {
				double student_rf23 = mbui.getLinkagePanel().getG2G3LinkageChoice();
				if (student_rf23 != -1.0f) {
					double error23 = Math.abs(rf23 - student_rf23);
					totalError = totalError + error23;
					if (error23 > maxError) maxError = error23;
					if (error23 > ERROR_TOLERANCE) linkageCorrect = false;
				} else {
					linkageCorrect = false;
				}

				double student_rf13 = mbui.getLinkagePanel().getG1G3LinkageChoice();
				if (student_rf13 != -1.0f) {
					double error13 = Math.abs(rf13 - student_rf13);
					totalError = totalError + error13;
					if (error13 > maxError) maxError = error13;
					if (error13 > ERROR_TOLERANCE) linkageCorrect = false;				
				} else {
					linkageCorrect = false;
				}
			}
			Element le = new Element("Linkage");
			le.addContent((new Element("Correct")).addContent(String.valueOf(linkageCorrect)));
			double averageError = totalError/gm.getNumberOfGeneModels();
			le.addContent((new Element("AverageError")).addContent(String.format("%4.3f", averageError)));
			le.addContent((new Element("MaxError")).addContent(String.format("%4.3f", maxError)));

			// see if justified by right cages
			boolean linkageJustified = true;
			/*
			 * always do 1-2
			 *   use a negative test - if any fail, then fail overall
			 */
			CageScoreResult g1g2LinkageCageChoiceResult = cageScorer.scoreCage(mbui.getLinkagePanel().getG1G2LinkageRelevantCage());
			if (g1g2LinkageCageChoiceResult == null) {
				linkageJustified = false;  // they selected "?"
			} else {
				if (!g1g2LinkageCageChoiceResult.getCageScoreForCharacter(0).capableOfShowingLinkage || 
						!g1g2LinkageCageChoiceResult.getCageScoreForCharacter(1).capableOfShowingLinkage) linkageJustified = false;
			}

			// if needed, then do 2-3 and 1-3
			if (gm.getNumberOfGeneModels() == 3) {
				CageScoreResult g2g3LinkageCageChoiceResult = cageScorer.scoreCage(mbui.getLinkagePanel().getG2G3LinkageRelevantCage());
				if (g2g3LinkageCageChoiceResult == null) {
					linkageJustified = false;  // they selected "?"
				} else {
					if (!g2g3LinkageCageChoiceResult.getCageScoreForCharacter(1).capableOfShowingLinkage || 
							!g2g3LinkageCageChoiceResult.getCageScoreForCharacter(2).capableOfShowingLinkage) linkageJustified = false;
				}

				CageScoreResult g1g3LinkageCageChoiceResult = cageScorer.scoreCage(mbui.getLinkagePanel().getG1G3LinkageRelevantCage());
				if (g1g3LinkageCageChoiceResult == null) {
					linkageJustified = false;  // they selected "?"
				} else {
					if (!g1g3LinkageCageChoiceResult.getCageScoreForCharacter(0).capableOfShowingLinkage || 
							!g1g3LinkageCageChoiceResult.getCageScoreForCharacter(2).capableOfShowingLinkage) linkageJustified = false;
				}
			}	

			le.addContent((new Element("Justified")).addContent(String.valueOf(linkageJustified)));
			e.addContent(le);
		}
		return e;
	}
}
