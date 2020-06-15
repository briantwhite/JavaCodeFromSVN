package edu.umb.jsVGL.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.umb.jsVGL.client.VGL.VGLII;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class JsVGL implements EntryPoint {
	
	private MenuItem saveWorkItem = null;
	private MenuItem openWorkItem = null;
	private MenuItem exportDataItem = null;
	private MenuItem clearWorkspaceItem = null;
	private MenuItem newPracticeProblemItem = null;
	private MenuItem newGradedProblemItem = null;
	private Button crossButton = null;
	private Button superCrossButton = null;

	private ListBox superCrossChoices = null;

	private SimplePanel modelBuilderPanel = null;
	private FlowPanel superCrossPanel = null;

	private VGLII vglII;

	private ScrollPanel cageScrollPanel;
	private VerticalPanel cagesPanel;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		Dictionary params = Dictionary.getDictionary("Params");
		vglII = new VGLII(params, this);
		buildMainPanelUI(params);
		exportMethods(this);
	}

	private void buildMainPanelUI(Dictionary params) {
		
		// set up the menu
		MenuBar menuBar = new MenuBar();
		menuBar.setAutoOpen(true);
		menuBar.setWidth("200px");
		menuBar.setAnimationEnabled(true);
		
		MenuBar fileMenu = new MenuBar(true);
		fileMenu.setAnimationEnabled(true);
		saveWorkItem = new MenuItem("Save work in progress...", new Command() {
			public void execute() {
				
			}
		});
		fileMenu.addItem(saveWorkItem);
		exportDataItem = new MenuItem("Export data...", new Command() {
			public void execute() {
				
			}
		});
		fileMenu.addItem(exportDataItem);
		openWorkItem = new MenuItem("Open saved work...", new Command() {
			public void execute() {
				final DialogBox openWorkDialog = new DialogBox(true, true);  // autoHide, modal
				VerticalPanel dialogPanel = new VerticalPanel();
				final FileUpload openWorkFileUpload = new FileUpload();
				HorizontalPanel hPanel = new HorizontalPanel();
				hPanel.add(openWorkFileUpload);
				hPanel.add(new Button("Load File", new ClickHandler() {
					public void onClick(ClickEvent event) {
						
					}					
				}));
				dialogPanel.add(hPanel);
				dialogPanel.add(new Button("Cancel", new ClickHandler() {
					public void onClick(ClickEvent event) {
						openWorkDialog.hide();
					}					
				}));
				openWorkDialog.setWidget(dialogPanel);
				openWorkDialog.center();
				openWorkDialog.show();
			}
		});
		fileMenu.addItem(openWorkItem);

		menuBar.addItem(new MenuItem("File",fileMenu));
		
		MenuBar problemMenu = new MenuBar(true);
		problemMenu.setAnimationEnabled(true);
		newPracticeProblemItem = new MenuItem("New Practice Problem", new Command() {
			public void execute() {
				vglII.newPracticeProblem();
			}
		});
		problemMenu.addItem(newPracticeProblemItem);
		newGradedProblemItem = new MenuItem("New Graded Problem", new Command() {
			public void execute() {
				vglII.newGradedProblem();
			}
		});
		problemMenu.addItem(newGradedProblemItem);
		clearWorkspaceItem = new MenuItem("Clear Workspace", new Command() {
			public void execute() {
				if (Window.confirm("Are you sure you want to clear the workspace?\nThis will delete all your work permanently.")) {
					vglII.resetProblemSpace();
					resetUI();
					modelBuilderPanel.clear();
					modelBuilderPanel.add(new Label("Please Start a problem before making a model."));					
				}
			}
		});
		problemMenu.addItem(clearWorkspaceItem);
		menuBar.addItem(new MenuItem("Problem", problemMenu));
		RootPanel.get("menuContainer").add(menuBar);


		crossButton = new Button("Cross Two");
		crossButton.setStyleName("jsVGL_CrossButton");
		RootPanel.get("crossButtonContainer").add(crossButton);
		crossButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				vglII.crossTwo(false);
			}			
		});
		
		setButtonState(false);

		final TabPanel mainPanel = new TabPanel();

		final FlowPanel instructionsPanel = new FlowPanel();
		instructionsPanel.add(new HTML(TextStrings.WELCOME_TEXT));
		mainPanel.add(instructionsPanel, "Instructions");

		modelBuilderPanel = new SimplePanel();
		modelBuilderPanel.add(new Label("Please Start a problem before making a model."));
		mainPanel.add(modelBuilderPanel, "Genetic Model");

		if (params.keySet().contains("SuperCrossEnabled") && Boolean.parseBoolean(params.get("SuperCrossEnabled"))) {
			superCrossPanel = new FlowPanel();
			superCrossPanel.add(new HTML(TextStrings.SUPER_CROSS_TEXT));
			superCrossChoices = new ListBox();
			superCrossChoices.addItem("100");
			superCrossChoices.addItem("200");
			superCrossChoices.addItem("500");
			superCrossChoices.addItem("1000");
			superCrossChoices.addItem("2000");
			superCrossChoices.setVisibleItemCount(1);
			superCrossPanel.add(superCrossChoices);
			superCrossButton = new Button("Super Cross");
			superCrossPanel.add(superCrossButton);
			superCrossButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					vglII.crossTwo(true);
				}			
			});

			mainPanel.add(superCrossPanel, "Super Cross");
		}
		
		final SimplePanel aboutPanel = new SimplePanel();
		aboutPanel.setWidget(new HTML(TextStrings.ABOUT_jsVGL));
		mainPanel.add(aboutPanel, "About jsVGL");

		mainPanel.selectTab(0);
		mainPanel.setSize("650px", "250px");
		mainPanel.addStyleName("table-center");
		RootPanel.get("mainPanelContainer").add(mainPanel);	

		cagesPanel = new VerticalPanel();
		cageScrollPanel = new ScrollPanel(cagesPanel);
		cageScrollPanel.setSize("650px", "500px");
		CaptionPanel captionedCageScrollPanel = new CaptionPanel("Cages with Organisms for crossing");
		captionedCageScrollPanel.add(cageScrollPanel);
		captionedCageScrollPanel.setStyleName("jsVGL_CagePanel");
		RootPanel.get("cagesContainer").add(captionedCageScrollPanel);
	}
	
	/*
	 * methods for interfacing with LMS
	 */
	
	public native void exportMethods(JsVGL jsvgl) /*-{
		$wnd.getStateXML = $entry(function() {return jsvgl.@edu.umb.jsVGL.client.JsVGL::getStateXML()();});
		$wnd.setStateXML = $entry(function(xmlString) {return jsvgl.@edu.umb.jsVGL.client.JsVGL::setStateXML(Ljava/lang/String;)(xmlString);});
		$wnd.getGradeXML = $entry(function() {return jsvgl.@edu.umb.jsVGL.client.JsVGL::getGradeXML()();});
		$wnd.getGradeHTML = $entry(function() {return jsvgl.@edu.umb.jsVGL.client.JsVGL::getGradeHTML()();});
	}-*/;
	
	public String getStateXML() {
		return vglII.saveProblem().problemXML;
	}
	
	public void setStateXML(String state) {
		resetUI();
		vglII.openProblem(state);
	}
	
	public String getGradeXML() {
		return vglII.saveProblem().gradeXML;
	}
	
	public String getGradeHTML() {
		return vglII.saveProblem().gradeHTML;
	}

	/*
	 * two states
	 * 	FALSE startup (no problem):
	 * 		clear workspace disabled
	 * 		new Prob enabled
	 * 		open work enabled
	 * 		cross disabled
	 * 		save disabled
	 * 
	 * TRUE have problem to work on
	 * 		clear workspace enabled
	 * 		new prob disabled
	 * 		open work disabled
	 * 		cross enabled
	 * 		save enabled
	 */
	public void setButtonState(boolean state) {
		clearWorkspaceItem.setEnabled(state);
		newPracticeProblemItem.setEnabled(!state);
		newGradedProblemItem.setEnabled(!state);
		saveWorkItem.setEnabled(state);
		openWorkItem.setEnabled(!state);
		exportDataItem.setEnabled(state);
		crossButton.setEnabled(state);
		if (state) {
			clearWorkspaceItem.setStyleName("jsVGL_EnabledMenuItem");
			newPracticeProblemItem.setStyleName("jsVGL_DisabledMenuItem");
			newGradedProblemItem.setStyleName("jsVGL_DisabledMenuItem");
			openWorkItem.setStyleName("jsVGL_DisabledMenuItem");
			saveWorkItem.setStyleName("jsVGL_EnabledMenuItem");
			exportDataItem.setStyleName("jsVGL_EnabledMenuItem");
		} else {
			clearWorkspaceItem.setStyleName("jsVGL_DisabledMenuItem");
			newPracticeProblemItem.setStyleName("jsVGL_EnabledMenuItem");
			newGradedProblemItem.setStyleName("jsVGL_EnabledMenuItem");
			openWorkItem.setStyleName("jsVGL_EnabledMenuItem");
			saveWorkItem.setStyleName("jsVGL_DisabledMenuItem");
			exportDataItem.setStyleName("jsVGL_DisabledMenuItem");
		}
	}

	public SimplePanel getModelBuilderPanel() {
		return modelBuilderPanel;
	}

	public Panel getSuperCrossPanel() {
		return superCrossPanel;
	}

	public VerticalPanel getCagesPanel() {
		return cagesPanel;
	}

	public void scrollCagesToBottom() {
		cageScrollPanel.scrollToBottom();
	}

	public void resetUI() {
		cagesPanel.clear();
		setButtonState(false);
	}

	public int getSuperCrossChoice() {
		if (superCrossChoices != null) {
			return Integer.parseInt(superCrossChoices.getValue(superCrossChoices.getSelectedIndex()));
		} else {
			return 0;
		}
	}

}
