package utilities;

import java.awt.Dimension;
import java.util.Random;

import javax.swing.ImageIcon;

import molBiol.ExpressedGene;
import molBiol.Gene;
import molGenExp.ProteinImageFactory;
import molGenExp.ProteinImageSet;
import preferences.MGEPreferences;
import biochem.Attributes;
import biochem.FoldedPolypeptide;
import biochem.FoldingException;
import biochem.FoldingManager;
import biochem.OutputPalette;

public class Mutator {
	
	private MGEPreferences preferences;
	
	protected Mutator() {
		preferences = MGEPreferences.getInstance();
	}
	
	protected ExpressedGene mutateGene(ExpressedGene eg) {
		//change one base in the DNA
		Gene gene = eg.getGene();

		if (gene.getDNASequenceLength() == 0) {
			return eg;
		}
		Gene newGene = 
			new Gene(mutateDNASequence(gene.getDNASequence()), 
					GlobalDefaults.molBiolParams);
		newGene.transcribe();
		newGene.process();
		newGene.translate();
		String html = newGene.generateHTML(0);

		String proteinSequence = newGene.getProteinString();

		if (proteinSequence.indexOf("none") != -1) {
			proteinSequence = "";
		} else {
			//remove leading/trailing spaces and the N- and C-
			proteinSequence = 
				proteinSequence.replaceAll(" ", "");
			proteinSequence = 
				proteinSequence.replaceAll("N-", "");
			proteinSequence = 
				proteinSequence.replaceAll("-C", "");

			//insert spaces between amino acid codes
			StringBuffer psBuffer = new StringBuffer(proteinSequence);
			for (int i = 3; i < psBuffer.length(); i = i + 4) {
				psBuffer = psBuffer.insert(i, " ");
			}
			proteinSequence = psBuffer.toString();
		}

		//fold it
		Attributes attributes = new Attributes(
				proteinSequence, 
				3,
		"straight");
		FoldingManager manager = FoldingManager.getInstance();
		try {
			manager.fold(attributes);
		} catch (FoldingException e) {
			e.printStackTrace();
		}

		//make an icon and display it in a dialog
		OutputPalette op = new OutputPalette();
		manager.createCanvas(op);
		Dimension requiredCanvasSize = 
			op.getDrawingPane().getRequiredCanvasSize();

		ProteinImageSet images = 
			ProteinImageFactory.generateImages(op, requiredCanvasSize);

		FoldedPolypeptide fp = new FoldedPolypeptide(
				proteinSequence,
				op.getDrawingPane().getGrid(), 
				new ImageIcon(images.getThumbnailImage()), 
				op.getProteinColor());

		ExpressedGene newEg = new ExpressedGene(html, newGene);
		newEg.setFoldedPolypeptide(fp);

		images = null;

		return newEg;
	}

	protected String mutateDNASequence(String DNASequence) {
		preferences = MGEPreferences.getInstance();
		Random r = new Random();

		StringBuffer DNABuffer = new StringBuffer(DNASequence);

		//mutation: pointMutationRate chance of changing each base
		if (preferences.getPointMutationRate() != 0) {
			int pointOdds = Math.round(1/preferences.getPointMutationRate());
			for (int i = 0; i < DNABuffer.length(); i++) {
				if (r.nextInt(pointOdds) == 0) {
					int base = r.nextInt(4);
					String newBase = "AGCT".substring(base, base + 1);
					DNABuffer = DNABuffer.replace(i, i + 1, newBase);
				}
			}
		}

		//deletion mutations
		if (preferences.getDeletionMutationRate() != 0) {
			int delOdds = Math.round(1/preferences.getDeletionMutationRate());
			for (int i = 0; i < DNABuffer.length(); i++) {
				if (r.nextInt(delOdds) == 0) {
					DNABuffer = DNABuffer.deleteCharAt(i);
				}
			}
		}

		//insertion mutations
		if (preferences.getInsertionMutationRate() != 0) {
			int insOdds = Math.round(1/preferences.getInsertionMutationRate());
			for (int i = 0; i < DNABuffer.length(); i++) {
				if (r.nextInt(insOdds) == 0) {
					int base = r.nextInt(4);
					String newBase = "AGCT".substring(base, base + 1);
					DNABuffer = DNABuffer.insert(i, newBase);
				}
			}
		}

		return DNABuffer.toString();
	}


}
