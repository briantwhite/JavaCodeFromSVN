package evolution;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import preferences.MGEPreferences;

import utilities.GlobalDefaults;

import molGenExp.MolGenExp;
import molGenExp.Organism;

public class EvolutionWorkArea extends JPanel {
	
	private MolGenExp mge;
	private MGEPreferences preferences;
	
	private JPanel leftPanel;
	private JPanel controlPanel;
	private JButton loadButton;
	private JButton startButton;
	private JButton stopButton;
	private JPanel fitnessPanel;
	private JPanel rightPanel;
	private World world;
	private JLabel generationLabel;
	private int generation = 0;
	private boolean running = false;
	
	String[] colorList = {"White", "Blue", "Yellow", "Green",
			"Red", "Purple", "Orange", "Black"};
	ColorFitnessSlider[] sliders = new ColorFitnessSlider[colorList.length];

	
	public EvolutionWorkArea(MolGenExp mge) {
		this.mge = mge;
		preferences = MGEPreferences.getInstance();
		setupUI();
	}
	
	private void setupUI() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(Box.createRigidArea(new Dimension(200,1)));
		
		fitnessPanel = new JPanel();
		fitnessPanel.setBorder(BorderFactory.createTitledBorder("Relative Fitness Selection"));
		Color backgroundColor = new Color(128,128,128);
		fitnessPanel.setBackground(backgroundColor);
		fitnessPanel.setLayout(new SpringLayout());
		JLabel[] colorLabels = new JLabel[colorList.length];
		for (int i = 0; i < colorList.length; i++) {
			sliders[i] = new ColorFitnessSlider(colorList[i]);
			colorLabels[i] = new JLabel(sliders[i].getColorString());
			colorLabels[i].setBackground(backgroundColor);
			colorLabels[i].setForeground(sliders[i].getColor());
			colorLabels[i].setOpaque(true);
			fitnessPanel.add(colorLabels[i]);
			colorLabels[i].setLabelFor(sliders[i]);
			fitnessPanel.add(sliders[i]);
		}
		SpringUtilities.makeCompactGrid(fitnessPanel,
				colorList.length, 2,
				6, 6,
				6, 6);
		
		leftPanel.add(fitnessPanel);
		
		controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
		controlPanel.setBorder(BorderFactory.createTitledBorder("Controls"));
		loadButton = new JButton("Load");
		controlPanel.add(loadButton);
		startButton = new JButton("Start");
		startButton.setEnabled(false);
		controlPanel.add(startButton);
		stopButton = new JButton("Stop");
		stopButton.setEnabled(false);
		controlPanel.add(stopButton);
		leftPanel.add(controlPanel);
				
		this.add(leftPanel);
		
		rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setBorder(BorderFactory.createTitledBorder("World"));

		rightPanel.add(Box.createRigidArea(new Dimension(500,1)));
		world = new World();
		rightPanel.add(world);
		generationLabel = new JLabel("Generation 0");
		rightPanel.add(generationLabel);

		this.add(rightPanel);
		
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mge.loadSelectedIntoWorld();
				mge.getGreenhouse().clearSelection();
			}
		});

		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startButton.setEnabled(false);
				stopButton.setEnabled(true);
				loadButton.setEnabled(false);
				running = true;
				mge.startEvolving();
			}
		});
		
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopButton.setEnabled(false);
				startButton.setEnabled(true);
				loadButton.setEnabled(true);
				running = false;
				mge.stopEvolving();
			}
		});

	}
	
	public boolean running() {
		return running;
	}
	
	public void setReadyToRun() {
		startButton.setEnabled(true);
		stopButton.setEnabled(false);
		running = false;
	}
	
	public void updateGenerationLabel() {
		generation++;
		generationLabel.setText("Generation " + generation);
	}
	
	public int getGeneration() {
		return generation;
	}
	
	public World getWorld() {
		return world;
	}
	
	public int[] getFitnessValues() {
		int[] values = new int[sliders.length];
		for (int i = 0; i < sliders.length; i++) {
			values[i] = sliders[i].getValue();
		}
		return values;
	}
		
	public void clearSelection() {
		world.clearSelectedOrganism();
	}

	public void saveWorldToFile() {
		if (world.getThinOrganism(0, 0) == null) {
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		JFileChooser outfileChooser = new JFileChooser();
		int resultVal = outfileChooser.showSaveDialog(this);
		if (resultVal == JFileChooser.APPROVE_OPTION) {
			File outFile = outfileChooser.getSelectedFile();
			Writer output = null;
			try {
				output = new BufferedWriter(new FileWriter(outFile) );
				output.write("X,Y,Gene#,DNA,Protein,R,G,B\n");
				for (int x = 0; x < preferences.getWorldSize(); x++) {
					for (int y = 0; y < preferences.getWorldSize(); y++) {
						Organism o = new Organism(world.getThinOrganism(x, y));
						output.write(x + "," + y + ",0,");
						output.write(o.getGene1().getExpressedGene().getDNA() + ",");
						output.write(o.getGene1().getFoldedPolypeptide().getAaSeq() + ",");
						output.write(
								o.getGene1().getFoldedPolypeptide().getColor().getRed() 
								+ ",");
						output.write(
								o.getGene1().getFoldedPolypeptide().getColor().getGreen() 
								+ ",");
						output.write(
								o.getGene1().getFoldedPolypeptide().getColor().getBlue() 
								+ "\n");
						
						output.write(x + "," + y + ",1,");
						output.write(o.getGene2().getExpressedGene().getDNA() + ",");
						output.write(o.getGene2().getFoldedPolypeptide().getAaSeq() + ",");
						output.write(
								o.getGene2().getFoldedPolypeptide().getColor().getRed() 
								+ ",");
						output.write(
								o.getGene2().getFoldedPolypeptide().getColor().getGreen() 
								+ ",");
						output.write(
								o.getGene2().getFoldedPolypeptide().getColor().getBlue() 
								+ "\n");
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (output != null)
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}

		}

	}
}
