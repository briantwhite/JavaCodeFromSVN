package evolution;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import molGenExp.FoldedProteinArchive;
import molGenExp.MolGenExp;
import preferences.MGEPreferences;
import utilities.GeneExpresser;
import utilities.GlobalDefaults;
import utilities.Mutator;
import biochem.FoldingException;
import biochem.FoldingManager;
import biochem.HexCanvas;
import biochem.HexGrid;
import biochem.PolypeptideFactory;

public class Evolver implements Runnable {

	private MolGenExp mge;
	private EvolutionWorkArea evolutionWorkArea;
	private World world;
	private ArrayList<String> genePool;
	private int lengthOfTask;
	private int progress;
	private boolean keepGoing;
	private boolean oneGenerationOnly;

	private FoldedProteinArchive archive;

	private MGEPreferences preferences;
	private Mutator mutator;
	private GeneExpresser expresser;
	private FoldingManager foldingManager;

	public Evolver(final MolGenExp mge) {
		this.mge = mge;
		this.evolutionWorkArea = mge.getEvolutionWorkArea();
		this.world = mge.getEvolutionWorkArea().getWorld();
		keepGoing = true;
		oneGenerationOnly = false;
		preferences = MGEPreferences.getInstance();
		mutator = Mutator.getInstance();
		archive = FoldedProteinArchive.getInstance();
		expresser = new GeneExpresser();
		foldingManager = new FoldingManager();
	}

	public void run() {
		mge.setStatusLabelText("Running");
		keepGoing = true;
		while (keepGoing) {
			world.updateCounts();
			evolutionWorkArea.updateColorCountDisplay();
			savePic();
			createGenePool();
			makeNextGeneration();
			if (oneGenerationOnly) {
				keepGoing = false;
				return;
			}
		}
	}


	public void setOneGenerationOnly() {
		oneGenerationOnly = true;
	}
	
	public int getLengthOfTask() {
		return lengthOfTask;
	}

	public int getProgress() {
		return progress;
	}

	public boolean done() {
		return !keepGoing;
	}

	public void stop() {
		keepGoing = false;
	}

	private void createGenePool() {
		if (keepGoing) {
			// find the gene pool
			// get the fitness settings
			int[] fitnessSettings = evolutionWorkArea.getFitnessValues();
			
			lengthOfTask = preferences.getWorldSize() * preferences.getWorldSize();
			mge.getProgressBar().setMaximum(lengthOfTask);
			mge.setStatusLabelText("Generating Gene Pool");
			progress = 0;

			// each organism in the world contributes fitness # of alleles to pool		
			genePool = new ArrayList<String>();
			for (int i = 0; i < preferences.getWorldSize(); i++) {
				for (int j = 0; j < preferences.getWorldSize(); j++) {
					ThinOrganism org = world.getThinOrganism(i, j);
					int colorNumber = 
						GlobalDefaults.colorModel.getColorNumber(org.getOverallColor());
					// see if color number = 8; meaning it is a dead organism
					//  because one or both proteins is folded in a corner
					//  if so, it's dead, so fitness = 0
					if (colorNumber != 8) {
						for (int x = 0; x < fitnessSettings[colorNumber]; x++) {
							genePool.add(org.getRandomDNASequence());
						}
					}
					progress++;
				}
			}
		}
	}

	private synchronized void makeNextGeneration() {
		if (genePool.size() == 0) {
			JOptionPane.showMessageDialog(null, "<html><body>" 
					+ "<font color=red>No organisms contributed to the gene pool.<br>"
					+ "</font>You should be sure that the fitness of at least one of the<br>"
					+ "organisms in the world has a non-zero fitness.</body></html>",
					"Empty Gene Pool",
					JOptionPane.ERROR_MESSAGE);
			keepGoing = false;
			progress = getLengthOfTask();
			evolutionWorkArea.setReadyToRun();
			evolutionWorkArea.setFitnessSpinnersEnabled(true);
			evolutionWorkArea.setLoadButtonEnabled(true);
			return;
		}

		//make next generation
		// choose random alleles from gene pool, mutate and make new organisms
		// procedure depends on if using a server or not
		//  if server - save all requests and send at once
		//  if locally-folded - fold as you go
		mge.setStatusLabelText("Mutating Alleles In Gene Pool");
		lengthOfTask = preferences.getWorldSize() * preferences.getWorldSize();
		mge.getProgressBar().setMaximum(lengthOfTask);
		progress = 0;
		ThinOrganism[][] nextGeneration = 
			new ThinOrganism[preferences.getWorldSize()][preferences.getWorldSize()];

		PairOfProteinAndDNASequences[][] pairs =
			new PairOfProteinAndDNASequences[preferences.getWorldSize()][preferences.getWorldSize()];
		HashSet<String> sequencesToBeFolded = new HashSet<String>();
		// make the new protein sequences and see which are in archive
		//  make a list of those that need to be folded
		for (int i = 0; i < preferences.getWorldSize(); i++) {
			for (int j = 0; j < preferences.getWorldSize(); j++) {
				String DNA1 = mutator.mutateDNASequence(getRandomAlleleFromPool());
				String protein1 = 
					convertThreeLetterProteinStringToOneLetterProteinString((
							expresser.expressGene(DNA1, -1)).getProtein());
				if (!archive.isInArchive(protein1)) {
					sequencesToBeFolded.add(protein1);
				}
				String DNA2 = mutator.mutateDNASequence(getRandomAlleleFromPool());
				String protein2 = 
					convertThreeLetterProteinStringToOneLetterProteinString((
							expresser.expressGene(DNA2, -1)).getProtein());
				if (!archive.isInArchive(protein2)) {
					sequencesToBeFolded.add(protein2);
				}
				pairs[i][j] = new PairOfProteinAndDNASequences(
						DNA1, protein1, DNA2, protein2);
			}
		}

		if (!keepGoing) {
			nextGeneration = null;
			genePool = null;
			progress = getLengthOfTask();
			return;
		}

		// fold the sequences that haven't been already
		//   and add to archive
		mge.setStatusLabelText("Folding " 
				+ sequencesToBeFolded.size() 
				+ " new protein sequences.");
		lengthOfTask = sequencesToBeFolded.size();
		mge.getProgressBar().setMaximum(lengthOfTask);
		progress = 0;
		
		Iterator<String> aaSeqIt = sequencesToBeFolded.iterator();
		while (aaSeqIt.hasNext()) {
			foldProteinAndAddToArchive(aaSeqIt.next(), archive);
			progress++;
		}

		//make new generation using updated archive
		mge.setStatusLabelText("Populating Next Generation");
		lengthOfTask = preferences.getWorldSize() * preferences.getWorldSize();
		mge.getProgressBar().setMaximum(lengthOfTask);
		progress = 0;
		
		for (int i = 0; i < preferences.getWorldSize(); i++) {
			for (int j = 0; j < preferences.getWorldSize(); j++) {
				PairOfProteinAndDNASequences pair = pairs[i][j];
				nextGeneration[i][j] = new ThinOrganism(
						pair.getDNA1(),
						pair.getDNA2(),
						(archive.getArchiveEntry(
								pair.getProtein1())
						).getColor(),
						(archive.getArchiveEntry(
								pair.getProtein2())
						).getColor(),
						GlobalDefaults.colorModel.mixTwoColors(
								(archive.getArchiveEntry(
										pair.getProtein1())
								).getColor(), 
								(archive.getArchiveEntry(
										pair.getProtein2())
								).getColor()));
				// if it's gray, it's dead; count it.
				if (GlobalDefaults.colorModel.mixTwoColors(
								(archive.getArchiveEntry(
										pair.getProtein1())
								).getColor(), 
								(archive.getArchiveEntry(
										pair.getProtein2())
								).getColor()).equals(Color.GRAY)) {
					evolutionWorkArea.anOrganismDied();
				}
				progress++;
			}
		}

		if (!keepGoing) {
			nextGeneration = null;
			genePool = null;
			progress = getLengthOfTask();
			return;
		}

		genePool = null;
		world.setOrganisms(nextGeneration);
		evolutionWorkArea.updateGenerationLabel();
	}

	private String getRandomAlleleFromPool() {
		Random r = new Random();
		int x = r.nextInt(genePool.size());
		return (String)genePool.get(x);
	}

	public void savePic() {
		if (preferences.isGenerationPixOn()) {
			//draw it
			BufferedImage pic = new BufferedImage(
					World.pictureSize, 
					World.pictureSize,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = pic.getGraphics();
			world.paint(g);
			g.setColor(Color.DARK_GRAY);
			g.drawString("Generation: " + evolutionWorkArea.getGeneration(), 
					20, 20);
			g.dispose();

			//save it
			File imageFile  = new File(preferences.getSavePixToPath() 
					+ System.getProperty("file.separator")
					+ evolutionWorkArea.getGeneration()
					+ ".png");				
			try {
				ImageIO.write(pic, "png", imageFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			pic = null;
		}
	}

	private void foldProteinAndAddToArchive(String aaSeq, FoldedProteinArchive archive) {
		try {
			foldingManager.fold(aaSeq);
			HexGrid grid = (HexGrid)foldingManager.getGrid();
			HexCanvas canvas = new HexCanvas();
			canvas.setGrid(grid);
			GlobalDefaults.colorModel.categorizeAcids(grid);
			archive.add(aaSeq, grid.getPP().getProteinString(), grid.getProteinColor());
		} catch (FoldingException e) {
			// if folded in a corner, the shape and color are null
			archive.add(aaSeq, null, null);
			return;
		}
	}

	private String convertThreeLetterProteinStringToOneLetterProteinString(
			String threeLetter) {
		//insert spaces between amino acid codes
		StringBuffer psBuffer = new StringBuffer(threeLetter);
		for (int i = 3; i < psBuffer.length(); i = i + 4) {
			psBuffer = psBuffer.insert(i, " ");
		}
		threeLetter = psBuffer.toString();

		// parse input into strings, each representing an acid
		ArrayList acidStrings = 
			PolypeptideFactory.getInstance().getTokens(threeLetter);

		// parsing each acid string into AminoAcids using the AminoAcidTable.
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < acidStrings.size(); i++) {
			b.append((
					GlobalDefaults.aaTable.get(
							(String)acidStrings.get(i))).getAbName());
		}
		return b.toString();
	}
}
