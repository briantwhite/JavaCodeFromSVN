package edu.umb.bio.jsMolCalc.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

public class TextSubmissionButtonProcessor implements ClickHandler {
	
	private TextBox submission;
	private Button button;
	private HTML result;
	private String correctAnswer;
	
	public TextSubmissionButtonProcessor(TextBox submission, Button button, HTML result) {
		this.submission = submission;
		this.button = button;
		this.result = result;
		correctAnswer = "";
	}

	public void onClick(ClickEvent event) {
		if (submission.getText().replaceAll("\\s","").equals(correctAnswer)) {
			result.setHTML("<html><body>CORRECT</body></html>");
			submission.setEnabled(false);
			button.setEnabled(false);
		} else {
			result.setHTML("<html><body><font color=\"red\">INCORRECT</font></body></html>");
		}
	}
	
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

}
