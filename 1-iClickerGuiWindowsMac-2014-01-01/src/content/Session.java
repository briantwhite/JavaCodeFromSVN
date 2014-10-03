package content;

import java.io.*;
import java.util.*;

import setting.enumoption.CorrectAnswerDisplay;
import setting.enumoption.ShowResponseGrid;

import com.csvreader.*;

import content.enumoption.HistogramDisplayOption;


import gui.HistogramInstructor;
import gui.HistogramStudent;
import gui.enumoption.EnumQuestionListState;
import iClickerDriver.*;

/**
 * A class describes a session, which may contain one or more click questions.
 * @author Junhao
 *
 */
public class Session {
	private Course course;
	private ClickerControl clickerControl;
	private InstructorControl instructorControl;
	private ArrayList<Question> questions;
	private Stack<Question> waitingListQuestions;	// Questions that are in the queue to appear but not yet.
	private String startTime;					// Start time of this question in YYMMDDHHMM.
	private boolean isVotingEnabled;			// State variable indicating whether the voting mode is on.
	private int currentQuestionId;				// Id of most up-to-date question or the one displayed on the histogram panel.

	/**
	 * Constructor.
	 * @param course
	 * @throws Exception
	 */
	public Session(Course course) throws Exception {
		this.course = course;
		this.clickerControl = new ClickerControlStation(this);
		//this.clickerControl = new ClickerControlSocket(this);
		this.instructorControl = new InstructorControl(this);
		this.questions = new ArrayList<Question>();
		this.waitingListQuestions = new Stack<Question>();
		this.startTime = TimeProcess.getTime(TimeEnum.YMDHM);
		this.isVotingEnabled = false;
		this.currentQuestionId = -1;

		createSessionCsv();
		createSessionLog();

		Thread t = new Thread(this.clickerControl);
		t.start();
	}

	/**
	 * Create the .csv file that describes this session.
	 * @throws IOException
	 */
	private void createSessionCsv() throws IOException {
		String path = "./Classes/";
		String time = this.getStartTime();
		String filename = path + course.getCourseName() + "/SessionData/L" + time + ".csv";

		int performance = course.getTotalPerformancePoints() == Integer.MAX_VALUE ? -1 : course.getTotalPerformancePoints();

		int participation = course.getSessionParticipationPoints();

		int minReply;
		switch (course.getParticipationRequirement()) {
		case ALL:	minReply = this.getQuestionAmount(); break;
		case ALLBUTONE:	minReply = this.getQuestionAmount() - 1; break;
		case ATLEAST75PERCENT: minReply = (int) Math.round(this.getQuestionAmount() * 0.75); break;
		case ATLEAST50PERCENT: minReply = (int) Math.round(this.getQuestionAmount() * 0.5); break;
		default: minReply = 1;  
		}

		int minCorrect = 0;

		CsvWriter csvWriter = new CsvWriter(filename);

		String[] firstLine  = {"Scoring", 
				"Performance = "   + performance,
				"Participation = " + participation,
				"Min Reply = " 	  + minReply,
				"Min Correct = "   + minCorrect,
		""};
		String[] secondLine = {"Question", "", ""};
		String[] thirdLine  = {"Start Time", "", ""};
		String[] fourthLine = {"Stop Time", "", ""};
		String[] fifthLine  = {"Correct Answer", "", ""};

		csvWriter.writeRecord(firstLine);
		csvWriter.writeRecord(secondLine);
		csvWriter.writeRecord(thirdLine);
		csvWriter.writeRecord(fourthLine);
		csvWriter.writeRecord(fifthLine);

		csvWriter.flush();
		csvWriter.close();
	}

	/**
	 * Create a log file which keeps track of all incoming clicks.
	 * @throws IOException
	 */
	private void createSessionLog() throws IOException {
		String path = "./Classes/";
		String time = this.getStartTime();
		String fileName = path + course.getCourseName() + "/SessionData/L" + time + ".log.csv";

		CsvWriter csvWriter = new CsvWriter(fileName);

		csvWriter.write("   === Log ===   ");

		csvWriter.flush();
		csvWriter.close();
	}

	/**
	 * Get the current number of questions in this session. 
	 * @return number of existing questions.
	 */
	public int getQuestionAmount() {
		return questions.size();
	}

	/**
	 * Get the question according to index.
	 * @param index index of a question.
	 * @return the question with the specified index, null if index is invalid.
	 */
	public Question getQuestion(int index) {
		if (index >= questions.size()) {
			System.err.println("There is no question with index: " + index);
			return null;
		} else {
			return questions.get(index);
		}
	}

	/**
	 * A vote comes.
	 * @param vote
	 * @throws Exception 
	 */
	public void processVote(Vote vote) throws Exception {
		String instructorIdString = this.getCourse().getInstructorID();

		if (this.isVotingEnabled) {
			if (vote.getId().equals(instructorIdString)) {
				this.instructorControl.checkInstructorButton(vote);
			} else {
				this.getQuestion(this.currentQuestionId).newVote(vote);
			}
		} else {
			this.instructorControl.checkInstructorButton(vote);
		}
	}

	/**
	 * Start a new question.
	 * @throws Exception
	 */
	public void startQuestion() throws Exception {
		this.isVotingEnabled = true;
		
		if (this.waitingListQuestions.size() != 0) {
			questions.add(this.waitingListQuestions.pop());
		} else if (this.getCourse().getTest().getRunQuestionList().getEnumQuestionListState().equals(EnumQuestionListState.PLAY) ) {
			this.waitingListQuestions.push(new Question(this, this.getCourse().getTest().getRunQuestionList().fetchQuestion()));
			questions.add(this.waitingListQuestions.pop());
		} else {
			Question question = new Question(this, null);
			question.SetTitle("Question " + (questions.size() + 1));
			questions.add(question);
		}
		
		this.currentQuestionId = this.questions.size() - 1;

		// Change labels and widgets for all toolbars.
		this.course.getTest().getToolbarInstructor().startChangeLabel();
		this.course.getTest().getToolbarStudent().startChangeLabel();

		// Update histogram.
		HistogramInstructor histogramInstructor = this.course.getTest().getHistogramInstructor();
		histogramInstructor.disableButtons();
		histogramInstructor.setQuestion(this.currentQuestionId);
		histogramInstructor.update();
		
		HistogramStudent histogramStudent = this.course.getTest().getHistogramStudent();
		histogramStudent.setQuestion(this.currentQuestionId);
		if (histogramStudent.getHistogramDisplayOption() == HistogramDisplayOption.SHOWWITHCORRECTANSWER) {
			histogramStudent.setHistogramDisplayOption(HistogramDisplayOption.SHOWWITHOUTCORRECTANSWER);
		}
		histogramStudent.update();

		// Reset response grid.
		this.course.getTest().getResponseGrid().resetResponseGrid();

		// Show response grid if necessary.
		if (this.course.getTest().getCourse().getShowResponseGrid() == ShowResponseGrid.SHOW) {
			this.course.getTest().getResponseGrid().setVisible(true);
		}

		this.questions.get(currentQuestionId).start();
	}

	/**
	 * Stop the current question.
	 * @throws Exception
	 */
	public void stopQuestion() throws Exception {
		this.isVotingEnabled = false;

		// Change labels and widgets for all toolbars.
		this.course.getTest().getToolbarInstructor().stopChangeLabel();
		this.course.getTest().getToolbarStudent().stopChangeLabel();

		// Update histogram.
		HistogramInstructor histogramInstructor = this.course.getTest().getHistogramInstructor();
		histogramInstructor.enableButtons();
		if (this.course.getCorrectAnswerDisplay() == CorrectAnswerDisplay.SHOW) {
			this.course.getTest().getHistogramStudent().setHistogramDisplayOption(HistogramDisplayOption.SHOWWITHCORRECTANSWER);
		}

		// Show response grid if necessary.
		if (this.course.getTest().getCourse().getShowResponseGrid() == ShowResponseGrid.SHOW) {
			this.course.getTest().getResponseGrid().setVisible(true);
		}
		
		this.getQuestion(this.currentQuestionId).end();

	}

	/**
	 * Go to the next question.
	 */
	public void nextQuestion() {
		this.currentQuestionId++;
		
		this.course.getTest().getHistogramInstructor().setQuestion(this.currentQuestionId);
		this.course.getTest().getHistogramStudent().setQuestion(this.currentQuestionId);
	}
	
	/**
	 * Go to the last question.
	 */
	public void lastQuestion() {
		this.currentQuestionId--;
		
		this.course.getTest().getHistogramInstructor().setQuestion(this.currentQuestionId);
		this.course.getTest().getHistogramStudent().setQuestion(this.currentQuestionId);
	}

	public Course getCourse() {
		return this.course;
	}

	public ClickerControl getClickerControl() {
		return clickerControl;
	}

	public String getStartTime() {
		return startTime;
	}

	public boolean isVotingEnabled() {
		return isVotingEnabled;
	}

	public int getCurrentQuestionIndex() {
		return currentQuestionId;
	}

	public Question getCurrentQuestion() {
		return this.questions.get(this.currentQuestionId);
	}
	
	public void addWaitingListQuestion(QuestionBody questionBody) throws Exception {
		this.waitingListQuestions.push(new Question(this, questionBody));
	}
}
