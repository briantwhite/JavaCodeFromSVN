package jsVGLReader;


public class ModelSet {
	
	private String studentModelHTML;
	private String correctModelHTML;
	
	public ModelSet(String studentModelHTML, String correctModelHTML) {
		this.studentModelHTML = studentModelHTML;
		this.correctModelHTML = correctModelHTML;
	}

	public String getStudentAnswerHTML() {
		return studentModelHTML;
	}

	public String getCorrectAnswerHTML() {
		return correctModelHTML;
	}

}
