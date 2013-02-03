package protex;


public class ProblemSpecification {

	public String targetShapeString;
	public String edXCookieURL;
	public String edXLoginURL;
	public String edXSubmissionURL;
	public String edXLocation;
	public boolean complete;

	public ProblemSpecification() {
		targetShapeString = null;
		edXCookieURL = null;
		edXLoginURL = null;
		edXSubmissionURL = null;
		edXLocation = null;	
		complete = false;
	}
	
	public void setTargetShapeString(String targetShapeString) {
		this.targetShapeString = targetShapeString;
		complete = checkCompleteness();
	}
	
	public void setEdXCookieURL(String edXCookieURL) {
		this.edXCookieURL = edXCookieURL;
		complete = checkCompleteness();
	}

	public void setEdXLoginURL(String edXLoginURL) {
		this.edXLoginURL = edXLoginURL;
		complete = checkCompleteness();
	}

	public void setEdXSubmissionURL(String edXSubmissionURL) {
		this.edXSubmissionURL = edXSubmissionURL;
		complete = checkCompleteness();
	}

	public void setEdXLocation(String edXLocation) {
		this.edXLocation = edXLocation;
		complete = checkCompleteness();
	}
	
	private boolean checkCompleteness() {
		boolean complete = ((targetShapeString != null) 
				&& (edXCookieURL != null) 
				&& (edXLocation != null) 
				&& (edXLoginURL != null) 
				&& (edXSubmissionURL != null));
		return complete;
	}
	
}
