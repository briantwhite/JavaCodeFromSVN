package edu.umb.jsAipotu.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class JsAipotu implements EntryPoint {
	
	//indices for tabbed panes
	private final static int GENETICS = 0;
	private final static int BIOCHEMISTRY = 1;
	private final static int MOLECULAR_BIOLOGY = 2;
	private final static int EVOLUTION = 3;
	
	public void onModuleLoad() {
		buildUI();
	}
	
	private void buildUI() {
				
		final DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.EM);
		
		MenuBar menuBar = new MenuBar(false);
		
		MenuBar fileMenu = new MenuBar(true);
		fileMenu.addItem("Preferences", new Command() {
			public void execute() {
				
			}
		});
		menuBar.addItem("File", fileMenu);
		
		MenuBar editMenu = new MenuBar(true);
		editMenu.addItem("Copy upper sequence to clipboard", new Command() {
			public void execute() {
				
			}
		});
		editMenu.addItem("Copy lower sequence to clipboard", new Command() {
			public void execute() {
				
			}
		});
		menuBar.addItem("Edit", editMenu);
		
		
		RootPanel.get("mainPanelContainer").add(menuBar);
	}
}
