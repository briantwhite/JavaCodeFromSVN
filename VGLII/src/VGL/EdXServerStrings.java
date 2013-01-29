package VGL;

public class EdXServerStrings {
	
	public String edXCookieURL;
	public String edXLoginURL;
	public String edXSubmissionURL;
	public String edXLocation;

	public EdXServerStrings() {
		edXCookieURL = null;
		edXLoginURL = null;
		edXSubmissionURL = null;
		edXLocation = null;		
	}
	
	public void setEdXCookieURL(String edXCookieURL) {
		this.edXCookieURL = edXCookieURL;
	}

	public void setEdXLoginURL(String edXLoginURL) {
		this.edXLoginURL = edXLoginURL;
	}

	public void setEdXSubmissionURL(String edXSubmissionURL) {
		this.edXSubmissionURL = edXSubmissionURL;
	}

	public void setEdXLocation(String edXLocation) {
		this.edXLocation = edXLocation;
	}

}
