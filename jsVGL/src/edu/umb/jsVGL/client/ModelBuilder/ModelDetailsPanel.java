package edu.umb.jsVGL.client.ModelBuilder;

import java.awt.event.ItemListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import VGL.CageUI;
import VGL.Messages;

public abstract class ModelDetailsPanel extends JPanel implements ItemListener {
	String[] phenos;
	public JComboBox t1Choices;
	public JComboBox t2Choices;
	public JComboBox t3Choices;
	public JComboBox t4Choices;
	public JComboBox t5Choices;
	public JComboBox t6Choices;
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
		
	public abstract String getAsHtml(boolean isForGrader);
}
