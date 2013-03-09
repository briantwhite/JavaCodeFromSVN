package edu.umb.jsVGL.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;

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
	
	private Button newProblemButton = null;
	private Button openWorkButton = null;
	private Button saveWorkButton = null;
	private Button crossButton = null;
	private Button superCrossButton = null;
	
	private UIImageResource uiImageResource;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		uiImageResource = GWT.create(UIImageResource.class);
		buildMainPanelUI();
		

	}
	
	private void buildMainPanelUI() {
		newProblemButton = new Button("New Problem");
		RootPanel.get("newProblemButtonContainer").add(newProblemButton);
		newProblemButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				newProblem();
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
		
		final FlowPanel modelBuilderPanel = new FlowPanel();
		modelBuilderPanel.add(new Label("MBUI"));
		mainPanel.add(modelBuilderPanel, "Model Builder");
		
		final FlowPanel summaryChartPanel = new FlowPanel();
		summaryChartPanel.add(new Label("SCUI"));
		mainPanel.add(summaryChartPanel, "Summary Chart");
		
		mainPanel.selectTab(0);
		mainPanel.setSize("500px", "250px");
		mainPanel.addStyleName("table-center");
		RootPanel.get("mainPanelContainer").add(mainPanel);
	}
	
	public void newProblem() {
		DialogBox test = new DialogBox(false);
		Image male = new Image(uiImageResource.male());
		String picU = male.getUrl();
		test.setHTML("<img src=\"" + picU + "\">");
		test.center();
	}
}
