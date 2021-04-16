package edu.umb.jsAipotu.client.evolution;

import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;

import edu.umb.jsAipotu.client.molGenExp.MolGenExp;
import edu.umb.jsAipotu.client.preferences.GlobalDefaults;

public class FitnessSettingsPanel extends CaptionPanel {
	
	private MolGenExp mge;
	
	private ColorFitnessSpinner[] spinners = new ColorFitnessSpinner[GlobalDefaults.colorList.length];
	private HTML[] absoluteFitnessValues = new HTML[GlobalDefaults.colorList.length];
	private HTML[] populationCounts = new HTML[GlobalDefaults.colorList.length];
	
	public FitnessSettingsPanel(MolGenExp mge) {
		super("Color Fitness and Population Counts");
		this.mge = mge;
		buildUI();
	}

	private void buildUI() {
		this.setStyleName("fitnessSettingPanel");
		
		Grid mainGrid = new Grid(9, 4);
		this.setContentWidget(mainGrid);
		
		mainGrid.setWidget(0, 0, new HTML("<b><u>Color</b></u>"));
		mainGrid.setWidget(0, 1, new HTML("<b><u>Relative Fitness</b></u>"));
		mainGrid.setWidget(0, 2, new HTML("<b><u>Absolute Fitness</b></u>"));
		mainGrid.setWidget(0, 3, new HTML("<b><u>Population Count</b></u>"));
		for (int i = 0; i < GlobalDefaults.colorList.length; i++) {
			spinners[i] = new ColorFitnessSpinner(GlobalDefaults.colorList[i]);
			mainGrid.setWidget(i + 1, 0, new HTML("<font color=\"" + spinners[i].getColorHTML() + "\">" + spinners[i].getColorString() + "</font>")); 
			mainGrid.setWidget(i + 1, 1, spinners[i]);
			absoluteFitnessValues[i] = new HTML("1");
			mainGrid.setWidget(i + 1, 2, absoluteFitnessValues[i]);
			populationCounts[i] = new HTML("0");
			mainGrid.setWidget(i + 1, 3, populationCounts[i]);
		}
	}
}
