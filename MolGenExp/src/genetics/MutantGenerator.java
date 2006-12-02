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

public class MutantGenerator {

	private int mutantCount;	//number of mutants to make
	private int current;
	private Organism o;
	private int trayNum;
	private int location;
	private GeneticsWorkshop gw;
	private OffspringList offspringList;
	private boolean running;

	MutantGenerator (Organism o, 
			int trayNum,
			int location,
			OffspringList offspringList, 
			GeneticsWorkshop gw) {
		this.o = o;
		this.trayNum = trayNum;
		this.location = location;
		this.gw = gw;
		this.offspringList = offspringList;

		//figure out how many mutants to make
		Random random = new Random();
		mutantCount = 10 + random.nextInt(10);

		running = false;
	}

	public void go() {
		final SwingWorker worker = new SwingWorker() {
			public Object construct() {
				return new Mutator();
			}
		};
		worker.start();
		running = true;
	}

	public int getLengthOfTask() {
		return mutantCount;
	}

	public int getCurrent() {
		return current;
	}

	public void stop() {
		current = mutantCount;
		running = false;
	}

	boolean done() {
		if (current >= mutantCount) {
			return true;
		} else {
			return false;
		}
	}

	private class Mutator {
		Mutator() {
			for (current = 0; current < mutantCount; current++) {
				ExpressedGene eg1 = null;
				ExpressedGene eg2 = null;
				if (running) {
					eg1 = mutateGene(o.getGene1());
				}
				
				if (running) {
					eg2 = mutateGene(o.getGene2());
				}
				
				if ((eg1 != null) && (eg2 != null) && running) {
					offspringList.add(new Organism(
							location,
							trayNum + "-" + (current + 1),
							eg1,
							eg2,
							gw.getProteinColorModel()));
				}
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
		Random random = new Random();
		int targetBase = random.nextInt(DNABuffer.length());
		int base = random.nextInt(4);
		String newBase = "AGCT".substring(base, base + 1);
		DNASequence = (DNABuffer.replace(
				targetBase, 
				targetBase + 1, 
				newBase)).toString();
		Gene newGene = 
			new Gene(DNASequence, gw.getMolGenExp().getGenex().getParams());
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
				new RYBColorModel(),
				"straight",
		"test");
		FoldingManager manager = FoldingManager.getInstance(
				gw.getMolGenExp().getOverallColorModel());
		try {
			manager.fold(attributes);
		} catch (FoldingException e) {
			e.printStackTrace();
		}

		//make an icon and display it in a dialog
		OutputPalette op = new OutputPalette(
				gw.getMolGenExp().getOverallColorModel());
		manager.createCanvas(op);
		Dimension requiredCanvasSize = 
			op.getDrawingPane().getRequiredCanvasSize();

		ProteinImageSet images = 
			ProteinImageFactory.generateImages(op, requiredCanvasSize);

		FoldedPolypeptide fp = new FoldedPolypeptide(
				proteinSequence,
				op.getDrawingPane().getGrid(), 
				new ImageIcon(images.getFullScaleImage()),
				new ImageIcon(images.getThumbnailImage()), 
				op.getProteinColor());

		ExpressedGene newEg = new ExpressedGene(html, newGene);
		newEg.setFoldedPolypeptide(fp);

		return newEg;
	}

}
