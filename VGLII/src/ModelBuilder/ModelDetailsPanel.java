package ModelBuilder;

import java.awt.event.ItemListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import VGL.CageUI;

public abstract class ModelDetailsPanel extends JPanel implements ItemListener {
	String[] phenos;
	JComboBox t1Choices;
	JComboBox t2Choices;
	JComboBox t3Choices;
	JComboBox t4Choices;
	JComboBox t5Choices;
	JComboBox t6Choices;
	ModelPane mp;
	
	private URL combineArrowURL = CageUI.class.getResource("UIimages/combineArrow.gif");
	ImageIcon combineArrow = new ImageIcon(combineArrowURL);

	private URL combineArrow2URL = CageUI.class.getResource("UIimages/combineArrow2.gif");
	ImageIcon combineArrow2 = new ImageIcon(combineArrow2URL);

	// must over-ride these for the combo boxes present in each details panel type
	public void updateT1Choices(String s) {}
	public void updateT2Choices(String s) {}
	public void updateT3Choices(String s) {}
	public void updateT4Choices(String s) {}
	public void updateT5Choices(String s) {}
	public void updateT6Choices(String s) {}
	
}
