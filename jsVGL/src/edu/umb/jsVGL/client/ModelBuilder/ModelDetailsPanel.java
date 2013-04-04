package edu.umb.jsVGL.client.ModelBuilder;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;

import edu.umb.jsVGL.client.VGL.UIimages.UIImageResource;

public abstract class ModelDetailsPanel extends AbsolutePanel implements ChangeHandler {
	String[] phenos;
	public ListBox t1Choices;
	public ListBox t2Choices;
	public ListBox t3Choices;
	public ListBox t4Choices;
	public ListBox t5Choices;
	public ListBox t6Choices;
	ModelPane mp;
	private UIImageResource uiImageResource = GWT.create(UIImageResource.class);;
	
	Image combineArrow = new Image(uiImageResource.combineArrow());
	Image combineArrow2 = new Image(uiImageResource.combineArrow2());

	// must over-ride these for the combo boxes present in each details panel type
	public void updateT1Choices(int x) {}
	public void updateT2Choices(int x) {}
	public void updateT3Choices(int x) {}
	public void updateT4Choices(int x) {}
	public void updateT5Choices(int x) {}
	public void updateT6Choices(int x) {}
		
}
