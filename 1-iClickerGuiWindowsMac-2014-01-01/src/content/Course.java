package content;

import java.io.*;
import java.util.ArrayList;
import java.util.Vector;

import javax.xml.stream.*;

import org.apache.commons.io.FileUtils;

import setting.enumoption.*;

import com.csvreader.*;

import gui.enumoption.EnumCorrectAnswer;
import iClickerDriver.*;

import app.*;

/**
 * A course.
 * @author Junhao
 *
 */
public class Course {
	// General
	private String courseName;
	private String instructorID;
	private FrequencyEnum frequencyEnum1;
	private FrequencyEnum frequencyEnum2;
	private FrequencyAlert frequencyAlert;
	private ShowResponseGrid showResponseGrid;
	private ShowResponsePattern showResponsePattern;
	// Our feature begins
	private boolean showChoice;
	private boolean showAttempts;
	// Our feature ends

	// Polling
	private TimerMode timerMode;
	private PollingTimer pollingTimer;
	
	// Results
	private CorrectAnswerDisplay correctAnswerDisplay;
	
	// Scoring
	private int sessionParticipationPoints;
	private ParticipationRequirement participationRequirement;
	private int participationBarValue;
	private int respondingPoints;
	private int correctRespondingPoints;
	private boolean sessionPointsLimit;
	private int totalPerformancePoints;
	private int performanceBarValue;
	
	// Base Display
	private MultipleChoiceDisplay multipleChoiceDisplay;
	private MultipleChoiceAlternateTime multipleChoiceAlternateTime;
	
	// Demographics
	private boolean autoAdvanceCheck;
	private AutoAdvanceCheckTime autoAdvanceCheckTime;
	
	private String configCsvPath;
	private String studentRecordCsvPath;
	private String demographicsXmlPath;

	private Test test;
	private Session session;
	private StudentList students;
	// The StudentList.csv is constructed when the course is created. If this file 
	// is deleted by the user, the following variable is set to false. If this file
	// is there but contains no record, the following variable is also set to false.
	private boolean hasRegisteredInfo;

	private ArrayList<ListQuestion> listQuestions;
	
	/**
	 * Constructor.
	 * @param courseName name of the course.
	 * @param test
	 * @throws Exception
	 */
	public Course(String courseName, Test test) throws Exception {
		this.configCsvPath 		  = "./Classes/" + courseName + "/SessionData/Config.csv";
		this.studentRecordCsvPath = "./Classes/" + courseName + "/StudentData/StudentList.csv";
	    this.demographicsXmlPath = "./Classes/" + courseName + "/Demographics.xml";
	    
		this.test = test;
		this.session = null;	    
	    
	    this.readConfig();
	    
	    this.students = new StudentList(this);
	    
	    this.listQuestions = new ArrayList<ListQuestion> ();
	}
	
	/**
	 * Generate course related resources when this course is created.
	 * @param courseName name of the course.
	 * @throws Exception 
	 */
	public static void createCourse(String courseName) throws Exception {
		String path = "./Classes/" + courseName + "/Images";
		if ((new File(path)).mkdirs() == false) {
			throw new Exception("Fail to create Imagges dir.");
		}
		
		path = "./Classes/" + courseName + "/Reports";
		if ((new File(path)).mkdirs() == false) {
			throw new Exception("Fail to create Reports dir.");
		}

		path = "./Classes/" + courseName + "/SessionData";
		if ((new File(path)).mkdirs() == false) {
			throw new Exception("Fail to create SessionData dir.");
		}
		
		path = "./Classes/" + courseName + "/StudentData";
		if ((new File(path)).mkdirs() == false) {
			throw new Exception("Fail to create StudentData dir.");
		}
		
		createStudentData(courseName);

		createConfigData(courseName);
		
		createDemographicsData(courseName);
	}

	/**
	 * Create StudentData folder and StudentList.csv file.
	 * @param courseName name of the course.
	 * @throws IOException 
	 */
	private static void createStudentData(String courseName) throws IOException {
		String studentRecordCsvPath = "./Classes/" + courseName + "/StudentData/StudentList.csv";
		
		CsvWriter csvWriter = new CsvWriter(studentRecordCsvPath);
		
		csvWriter.write("StudentId");
		csvWriter.write("First Name");
		csvWriter.write("Last Name");
		csvWriter.write("iClickerId");
		csvWriter.write("Lab Group");
		csvWriter.endRecord();
		
		csvWriter.flush();
		csvWriter.close();
	}
	
	/**
	 * Generate default configuration values and write them back to Config.CSV.
	 * @param courseName name of the course.
	 * @throws IOException 
	 */
	private static void createConfigData(String courseName) throws IOException {
		String configCsvPath = "./Classes/" + courseName + "/SessionData/Config.csv";
		
		CsvWriter csvWriter = new CsvWriter(configCsvPath);

		String field = "";	String value = "";
		
		// General
		field = "COURSENAME";	value = courseName;	csvWriteConfigRecord(csvWriter, field, value);
		field = "INSTRUCTORID";	value = "";	csvWriteConfigRecord(csvWriter, field, value);
		field = "FREQUENCY1";	value = FrequencyEnum.A.toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "FREQUENCY2";	value = FrequencyEnum.A.toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "FREQUENCYALERT";	value = FrequencyAlert.MANUALLY.toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "SHOWRESPONSEGRID";	value = ShowResponseGrid.HIDE.toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "SHOWRESPONSEPATTERN";	value = ShowResponsePattern.ICLICKERID.toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "SHOWCHOICE";	value = Boolean.FALSE.toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "SHOWATTEMPTS";	value = Boolean.FALSE.toString();	csvWriteConfigRecord(csvWriter, field, value);
				
		// Polling
		field = "TIMERMODE";	value = TimerMode.UP.toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "POLLINGTIMER";	value = PollingTimer.ZERO30.toString();	csvWriteConfigRecord(csvWriter, field, value);
		
		// Results
		field = "CORRECTANSWERDISPLAY";	value = CorrectAnswerDisplay.HIDE.toString();	csvWriteConfigRecord(csvWriter, field, value);
		
		// Scoring
		field = "SESSIONPARTICIPATION";	value = Integer.toString(1);	csvWriteConfigRecord(csvWriter, field, value);
		field = "PARTICIPATIONREQUIREMENT";	value = ParticipationRequirement.ATLEAST75PERCENT.toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "PARTICIPATIONBARVALUE";	value = Integer.toString(80);	csvWriteConfigRecord(csvWriter, field, value);
		field = "RESPONDINGPOINTS";	value = Integer.toString(0);	csvWriteConfigRecord(csvWriter, field, value);
		field = "CORRECTRESPONDINGPOINTS";	value = Integer.toString(1);	csvWriteConfigRecord(csvWriter, field, value);
		field = "SESSIONPOINTSLIMIT";	value = Boolean.FALSE.toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "TOTALPERFORMANCEPOINTS";	value = Integer.toString(Integer.MAX_VALUE);	csvWriteConfigRecord(csvWriter, field, value);
		field = "PERFORMANCEBARVALUE";	value = Integer.toString(75);	csvWriteConfigRecord(csvWriter, field, value);
		
		// Base Display
		field = "MULTIPLECHOICEDISPLAY";	value = MultipleChoiceDisplay.PERCENTAGE.toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "MULTIPLECHOICEALTERNATETIME";	value = MultipleChoiceAlternateTime.FIVE.toString();	csvWriteConfigRecord(csvWriter, field, value);
		
		// Demographics		
		field = "AUTOADVANCECHECK";	value = Boolean.FALSE.toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "AUTOADVANCECHECKTIME";	value = AutoAdvanceCheckTime.ZERO30.toString();	csvWriteConfigRecord(csvWriter, field, value);
		
		csvWriter.flush();
		csvWriter.close();
	}
	
	/**
	 * Generate default configuration demographics data and save them in Demographics.xml.
	 * @param courseName name of the course.
	 * @throws FileNotFoundException 
	 * @throws XMLStreamException 
	 */
	private static void createDemographicsData(String courseName) throws FileNotFoundException, XMLStreamException {
		String demographicsXmlPath  = "./Classes/" + courseName + "/Demographics.xml";
	    
    	FileOutputStream fos = new FileOutputStream(demographicsXmlPath);
    	XMLOutputFactory xmlOutFact = XMLOutputFactory.newInstance();
    	XMLStreamWriter writer = xmlOutFact.createXMLStreamWriter(fos, "UTF-8");
    	
    	writer.writeStartDocument("UTF-8", "1.0");
		writer.writeStartElement("QuestionList");
		writer.writeAttribute("AutoAdvance", "False");
		writer.writeEndDocument();

        writer.flush();
        writer.close();
	}
	
	/**
	 * Modify the name of the folder that associated with the selected 
	 * course, as well as the course name in the .csv file.
	 * @param oldName old name of the course.
	 * @param newName new name of the course.
	 * @throws IOException 
	 */
	public static void modifyCourse(String oldName, String newName) throws IOException {
    	// Rename course folder
    	File oldFile = new File("./Classes/" + oldName);
    	File newFile = new File("./Classes/" + newName);
    	oldFile.renameTo(newFile);
    	
    	// Change the course name in config.csv.
		String fileNameSrc = "./Classes/" + newName + "/SessionData/Config.csv";
		String fileNameDes = "./Classes/" + newName + "/SessionData/Config.tmp.csv";
		
		CsvReader csvReader = new CsvReader(fileNameSrc);
		CsvWriter csvWriter = new CsvWriter(fileNameDes);
		
		csvReader.readRecord();
		Course.csvWriteConfigRecord(csvWriter, "COURSENAME", newName);
		
		for (int i = 0; i < 45; i++) {
			Question.copyRecord(csvReader, csvWriter, true);
		}
		
		csvWriter.flush();
		csvWriter.close();
		csvReader.close();
		
		File f = new File(fileNameSrc);
		f.delete();
		
		File fileFrom = new File(fileNameDes);
		File fileto   = new File(fileNameSrc);
		fileFrom.renameTo(fileto);
	}
	
	/**
	 * Delete a course.
	 * @param courseName name of the course.
	 * @throws IOException
	 */
	public static void delectCourse(String courseName) throws IOException {
		FileUtils.deleteDirectory(new File("./Classes/" + courseName));
	}
	
    /**
     * Get courses from file system.
     */
	public static Vector<String> getCourseList() {
		Vector<String> courseList = new Vector<String> ();
		
        File dir = new File("./Classes/");
        File listDir[] = dir.listFiles();
        for (int i = 0; i < listDir.length; i++) {
        	if (listDir[i].isDirectory()) {
        		courseList.add(listDir[i].getName());
        	}
        }
        
        return courseList;
	}
	
	/**
	 * Write configuration back to Config.CSV.
	 * @throws IOException 
	 * @throws XMLStreamException 
	 */
	public void writeConfig() throws IOException, XMLStreamException {
		CsvWriter csvWriter = new CsvWriter(configCsvPath);

		String field = "";	String value = "";
		
		// General
		field = "COURSENAME";	value = this.courseName;	csvWriteConfigRecord(csvWriter, field, value);
		field = "INSTRUCTORID";	value = this.instructorID;	csvWriteConfigRecord(csvWriter, field, value);
		field = "FREQUENCY1";	value = this.frequencyEnum1.toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "FREQUENCY2";	value = this.frequencyEnum2.toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "FREQUENCYALERT";	value = this.frequencyAlert.toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "SHOWRESPONSEGRID";	value = this.showResponseGrid.toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "SHOWRESPONSEPATTERN";	value = this.showResponsePattern.toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "SHOWCHOICE";	value = Boolean.valueOf(this.showChoice).toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "SHOWATTEMPTS";	value = Boolean.valueOf(this.showAttempts).toString();	csvWriteConfigRecord(csvWriter, field, value);
				
		// Polling
		field = "TIMERMODE";	value = this.timerMode.toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "POLLINGTIMER";	value = this.pollingTimer.toString();	csvWriteConfigRecord(csvWriter, field, value);
		
		// Results
		field = "CORRECTANSWERDISPLAY";	value = this.correctAnswerDisplay.toString();	csvWriteConfigRecord(csvWriter, field, value);
		
		// Scoring
		field = "SESSIONPARTICIPATION";	value = Integer.toString(this.sessionParticipationPoints);	csvWriteConfigRecord(csvWriter, field, value);
		field = "PARTICIPATIONREQUIREMENT";	value = this.participationRequirement.toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "PARTICIPATIONBARVALUE";	value = Integer.valueOf(this.participationBarValue).toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "RESPONDINGPOINTS";	value = Integer.toString(this.respondingPoints).toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "CORRECTRESPONDINGPOINTS";	value = Integer.toString(this.correctRespondingPoints).toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "SESSIONPOINTSLIMIT";	value = Boolean.valueOf(this.sessionPointsLimit).toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "TOTALPERFORMANCEPOINTS";	value = Integer.toString(this.totalPerformancePoints).toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "PERFORMANCEBARVALUE";	value = Integer.valueOf(this.performanceBarValue).toString();	csvWriteConfigRecord(csvWriter, field, value);
		
		// Base Display
		field = "MULTIPLECHOICEDISPLAY";	value = this.multipleChoiceDisplay.toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "MULTIPLECHOICEALTERNATETIME";	value = this.multipleChoiceAlternateTime.toString();	csvWriteConfigRecord(csvWriter, field, value);
		
		// Demographics
		writeDemographicsQuestions();
		
		field = "AUTOADVANCECHECK";	value = Boolean.valueOf(this.autoAdvanceCheck).toString();	csvWriteConfigRecord(csvWriter, field, value);
		field = "AUTOADVANCECHECKTIME";	value = this.autoAdvanceCheckTime.toString();	csvWriteConfigRecord(csvWriter, field, value);
		
		csvWriter.flush();
		csvWriter.close();
	}

	private static void csvWriteConfigRecord(CsvWriter csvWriter, String field, String value) throws IOException {
		csvWriter.write(field);
		csvWriter.write(value);
		csvWriter.endRecord();
	}
	
	private void writeDemographicsQuestions() throws XMLStreamException, FileNotFoundException {
    	FileOutputStream fos = new FileOutputStream(this.demographicsXmlPath);
		XMLOutputFactory output = XMLOutputFactory.newInstance();
		XMLStreamWriter writer = output.createXMLStreamWriter(fos, "utf-8");

		writer.writeStartDocument("utf-8","1.0");
		writer.writeStartElement("QuestionList");
		writer.writeAttribute("AutoAdvance", this.isAutoAdvanceCheck() ? "True" : "False");
		
		writer.writeEndElement();
		writer.writeEndDocument();
		
		writer.flush();
		writer.close();
	}
	
	/**
	 * Read configuration from Config.CSV.
	 * @throws IOException 
	 * @throws XMLStreamException 
	 */
	private void readConfig() throws IOException, XMLStreamException {
		CsvReader csvReader = new CsvReader(configCsvPath);
		
		// General
		csvReader.readRecord();	this.courseName = csvReader.get(1);
		csvReader.readRecord();	this.instructorID = csvReader.get(1);
		csvReader.readRecord(); this.frequencyEnum1 = FrequencyEnum.valueOf(csvReader.get(1));
		csvReader.readRecord();	this.frequencyEnum2 = FrequencyEnum.valueOf(csvReader.get(1));
		csvReader.readRecord(); this.frequencyAlert = FrequencyAlert.valueOf(csvReader.get(1));
		csvReader.readRecord(); this.showResponseGrid = ShowResponseGrid.valueOf(csvReader.get(1));
		csvReader.readRecord();	this.showResponsePattern = ShowResponsePattern.valueOf(csvReader.get(1));
		csvReader.readRecord();	this.showChoice = Boolean.parseBoolean(csvReader.get(1));
		csvReader.readRecord();	this.showAttempts = Boolean.parseBoolean(csvReader.get(1));
		
		// Polling
		csvReader.readRecord();	this.timerMode = TimerMode.valueOf(csvReader.get(1));
		csvReader.readRecord();	this.pollingTimer = PollingTimer.valueOf(csvReader.get(1));
		
		// Results
		csvReader.readRecord();	this.correctAnswerDisplay = CorrectAnswerDisplay.valueOf(csvReader.get(1));

		// Scoring
		csvReader.readRecord();	this.sessionParticipationPoints = Integer.parseInt(csvReader.get(1));
		csvReader.readRecord();	this.participationRequirement = ParticipationRequirement.valueOf(csvReader.get(1));
		csvReader.readRecord();	this.participationBarValue = Integer.parseInt(csvReader.get(1));
		csvReader.readRecord();	this.respondingPoints = Integer.parseInt(csvReader.get(1));
		csvReader.readRecord();	this.correctRespondingPoints = Integer.parseInt(csvReader.get(1));
		csvReader.readRecord();	this.sessionPointsLimit = Boolean.parseBoolean(csvReader.get(1));
		csvReader.readRecord();	this.totalPerformancePoints = Integer.parseInt(csvReader.get(1));
		csvReader.readRecord();	this.performanceBarValue = Integer.parseInt(csvReader.get(1));
		
		// Base Display
		csvReader.readRecord();	this.multipleChoiceDisplay = MultipleChoiceDisplay.valueOf(csvReader.get(1));
		csvReader.readRecord();	this.multipleChoiceAlternateTime = MultipleChoiceAlternateTime.valueOf(csvReader.get(1));
		
		csvReader.readRecord();	this.autoAdvanceCheck = Boolean.parseBoolean(csvReader.get(1));
		csvReader.readRecord();	this.autoAdvanceCheckTime = AutoAdvanceCheckTime.valueOf(csvReader.get(1));
		
		csvReader.close();
	}
	
	/**
	 * Load a question list and associated it with this course. 
	 * @param path question list file path
	 * @throws IOException 
	 */
	public void loadCourseQuestionList(String path) throws IOException {
		this.listQuestions = loadQuestionList(path);
	}
	
	/**
	 * Load a question list.
	 * @param path question list file path.
	 * @throws IOException
	 */
	public static ArrayList<ListQuestion> loadQuestionList(String path) throws IOException {
		ArrayList<ListQuestion> listQuestions = new ArrayList<ListQuestion> ();
		
		CsvReader csvReader = new CsvReader(path);
    	
    	csvReader.readRecord();
    	
    	while (csvReader.readRecord()){
    		@SuppressWarnings("unused")
			String index = csvReader.get(0);
    		String title = csvReader.get(1);
    		@SuppressWarnings("unused")
			String space = csvReader.get(2);
    		@SuppressWarnings("unused")
			String questionType = csvReader.get(3);
    		String choiceA = csvReader.get(4);
    		String choiceB = csvReader.get(5);
    		String choiceC = csvReader.get(6);
    		String choiceD = csvReader.get(7);
    		String choiceE = csvReader.get(8);
    		int pointA = Integer.parseInt(csvReader.get(9));
    		int pointB = Integer.parseInt(csvReader.get(10));
    		int pointC = Integer.parseInt(csvReader.get(11));
    		int pointD = Integer.parseInt(csvReader.get(12));
    		int pointE = Integer.parseInt(csvReader.get(13));
    		@SuppressWarnings("unused")
			String anonymous = csvReader.get(14);
    		
    		String correctAnswer = csvReader.get(15);
    		EnumCorrectAnswer enumCorrectAnswer = correctAnswer.equals("") ? EnumCorrectAnswer.NA : EnumCorrectAnswer.valueOf(correctAnswer);
    		
    		@SuppressWarnings("unused")
			String color = csvReader.get(16);
    		
    		String timerModeStr = csvReader.get(17);
    		TimerMode timerMode = timerModeStr.equals("0") ? TimerMode.UP : TimerMode.DOWN;
    		
    		String showCorrectAnswer = csvReader.get(18);
    		CorrectAnswerDisplay correctAnswerDisplay = showCorrectAnswer.equals("FALSE") ? CorrectAnswerDisplay.HIDE : CorrectAnswerDisplay.SHOW;
    		
    		String pollingTimerStr = csvReader.get(19);
    		PollingTimer pollingTimer = PollingTimer.values()[Integer.parseInt(pollingTimerStr)];
    		
    		ListQuestion listQuestion = new ListQuestion(title, "", choiceA, choiceB, choiceC, choiceD, choiceE, 
    				pointA, pointB, pointC, pointD, pointE, enumCorrectAnswer, timerMode, pollingTimer, correctAnswerDisplay);
    		
    		listQuestions.add(listQuestion);
    	}
    	
    	csvReader.close();
    	
    	return listQuestions;
	}
	
	/**
	 * Store a question list.
	 * @param path question list file path
	 * @param listQuestions list question needs to be stored
	 * @throws IOException
	 */
    public static void storeQuestionList(String path, ArrayList<ListQuestion> listQuestions) throws IOException {
    	CsvWriter csvWriter = new CsvWriter(path);
    	
    	csvWriter.write("QLCSV");
    	csvWriter.endRecord();
    	
    	for (int i = 0; i < listQuestions.size(); i++) {
    		ListQuestion listQuestion = listQuestions.get(i);
    		
    		csvWriter.write(i + "");
    		csvWriter.write(listQuestion.getTitle());
    		csvWriter.write("");
    		csvWriter.write("MULTIPLE");
    		csvWriter.write(listQuestion.getChoiceA());
    		csvWriter.write(listQuestion.getChoiceB());
    		csvWriter.write(listQuestion.getChoiceC());
    		csvWriter.write(listQuestion.getChoiceD());
    		csvWriter.write(listQuestion.getChoiceE());
    		csvWriter.write(listQuestion.getAPoint() + "");
    		csvWriter.write(listQuestion.getBPoint() + "");
    		csvWriter.write(listQuestion.getCPoint() + "");
    		csvWriter.write(listQuestion.getDPoint() + "");
    		csvWriter.write(listQuestion.getEPoint() + "");
    		csvWriter.write("FALSE");
    		csvWriter.write(listQuestion.getEnumCorrectAnswer().toString());
    		csvWriter.write("0");
    		csvWriter.write(listQuestion.getTimerMode() == TimerMode.UP ? "0" : "1");
    		csvWriter.write(listQuestion.getCorrectAnswerDisplay() == CorrectAnswerDisplay.HIDE ? "FALSE" : "TRUE");
    		csvWriter.write(listQuestion.getPollingTimer().ordinal() + "");
    		
    		csvWriter.endRecord();
    	}
    	
    	csvWriter.flush();
    	csvWriter.close();
    }
	
	/**
	 * Test if the given string is legal for being a component of the course 
	 * name (only contains characters from A-Z, a-z, 0-9 and space).
	 * @param name name of the course.
	 * @return true if it is legal; false otherwise.
	 */
	public static boolean ifTextLegal(String name) {
		for (char e : name.toCharArray()) {  
		   if (!Character.isLetter(e) && !Character.isDigit(e) && !Character.isWhitespace(e)) {
		      return false;
		   }  
		}
		return true;
	}
	
	/**
	 * Create a session for this course.
	 * @throws Exception
	 */
	public void createSession() throws Exception {
		this.session = new Session(this);
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getInstructorID() {
		return instructorID;
	}

	public void setInstructorID(String instructorID) {
		this.instructorID = instructorID;
	}

	public FrequencyEnum getFrequencyEnum1() {
		return frequencyEnum1;
	}

	public void setFrequencyEnum1(FrequencyEnum frequencyEnum1) {
		this.frequencyEnum1 = frequencyEnum1;
	}

	public FrequencyEnum getFrequencyEnum2() {
		return frequencyEnum2;
	}

	public void setFrequencyEnum2(FrequencyEnum frequencyEnum2) {
		this.frequencyEnum2 = frequencyEnum2;
	}

	public FrequencyAlert getFrequencyAlert() {
		return frequencyAlert;
	}

	public void setFrequencyAlert(FrequencyAlert frequencyAlert) {
		this.frequencyAlert = frequencyAlert;
	}

	public ShowResponseGrid getShowResponseGrid() {
		return showResponseGrid;
	}

	public void setShowResponseGrid(ShowResponseGrid showResponseGrid) {
		this.showResponseGrid = showResponseGrid;
	}

	public ShowResponsePattern getShowResponsePattern() {
		return showResponsePattern;
	}

	public void setShowResponsePattern(ShowResponsePattern showResponsePattern) {
		this.showResponsePattern = showResponsePattern;
	}

	public boolean isShowChoice() {
		return showChoice;
	}

	public void setShowChoice(boolean showChoice) {
		this.showChoice = showChoice;
	}

	public boolean isShowAttempts() {
		return showAttempts;
	}

	public void setShowAttempts(boolean showAttempts) {
		this.showAttempts = showAttempts;
	}

	public TimerMode getTimerMode() {
		return timerMode;
	}

	public void setTimerMode(TimerMode timerMode) {
		this.timerMode = timerMode;
	}

	public PollingTimer getPollingTimer() {
		return pollingTimer;
	}

	public void setPollingTimer(PollingTimer pollingTimer) {
		this.pollingTimer = pollingTimer;
	}

	public int getSessionParticipationPoints() {
		return sessionParticipationPoints;
	}

	public void setSessionParticipationPoints(int sessionParticipationPoints) {
		this.sessionParticipationPoints = sessionParticipationPoints;
	}

	public ParticipationRequirement getParticipationRequirement() {
		return participationRequirement;
	}

	public void setParticipationRequirement(
			ParticipationRequirement participationRequirement) {
		this.participationRequirement = participationRequirement;
	}

	public int getParticipationBarValue() {
		return participationBarValue;
	}

	public void setParticipationBarValue(int participationBarValue) {
		this.participationBarValue = participationBarValue;
	}
	
	public int getRespondingPoints() {
		return respondingPoints;
	}

	public void setRespondingPoints(int respondingPoints) {
		this.respondingPoints = respondingPoints;
	}

	public int getCorrectRespondingPoints() {
		return correctRespondingPoints;
	}

	public void setCorrectRespondingPoints(int correctRespondingPoints) {
		this.correctRespondingPoints = correctRespondingPoints;
	}

	public boolean isSessionPointsLimit() {
		return sessionPointsLimit;
	}

	public void setSessionPointsLimit(boolean sessionPointsLimit) {
		this.sessionPointsLimit = sessionPointsLimit;
	}

	public int getTotalPerformancePoints() {
		return totalPerformancePoints;
	}

	public void setTotalPerformancePoints(int totalPerformancePoints) {
		this.totalPerformancePoints = totalPerformancePoints;
	}

	public int getPerformanceBarValue() {
		return performanceBarValue;
	}

	public void setPerformanceBarValue(int performanceBarValue) {
		this.performanceBarValue = performanceBarValue;
	}
	
	public CorrectAnswerDisplay getCorrectAnswerDisplay() {
		return correctAnswerDisplay;
	}

	public void setCorrectAnswerDisplay(CorrectAnswerDisplay correctAnswerDisplay) {
		this.correctAnswerDisplay = correctAnswerDisplay;
	}

	public MultipleChoiceDisplay getMultipleChoiceDisplay() {
		return multipleChoiceDisplay;
	}

	public void setMultipleChoiceDisplay(MultipleChoiceDisplay multipleChoiceDisplay) {
		this.multipleChoiceDisplay = multipleChoiceDisplay;
	}

	public MultipleChoiceAlternateTime getMultipleChoiceAlternateTime() {
		return multipleChoiceAlternateTime;
	}

	public void setMultipleChoiceAlternateTime(
			MultipleChoiceAlternateTime multipleChoiceAlternateTime) {
		this.multipleChoiceAlternateTime = multipleChoiceAlternateTime;
	}
	
	public boolean isAutoAdvanceCheck() {
		return autoAdvanceCheck;
	}

	public void setAutoAdvanceCheck(boolean autoAdvanceCheck) {
		this.autoAdvanceCheck = autoAdvanceCheck;
	}

	public AutoAdvanceCheckTime getAutoAdvanceCheckTime() {
		return autoAdvanceCheckTime;
	}

	public void setAutoAdvanceCheckTime(AutoAdvanceCheckTime autoAdvanceCheckTime) {
		this.autoAdvanceCheckTime = autoAdvanceCheckTime;
	}
	
	public String getConfigCsvPath() {
		return configCsvPath;
	}

	public void setConfigCsvPath(String configCsvPath) {
		this.configCsvPath = configCsvPath;
	}

	public String getStudentRecordCsvPath() {
		return studentRecordCsvPath;
	}

	public void setStudentRecordCsvPath(String studentRecordCsvPath) {
		this.studentRecordCsvPath = studentRecordCsvPath;
	}

	public String getDemographicsXmlPath() {
		return demographicsXmlPath;
	}

	public void setDemographicsXmlPath(String demographicsXmlPath) {
		this.demographicsXmlPath = demographicsXmlPath;
	}
	
	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public StudentList getStudents() {
		return students;
	}

	public void setStudents(StudentList students) {
		this.students = students;
	}

	public boolean isHasRegisteredInfo() {
		return hasRegisteredInfo;
	}

	public void setHasRegisteredInfo(boolean hasRegisteredInfo) {
		this.hasRegisteredInfo = hasRegisteredInfo;
	}

	public ArrayList<ListQuestion> getListQuestions() {
		return listQuestions;
	}
	
	public ListQuestion getListQuestion(int index) {
		return this.listQuestions.get(index);
	}
}
