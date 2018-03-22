package PathwayPanel;

public class PathwayDrawingException extends Exception {
	
	private String message;
	
	public PathwayDrawingException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
