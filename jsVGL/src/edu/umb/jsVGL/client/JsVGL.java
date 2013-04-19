package edu.umb.jsVGL.client;

import java.io.UnsupportedEncodingException;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.umb.jsVGL.client.VGL.SavedProblemStrings;
import edu.umb.jsVGL.client.VGL.StringCompressionUtils;
import edu.umb.jsVGL.client.VGL.VGLII;
import edu.umb.jsVGL.client.VGL.UIimages.UIImageResource;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class JsVGL implements EntryPoint {

	private final static String WELCOME_HTML = "<html><body>"
			+ "<h3>Welcome to VGL</h3>"
			+ "You can either:"
			+ "<ul><li>Start a new problem by clicking the &quot;New Problem&quot; button above.</li>"
			+ "<li>Open work you have saved on this problem by clicking the &quot;Open Work&quot; button above.</li></ul>"
			+ "</body></html>";

	private Button clearWorkspaceButton = null;
	private Button newPracticeProblemButton = null;
	private Button newGradedProblemButton = null;
	private Button openWorkButton = null;
	private Button saveWorkButton = null;
	private Button crossButton = null;
	private Button superCrossButton = null;

	private ListBox superCrossChoices = null;

	private SimplePanel modelBuilderPanel = null;
	private FlowPanel summaryChartPanel = null;
	private FlowPanel superCrossPanel = null;

	private UIImageResource uiImageResource;

	private VGLII vglII;

	private ScrollPanel cageScrollPanel;
	private VerticalPanel cagesPanel;

	private TextArea problemText;
	private TextArea gradeText;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		uiImageResource = GWT.create(UIImageResource.class);
		Dictionary params = Dictionary.getDictionary("Params");
		vglII = new VGLII(params, this);
		buildMainPanelUI();
	}

	private void buildMainPanelUI() {

		cagesPanel = new VerticalPanel();
		cageScrollPanel = new ScrollPanel(cagesPanel);

		clearWorkspaceButton = new Button("Clear Workspace");
		RootPanel.get("clearWorkspaceButtonContainer").add(clearWorkspaceButton);
		clearWorkspaceButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				vglII.resetProblemSpace();
				resetUI();
				modelBuilderPanel.clear();
				modelBuilderPanel.add(new Label("Please Start a problem before making a model."));
			}			
		});

		newPracticeProblemButton = new Button("New Practice Problem");
		RootPanel.get("newPracticeProblemButtonContainer").add(newPracticeProblemButton);
		newPracticeProblemButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				vglII.newPracticeProblem();
				saveWorkButton.setEnabled(false);
			}			
		});

		newGradedProblemButton = new Button("New Graded Problem");
		RootPanel.get("newGradedProblemButtonContainer").add(newGradedProblemButton);
		newGradedProblemButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				vglII.newGradedProblem();
				saveWorkButton.setEnabled(true);
			}			
		});

		openWorkButton = new Button("Open Work");
		RootPanel.get("openWorkButtonContainer").add(openWorkButton);
		openWorkButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				gradeText.setText("");
				String problemXML = problemText.getText();
				vglII.openProblem(problemXML);
				saveWorkButton.setEnabled(true);
			}			
		});

		saveWorkButton = new Button("Save Work");
		RootPanel.get("saveWorkButtonConatiner").add(saveWorkButton);
		saveWorkButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SavedProblemStrings result = vglII.saveProblem();
				problemText.setText(result.problemXML);
				gradeText.setText(result.gradeXML);
			}			
		});


		crossButton = new Button("Cross Two");
		RootPanel.get("crossButtonContainer").add(crossButton);
		crossButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				vglII.crossTwo(false);
			}			
		});

		setButtonState(false);

		final TabPanel mainPanel = new TabPanel();

		final FlowPanel instructionsPanel = new FlowPanel();
		instructionsPanel.add(new HTML(WELCOME_HTML));
		mainPanel.add(instructionsPanel, "Instructions");

		modelBuilderPanel = new SimplePanel();
		modelBuilderPanel.add(new Label("Please Start a problem before making a model."));
		mainPanel.add(modelBuilderPanel, "Genetic Model");

		summaryChartPanel = new FlowPanel();
		summaryChartPanel.add(new Label("SCUI"));
		mainPanel.add(summaryChartPanel, "Summary Chart");

		superCrossPanel = new FlowPanel();
		superCrossPanel.add(new HTML(
				"<h3>Super Cross</h3>"
						+ "This carries out a cross with a large number of offpspring.<br>"
						+ "It is useful for getting recombination frequency data.<br>"
						+ "WARNING: it produces large files which are slow to save,<br>"
						+ "so it should be used sparingly.<br>"
						+"Choose the desired number of offspring from the list below:<br>"));
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


		mainPanel.selectTab(0);
		mainPanel.setSize("300px", "250px");
		mainPanel.addStyleName("table-center");
		RootPanel.get("mainPanelContainer").add(mainPanel);	

		problemText = new TextArea();
		problemText.setSize("500px", "300px");
		RootPanel.get("problemTextContainer").add(problemText);
		gradeText = new TextArea();
		gradeText.setSize("500px", "300px");
		RootPanel.get("gradeTextContainer").add(gradeText);

		cageScrollPanel.setSize("650px", "500px");
		RootPanel.get("cagesContainer").add(cageScrollPanel);
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
		clearWorkspaceButton.setEnabled(state);
		newPracticeProblemButton.setEnabled(!state);
		newGradedProblemButton.setEnabled(!state);
		openWorkButton.setEnabled(!state);
		crossButton.setEnabled(state);
		if ((vglII.getGeneticModel() != null) && (vglII.getGeneticModel().isBeginnerMode())) {
			saveWorkButton.setEnabled(false);
		} else {
			saveWorkButton.setEnabled(state);
		}
	}

	public SimplePanel getModelBuilderPanel() {
		return modelBuilderPanel;
	}

	public Panel getSummaryChartPanel() {
		return summaryChartPanel;
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
		problemText.setText("");
		gradeText.setText("");
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
