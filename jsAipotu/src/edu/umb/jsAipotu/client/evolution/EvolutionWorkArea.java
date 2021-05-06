package edu.umb.jsAipotu.client.evolution;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.umb.jsAipotu.client.molGenExp.MolGenExp;
import edu.umb.jsAipotu.client.molGenExp.OrganismFactory;
import edu.umb.jsAipotu.client.molGenExp.OrganismUI;
import edu.umb.jsAipotu.client.preferences.GlobalDefaults;
import edu.umb.jsAipotu.client.preferences.MGEPreferences;

public class EvolutionWorkArea extends HorizontalPanel {

	private MolGenExp mge;
	private MGEPreferences preferences;

	private OrganismFactory organismFactory;

	private ColorCountsRecorder colorCountsRecorder;

	private HTML generationDisplay;
	private HTML avgFitnessDisplay;

	private World world;
	private FitnessSettingsPanel fitnessSettingsPanel;

	public EvolutionWorkArea(MolGenExp mge) {
		super();
		this.mge = mge;
		preferences = MGEPreferences.getInstance();
		//		organismFactory = new OrganismFactory();
		colorCountsRecorder = ColorCountsRecorder.getInstance();
		//		evolverTimer = new Timer(100, new EvolverTimerListener());
		setupUI();
	}

	private void setupUI() {

		VerticalPanel leftPanel = new VerticalPanel();

		fitnessSettingsPanel = new FitnessSettingsPanel(mge);
		leftPanel.add(fitnessSettingsPanel);

		CaptionPanel controlPanel = new CaptionPanel("Controls");
		HorizontalPanel buttonPanel = new HorizontalPanel();

		Button loadButton = new Button("Load");
		loadButton.setStyleName("evolutionButton");
		loadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadWorldFromGreenhouse();
			}
		});
		buttonPanel.add(loadButton);

		Button runButton = new Button("Run");
		runButton.setEnabled(false);
		runButton.setStyleName("evolutionButton");
		runButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

			}
		});
		buttonPanel.add(runButton);

		Button pauseButton = new Button("Pause");
		pauseButton.setEnabled(false);
		pauseButton.setStyleName("evolutionButton");
		pauseButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

			}
		});
		buttonPanel.add(pauseButton);

		Button oneGenButton = new Button("One Generation Only");
		oneGenButton.setEnabled(false);
		oneGenButton.setStyleName("evolutionButton");
		oneGenButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

			}
		});
		buttonPanel.add(oneGenButton);

		controlPanel.setContentWidget(buttonPanel);
		leftPanel.add(controlPanel);

		HorizontalPanel generationDisplayPanel = new HorizontalPanel();
		generationDisplay = new HTML("0");
		generationDisplayPanel.add(new HTML("Generation: "));
		generationDisplayPanel.add(generationDisplay);
		leftPanel.add(generationDisplayPanel);

		HorizontalPanel avgFitDispPanel = new HorizontalPanel();
		avgFitnessDisplay = new HTML("");
		avgFitDispPanel.add(new HTML("Average fitness = "));
		avgFitDispPanel.add(avgFitnessDisplay);
		leftPanel.add(avgFitDispPanel);

		add(leftPanel);

		world = new World(mge);
		add(world);

		//
		//		rightPanel.add(Box.createRigidArea(new Dimension(500,1)));
		//		world = new World();
		//		rightPanel.add(world);
		//		generationLabel = new JLabel("Generation 0");
		//		rightPanel.add(generationLabel);
		//		averageFitnessLabel = new JLabel("Average Fitness = ?");
		//		rightPanel.add(averageFitnessLabel);
		//
		//		this.add(rightPanel);
		//
		//		loadButton.addActionListener(new ActionListener() {
		//			public void actionPerformed(ActionEvent e) {
		//				mge.loadSelectedIntoWorld();
		//				mge.getGreenhouse().clearSelection();
		//				world.updateCounts();
		//				updateColorCountDisplay();
		//				setFitnessSpinnersEnabled(true);
		//				mge.getProgressBar().setValue(0);
		//				clearSelection();
		//			}
		//		});
		//
		//		runButton.addActionListener(new ActionListener() {
		//			public void actionPerformed(ActionEvent e) {
		//				runButton.setEnabled(false);
		//				pauseButton.setEnabled(true);
		//				loadButton.setEnabled(false);
		//				setFitnessSpinnersEnabled(false);
		//				clearSelection();
		//				startEvolving(false);
		//			}
		//		});
		//
		//		pauseButton.addActionListener(new ActionListener() {
		//			public void actionPerformed(ActionEvent e) {
		//				pauseButton.setEnabled(false);
		//				runButton.setEnabled(true);
		//				loadButton.setEnabled(true);
		//				setFitnessSpinnersEnabled(true);
		//				stopEvolving();
		//				clearSelection();
		//			}
		//		});
		//		
		//		runOneGenerationButton.addActionListener(new ActionListener() {
		//			public void actionPerformed(ActionEvent e) {
		//				runButton.setEnabled(false);
		//				pauseButton.setEnabled(true);
		//				loadButton.setEnabled(false);
		//				setFitnessSpinnersEnabled(false);
		//				clearSelection();
		//				startEvolving(true);
		//			}
		//		});
		//
		//		updateAbsFitnessDisplay(getFitnessValues());
	}

	private void loadWorldFromGreenhouse() {
		ArrayList<OrganismUI> orgUIs = mge.getGreenhouse().getAllSelectedOrganisms();
		if (orgUIs.size() > 0) {
			world.initialize(orgUIs);
			mge.getGreenhouse().clearAllSelections();
			world.updateCounts();
			fitnessSettingsPanel.updateColorCountDisplay(colorCountsRecorder);
		}

		//		setFitnessSpinnersEnabled(true);
		//		mge.getProgressBar().setValue(0);
		//		clearSelection();

	}

	//	public ColorPopulationLabel[] getPopulationLabels() {
	//		return populationLabels;
	//	}

	//	public void setReadyToRun() {
	//		runButton.setEnabled(true);
	//		runOneGenerationButton.setEnabled(true);
	//		pauseButton.setEnabled(false);
	//	}

	//	public void setLoadButtonEnabled(boolean b) {
	//		loadButton.setEnabled(b);
	//	}
	//	
	//	public void setFitnessSpinnersEnabled(boolean b) {
	//		for (int i = 0; i < spinners.length; i++) {
	//			spinners[i].setEnabled(b);
	//		}
	//	}
	//
	//	public void updateGenerationLabel() {
	//		generation++;
	//		generationLabel.setText("Generation " + generation);
	//	}
	//
	//	public int getGeneration() {
	//		return generation;
	//	}
	//
	//	public void setGeneration(int i) {
	//		generation = i;
	//		generationLabel.setText("Generation " + generation);
	//	}
	//	
	//	public void updateAverageFitnessDisplay(float af) {
	//		averageFitnessLabel.setText(String.format("Average Fitness = %3.3f", af));
	//	}
	//
	//	public World getWorld() {
	//		return world;
	//	}
	//
	//	public Fitnesses[] getFitnessValues() {
	//		Fitnesses[] values = new Fitnesses[spinners.length];
	//		int totalRelFitness = 0;
	//		for (int i = 0; i < spinners.length; i++) {
	//			values[i] = new Fitnesses();
	//			int x = ((Integer)spinners[i].getValue()).intValue();
	//			values[i].relFit = x;
	//			totalRelFitness += x;
	//		}
	//		for (int i = 0; i < spinners.length; i++) {
	//			int x = ((Integer)spinners[i].getValue()).intValue();
	//			values[i].absFit = ((double)x)/((double)totalRelFitness);
	//		}		
	//		return values;
	//	}
	//
	//	public void clearSelection() {
	//		world.clearSelectedOrganism();
	//		world.repaint();
	//	}
	//	
	//	// this is when an organism dies because one or both of its proteins
	//	//   is folded in a corner
	//	public void anOrganismDied() {
	//		totalNumberOfDeadOrganisms++;
	//	}
	//	
	//	public void startEvolving(boolean oneGenerationOnly) {
	//		world.updateCounts();
	//		updateColorCountDisplay();
	//		mge.getProgressBar().setMinimum(0);
	//		mge.getProgressBar().setMaximum(preferences.getWorldSize() * preferences.getWorldSize());
	//		mge.setButtonStatusWhileEvolving();
	//		evolver = new Evolver(mge);
	//		if (oneGenerationOnly) evolver.setOneGenerationOnly();
	//		Thread t = new Thread(evolver);
	//		t.start();
	//		evolverTimer.start();
	//	}
	//	
	//	private class EvolverTimerListener implements ActionListener {
	//		public void actionPerformed(ActionEvent arg0) {
	//			if (evolver.done()) {
	//				evolverTimer.stop();
	//				mge.setCursor(
	//						Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	//				mge.getProgressBar().setValue(0);
	//				world.updateCounts();
	//				updateColorCountDisplay();
	//				pauseButton.setEnabled(false);
	//				runButton.setEnabled(true);
	//				loadButton.setEnabled(true);
	//				setFitnessSpinnersEnabled(true);
	//				stopEvolving();
	//			} else {
	//				mge.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	//				mge.getProgressBar().setValue(evolver.getProgress());
	//				DecimalFormat myFormatter = new DecimalFormat("###,###");
	//				String output = myFormatter.format(FoldedProteinArchive.getTotalFoldedSequences());
	//				mge.getFoldingStatsLabel().setText("Of the " + output
	//						+ " protein sequences folded, " 
	//						+ totalNumberOfDeadOrganisms
	//						+ " were lethal.");
	//			}
	//		}
	//	}
	//
	//	public void stopEvolving() {
	//		evolver.stop();
	//		evolverTimer.stop();
	//		mge.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	//		mge.restoreButtonStatusWhenDoneEvolving();
	//	}
	//	
	//
	//	public void saveWorldToFile() {
	//		if (world.getThinOrganism(0, 0) == null) {
	//			Toolkit.getDefaultToolkit().beep();
	//			return;
	//		}
	//		JFileChooser outfileChooser = new JFileChooser(
	//				System.getProperty("user.home") + "/Desktop");
	//		outfileChooser.setDialogTitle("Enter a file name...");
	//		int resultVal = outfileChooser.showSaveDialog(this);
	//		if (resultVal == JFileChooser.APPROVE_OPTION) {
	//			File outFile = outfileChooser.getSelectedFile();
	//			Writer output = null;
	//			try {
	//				output = new BufferedWriter(new FileWriter(outFile) );
	//				output.write("Aipotu world file\n");
	//				output.write("#" + preferences.getWorldSize() + "\n");
	//				output.write("X,Y,Gene#,DNA,Protein,R,G,B\n");
	//				for (int x = 0; x < preferences.getWorldSize(); x++) {
	//					for (int y = 0; y < preferences.getWorldSize(); y++) {
	//						Organism o = organismFactory.createOrganism(
	//								world.getThinOrganism(x, y));
	//						output.write(x + "," + y + ",0,");
	//						output.write(o.getGene1().getExpressedGene().getDNA() + ",");
	//						output.write(
	//								o.getGene1()
	//								.getFoldedProteinWithImages()
	//								.getAaSeq() + ",");
	//						output.write(
	//								o.getGene1().getFoldedProteinWithImages().getColor().getRed() 
	//								+ ",");
	//						output.write(
	//								o.getGene1().getFoldedProteinWithImages().getColor().getGreen() 
	//								+ ",");
	//						output.write(
	//								o.getGene1().getFoldedProteinWithImages().getColor().getBlue() 
	//								+ "\n");
	//
	//						output.write(x + "," + y + ",1,");
	//						output.write(o.getGene2().getExpressedGene().getDNA() + ",");
	//						output.write(
	//								o.getGene2()
	//								.getFoldedProteinWithImages().getAaSeq() + ",");
	//						output.write(
	//								o.getGene2().getFoldedProteinWithImages().getColor().getRed() 
	//								+ ",");
	//						output.write(
	//								o.getGene2().getFoldedProteinWithImages().getColor().getGreen() 
	//								+ ",");
	//						output.write(
	//								o.getGene2().getFoldedProteinWithImages().getColor().getBlue() 
	//								+ "\n");
	//					}
	//				}
	//			}
	//			catch (Exception e) {
	//				e.printStackTrace();
	//			}
	//			finally {
	//				if (output != null)
	//					try {
	//						output.close();
	//					} catch (IOException e) {
	//						e.printStackTrace();
	//					}
	//			}
	//		}
	//	}
	//
	//	public void loadWorldFromFile() {
	//
	//		ThinOrganism[][] newWorld = null;
	//
	//		String DNA1 = "";
	//		String DNA2 = "";
	//		Color color1 = null;
	//		Color color2 = null;
	//
	//		JFileChooser infileChooser = new JFileChooser();
	//		infileChooser.setDialogTitle(
	//		"Choose a file of organisms to load into the world");
	//		int resultVal = infileChooser.showOpenDialog(this);
	//		if (resultVal == JFileChooser.APPROVE_OPTION) {
	//			File infile = infileChooser.getSelectedFile();
	//			BufferedReader input = null;
	//			try {
	//				input = new BufferedReader(new FileReader(infile));
	//				String line = null;
	//				boolean haveReadFirstLine = false;
	//				boolean haveSetWorldSize = false;
	//				while ((line = input.readLine()) != null) {
	//					//check first line to be sure it's a world file
	//					if (!line.equals("Aipotu world file") && !haveReadFirstLine) {
	//						JOptionPane.showMessageDialog(
	//								null, 
	//								"<html>Aipotu cannot read the file,<br>"
	//								+ "it is probably the wrong format.<br>"
	//								+ "You should check it or try another.</html>",
	//								"Unreadable file format", 
	//								JOptionPane.WARNING_MESSAGE);
	//						break;
	//					} else {
	//						haveReadFirstLine = true;
	//					}
	//
	//					// read second line to get world size
	//					if (line.startsWith("#")) {
	//						line = line.replaceAll("#", "");
	//						int worldSize = Integer.parseInt(line);
	//						newWorld = new ThinOrganism[worldSize][worldSize];
	//						haveSetWorldSize = true;
	//					}
	//
	//					//now, the rest - parse each line
	//					if (haveSetWorldSize) {
	//						//ignore the header line
	//						if (!line.startsWith("X")) {
	//							String[] lineParts = line.split(",");
	//							if (lineParts.length == 8) {
	//								int x = Integer.parseInt(lineParts[0]);
	//								int y = Integer.parseInt(lineParts[1]);
	//								int geneNum = Integer.parseInt(lineParts[2]);
	//								if (geneNum == 0) {
	//									DNA1 = lineParts[3];
	//									color1 = new Color(
	//											Integer.parseInt(lineParts[5]),
	//											Integer.parseInt(lineParts[6]),
	//											Integer.parseInt(lineParts[7]));
	//								} else {
	//									DNA2 = lineParts[3];
	//									color2 = new Color(
	//											Integer.parseInt(lineParts[5]),
	//											Integer.parseInt(lineParts[6]),
	//											Integer.parseInt(lineParts[7]));
	//									newWorld[x][y] = 
	//										new ThinOrganism(
	//												DNA1, 
	//												DNA2, 
	//												color1,
	//												color2,
	//												GlobalDefaults.colorModel.mixTwoColors(
	//														color1, color2));
	//								}
	//							}
	//						}
	//					}
	//				}
	//			} catch (IOException e) {
	//				e.printStackTrace();
	//			}
	//			finally {
	//				try {
	//					if (input!= null) {
	//						input.close();
	//					}
	//				}
	//				catch (IOException ex) {
	//					ex.printStackTrace();
	//				}
	//			}
	//		}
	//		world.setOrganisms(newWorld);
	//		setReadyToRun();
	//		world.repaint();
	//		world.updateCounts();
	//		updateColorCountDisplay();
	//	}
	//	
	//	public BufferedImage takeSnapshot() {
	//		BufferedImage imageBuffer = new BufferedImage(
	//				this.getWidth(),
	//				this.getHeight(),
	//				BufferedImage.TYPE_INT_RGB);
	//		Graphics g = imageBuffer.getGraphics();
	//		this.paint(g);
	//		return imageBuffer;
	//	}
	//
	//	
	//	/*
	//	 * event handler when fitness spinners changed
	//	 */
	//	public void stateChanged(ChangeEvent arg0) {
	//		updateAbsFitnessDisplay(getFitnessValues());
	//	}
	//
	//	private void updateAbsFitnessDisplay(Fitnesses[] fitnesses) {
	//		for (int i = 0; i < spinners.length; i++) {
	//			absFitLabels[i].setText(String.format("%3.3f", fitnesses[i].absFit));
	//		}		
	//
	//	}
}
