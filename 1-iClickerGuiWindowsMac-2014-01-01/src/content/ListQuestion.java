package content;

import content.enumoption.EnumQuestionType;
import setting.enumoption.CorrectAnswerDisplay;
import setting.enumoption.PollingTimer;
import setting.enumoption.TimerMode;
import gui.enumoption.EnumCorrectAnswer;

/**
 * A question stored in the question list.
 * @author Junhao
 *
 */
public class ListQuestion extends QuestionBody {


	public ListQuestion(String title, String body, String choiceA,
			String choiceB, String choiceC, String choiceD, String choiceE,
			int aPoint, int bPoint, int cPoint, int dPoint, int ePoint,
			EnumCorrectAnswer correctAnswer, TimerMode timerMode,
			PollingTimer pollingTimer, CorrectAnswerDisplay correctAnswerDisplay) {
		super(title, body, choiceA, choiceB, choiceC, choiceD, choiceE, correctAnswer, EnumQuestionType.LIST);
		APoint = aPoint;
		BPoint = bPoint;
		CPoint = cPoint;
		DPoint = dPoint;
		EPoint = ePoint;
		this.timerMode = timerMode;
		this.pollingTimer = pollingTimer;
		this.correctAnswerDisplay = correctAnswerDisplay;
	}
	
	public ListQuestion(ListQuestion listQuestion) {
		super(listQuestion.getTitle(), listQuestion.getBody(), listQuestion.getChoiceA(), listQuestion.getChoiceB(),
				listQuestion.getChoiceC(), listQuestion.getChoiceD(), listQuestion.getChoiceE(), 
				listQuestion.getEnumCorrectAnswer(), listQuestion.getEnumQuestionType());
		
		APoint = listQuestion.APoint;
		BPoint = listQuestion.BPoint;
		CPoint = listQuestion.CPoint;
		DPoint = listQuestion.DPoint;
		EPoint = listQuestion.EPoint;
		this.timerMode = listQuestion.timerMode;
		this.pollingTimer = listQuestion.pollingTimer;
		this.correctAnswerDisplay = listQuestion.correctAnswerDisplay;
	}
	
	public int getAPoint() {
		return APoint;
	}
	
	public void setAPoint(int aPoint) {
		APoint = aPoint;
	}
	
	public int getBPoint() {
		return BPoint;
	}
	
	public void setBPoint(int bPoint) {
		BPoint = bPoint;
	}
	
	public int getCPoint() {
		return CPoint;
	}
	
	public void setCPoint(int cPoint) {
		CPoint = cPoint;
	}
	
	public int getDPoint() {
		return DPoint;
	}
	
	public void setDPoint(int dPoint) {
		DPoint = dPoint;
	}
	
	public int getEPoint() {
		return EPoint;
	}
	
	public void setEPoint(int ePoint) {
		EPoint = ePoint;
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
	
	public CorrectAnswerDisplay getCorrectAnswerDisplay() {
		return correctAnswerDisplay;
	}
	
	public void setCorrectAnswerDisplay(CorrectAnswerDisplay correctAnswerDisplay) {
		this.correctAnswerDisplay = correctAnswerDisplay;
	}

	private int APoint;
	private int BPoint;
	private int CPoint;
	private int DPoint;
	private int EPoint;
	TimerMode timerMode;
	PollingTimer pollingTimer;
	CorrectAnswerDisplay correctAnswerDisplay;
}
