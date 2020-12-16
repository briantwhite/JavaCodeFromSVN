package edu.umb.jsAipotu.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.umb.jsAipotu.client.biochem.BiochemistryWorkbench;
import edu.umb.jsAipotu.client.evolution.EvolutionWorkArea;
import edu.umb.jsAipotu.client.genetics.GeneticsWorkbench;
import edu.umb.jsAipotu.client.molBiol.MolBiolWorkbench;
import edu.umb.jsAipotu.client.molGenExp.MolGenExp;



/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class JsAipotu implements EntryPoint {
	
	private MolGenExp mge;

	// gui elements
	private DockPanel mainPanel = null;
	private MenuBar menuBar = null;
	private MenuBar fileMenu = null;
	private MenuBar editMenu = null;
	private MenuBar compareMenu = null;
	private MenuBar greenhouseMenu = null;

	private HorizontalPanel innerPanel = null;
	
	private TabLayoutPanel explorerPane = null;
//	private GeneticsWorkbench geneticsWorkbench = null;
//	private BiochemistryWorkbench biochemistryWorkbench = null;
//	private MolBiolWorkbench molBiolWorkbench = null;
//	private EvolutionWorkArea evolutionWorkArea = null;
	
	private VerticalPanel rightPanel = null;
	private CaptionPanel rightPanelCaption = null;
	private Button addToGreenhouseButton = null;
	private ScrollPanel greenhousePanel = null;
		
	public void onModuleLoad() {
		mge = new MolGenExp();
		buildUI();
	}

	private void buildUI() {
		mainPanel = new DockPanel();
		mainPanel.setSize("1000px", "500px");

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
		mainPanel.add(menuBar, DockPanel.NORTH);
		
		innerPanel = new HorizontalPanel();
		
		// explorer pane - tabbed pane for the 4 workbenches/workarea
//		explorerPane = new TabLayoutPanel(5, Unit.PX);
//		geneticsWorkbench = new GeneticsWorkbench(mge);
//		explorerPane.add(geneticsWorkbench, "Genetics");
//		biochemistryWorkbench = new BiochemistryWorkbench(mge);
//		explorerPane.add(biochemistryWorkbench, "Biochemistry");
//		molBiolWorkbench = new MolBiolWorkbench(mge);
//		explorerPane.add(molBiolWorkbench, "Molecular Biology");
//		evolutionWorkArea = new EvolutionWorkArea(mge);
//		explorerPane.add(evolutionWorkArea, "Evolution");
//		innerPanel.add(explorerPane);
		
		// right-hand side: greenhouse and add to greenhouse button
		rightPanel = new VerticalPanel();
		rightPanelCaption = new CaptionPanel("Greenhouse");
		addToGreenhouseButton = new Button("Add...");
		addToGreenhouseButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
			}
		});
		rightPanel.add(addToGreenhouseButton);
		greenhousePanel = new ScrollPanel(mge.getGreenhouse());
		greenhousePanel.setWidth("100px");
		rightPanel.add(greenhousePanel);	
		rightPanelCaption.setContentWidget(rightPanel);
		innerPanel.add(rightPanelCaption);

		mainPanel.add(innerPanel, DockPanel.CENTER);
		
		RootPanel.get("mainPanelContainer").add(mainPanel);
	}

	public static native void consoleLog(String message) /*-{
		console.log(message);
	}-*/;
}
