package phylogenySurvey;

import java.awt.Container;

import javax.swing.JPanel;

public class SurveyUI {
	
	private Container masterContainer;
	private JPanel workPanel;
	
	public SurveyUI(Container masterContainer) {
		this.masterContainer = masterContainer;
	}
	
	public void setupUI() {
		workPanel = new JPanel();
		masterContainer.add(workPanel);
	}

}
