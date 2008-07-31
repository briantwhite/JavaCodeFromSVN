package phylogenySurvey;

import javax.swing.JApplet;

public class SurveyApplet extends JApplet {
	
	private SurveyUI surveyUI;
	
	public void init() {
		setSize(500, 500);
		surveyUI = new SurveyUI(this.getContentPane());
		surveyUI.setupUI();
	}

}
