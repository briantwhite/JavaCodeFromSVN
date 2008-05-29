package evolution;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

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
	private ExecutorService foldingThreadPool;
	private MultiSequenceThreadedFolder[] folders;
	private boolean[] doneFolders;

	private int progress;  // -1 means indeterminate
	
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
		foldingThreadPool = null;
		lengthOfTask = 0;
		folders = new MultiSequenceThreadedFolder[numProcessors];
		doneFolders = new boolean[numProcessors];
		progress = 0;
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
		return progress;
	}
	
	public boolean done() {
		return !keepGoing;
	}

	public void stop() {
		keepGoing = false;
		if (foldingThreadPool != null) {
			foldingThreadPool.shutdownNow();
		}
	}

	public LinkedBlockingQueue<String> getSequencesToFold() {
		return evolutionWorkArea.getSequencesToFold();
	}

	public FoldedProteinArchive getArchive() {
		return archive;
	}

	private void createGenePool() {
		if (keepGoing) {
			// find the gene pool
			// get the fitness settings
			int[] fitnessSettings = evolutionWorkArea.getFitnessValues();
			mge.setStatusLabelText("Generating Gene Pool");
			progress = -1;

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
			progress = -1;

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
						evolutionWorkArea.getSequencesToFold().add(protein1);
					}
					String DNA2 = mutator.mutateDNASequence(getRandomAlleleFromPool());
					String protein2 = 
						ProteinUtilities.convertThreeLetterProteinStringToOneLetterProteinString((
								expresser.expressGene(DNA2, -1)).getProtein());
					if (!archive.isInArchive(protein2)) {
						evolutionWorkArea.getSequencesToFold().add(protein2);
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
				Iterator<String> it = evolutionWorkArea.getSequencesToFold().iterator();
				while (it.hasNext()) {
					b.append(it.next() + ",");
				}
				if (b.length() > 0) {
					b.deleteCharAt(b.length() -1);
				}
				mge.setStatusLabelText("Sending " 
						+ evolutionWorkArea.getSequencesToFold().size() 
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
				foldingThreadPool = Executors.newFixedThreadPool(numProcessors);

				folders = new MultiSequenceThreadedFolder[numProcessors];
				lengthOfTask = evolutionWorkArea.getSequencesToFold().size();
				mge.setStatusLabelText("Folding " + 
						lengthOfTask +
						" new proteins using " +
						numProcessors + " processors.");

				mge.notifyLengthOfTask(lengthOfTask + 1);
				progress = 0;

				// start the threads
				for (int i = 0; i < numProcessors; i++) {
					doneFolders[i] = false;
					folders[i] = new MultiSequenceThreadedFolder(i, this);
					foldingThreadPool.execute(folders[i]);
				}

				// wait for all threads to finish

				while (!allThreadsDone()) {
					progress = lengthOfTask - evolutionWorkArea.getSequencesToFold().size();
				}
				foldingThreadPool.shutdown();
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

	public void informThreadDone(int i) {
		doneFolders[i] = true;
	}

	public boolean allThreadsDone() {
		for (int i = 0; i < numProcessors; i++) {
			if (!doneFolders[i]) {
				return false;
			}
		}
		return true;
	}

}
