package content;

import java.awt.AWTException;
import java.io.*;
import java.util.*;

import utility.MultipleScreens;

import com.csvreader.*;

import content.enumoption.EnumQuestionType;

import gui.enumoption.EnumCorrectAnswer;
import iClickerDriver.*;

/**
 * A class describes a question. A session is composed of one or more questions.
 * @author Junhao
 *
 */
public class Question {
	private Session session;			// The session this question belongs to.
	private int questionId;				// Id of this question, starting from 0; when displayed on screen, add one to it.
	private String startTime;			// Start time of this question in HH:MM:SS.
	private String endTime;				// End time of this question in HH:MM:SS.
	private ArrayList<Vote> votes;		// An array of votes.
	private SummaryList summaryList;	// An array of summaries.
	private QuestionBody questionBody;	// Content of this question. It can be a demographics question, or a list question, or just a pure question.
	
	/**
	 * Constructor. If the second parameter is not null, it is a demographics questions or a list question.
	 * @param session session.
	 * @param questionBody question body.
	 * @throws Exception
	 */
	public Question(Session session, QuestionBody questionBody) throws Exception {
		this.session = session;
		this.questionId = session.getQuestionAmount();
		this.startTime = "";
		this.endTime = "";
		this.votes = new ArrayList<Vote>();
		
		if (this.session.getCourse().isHasRegisteredInfo()) {
			// Otherwise we always check if two clickers belongs to the same student. 
			this.summaryList = new SummaryListWithLabInfo(this);
		} else {
			// If there is no student record, we assume every single clicker represents a different student.
			this.summaryList = new SummaryListWithoutLabInfo(this);
		}
		
		if (questionBody == null) {
			this.questionBody = new QuestionBody("", "", "", "", "", "", "", EnumCorrectAnswer.NA, EnumQuestionType.REGULAR);
		} else {
			this.questionBody = questionBody;
		}
	}
	
	/**
	 * Start voting for this question.
	 * @throws Exception
	 */
	public void start() throws Exception {
		this.startTime = TimeProcess.getTime(TimeEnum.HMS);
		
		this.saveScreenShotStart();
		
		if (this.questionBody.getEnumQuestionType() == EnumQuestionType.LIST) {
			this.getSession().getCourse().getTest().getQuestionPanel().loadListQuestion(this.questionBody);
			this.getSession().getCourse().getTest().getQuestionPanel().setVisible(true);
		} else if (this.questionBody.getEnumQuestionType() == EnumQuestionType.ONTHEFLY) {
			this.getSession().getCourse().getTest().getQuestionPanel().loadOnTheFlyQuestion(this.questionBody);
			this.getSession().getCourse().getTest().getQuestionPanel().setVisible(true);
		} else {
			this.getSession().getCourse().getTest().getQuestionPanel().setVisible(false);
		}
		
		this.session.getClickerControl().startAcceptingVotes();
		
		Timer timer = new Timer(this.session.getCourse().getTest());
		Thread t = new Thread(timer);
		t.start();
	}
	
	/**
	 * Screen capture.
	 */
	private void saveScreenShotStart() {
		try {
	        MultipleScreens.takeScreenShotsAll("./Classes/" + this.session.getCourse().getCourseName() + "/Images" +
					   "/L" + this.session.getStartTime() + "_Q" + this.session.getQuestionAmount() + ".jpg");
	    } catch (AWTException awte) {
			awte.printStackTrace();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/**
	 * A piece of vote comes in.
	 * @param vote vote.
	 * @throws ClassNotFoundException
	 */
	public void newVote(Vote vote) throws ClassNotFoundException {
		this.votes.add(vote);
		this.summaryList.add(vote);
	}
	
	/**
	 * End voting and write results to file. Also do garbage collection after each question ends.
	 * @throws Exception
	 */
	public void end() throws Exception {
		ArrayList<Vote> array = this.session.getClickerControl().stopAccpetingVotes();
		
		Iterator<Vote> it = array.iterator();
		while (it.hasNext()) {
			Vote vote = it.next();
			this.votes.add(vote);
			this.summaryList.add(vote);
		}
		endTime = TimeProcess.getTime(TimeEnum.HMS);
		
		if (this.questionBody.getEnumQuestionType() == EnumQuestionType.LIST) {
			this.getSession().getCourse().getTest().getRunQuestionList().nextQuestion();
		}
		
		outputQuestionLog();
		outputQuestionCsv();
		
		System.gc();
	}
	
	public Session getSession() {
		return session;
	}
	
	/**
	 * Total number of responses contributed by either each single student or each 
	 * single clicker, depending on whether student information is available.
	 * @return total number of responses.
	 */
	public int getSummaryAmount() {
		return summaryList.getSummaryAmount();
	}
	
	public SummaryList getSummaryList() {
		return summaryList;
	}
	
	public int getResponseAAmont() {
		return summaryList.getResponseA();
	}
	
	public int getResponseBAmont() {
		return summaryList.getResponseB();
	}
	
	public int getResponseCAmont() {
		return summaryList.getResponseC();
	}
	
	public int getResponseDAmont() {
		return summaryList.getResponseD();
	}
	
	public int getResponseEAmont() {
		return summaryList.getResponseE();
	}
	
	public double getResponseAPercent() {
		return summaryList.getResponseAPerc();
	}
	
	public double getResponseBPercent() {
		return summaryList.getResponseBPerc();
	}
	
	public double getResponseCPercent() {
		return summaryList.getResponseCPerc();
	}
	
	public double getResponseDPercent() {
		return summaryList.getResponseDPerc();
	}
	
	public double getResponseEPercent() {
		return summaryList.getResponseEPerc();
	}
	
	public LabInfoList getResponseByLab() {
		return summaryList.getLabInfoList();
	}
	
	/**
	 * Write votes to a disk log file. Sample line:
	 *  1FD009 role 12345678 A 13:16:59  
	 * @param votes 
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	private void outputQuestionLog() throws IOException {
		String path1 = "./Classes/";
		String courseName = session.getCourse().getCourseName();
		String path2 = "/SessionData/L";
		String time = session.getStartTime();
		String fileNameSrc = path1 + courseName + path2 + time + ".log.csv";
		String fileNameDes = path1 + courseName + path2 + time + ".temp.log.csv";
		
		CsvReader csvReader = new CsvReader(fileNameSrc);
	    CsvWriter csvWriter = new CsvWriter(fileNameDes);
	    
	    while (this.copyRecord(csvReader, csvWriter, true)) {
	    }
	    
	    csvWriter.write(this.startTime);
	    csvWriter.write(this.endTime);
	    csvWriter.endRecord();
	    
	    Iterator<Vote> it = votes.iterator();
	    while (it.hasNext()) {
	    	Vote vote = it.next();
	    	csvWriter.write(vote.getId());
	    	csvWriter.write("role");
	    	csvWriter.write("StudentID");
	    	csvWriter.write(vote.getButton() + "");
	    	csvWriter.write(vote.getTimeStamp());
	    	csvWriter.endRecord();
	    }
	    
	    csvReader.close();
		csvWriter.flush();
		csvWriter.close();
		
		new File(fileNameSrc).delete();
		
		File fileFrom = new File(fileNameDes);
		File fileto   = new File(fileNameSrc);
		fileFrom.renameTo(fileto);
	}
	
	/**
	 * Write .csv file to the disk.
	 * @throws IOException
	 */
	private void outputQuestionCsv() throws IOException {
		String path1 = "./Classes/";
		String courseName = session.getCourse().getCourseName();
		String path2 = "/SessionData/L";
		String time = session.getStartTime();
		String fileNameSrc = path1 + courseName + path2 + time + ".csv";
		String fileNameDes = path1 + courseName + path2 + time + ".temp.csv";
		
		CsvReader csvReader = new CsvReader(fileNameSrc);
		CsvWriter csvWriter = new CsvWriter(fileNameDes);
		
		// First line.
		copyRecord(csvReader, csvWriter, true);
		
		// Second line.
		copyRecord(csvReader, csvWriter, false);
		csvWriter.write(this.questionBody.getTitle() + "|MC|16");
		csvWriter.write("Score");
		csvWriter.write("Final Answer Time");
		csvWriter.write("Number of Attempts");
		csvWriter.write("First Response");
		csvWriter.write("Time");
		csvWriter.endRecord();
		
		// Third line.
		copyRecord(csvReader, csvWriter, false);
		csvWriter.write(this.startTime);
		csvWriter.write("");
		csvWriter.write("");
		csvWriter.write("");
		csvWriter.write("");
		csvWriter.write("");
		csvWriter.endRecord();
		
		// Forth line.
		copyRecord(csvReader, csvWriter, false);
		csvWriter.write(this.endTime);
		csvWriter.write("");
		csvWriter.write("");
		csvWriter.write("");
		csvWriter.write("");
		csvWriter.write("");
		csvWriter.endRecord();
		
		// Fifth line
		copyRecord(csvReader, csvWriter, true);
		
		// Search each existing iClicker id in the vote vector.
		writeEachIclicker(csvReader, csvWriter);
		
		// If there is any remaining summary, append them in the end of the .csv file.
		writeNewIclicker(csvWriter);
		
	    csvReader.close();
		csvWriter.flush();
		csvWriter.close();
		
		new File(fileNameSrc).delete();
		
		File fileFrom = new File(fileNameDes);
		File fileto   = new File(fileNameSrc);
		fileFrom.renameTo(fileto);
	}

	/**
	 * Copy a record read from csvReader to csvWriter.
	 * @param csvReader source.
	 * @param csvWriter destination.
	 * @param endRecord whether to finish writing the current record.
	 * @throws IOException
	 */
	public static boolean copyRecord(CsvReader csvReader, CsvWriter csvWriter, boolean endRecord) throws IOException {
		if(csvReader.readRecord()) {
			for (int i = 0; i < csvReader.getColumnCount(); i++) {
				csvWriter.write(csvReader.get(i));
			}
			if (endRecord) {
				csvWriter.endRecord();
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	@SuppressWarnings("unused")
	private void writeVoteDistribution(CsvReader csvReader, CsvWriter csvWriter, int numberOfResponse) throws IOException {
		copyRecord(csvReader, csvWriter, false);
		csvWriter.write(numberOfResponse + "");
		csvWriter.write(session.getCourse().getRespondingPoints() + "");
		csvWriter.write("");
		csvWriter.write("");
		csvWriter.write("");
		csvWriter.write("");
		csvWriter.endRecord();
	}
	
	private void writeEachIclicker(CsvReader csvReader, CsvWriter csvWriter) throws IOException {
		while (csvReader.readRecord()) {
			String id = csvReader.get(0).substring(1);
			Summary summary = null;
			
			// Test if this is the first question a certain student answers in this session.
			int index = -1;
			for (int i = 0; i < summaryList.getSummaryAmount(); i++) {
				summary = summaryList.get(i);
				if (id.equals(summary.getId())) {
					index = i;
					break;
				}
			}

			// Copy the first column, which is iClicker Id.
			csvWriter.write(csvReader.get(0));
			// Copy the second column, which is blank.
			csvWriter.write(csvReader.get(1));
			// Copy the third column, which is total score.
			if (index > -1) {
				int total = Integer.parseInt(csvReader.get(2)) + session.getCourse().getRespondingPoints();
				csvWriter.write(total + "");
			}
			else {
				int total = Integer.parseInt(csvReader.get(2));
				csvWriter.write(total + "");
			}
			// Copy rest of the columns.
			for (int i = 3; i < csvReader.getColumnCount(); i++) {
				csvWriter.write(csvReader.get(i));
			}
			// Write the new question.
			if (index > -1) {
				writeNewQuestion(csvWriter, summary);
				summaryList.remove(index);	// TODO make it more efficient?
			}
			else {
				writeEmptyQuestion(csvWriter);
			}
		}
	}

	/**
	 * Get the value of time1 - time2.
	 * @param timeEnum1
	 * @param time1
	 * @param timeEnum2
	 * @param time2
	 * @return
	 */
	private String getTimeDifference(TimeEnum timeEnum1, String time1, TimeEnum timeEnum2, String time2) {
		String diffStr = "";
		
		switch (timeEnum1) {
		case HMSM:
			switch(timeEnum2) {
			case HMS: diffStr = getDiffHMSM_HMS(time1, time2);	break;
			default:
				System.err.println("Bad time format");
			}
			break;
		default:
			System.err.println("Bad time format");
		}
		
		return diffStr;
	}
	
	private static String getDiffHMSM_HMS(String time1, String time2) {
		String diffStr = "";
		
		time2 = time2 + ".000";
		
		int time1Int = Integer.valueOf(time1.substring(0, 2)) * 60 * 60 * 1000 +
					   Integer.valueOf(time1.substring(3, 5)) 	   * 60 * 1000 +
					   Integer.valueOf(time1.substring(6, 8))		    * 1000 +
					   Integer.valueOf(time1.substring(9   ));
		int time2Int = Integer.valueOf(time2.substring(0, 2)) * 60 * 60 * 1000 +
				   	   Integer.valueOf(time2.substring(3, 5)) 	   * 60 * 1000 +
				   	   Integer.valueOf(time2.substring(6, 8))		    * 1000 +
				   	   Integer.valueOf(time2.substring(9   ));
		int diffRemain = time1Int - time2Int;
		
		int diffMilli = diffRemain % 1000;
		diffRemain = diffRemain / 1000;

		diffStr = diffRemain + "." + diffMilli;
				
		return diffStr;
	}
	
	private void writeNewQuestion(CsvWriter csvWriter, Summary summary) throws IOException {
		csvWriter.write(summary.getButtonFinal() + "");
		csvWriter.write(session.getCourse().getRespondingPoints() + "");
		csvWriter.write(this.getTimeDifference(TimeEnum.HMSM, summary.getTimeStampFinal(), TimeEnum.HMS, this.startTime));
		csvWriter.write(summary.getNumberOfAttempts() + "");
		csvWriter.write(summary.getButtonFirst() + "");
		csvWriter.write(this.getTimeDifference(TimeEnum.HMSM, summary.getTimeStampFirst(), TimeEnum.HMS, this.startTime));
		csvWriter.endRecord();
	}

	private void writeEmptyQuestion(CsvWriter csvWriter) throws IOException {
		csvWriter.write("");
		csvWriter.write("");
		csvWriter.write("");
		csvWriter.write("");
		csvWriter.write("");
		csvWriter.write("");
		csvWriter.endRecord();
	}
	
	private void writeNewIclicker(CsvWriter csvWriter) throws IOException {
		for (int i = 0; i < summaryList.getSummaryAmount(); i++) {
			Summary summary = summaryList.get(i);
			
			// Write the first column, which is iClicker Id.
			csvWriter.write("#" + summary.getId());
			// Write the second column, which is blank.
			csvWriter.write("");
			// Write the third column, which is total score.
			csvWriter.write(session.getCourse().getRespondingPoints() + "");
			// Write the previous questions.
			for (int j = 0; j < this.questionId; j++) {
				csvWriter.write("");
				csvWriter.write("0");
				csvWriter.write("0");
				csvWriter.write("0");
				csvWriter.write("");
				csvWriter.write("0");
			}
			// Write the current question.
			writeNewQuestion(csvWriter, summary);
		}
	}

	public String getTitle() {
		return this.questionBody.getTitle();
	}

	public void SetTitle(String title) {
		this.questionBody.setTitle(title);
	}
	
	public EnumCorrectAnswer getCorrectAnswer() {
		return this.questionBody.getEnumCorrectAnswer();
	}

	public void setCorrectAnswer(EnumCorrectAnswer enumCorrectAnswer) {
		this.questionBody.setEnumCorrectAnswer(enumCorrectAnswer);
	}
}
