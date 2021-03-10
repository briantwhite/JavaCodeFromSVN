package edu.umb.jsAipotu.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.umb.jsAipotu.client.biochem.BiochemistryWorkbench;
import edu.umb.jsAipotu.client.biochem.BiochemistryWorkpanel;
import edu.umb.jsAipotu.client.genetics.GeneticsWorkbench;
import edu.umb.jsAipotu.client.molBiol.MolBiolWorkbench;
import edu.umb.jsAipotu.client.molBiol.MolBiolWorkpanel;
import edu.umb.jsAipotu.client.molGenExp.DNASequenceComparator;
import edu.umb.jsAipotu.client.molGenExp.MolGenExp;
import edu.umb.jsAipotu.client.molGenExp.ProteinSequenceComparator;
import edu.umb.jsAipotu.client.molGenExp.SequenceComparator;
import edu.umb.jsAipotu.client.molGenExp.SequenceComparatorCommand;
import edu.umb.jsAipotu.client.preferences.GlobalDefaults;



/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class JsAipotu implements EntryPoint {

	private MolGenExp mge;

	private String probeSequence;

	// gui elements
	private DockLayoutPanel mainPanel = null;
	private MenuBar menuBar = null;
	private MenuBar fileMenu = null;
	private MenuBar editMenu = null;
	private MenuBar compareMenu = null;
	private MenuBar greenhouseMenu = null;

	private HorizontalPanel innerPanel = null;

	private TabLayoutPanel explorerPane = null;
	private GeneticsWorkbench geneticsWorkbench = null;
	private BiochemistryWorkbench biochemistryWorkbench = null;
	private MolBiolWorkbench molBiolWorkbench = null;
	//	private EvolutionWorkArea evolutionWorkArea = null;

	private VerticalPanel rightPanel = null;
	private CaptionPanel rightPanelCaption = null;
	private Button addToGreenhouseButton = null;
	private ScrollPanel greenhousePanel = null;

	public void onModuleLoad() {
		mge = new MolGenExp(this);
		buildUI();
		exportMethods(this);
	}

	private void buildUI() {
		mainPanel = new DockLayoutPanel(Unit.PX);
		mainPanel.setStyleName("mainPanel");

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
		editMenu.addItem("Copy upper sequence to Probe", new Command() {
			public void execute() {
				switch (explorerPane.getSelectedIndex()) {
				case MolGenExp.GENETICS:
					return;
				case MolGenExp.BIOCHEMISTRY:
					probeSequence = ((BiochemistryWorkpanel)biochemistryWorkbench.getUpperPanel()).getAaSeq();
					return;
				case MolGenExp.MOLECULAR_BIOLOGY:
					probeSequence = ((MolBiolWorkpanel)molBiolWorkbench.getUpperPanel()).getDNA();
					return;
				case MolGenExp.EVOLUTION:
					return;
				}
			}
		});
		editMenu.addItem("Copy lower sequence to Probe", new Command() {
			public void execute() {
				switch (explorerPane.getSelectedIndex()) {
				case MolGenExp.GENETICS:
					return;
				case MolGenExp.BIOCHEMISTRY:
					probeSequence = ((BiochemistryWorkpanel)biochemistryWorkbench.getLowerPanel()).getAaSeq();
					return;
				case MolGenExp.MOLECULAR_BIOLOGY:
					probeSequence = ((MolBiolWorkpanel)molBiolWorkbench.getLowerPanel()).getDNA();
					return;
				case MolGenExp.EVOLUTION:
					return;
				}
			}
		});
		menuBar.addItem("Edit", editMenu);
		// Compare menu
		compareMenu = new MenuBar(true);
		compareMenu.addItem("Upper vs Lower", new SequenceComparatorCommand(this, SequenceComparator.UPPER, SequenceComparator.LOWER));
		compareMenu.addItem("Upper vs Sample", new SequenceComparatorCommand(this, SequenceComparator.UPPER, SequenceComparator.SAMPLE));
		compareMenu.addItem("Lower vs Sample", new SequenceComparatorCommand(this, SequenceComparator.LOWER, SequenceComparator.SAMPLE));
		compareMenu.addItem("Upper vs Probe", new SequenceComparatorCommand(this, SequenceComparator.UPPER, SequenceComparator.PROBE));
		compareMenu.addItem("Lower vs Probe", new SequenceComparatorCommand(this, SequenceComparator.LOWER, SequenceComparator.PROBE));
		menuBar.addItem("Compare", compareMenu);
		// Greenhouse menu
		greenhouseMenu = new MenuBar(true);
		greenhouseMenu.addItem("Load Greenhouse file...", new Command() {
			public void execute() {
				mge.showLoadGreenhouseFileDialog();
			}
		});
		greenhouseMenu.addItem("Save Greenhouse to file...", new Command() {
			public void execute() {
				mge.saveGreenhouseToFile();
			}
		});
		menuBar.addItem("Greenhouse", greenhouseMenu);
		mainPanel.addNorth(menuBar, 30);

		innerPanel = new HorizontalPanel();

		// explorer pane - tabbed pane for the 4 workbenches/workarea
		explorerPane = new TabLayoutPanel(40, Unit.PX);
		explorerPane.setStyleName("explorerPane");
		geneticsWorkbench = new GeneticsWorkbench(mge);
		explorerPane.add(geneticsWorkbench, "Genetics");
		biochemistryWorkbench = new BiochemistryWorkbench(mge);
		explorerPane.add(biochemistryWorkbench, "Biochemistry");
		molBiolWorkbench = new MolBiolWorkbench(mge);
		explorerPane.add(molBiolWorkbench, "Molecular Biology");
		//		evolutionWorkArea = new EvolutionWorkArea(mge);
		//		explorerPane.add(evolutionWorkArea, "Evolution");
		innerPanel.add(explorerPane);

		// right-hand side: greenhouse and add to greenhouse button
		rightPanel = new VerticalPanel();
		rightPanelCaption = new CaptionPanel("Greenhouse");
		addToGreenhouseButton = new Button("Add...");
		addToGreenhouseButton.setEnabled(false);
		rightPanel.add(addToGreenhouseButton);
		greenhousePanel = new ScrollPanel(mge.getGreenhouse());
		greenhousePanel.setStyleName("greenhousePanel");
		rightPanel.add(greenhousePanel);	
		rightPanelCaption.setContentWidget(rightPanel);
		innerPanel.add(rightPanelCaption);

		mainPanel.add(innerPanel);

		RootPanel.get("mainPanelContainer").add(mainPanel);

		explorerPane.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				mge.clearSelectedOrganisms();
				enableAddToGreenhouseButton(false);
			}
		});

		addToGreenhouseButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				mge.saveSelectedOrganismToGreenhouse();
			}
		});

	}

	public int getSelectedTabIndex() {
		return explorerPane.getSelectedIndex();
	}
	
	public ProteinSequenceComparator getProteinSequenceComparator() {
		return new ProteinSequenceComparator(
				((BiochemistryWorkpanel)biochemistryWorkbench.getUpperPanel()).getAaSeq(),
				((BiochemistryWorkpanel)biochemistryWorkbench.getLowerPanel()).getAaSeq(),
				GlobalDefaults.sampleProtein,
				probeSequence);
	}
	
	public DNASequenceComparator getDNASequenceComparator() {
		return new DNASequenceComparator(
				((MolBiolWorkpanel)molBiolWorkbench.getUpperPanel()).getDNA(),
				((MolBiolWorkpanel)molBiolWorkbench.getLowerPanel()).getDNA(),
				GlobalDefaults.sampleDNA,
				probeSequence);
	}

	public static native void consoleLog(String message) /*-{
		console.log(message);
	}-*/;

	public GeneticsWorkbench getGeneticsWorkbench() {
		return geneticsWorkbench;
	}

	public BiochemistryWorkbench getBiochemWorkbench() {
		return biochemistryWorkbench;
	}

	public MolBiolWorkbench getMolBiolWorkbench() {
		return molBiolWorkbench;
	}

	public void enableAddToGreenhouseButton(boolean b) {
		addToGreenhouseButton.setEnabled(b);
	}

	public void setGreenhouse(String greenhouseString) {
		mge.loadGreenhouseFromFile(greenhouseString);
	}

	public native void exportMethods(JsAipotu jsA) /*-{
		$wnd.setGreenhouse = $entry(function(greenhouseString) {return jsA.@edu.umb.jsAipotu.client.JsAipotu::setGreenhouse(Ljava/lang/String;)(greenhouseString);});
	}-*/;

}
