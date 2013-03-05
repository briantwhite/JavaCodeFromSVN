package edu.umb.jsVGL.client.VGL;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

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
	
	public Element save() {
		Element e = new Element("EdXServerStrings");
		e.setAttribute("edXCookieURL", edXCookieURL);
		e.setAttribute("edXLoginURL", edXLoginURL);
		e.setAttribute("edXSubmissionURL", edXSubmissionURL);
		e.setAttribute("edXLocation", edXLocation);
		return e;
	}
	
	public String toString() {
		Element root = new Element("ProblemSpec");
		try {
			root.addContent(save());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Document doc = new Document(root);
		XMLOutputter outputter = 
				new XMLOutputter(Format.getPrettyFormat());
		return outputter.outputString(doc);
	}

}
