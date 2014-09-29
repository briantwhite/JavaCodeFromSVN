package content;

import java.util.*;

import iClickerDriver.*;

/**
 * A class holds all the summary data. Note: all information in this class are 
 * counted as per student, not per clicker remote. If we do not have student 
 * information, assume that each clicker remote corresponds to one student.
 * @author Junhao
 *
 */
public abstract class SummaryList {
	protected Question question;
	// A summary corresponds to a student. Votes from different clickers 
	// belonging to the same student will modify the same piece of summary.
	protected ArrayList<Summary> summaryList;
	protected StudentList studentList;
	protected HashMap<String, Summary> clickerIdToSummary;
	protected HashMap<Summary, Integer> SummaryToIndex;
	
	protected int responseA;
	protected int responseB;
	protected int responseC;
	protected int responseD;
	protected int responseE;
	protected double responseAPerc;
	protected double responseBPerc;
	protected double responseCPerc;
	protected double responseDPerc;
	protected double responseEPerc;
	
	// How many different students have voted
	protected int studentTotal;
	
	protected LabInfoList labInfoList;

	public SummaryList(Question question) {
		this.question = question;
		this.summaryList = new ArrayList<Summary>();
		this.studentList = question.getSession().getCourse().getStudents();
		this.clickerIdToSummary = new HashMap<String, Summary> ();
		this.SummaryToIndex = new HashMap<Summary, Integer> ();
		
		responseA = 0;
		responseB = 0;
		responseC = 0;
		responseD = 0;
		responseE = 0;
		
		responseAPerc = 0;
		responseBPerc = 0;
		responseCPerc = 0;
		responseDPerc = 0;
		responseEPerc = 0;

		studentTotal = 0;
	}

	/**
	 * A new vote comes. First check if it comes from an ad-hoc clicker. If yes, the vote is discarded. 
	 * Otherwise, we update summary, histogram, toolbar, response grid and statistics information (such 
	 * as responseA).
	 * @param vote vote needs to be processed.
	 * @throws ClassNotFoundException 
	 */
	abstract public void add(Vote vote) throws ClassNotFoundException;

	/**
	 * A new clicker votes.
	 * @param vote vote.
	 * @throws ClassNotFoundException
	 */
	abstract protected void addNewVote(Vote vote) throws ClassNotFoundException;
	
	/**
	 * An old clicker votes again.
	 * @param index index of this clicker in the summary.
	 * @param vote vote.
	 */
	abstract protected void modifyExistingVote(Vote vote);
	
	/**
	 * Update vote count on toolbar.
	 */
	protected void updateToolbar() {
        this.question.getSession().getCourse().getTest().getToolbarInstructor().updateVotes(studentTotal);
        this.question.getSession().getCourse().getTest().getToolbarStudent().updateVotes(studentTotal);
	}
	
	/**
	 * Update histogram.
	 */
	protected void updateHistogram() {
		this.question.getSession().getCourse().getTest().getHistogramInstructor().update();
		this.question.getSession().getCourse().getTest().getHistogramStudent().update();
	}
	
	/**
	 * A new voter votes and we need to change the statistics.
	 * @param button choice.
	 */
	protected void increaseCount(ButtonEnum choice) {
		switch (choice) {
		case A:	responseA++;	break;
		case B: responseB++;	break;
		case C: responseC++;	break;
		case D: responseD++;	break;
		default:responseE++;
		}
		
		studentTotal++;
	}
	
	/**
	 * A voter votes again and we need to change the statistics.
	 * @param oldChoice previous choice.
	 * @param newChoice current choice.
	 */
	protected void modifyCount(ButtonEnum oldChoice, ButtonEnum newChoice) {
		if (oldChoice == newChoice) return;
		
		switch (oldChoice) {
		case A: responseA--; break;
		case B: responseB--; break;
		case C: responseC--; break;
		case D: responseD--; break;
		default:responseE--;
		}
		
		switch (newChoice) {
		case A:	responseA++;	break;
		case B: responseB++;	break;
		case C: responseC++;	break;
		case D: responseD++;	break;
		default:responseE++;
		}
	}
	
	public Summary get(int index) {
		return summaryList.get(index);
	}
	
	public void remove(int index) {
		summaryList.remove(index);
	}

	public int getSummaryAmount() {
		return summaryList.size();
	}
	
	public Question getQuestion() {
		return question;
	}

	public int getResponseA() {
		return responseA;
	}

	public int getResponseB() {
		return responseB;
	}

	public int getResponseC() {
		return responseC;
	}

	public int getResponseD() {
		return responseD;
	}

	public int getResponseE() {
		return responseE;
	}

	public double getResponseAPerc() {
		return responseAPerc;
	}

	public double getResponseBPerc() {
		return responseBPerc;
	}

	public double getResponseCPerc() {
		return responseCPerc;
	}

	public double getResponseDPerc() {
		return responseDPerc;
	}

	public double getResponseEPerc() {
		return responseEPerc;
	}

	public LabInfoList getLabInfoList() {
		return labInfoList;
	}
}
