package evolution;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JOptionPane;

import molGenExp.FoldedProteinArchive;
import molGenExp.MolGenExp;
import molGenExp.ServerCommunicator;
import preferences.MGEPreferences;
import utilities.GeneExpresser;
import utilities.GlobalDefaults;
import utilities.Mutator;
import utilities.ProteinUtilities;
import biochem.MultiSequenceThreadedFolder;

public class Evolver implements Runnable {

	private MolGenExp mge;
	private EvolutionWorkArea evolutionWorkArea;
	private World world;
	private ArrayList<String> genePool;

	private FoldedProteinArchive archive;

	private int lengthOfTask;

	private boolean keepGoing;

	private MGEPreferences preferences;
	private Mutator mutator;
	private GeneExpresser expresser;
	private ServerCommunicator communicator;

	private int numProcessors;
	private MultiSequenceThreadedFolder[] folders;
	private Thread[] foldingThreads;
	private LinkedBlockingQueue<String> sequencesToFold;

	public Evolver(final MolGenExp mge) {
		this.mge = mge;
		this.evolutionWorkArea = mge.getEvolutionWorkArea();
		this.world = mge.getEvolutionWorkArea().getWorld();
		preferences = MGEPreferences.getInstance();
		mutator = Mutator.getInstance();
		archive = FoldedProteinArchive.getInstance();
		expresser = GeneExpresser.getInstance();
		communicator = new ServerCommunicator(mge);
		numProcessors = Runtime.getRuntime().availableProcessors();
		sequencesToFold = new LinkedBlockingQueue<String>();
		lengthOfTask = 0;
		keepGoing = true;
	}

	public void run() {
		mge.setStatusLabelText("Running");
		keepGoing = true;
		while (keepGoing) {
			evolutionWorkArea.updateCounts();
			evolutionWorkArea.savePic();
			createGenePool();
			makeNextGeneration();
		}
	}

	public int getProgress() {
		if (lengthOfTask == 0) {
			return -1;
		} else {
			return lengthOfTask - sequencesToFold.size();
		}
	}

	public boolean done() {
		return !keepGoing;
	}

	public void stop() {
		keepGoing = false;
		for (int i = 0; i < numProcessors; i++) {
			foldingThreads[i].stop();
		}
	}

	public LinkedBlockingQueue<String> getSequencesToFold() {
		return sequencesToFold;
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
						GlobalDefaults.colorModel.getColorNumber(org.getColor());
					for (int x = 0; x < fitnessSettings[colorNumber]; x++) {
						genePool.add(org.getRandomDNASequence());
					}
				}
			}
		}
	}

	private void makeNextGeneration() {
		if (keepGoing) {
			if (genePool.size() == 0) {
				JOptionPane.showMessageDialog(null, "<html><body>" 
						+ "<font color=red>No organisms contributed to the gene pool.<br>"
						+ "</font>You should be sure that the fitness of at least one of the<br>"
						+ "organisms in the world has a non-zero fitness.</body></html>",
						"Empty Gene Pool",
						JOptionPane.ERROR_MESSAGE);
				evolutionWorkArea.setReadyToRun();
				return;
			}

			ThinOrganism[][] nextGeneration = 
				new ThinOrganism[preferences.getWorldSize()][preferences.getWorldSize()];

			//make next generation
			// choose random alleles from gene pool, mutate and make new organisms
			// procedure depends on if using a server or not
			//  always collect all seqs to be folded
			//  if server - send out to be folded
			//  if locally-folded - fold using local thread(s)

			mge.setStatusLabelText("Making Next Generation's DNA and Protein");

			PairOfProteinAndDNASequences[][] pairs =
				new PairOfProteinAndDNASequences[preferences.getWorldSize()][preferences.getWorldSize()];

			// make the new protein sequences and see which are in archive
			//  make a list of those that need to be folded
			for (int i = 0; i < preferences.getWorldSize(); i++) {
				for (int j = 0; j < preferences.getWorldSize(); j++) {
					String DNA1 = mutator.mutateDNASequence(getRandomAlleleFromPool());
					String protein1 = 
						ProteinUtilities.convertThreeLetterProteinStringToOneLetterProteinString((
								expresser.expressGene(DNA1, -1)).getProtein());
					if (!archive.isInArchive(protein1)) {
						sequencesToFold.add(protein1);
					}
					String DNA2 = mutator.mutateDNASequence(getRandomAlleleFromPool());
					String protein2 = 
						ProteinUtilities.convertThreeLetterProteinStringToOneLetterProteinString((
								expresser.expressGene(DNA2, -1)).getProtein());
					if (!archive.isInArchive(protein2)) {
						sequencesToFold.add(protein2);
					}
					pairs[i][j] = new PairOfProteinAndDNASequences(
							DNA1, protein1, DNA2, protein2);
				}
			}

			if (!keepGoing) {
				return;
			}

			// send the sequences out to be folded
			if (preferences.isUseFoldingServer()) {

				StringBuffer b = new StringBuffer();
				Iterator<String> it = sequencesToFold.iterator();
				while (it.hasNext()) {
					b.append(it.next() + ",");
				}
				if (b.length() > 0) {
					b.deleteCharAt(b.length() -1);
				}
				mge.setStatusLabelText("Sending " 
						+ sequencesToFold.size() 
						+ " new proteins to be folded");
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
			} else {
				// fold locally using threadPool with one thread per processor
				folders = new MultiSequenceThreadedFolder[numProcessors];
				foldingThreads = new Thread[numProcessors];

				lengthOfTask = sequencesToFold.size();
				if (numProcessors == 1) {
					mge.setStatusLabelText("Folding " + 
							lengthOfTask +
					" new proteins using 1 processor.");
				} else {
					mge.setStatusLabelText("Folding " + 
							lengthOfTask +
							" new proteins using " +
							numProcessors + " processors.");
				}

				mge.notifyLengthOfTask(lengthOfTask + 1);

				// start the threads
				for (int i = 0; i < numProcessors; i++) {
					folders[i] = new MultiSequenceThreadedFolder(i, this);
					foldingThreads[i] = new Thread(folders[i]);
					foldingThreads[i].start();
				}

				// wait for all threads to finish
				try {
					for (int i = 0; i < numProcessors; i++) {
						foldingThreads[i].join();
					}
				} catch (InterruptedException e) {
					for (int i = 0; i < numProcessors; i++) {
						foldingThreads[i].stop();
					}
				}

				// kill them all
				for (int i = 0; i < numProcessors; i++) {
					foldingThreads[i] = null;
					folders[i] = null;
				}
			}

			//make new generation using updated archive
			for (int i = 0; i < preferences.getWorldSize(); i++) {
				for (int j = 0; j < preferences.getWorldSize(); j++) {
					PairOfProteinAndDNASequences pair = pairs[i][j];
					nextGeneration[i][j] = new ThinOrganism(
							pair.getDNA1(),
							pair.getDNA2(),
							GlobalDefaults.colorModel.mixTwoColors(
									(archive.getArchiveEntry(
											pair.getProtein1())).getColor(), 
											(archive.getArchiveEntry(
													pair.getProtein2())).getColor()));
				}
			}
			genePool = null;
			world.setOrganisms(nextGeneration);
			evolutionWorkArea.updateGenerationLabel();
		}
	}

	private String getRandomAlleleFromPool() {
		Random r = new Random();
		int x = r.nextInt(genePool.size());
		return (String)genePool.get(x);
	}
}
