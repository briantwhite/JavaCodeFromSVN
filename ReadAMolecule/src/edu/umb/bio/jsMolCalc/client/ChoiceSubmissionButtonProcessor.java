package edu.umb.bio.jsMolCalc.client;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;


public class ChoiceSubmissionButtonProcessor implements ClickHandler {
	
	private ListBox submission;
	private Button button;
	private HTML result;
	private int correctAnswer;

	public ChoiceSubmissionButtonProcessor(ListBox submission, Button button, HTML result) {
		this.submission = submission;
		this.button = button;
		this.result = result;
		correctAnswer = -1;
	}

	public void onClick(ClickEvent event) {
		if (submission.getSelectedIndex() == correctAnswer) {
			result.setHTML("<html><body>CORRECT</body></html>");
			submission.setEnabled(false);
			button.setEnabled(false);
		} else {
			result.setHTML("<html><body><font color=\"red\">INCORRECT</font></body></html>");
		}
		
	}
	
	public void setCorrectAnswer(int correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

}
