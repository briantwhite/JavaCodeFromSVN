package content;

import content.enumoption.EnumQuestionType;
import gui.enumoption.EnumCorrectAnswer;

/**
 * A description of a question, including its title, body and all choices.
 * @author Junhao
 *
 */
public class QuestionBody {
	protected String title;
	protected String body;
	protected String choiceA;
	protected String choiceB;
	protected String choiceC;
	protected String choiceD;
	protected String choiceE;
	protected EnumCorrectAnswer enumCorrectAnswer;
	protected EnumQuestionType enumQuestionType;
	
	public QuestionBody(String title, String body, String choiceA, String choiceB, String choiceC, String choiceD, String choiceE, EnumCorrectAnswer enumCorrectAnswer, EnumQuestionType enumQuestionType) {
		this.title = title;
		this.body = body;
		this.choiceA = choiceA;
		this.choiceB = choiceB;
		this.choiceC = choiceC;
		this.choiceD = choiceD;
		this.choiceE = choiceE;
		this.enumCorrectAnswer = enumCorrectAnswer;
		this.enumQuestionType = enumQuestionType;
	}

	public QuestionBody(QuestionBody questionBody) {
		this.title = questionBody.title;
		this.body = questionBody.body;
		this.choiceA = questionBody.choiceA;
		this.choiceB = questionBody.choiceB;
		this.choiceC = questionBody.choiceC;
		this.choiceD = questionBody.choiceD;
		this.choiceE = questionBody.choiceE;
		this.enumCorrectAnswer = questionBody.enumCorrectAnswer;
		this.enumQuestionType = questionBody.enumQuestionType;
	}
	
	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	public String getChoiceA() {
		return choiceA;
	}

	public String getChoiceB() {
		return choiceB;
	}

	public String getChoiceC() {
		return choiceC;
	}

	public String getChoiceD() {
		return choiceD;
	}

	public String getChoiceE() {
		return choiceE;
	}

	public EnumCorrectAnswer getEnumCorrectAnswer() {
		return enumCorrectAnswer;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setChoiceA(String choiceA) {
		this.choiceA = choiceA;
	}

	public void setChoiceB(String choiceB) {
		this.choiceB = choiceB;
	}

	public void setChoiceC(String choiceC) {
		this.choiceC = choiceC;
	}

	public void setChoiceD(String choiceD) {
		this.choiceD = choiceD;
	}

	public void setChoiceE(String choiceE) {
		this.choiceE = choiceE;
	}

	public void setEnumCorrectAnswer(EnumCorrectAnswer enumCorrectAnswer) {
		this.enumCorrectAnswer = enumCorrectAnswer;
	}

	public EnumQuestionType getEnumQuestionType() {
		return enumQuestionType;
	}

	public void setEnumQuestionType(EnumQuestionType enumQuestionType) {
		this.enumQuestionType = enumQuestionType;
	}
}
