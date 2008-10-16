package phylogenySurvey;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

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
	
	public String getTreeXML() {
		String result = "";
		try {
			result = URLEncoder.encode(surveyUI.getState(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void setTreeXML(String newTreeXML) {
		try {
			surveyUI.setState(URLDecoder.decode(newTreeXML, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
