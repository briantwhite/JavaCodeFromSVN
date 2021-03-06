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
	}
	
	

}
