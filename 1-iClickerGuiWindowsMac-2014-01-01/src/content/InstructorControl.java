package content;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import content.enumoption.HistogramDisplayOption;

import gui.enumoption.EnumCorrectAnswer;
import iClickerDriver.ButtonEnum;
import iClickerDriver.Vote;

/**
 * In this version, there are two toolbars and two histograms. One set is for
 * instructor, the other is for students. Instructor toolbar controls instructor
 * histogram, Student toolbar controls student histogram. To set how instructor
 * histogram is displayed, one uses instructor toolbar. To set how student
 * histogram is displayed, one uses student toolbar or instructor's remote.
 * Similar to the class Question, this class deals with requesting votes and 
 * interpreting them. However, Question deals with incoming students' votes, 
 * while InstructorControl deals with votes coming from instructor's remote.
 * To choose the correct answer for a question, one needs to press 'E' first, 
 * which causes the program to go into "selecting correct answer" mode (the 
 * selectCorrectAnswerOn will be set to true). Then ones needs to press 
 * another time the key that represents the correct answer, and the program 
 * goes back to the "normal" mode (selectCorrectAnswerOn will be set to false).
 * @author Junhao
 *
 */
public class InstructorControl{
	private Session session;
	private boolean selectCorrectAnswerOn;

	public InstructorControl(Session session) {
		this.session = session;
		this.selectCorrectAnswerOn = false;
	}

	public void checkInstructorButton(Vote vote) throws Exception {
		if (selectCorrectAnswerOn == false) {				// If in "normal" mode.
			if (vote.getButton() == ButtonEnum.A) {			// Start or stop voting.
				if (this.session.isVotingEnabled() == false) {
					this.session.startQuestion();
				} else {
					this.session.stopQuestion();
				}
			} else if (vote.getButton() == ButtonEnum.B) {	// Hide or display histogram, or change whether correct answer is revealed. 
				this.session.getCourse().getTest().getHistogramStudent().changeHistogramDisplayOption();
			} else if (vote.getButton() == ButtonEnum.C) {	// Go to next slide.
				nextSlide();
			} else if (vote.getButton() == ButtonEnum.D) {	// Go to last slide.
				lastSlide();
			} else {										// Select correct answer.
				selectCorrectAnswerOn = true;
			}
		} else {											// If in "selecting correct answer" mode.
			this.session.getCurrentQuestion().setCorrectAnswer(EnumCorrectAnswer.valueOf(vote.getButton().toString()));
			selectCorrectAnswerOn = false;
			this.session.getCourse().getTest().getHistogramInstructor().setHistogramDisplayOption(HistogramDisplayOption.SHOWWITHCORRECTANSWER);
			this.session.getCourse().getTest().getHistogramInstructor().update();
			this.session.getCourse().getTest().getHistogramStudent().update();
		}
	}
	
	/**
	 * Go to the next slide by simulating the left mouse click. The focus should be set on the presentation application.  
	 */
	private void nextSlide() {
		try {
	        Robot robot = new Robot();
	        // Simulate a key press: next slide.
	        robot.keyPress(KeyEvent.VK_RIGHT);
	        robot.keyRelease(KeyEvent.VK_RIGHT);
		} catch (AWTException e) {
		        e.printStackTrace();
		}
	}
	
	/**
	 * Go to the last slide by simulating the right mouse click. The focus should be set on the presentation application.  
	 */
	private void lastSlide() {
		try {
	        Robot robot = new Robot();
	        // Simulate a key press: last slide.
	        robot.keyPress(KeyEvent.VK_LEFT);
	        robot.keyRelease(KeyEvent.VK_LEFT);
		} catch (AWTException e) {
		        e.printStackTrace();
		}	
	}
}
