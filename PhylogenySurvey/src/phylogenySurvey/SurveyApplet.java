package phylogenySurvey;

import javax.swing.JApplet;

public class SurveyApplet extends JApplet {
	
	private SurveyUI surveyUI;
	
	public void init() {
		setSize(800, 800);
		surveyUI = new SurveyUI(this.getContentPane());
		surveyUI.setupUI();
	}

}
