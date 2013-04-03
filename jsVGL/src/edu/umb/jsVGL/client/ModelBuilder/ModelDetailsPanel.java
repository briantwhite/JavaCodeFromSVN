package edu.umb.jsVGL.client.ModelBuilder;

import java.net.URL;

import javax.swing.ImageIcon;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ListBox;

import edu.umb.jsVGL.client.VGL.CageUI;

public abstract class ModelDetailsPanel extends AbsolutePanel implements ChangeHandler {
	String[] phenos;
	public ListBox t1Choices;
	public ListBox t2Choices;
	public ListBox t3Choices;
	public ListBox t4Choices;
	public ListBox t5Choices;
	public ListBox t6Choices;
	ModelPane mp;
	
	private URL combineArrowURL = CageUI.class.getResource("UIimages/combineArrow.gif");
	ImageIcon combineArrow = new ImageIcon(combineArrowURL);

	private URL combineArrow2URL = CageUI.class.getResource("UIimages/combineArrow2.gif");
	ImageIcon combineArrow2 = new ImageIcon(combineArrow2URL);

	// must over-ride these for the combo boxes present in each details panel type
	public void updateT1Choices(int x) {}
	public void updateT2Choices(int x) {}
	public void updateT3Choices(int x) {}
	public void updateT4Choices(int x) {}
	public void updateT5Choices(int x) {}
	public void updateT6Choices(int x) {}
		
}
