package edu.umb.jsAipotu.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;


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
		SplitLayoutPanel mainPanel = new SplitLayoutPanel(5);
		CaptionPanel greenhousePanel = new CaptionPanel("Greenhouse");
		ScrollPanel greenhouse = new ScrollPanel(new HTML("HI"));
		greenhousePanel.setContentWidget(greenhouse);

		mainPanel.addWest(greenhousePanel, 100);
		mainPanel.setSize("500px", "500px");
		
//		final TabLayoutPanel workspace = new TabLayoutPanel(1.5, Unit.EM);
//		mainPanel.add(workspace);
		
		
		// menus
		MenuBar menuBar = new MenuBar(false);
		menuBar.setStyleName("mainMenuBar");
		// File menu
		MenuBar fileMenu = new MenuBar(true);
		fileMenu.addItem("Preferences", new Command() {
			public void execute() {
				
			}
		});
		menuBar.addItem("File", fileMenu);
		// Edit menu
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
		// Compare menu
		MenuBar compareMenu = new MenuBar(true);
		compareMenu.addItem("Upper vs Lower", new Command() {
			public void execute() {
				
			}
		});
		compareMenu.addItem("Upper vs Sample", new Command() {
			public void execute() {
				
			}
		});
		compareMenu.addItem("Lower vs Sample", new Command() {
			public void execute() {
				
			}
		});
		compareMenu.addItem("Upper vs Clipboard", new Command() {
			public void execute() {
				
			}
		});
		compareMenu.addItem("Lower vs Clipboard", new Command() {
			public void execute() {
				
			}
		});
		menuBar.addItem("Compare", compareMenu);
		// Greenhouse menu
		MenuBar greenhouseMenu = new MenuBar(true);
		greenhouseMenu.addItem("Load Greenhouse file...", new Command() {
			public void execute() {
				
			}
		});
		greenhouseMenu.addItem("Save Greenhouse to file...", new Command() {
			public void execute() {
				
			}
		});
		menuBar.addItem("Greenhouse", greenhouseMenu);
		
		RootPanel.get("mainPanelContainer").add(menuBar);
		RootPanel.get("mainPanelContainer").add(mainPanel);
	}
}
