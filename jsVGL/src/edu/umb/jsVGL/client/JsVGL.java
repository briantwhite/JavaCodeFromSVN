package edu.umb.jsVGL.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

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
	
	private Button newPracticeProblemButton = null;
	private Button newGradedProblemButton = null;
	private Button openWorkButton = null;
	private Button saveWorkButton = null;
	private Button crossButton = null;
	private Button superCrossButton = null;
	
	private FlowPanel modelBuilderPanel = null;
	private FlowPanel summaryChartPanel = null;
	
	private UIImageResource uiImageResource;

	private VGLII vglII;
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		uiImageResource = GWT.create(UIImageResource.class);
		buildMainPanelUI();
		Dictionary params = Dictionary.getDictionary("Params");
		vglII = new VGLII(params, modelBuilderPanel, summaryChartPanel);
	}
	
	private void buildMainPanelUI() {
		newPracticeProblemButton = new Button("New Practice Problem");
		RootPanel.get("newPracticeProblemButtonContainer").add(newPracticeProblemButton);
		newPracticeProblemButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				saveWorkButton.setEnabled(false);
				vglII.newPracticeProblem();
			}			
		});
		
		newGradedProblemButton = new Button("New Graded Problem");
		RootPanel.get("newGradedProblemButtonContainer").add(newGradedProblemButton);
		newGradedProblemButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				saveWorkButton.setEnabled(true);
				newPracticeProblem();
			}			
		});
		
		openWorkButton = new Button("Open Work");
		RootPanel.get("openWorkButtonContainer").add(openWorkButton);
		
		saveWorkButton = new Button("Save Work");
		RootPanel.get("saveWorkButtonConatiner").add(saveWorkButton);
		saveWorkButton.setEnabled(false);
		
		crossButton = new Button("Cross Two");
		RootPanel.get("crossButtonContainer").add(crossButton);
		crossButton.setEnabled(false);

		superCrossButton = new Button("Super Cross");
		if (RootPanel.get("superCrossButtonContainer") != null) {
			RootPanel.get("superCrossButtonContainer").add(superCrossButton);
		}
		superCrossButton.setEnabled(false);

		final TabPanel mainPanel = new TabPanel();
		
		final FlowPanel instructionsPanel = new FlowPanel();
		instructionsPanel.add(new HTML(WELCOME_HTML));
		mainPanel.add(instructionsPanel, "Instructions");
		
		modelBuilderPanel = new FlowPanel();
		modelBuilderPanel.add(new Label("MBUI"));
		mainPanel.add(modelBuilderPanel, "Model Builder");
		
		summaryChartPanel = new FlowPanel();
		summaryChartPanel.add(new Label("SCUI"));
		mainPanel.add(summaryChartPanel, "Summary Chart");
		
		mainPanel.selectTab(0);
		mainPanel.setSize("500px", "250px");
		mainPanel.addStyleName("table-center");
		RootPanel.get("mainPanelContainer").add(mainPanel);	
	}
	
	public void newPracticeProblem() {
		final DialogBox test = new DialogBox();
		test.setHTML("&nbsp;&nbsp;Cage 2");
		
		VerticalPanel panel = new VerticalPanel();
		panel.setSize("100px", "100px");
		Image closeButtonImage = new Image(uiImageResource.closeButton());
		Anchor closeButton = new Anchor("");
		closeButton.getElement().appendChild(closeButtonImage.getElement());
		closeButton.setTitle("Close Cage");
		panel.add(closeButton);
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				test.hide();
			}			
		});
		closeButton.setStyleName("TopRight");

		Image male = new Image(uiImageResource.male());
		panel.add(male);
		
		test.add(panel);
		test.center();
	}
}
