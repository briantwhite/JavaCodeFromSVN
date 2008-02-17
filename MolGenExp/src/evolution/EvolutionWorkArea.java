package evolution;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import molGenExp.MolGenExp;

public class EvolutionWorkArea extends JPanel {
	
	private MolGenExp mge;
	private JPanel leftPanel;
	private JPanel controlPanel;
	private JButton startButton;
	private JButton stopButton;
	private JPanel fitnessPanel;
	private JPanel rightPanel;
	private World world;
	private JLabel generationLabel;
	private int generation = 0;
	
	public EvolutionWorkArea(MolGenExp mge) {
		this.mge = mge;
		setupUI();
	}
	
	private void setupUI() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(Box.createRigidArea(new Dimension(200,1)));
		
		fitnessPanel = new JPanel();
		fitnessPanel.setBorder(BorderFactory.createTitledBorder("Fitness Selection"));
		Color backgroundColor = new Color(128,128,128);
		fitnessPanel.setBackground(backgroundColor);
		fitnessPanel.setLayout(new SpringLayout());
		String[] colorList = {"White", "Blue", "Yellow", "Green",
				"Red", "Purple", "Orange", "Black"};
		ColorFitnessSlider[] sliders = new ColorFitnessSlider[colorList.length];
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
		startButton = new JButton("Start");
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
		
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startButton.setEnabled(false);
				stopButton.setEnabled(true);
				mge.startEvolving();
			}
		});
		
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopButton.setEnabled(false);
				startButton.setEnabled(true);
			}
		});

	}
	
	public boolean running() {
		return startButton.isSelected();
	}
	
	public void setGeneration(int i) {
		generation = i;
		generationLabel.setText("Generation " + generation);
	}

}
