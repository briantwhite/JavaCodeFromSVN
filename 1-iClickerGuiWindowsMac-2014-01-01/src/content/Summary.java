package content;

import iClickerDriver.*;

/**
 * This class is used to hold summary data of each clicker/student, 
 * depending on whether student information is available, after 
 * each question, which will be used to generate the .csv file.
 * @author Junhao
 *
 */
public class Summary {
	private String id;
	private ButtonEnum buttonFirst;
	private String timeStampFirst;
	private ButtonEnum buttonFinal;
	private String timeStampFinal;
	private int numberOfAttempts;
	private Student student;
	
	public Summary(Vote vote, StudentList list) {
		this.id = vote.getId();
		this.buttonFirst = vote.getButton();
		this.timeStampFirst = vote.getTimeStamp();
		this.buttonFinal = vote.getButton();
		this.timeStampFinal = vote.getTimeStamp();
		this.numberOfAttempts = 1;
		this.student = list.getStudentByClickerId(this.id);
	}

	public String getId() {
		return id;
	}

	public ButtonEnum getButtonFirst() {
		return buttonFirst;
	}

	public String getTimeStampFirst() {
		return timeStampFirst;
	}

	public ButtonEnum getButtonFinal() {
		return buttonFinal;
	}

	public void setButtonFinal(ButtonEnum buttonFinal) {
		this.buttonFinal = buttonFinal;
	}

	public String getTimeStampFinal() {
		return timeStampFinal;
	}

	public void setTimeStampFinal(String timeStampFinal) {
		this.timeStampFinal = timeStampFinal;
	}

	public int getNumberOfAttempts() {
		return numberOfAttempts;
	}
	
	public void increaseNumberOfAttempts() {
		numberOfAttempts++;
	}

	public Student getStudent() {
		return student;
	}
}
