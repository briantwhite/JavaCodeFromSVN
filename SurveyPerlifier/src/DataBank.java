
import java.util.ArrayList;

public class DataBank {

	String surveyHtmlFileName;

	String surveyFormPerlCode;

	String surveyTxtFilePerlCode;

	String surveyErrorCheckCode;

	ArrayList surveyHtmlFileLines;

	ArrayList questionList;

	StringBuffer questionInfo;

	String namesTxt; // file of names & Id #s

	String surveyDir; // directory to save survey files in

	String scriptURL; // base URL for survey scripts

	boolean prePost; // if true, then both pre & post survey

	String preSurveyHeadline; // headline for pre survey

	String postSurveyHeadline; // headline for post survey

	String getnamePlName; // name for entry/authentication script

	String surveyPlName; // name for survey script

	String outputDir;

	String startPreSurveyDate;

	String endPreSurveyDate;

	String startPostSurveyDate;

	String endPostSurveyDate;

	String getnamePl; //the entry & verification script

	String surveyPl; // the survey script

	public DataBank() {

		surveyHtmlFileName = new String("");
		surveyFormPerlCode = new String("");
		surveyTxtFilePerlCode = new String("");
		surveyHtmlFileLines = new ArrayList();
		surveyErrorCheckCode = new String("");
		questionList = new ArrayList();
		questionInfo = new StringBuffer();
		namesTxt = new String("/usr/local/data/names.txt");
		surveyDir = new String("/usr/local/surveys/");
		scriptURL = new String("https://www.securebio.umb.edu/cgi-bin/");
		prePost = true;
		preSurveyHeadline = new String("Pre Survey");
		postSurveyHeadline = new String("Post Survey");
		getnamePlName = new String("getname.pl");
		surveyPlName = new String("survey.pl");
		outputDir = new String("");
		startPreSurveyDate = new String("9/12");
		endPreSurveyDate = new String("9/22");
		startPostSurveyDate = new String("11/12");
		endPostSurveyDate = new String("11/22");
		getnamePl = new String("");
		surveyPl = new String("");
	}

	// methods for accesing the part of the perl script that prints the survey
	// to the web page
	public void setSurveyFormPerlCode(String text) {
		surveyFormPerlCode = text;
	}

	public String getSurveyFormPerlCode() {
		return surveyFormPerlCode;
	}

	// methods for accesing the perl script that gets user name & id#
	public void setGetnamePl(String text) {
		getnamePl = text;
	}

	public String getGetnamePl() {
		return getnamePl;
	}

	// methods for accesing the survey perl script
	public void setSurveyPl(String text) {
		surveyPl = text;
	}

	public String getSurveyPl() {
		return surveyPl;
	}

	// methods for accessing the part of the perl script that saves the survey
	// as a text file
	public void setSurveyTxtFilePerlCode(String text) {
		surveyTxtFilePerlCode = text;
	}

	public String getSurveyTxtFilePerlCode() {
		return surveyTxtFilePerlCode;
	}

	// methods for accessing the part of the perl script that verifies user
	// input
	public void setErrorCheckCode(String text) {
		surveyErrorCheckCode = text;
	}

	public String getErrorCheckCode() {
		return surveyErrorCheckCode;
	}

	//methods for dealing with the html input survey file name
	public String getSurveyHtmlFileName() {
		return surveyHtmlFileName;
	}

	public void setSurveyHtmlFileName(String newVal) {
		surveyHtmlFileName = newVal;
	}

	// methods for dealing with list of lines in html survey file
	public void clearSurveyHtmlLines() {
		surveyHtmlFileLines.clear();
	}

	public String getLineFromSurvey(int i) {
		return (String) surveyHtmlFileLines.get(i);
	}

	public int getNumSurveyHtmlLines() {
		return surveyHtmlFileLines.size();
	}

	public void addLineToSurveyHtml(String nextLine) {
		surveyHtmlFileLines.add(nextLine);
	}

	// methods for dealing with the list of questions
	public void clearQuestionList() {
		questionList.clear();
	}

	public void addNewQuestion(String question) {
		questionList.add(question);
	}

	public int getNumQuestions() {
		return questionList.size();
	}

	public String getAQuestion(int i) {
		return (String) questionList.get(i);
	}

	// methods for dealing with the list of question info
	public void clearQuestionInfo() {
		questionInfo = new StringBuffer();
	}

	public void addQuestionInfoLine(String line) {
		questionInfo.append(line + "\n");
	}

	public String getQuestionInfo() {
		return questionInfo.toString();
	}
}