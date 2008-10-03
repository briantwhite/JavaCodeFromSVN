package phylogenySurvey;

import javax.swing.JApplet;

public class SurveyApplet extends JApplet {
	
	private SurveyUI surveyUI;
	
	public void init() {
		boolean scoringEnabled = false;
		String scoreModeString = getParameter("Scoring");
		String password = getParameter("Password");
		if ((scoreModeString != null) && (password != null)) {
			if (scoreModeString.equals("true")) {
				scoringEnabled = true;
			}
		}
		setSize(800, 800);
		surveyUI = new SurveyUI(this.getContentPane());
		surveyUI.setupUI(scoringEnabled, password);
	}

}
