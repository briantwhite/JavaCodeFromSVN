package edu.umb.jsAipotu.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

import edu.umb.jsAipotu.client.biochem.FoldedProteinArchive;
import edu.umb.jsAipotu.client.molGenExp.Greenhouse;
import edu.umb.jsAipotu.client.molGenExp.GreenhouseCell;
import edu.umb.jsAipotu.client.molGenExp.GreenhouseLoader;



/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class JsAipotu implements EntryPoint {

	//indices for tabbed panes
	private final static int GENETICS = 0;
	private final static int BIOCHEMISTRY = 1;
	private final static int MOLECULAR_BIOLOGY = 2;
	private final static int EVOLUTION = 3;

	//private MolGenExp mge;

	// gui elements
	private SplitLayoutPanel mainPanel = null;
	private CaptionPanel greenhouseWrapper = null;
	private ScrollPanel greenhousePanel = null;
	private MenuBar menuBar = null;
	private MenuBar fileMenu = null;
	private MenuBar editMenu = null;
	private MenuBar compareMenu = null;
	private MenuBar greenhouseMenu = null;

	private Greenhouse greenhouse; 
	private GreenhouseLoader greenhouseLoader;
	
	public void onModuleLoad() {
		//mge = new MolGenExp();
		buildUI();
	}

	private void buildUI() {
		mainPanel = new SplitLayoutPanel(5);
		mainPanel.setSize("500px", "500px");

		//		final TabLayoutPanel workspace = new TabLayoutPanel(1.5, Unit.EM);
		//		mainPanel.add(workspace);


		// menus
		menuBar = new MenuBar(false);
		menuBar.setStyleName("mainMenuBar");
		// File menu
		fileMenu = new MenuBar(true);
		fileMenu.addItem("Preferences", new Command() {
			public void execute() {

			}
		});
		menuBar.addItem("File", fileMenu);
		// Edit menu
		editMenu = new MenuBar(true);
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
		compareMenu = new MenuBar(true);
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
		greenhouseMenu = new MenuBar(true);
		greenhouseMenu.addItem("Load Greenhouse file...", new Command() {
			public void execute() {

			}
		});
		greenhouseMenu.addItem("Save Greenhouse to file...", new Command() {
			public void execute() {

			}
		});
		menuBar.addItem("Greenhouse", greenhouseMenu);
		mainPanel.addNorth(menuBar, 50);
		
		greenhouseWrapper = new CaptionPanel("Greenhouse");
		greenhouse = new Greenhouse(new GreenhouseCell());
		greenhousePanel = new ScrollPanel(greenhouse);
		greenhouse.setSize("100px", "600px");
		greenhouseWrapper.setContentWidget(greenhousePanel);
		greenhouseLoader = new GreenhouseLoader(greenhouse);
		greenhouseLoader.load("default.greenhouse");
		mainPanel.addWest(greenhousePanel, 105);


		//RootPanel.get("mainPanelContainer").add(menuBar);
		RootPanel.get("mainPanelContainer").add(mainPanel);
	}

}
