package evolution;

import genetics.MutantGenerator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import preferences.MGEPreferences;

import sun.tools.tree.LengthExpression;

import molGenExp.MolGenExp;

public class Evolver implements Runnable {

	private MolGenExp mge;
	private EvolutionWorkArea evolutionWorkArea;
	private World world;
	private ArrayList genePool;
	private int current;
	private boolean keepGoing;

	private MGEPreferences preferences;

	public Evolver(final MolGenExp mge) {
		this.mge = mge;
		this.evolutionWorkArea = mge.getEvolutionWorkArea();
		this.world = mge.getEvolutionWorkArea().getWorld();
		keepGoing = true;
		preferences = MGEPreferences.getInstance();
	}

	public void run() {
		while (keepGoing) {
			savePic();
			createGenePool();
			makeNextGeneration();
		}
	}

	private void createGenePool() {
		// find the gene pool
		// get the fitness settings
		int[] fitnessSettings = evolutionWorkArea.getFitnessValues();

		// each organism in the world contributes fitness # of alleles to pool		
		genePool = new ArrayList();
		for (int i = 0; i < MolGenExp.worldSize; i++) {
			for (int j = 0; j < MolGenExp.worldSize; j++) {
				ThinOrganism org = world.getThinOrganism(i, j);
				int colorNumber = 
					MolGenExp.colorModel.getColorNumber(org.getColor());
				for (int x = 0; x < fitnessSettings[colorNumber]; x++) {
					genePool.add(org.getRandomDNASequence());
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
			current = getLengthOfTask();
			evolutionWorkArea.setReadyToRun();
			return;
		}

		current = 0;
		ThinOrganism[][] nextGeneration = 
			new ThinOrganism[MolGenExp.worldSize][MolGenExp.worldSize];
		//make next generation
		// choose random alleles from gene pool, mutate and make new organisms
		for (int i = 0; i < MolGenExp.worldSize; i++) {
			for (int j = 0; j < MolGenExp.worldSize; j++) {
				String alleleDNA = getRandomAlleleFromPool();
				String newAlleleDNA = MutantGenerator.mutateDNASequence(alleleDNA);
				nextGeneration[i][j] = new ThinOrganism(
						MutantGenerator.mutateDNASequence(getRandomAlleleFromPool()),
						MutantGenerator.mutateDNASequence(getRandomAlleleFromPool()));
				current++;
				if (!keepGoing) {
					nextGeneration = null;
					genePool = null;
					current = getLengthOfTask();
					return;
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

	private void savePic() {
		if (preferences.isGenerationPixOn()) {
			//draw it
			BufferedImage pic = new BufferedImage(
					world.pictureSize, 
					world.pictureSize,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = pic.getGraphics();
			world.paint(g);
			g.setColor(Color.DARK_GRAY);
			g.drawString("Generation: " + evolutionWorkArea.getGeneration(), 
					20, 20);

			//save it
			File imageFile = new File(preferences.getSavePixToPath() 
					+ System.getProperty("file.separator")
					+ evolutionWorkArea.getGeneration()
					+ ".png");
			try {
				ImageIO.write(pic, "png", imageFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean done() {
		if (current == getLengthOfTask()) {
			return true;
		}
		return false;
	}

	public int getLengthOfTask() {
		return MolGenExp.worldSize * MolGenExp.worldSize;
	}

	public int getCurrent() {
		return current;
	}

	public void setKeepGoing(boolean b) {
		keepGoing = b;
	}
}
