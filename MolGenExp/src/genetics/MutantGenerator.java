package genetics;

import java.awt.Dimension;
import java.util.Random;

import javax.swing.ImageIcon;

import biochem.Attributes;
import biochem.FoldedPolypeptide;
import biochem.FoldingException;
import biochem.FoldingManager;
import biochem.OutputPalette;

import molBiol.ExpressedGene;
import molBiol.Gene;
import molGenExp.Organism;
import molGenExp.ProteinImageFactory;
import molGenExp.ProteinImageSet;
import molGenExp.RYBColorModel;

public class MutantGenerator implements Runnable {

	private int mutantCount;	//number of mutants to make
	private int current;
	private Organism o;
	private int trayNum;
	private GeneticsWorkbench gw;
	private OffspringList offspringList;

	MutantGenerator (Organism o,
			int mutantCount,
			int trayNum,
			OffspringList offspringList, 
			GeneticsWorkbench gw) {
		this.o = o;
		this.trayNum = trayNum;
		this.gw = gw;
		this.offspringList = offspringList;
		this.mutantCount = mutantCount;
	}

	public int getLengthOfTask() {
		return mutantCount;
	}

	public int getCurrent() {
		return current;
	}

	public Organism getOrganism() {
		return o;
	}

	public void stop() {
		current = mutantCount;
	}

	boolean done() {
		if (current >= mutantCount) {
			return true;
		} else {
			return false;
		}
	}

	public void run() {
		for (current = 0; current < mutantCount; current++) {
			ExpressedGene eg1 = null;
			ExpressedGene eg2 = null;
			eg1 = mutateGene(o.getGene1());
			eg2 = mutateGene(o.getGene2());

			if (current < mutantCount) {
				offspringList.add(new Organism(trayNum + "-" + (current + 1),
						eg1,
						eg2));
			}
		}
	}


	public ExpressedGene mutateGene(ExpressedGene eg) {
		//change one base in the DNA
		Gene gene = eg.getGene();
		if (gene.getDNASequenceLength() == 0) {
			return eg;
		}
		String DNASequence = gene.getDNASequence();
		StringBuffer DNABuffer = new StringBuffer(DNASequence);

		//mutation: 1/100 chance of hitting each base
		Random r = new Random();
		for (int i = 0; i < DNABuffer.length(); i++) {
			if (r.nextInt(100) == 0) {
				int base = r.nextInt(4);
				String newBase = "AGCT".substring(base, base + 1);
				DNABuffer = DNABuffer.replace(i, i + 1, newBase);
			}
		}
		Gene newGene = 
			new Gene(DNABuffer.toString(), 
					gw.getMGE().getMolBiolWorkbench().getParams());
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
}
