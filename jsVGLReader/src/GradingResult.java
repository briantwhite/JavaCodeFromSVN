

public class GradingResult {
	
	private String studentAnswerHTML;
	private String correctAnswerHTML;
	
	public GradingResult(String studentAnswerHTML, String correctAnswerHTML) {
		this.studentAnswerHTML = studentAnswerHTML;
		this.correctAnswerHTML = correctAnswerHTML;
	}

	public String getStudentAnswerHTML() {
		return studentAnswerHTML;
	}

	public String getCorrectAnswerHTML() {
		return correctAnswerHTML;
	}

}
