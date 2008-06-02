package evolution;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import biochem.PolypeptideFactory;

import molGenExp.FoldedProteinArchive;
import molGenExp.MolGenExp;
import molGenExp.ServerCommunicator;
import preferences.MGEPreferences;
import utilities.ColorUtilities;
import utilities.GeneExpresser;
import utilities.GlobalDefaults;
import utilities.Mutator;

public class Evolver implements Runnable {

	private MolGenExp mge;
	private EvolutionWorkArea evolutionWorkArea;
	private World world;
	private ArrayList<String> genePool;
	private int progress;
	private boolean keepGoing;

	private FoldedProteinArchive archive;
	private ColorCountsRecorder colorCountsRecorder;
	
	private MGEPreferences preferences;
	private Mutator mutator;
	private GeneExpresser expresser;
	private ThinOrganismFactory thinOrganismFactory;
	private ServerCommunicator communicator;

	public Evolver(final MolGenExp mge) {
		this.mge = mge;
		this.evolutionWorkArea = mge.getEvolutionWorkArea();
		this.world = mge.getEvolutionWorkArea().getWorld();
		keepGoing = true;
		preferences = MGEPreferences.getInstance();
		mutator = Mutator.getInstance();
		archive = FoldedProteinArchive.getInstance();
		colorCountsRecorder = ColorCountsRecorder.getInstance();
		expresser = new GeneExpresser();
		thinOrganismFactory = new ThinOrganismFactory();
		communicator = new ServerCommunicator(mge);
	}

	public void run() {
		mge.setStatusLabelText("Running");
		keepGoing = true;
		while (keepGoing) {
			updateCounts();
			savePic();
			createGenePool();
			makeNextGeneration();
			evolutionWorkArea.notifyColorCountsChanged();
		}
	}

	public int getProgress() {
		return progress;
	}

	public int getLengthOfTask() {
		return preferences.getWorldSize() * preferences.getWorldSize();
	}
	
	public ColorCountsRecorder getColorCountsRecorder() {
		return colorCountsRecorder;
	}

	public boolean done() {
		if (progress == getLengthOfTask()) {
			return true;
		} else {
			return false;
		}
	}

	public void stop() {
		keepGoing = false;
	}

	private void createGenePool() {
		if (keepGoing) {
			// find the gene pool
			// get the fitness settings
			int[] fitnessSettings = evolutionWorkArea.getFitnessValues();
			mge.setStatusLabelText("Generating Gene Pool");

			// each organism in the world contributes fitness # of alleles to pool		
			genePool = new ArrayList<String>();
			for (int i = 0; i < preferences.getWorldSize(); i++) {
				for (int j = 0; j < preferences.getWorldSize(); j++) {
					ThinOrganism org = world.getThinOrganism(i, j);
					int colorNumber = 
						GlobalDefaults.colorModel.getColorNumber(org.getOverallColor());
					for (int x = 0; x < fitnessSettings[colorNumber]; x++) {
						genePool.add(org.getRandomDNASequence());
					}
				}
			}
		}
	}

	private void makeNextGeneration() {
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
			return;
		}

		progress = 0;
		ThinOrganism[][] nextGeneration = 
			new ThinOrganism[preferences.getWorldSize()][preferences.getWorldSize()];
		//make next generation
		// choose random alleles from gene pool, mutate and make new organisms
		// procedure depends on if using a server or not
		//  if server - save all requests and send at once
		//  if locally-folded - fold as you go

		if (preferences.isUseFoldingServer()) {
			mge.setStatusLabelText("Using Folding Server");
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

			// send the sequences out to be folded
			StringBuffer b = new StringBuffer();
			Iterator<String> it = sequencesToBeFolded.iterator();
			while (it.hasNext()) {
				b.append(it.next() + ",");
			}
			if (b.length() > 0) {
				b.deleteCharAt(b.length() -1);
			}
			mge.setStatusLabelText("Sending " 
					+ sequencesToBeFolded.size() 
					+ " sequences to be folded");
			String response = communicator.sendSequencesToServer(b.toString());

			//parse the reply and add to archive
			String[] responseLines = response.split("<br>");
			for (int i = 0; i < responseLines.length; i++) {
				String line = responseLines[i];
				if (line.indexOf(";") != -1) {
					archive.addEntryToArchive(line);
				}
				if (line.indexOf("it took") != -1) {
					mge.setStatusLabelText(line);
				}
			}

			//make new generation using updated archive
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
					progress++;
				}
			}
		} else {
			mge.setStatusLabelText("Populating the next generation");
			for (int i = 0; i < preferences.getWorldSize(); i++) {
				for (int j = 0; j < preferences.getWorldSize(); j++) {
					nextGeneration[i][j] = thinOrganismFactory.createThinOrganism(
							mutator.mutateDNASequence(getRandomAlleleFromPool()),
							mutator.mutateDNASequence(getRandomAlleleFromPool()));
					progress++;
					if (!keepGoing) {
						nextGeneration = null;
						genePool = null;
						progress = getLengthOfTask();
						return;
					}
				}
			}
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
	
	public void updateCounts() {
		//first, be sure that there are organisms in the world
		if (world.getThinOrganism(0,0) != null) {
			colorCountsRecorder.setAllToZero();
			for (int i = 0; i < preferences.getWorldSize(); i++) {
				for (int j = 0; j < preferences.getWorldSize(); j++) {
					ThinOrganism o = world.getThinOrganism(i, j);
					colorCountsRecorder.incrementCount(
							world.getThinOrganism(i, j).getOverallColor());
				}
			}
		}
	}
}
