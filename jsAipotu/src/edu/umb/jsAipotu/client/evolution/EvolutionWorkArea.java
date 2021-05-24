package edu.umb.jsAipotu.client.evolution;

import java.util.ArrayList;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.umb.jsAipotu.client.biochem.FoldingException;
import edu.umb.jsAipotu.client.molGenExp.MolGenExp;
import edu.umb.jsAipotu.client.molGenExp.OrganismUI;
import edu.umb.jsAipotu.client.preferences.GlobalDefaults;
import edu.umb.jsAipotu.client.preferences.MGEPreferences;

public class EvolutionWorkArea extends HorizontalPanel {

	private MolGenExp mge;

	private HTML generationDisplay;
	private int generation;
	private HTML avgFitnessDisplay;

	private World world;
	private FitnessSettingsPanel fitnessSettingsPanel;

	private Button loadButton;
	private Button clearButton;
	private Button oneGenButton;
	
	private CheckBox mutationsEnabledCheckbox;


	public EvolutionWorkArea(MolGenExp mge) {
		super();
		this.mge = mge;
		generation = 0;
		setupUI();
	}

	private void setupUI() {

		VerticalPanel leftPanel = new VerticalPanel();

		fitnessSettingsPanel = new FitnessSettingsPanel(mge);
		leftPanel.add(fitnessSettingsPanel);

		CaptionPanel controlPanel = new CaptionPanel("Controls");
		HorizontalPanel upperButtonPanel = new HorizontalPanel();

		loadButton = new Button("Load");
		clearButton = new Button("Clear World");
		oneGenButton = new Button("Run One Generation");

		loadButton.setStyleName("evolutionButton");
		loadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadWorldFromGreenhouse();
				clearButton.setEnabled(true);
				oneGenButton.setEnabled(true);
			}
		});
		upperButtonPanel.add(loadButton);

		clearButton.setEnabled(false);
		clearButton.setStyleName("evolutionButton");
		clearButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boolean confirm = Window.confirm("Are you sure you want to delete all organsims in the World?\nYou cannot undo this.");
				if (confirm) {
					world.initialize();
					generation = 0;
					generationDisplay.setHTML(String.valueOf(generation));
					loadButton.setEnabled(true);
					oneGenButton.setEnabled(false);
					clearButton.setEnabled(false);
					world.updateCounts();
					fitnessSettingsPanel.updateColorCountDisplay();
					fitnessSettingsPanel.updateAbsoluteFitnesses();
					updateAverageFitnessDisplay();
				}
			}
		});
		upperButtonPanel.add(clearButton);

		oneGenButton.setEnabled(false);
		oneGenButton.setStyleName("evolutionButton");
		oneGenButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				EvolveCommand ec = new EvolveCommand(mge.getjsAipotu().getEvolutionWorkArea());
				Scheduler.get().scheduleIncremental(ec);
			}
		});
		upperButtonPanel.add(oneGenButton);
		
		VerticalPanel buttonWrapperPanel = new VerticalPanel();
		buttonWrapperPanel.add(upperButtonPanel);
		mutationsEnabledCheckbox = new CheckBox("Mutations Enabled");
		buttonWrapperPanel.add(mutationsEnabledCheckbox);

		controlPanel.setContentWidget(buttonWrapperPanel);
		leftPanel.add(controlPanel);

		HorizontalPanel generationDisplayPanel = new HorizontalPanel();
		generationDisplay = new HTML(String.valueOf(generation));
		generationDisplayPanel.add(new HTML("Generation:&nbsp;"));
		generationDisplayPanel.add(generationDisplay);
		leftPanel.add(generationDisplayPanel);

		HorizontalPanel avgFitDispPanel = new HorizontalPanel();
		avgFitnessDisplay = new HTML("");
		avgFitDispPanel.add(new HTML("Average fitness =&nbsp;"));
		avgFitDispPanel.add(avgFitnessDisplay);
		leftPanel.add(avgFitDispPanel);

		add(leftPanel);

		world = new World(mge);
		add(world);

	}

	private void loadWorldFromGreenhouse() {
		ArrayList<OrganismUI> orgUIs = mge.getGreenhouse().getAllSelectedOrganisms();
		if (orgUIs.size() > 0) {
			world.initialize(orgUIs);
			mge.getGreenhouse().clearAllSelections();
			world.updateCounts();
			fitnessSettingsPanel.updateColorCountDisplay();
			fitnessSettingsPanel.updateAbsoluteFitnesses();
			updateAverageFitnessDisplay();
			generation = 0;
			generationDisplay.setHTML(String.valueOf(generation));
			loadButton.setEnabled(false);
		}
	}

	public void updateAverageFitnessDisplay() {
		float averageFitness = 0.0f;
		for (int i = 0; i < GlobalDefaults.colorList.length; i++) {
			float fractionOfPopulation = ((float)ColorCountsRecorder.getInstance().getCount(
					GlobalDefaults.colorModel.getColorFromString(GlobalDefaults.colorList[i])))/100;
			averageFitness = averageFitness + (((float)fitnessSettingsPanel.getAbsoluteFitnesses()[i]) * fractionOfPopulation);
		}
		NumberFormat f = NumberFormat.getFormat("0.000");
		String avgFitString = f.format(averageFitness);
		avgFitnessDisplay.setHTML(avgFitString);
	}

	public void updateCountsAndDisplays() {
		world.updateCounts();
		fitnessSettingsPanel.updateColorCountDisplay();
		fitnessSettingsPanel.updateAbsoluteFitnesses();
		updateAverageFitnessDisplay();
		updateGenerationCount();
	}

	public void updateGenerationCount() {
		generation++;
		generationDisplay.setHTML(String.valueOf(generation));
	}

	public World getWorld() {
		return world;
	}

	public FitnessSettingsPanel getFitnessSettingsPanel() {
		return fitnessSettingsPanel;
	}
	
	public boolean mutationsEnabled() {
		return mutationsEnabledCheckbox.getValue();
	}
	
	public void saveOrganismToGreenhouse() {
		try {
			mge.saveOrganismToGreenhouse(world.getSelectedOrganism());
		} catch (FoldingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
