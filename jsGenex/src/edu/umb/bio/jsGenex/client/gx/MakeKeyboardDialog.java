package edu.umb.bio.jsGenex.client.gx;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MakeKeyboardDialog {
	
	public static DialogBox makeKeyboardDialog(final GenexGWT genexGWT) {
		
	    final DialogBox keyboardDialog = new DialogBox(true);
	    
	    VerticalPanel mainPanel = new VerticalPanel();
	    
	    mainPanel.add(new HTML("Change base to:"));
	    
	    HorizontalPanel hp1 = new HorizontalPanel();
	    Button changeToAButton = new Button("A");
	    hp1.add(changeToAButton);
	    changeToAButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				genexGWT.replaceBase("A");
			}
	    });
	    Button changeToGButton = new Button("G");
	    hp1.add(changeToGButton);
	    changeToGButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				genexGWT.replaceBase("G");
			}
	    });
	    Button changeToCButton = new Button("C");
	    hp1.add(changeToCButton);
	    changeToCButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				genexGWT.replaceBase("C");
			}
	    });
	    Button changeToTButton = new Button("T");
	    hp1.add(changeToTButton);
	    changeToTButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				genexGWT.replaceBase("T");
			}
	    });
	    mainPanel.add(hp1);
	    
	    mainPanel.add(new HTML("Add a:"));
	    
	    HorizontalPanel hp2 = new HorizontalPanel();
	    Button addAButton = new Button("A");
	    hp2.add(addAButton);
	    addAButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				genexGWT.insertBase("A");
			}
	    });
	    Button addGButton = new Button("G");
	    hp2.add(addGButton);
	    addGButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				genexGWT.insertBase("G");
			}
	    });
	    Button addCButton = new Button("C");
	    hp2.add(addCButton);
	    addCButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				genexGWT.insertBase("C");
			}
	    });
	    Button addTButton = new Button("T");
	    hp2.add(addTButton);
	    addTButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				genexGWT.insertBase("T");
			}
	    });
	    mainPanel.add(hp2);
	    
	    mainPanel.add(new HTML("<hr>"));
	    
	    HorizontalPanel hp3 = new HorizontalPanel();
	    Button deleteButton = new Button("DEL");
	    hp3.add(deleteButton);
	    deleteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				genexGWT.deleteBase();
			}
	    });
	    Button leftButton = new Button("&larr;");
	    hp3.add(leftButton);
	    leftButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				genexGWT.moveLeft();
			}
	    });
	    Button rightButton = new Button("&rarr;");
	    hp3.add(rightButton);
	    rightButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				genexGWT.moveRight();
			}
	    });
	    Button closeButton = new Button("<font color='red'>X</font>");
	    hp3.add(closeButton);
	    closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				keyboardDialog.hide();
			}
	    });
	    mainPanel.add(hp3);

	    keyboardDialog.add(mainPanel);
	    return keyboardDialog;
	}

}
