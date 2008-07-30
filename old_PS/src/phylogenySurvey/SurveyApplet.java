package phylogenySurvey;

import javax.swing.JApplet;

public class SurveyApplet extends JApplet {
	
	private SurveyUI surveyUI;
	
	public void init() {
		surveyUI = new SurveyUI(this.getContentPane());
		surveyUI.setupUI();
	}

}
