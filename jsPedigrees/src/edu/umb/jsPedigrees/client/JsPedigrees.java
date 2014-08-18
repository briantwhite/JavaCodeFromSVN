package edu.umb.jsPedigrees.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

import edu.umb.jsPedigrees.client.Pelican.Pelican;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class JsPedigrees implements EntryPoint {
	
	private Pelican pelican;

	public void onModuleLoad() {
		
		setupUI();
	}
	
	private void setupUI() {
		RootPanel rootPanel = RootPanel.get("jsPedigreesContainer");
		pelican = new Pelican(rootPanel);
		rootPanel.add(pelican);
		exportMethods(this);
	}
	
	/*
	 * methods for interfacing with LMS
	 */
	
	public native void exportMethods(JsPedigrees jsped) /*-{
		$wnd.getStateXML = $entry(function() {return jsped.@edu.umb.jsPedigrees.client.JsPedigrees::getStateXML()();});
		$wnd.setStateXML = $entry(function(xmlString) {return jsped.@edu.umb.jsPedigrees.client.JsPedigrees::setStateXML(Ljava/lang/String;)(xmlString);});
		$wnd.getGradeXML = $entry(function() {return jsped.@edu.umb.jsPedigrees.client.JsPedigrees::getGradeXML()();});
	}-*/;
	
	public String getStateXML() {
		return pelican.getState();
	}
	
	public void setStateXML(String state) {
		pelican.setState(state);
	}
	
	public String getGradeXML() {
		return pelican.getGrade();
	}
	

}
