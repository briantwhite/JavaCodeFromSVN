package ModelBuilder;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import VGL.CageUI;

public abstract class ModelDetailsPanel extends JPanel {
	String[] phenos;
	JComboBox t1Choices;
	JComboBox t2Choices;
	JComboBox t3Choices;
	JComboBox t4Choices;
	JComboBox t5Choices;
	JComboBox t6Choices;
	
	private URL combineArrowURL = CageUI.class.getResource("UIimages/combineArrow.gif");
	ImageIcon combineArrow = new ImageIcon(combineArrowURL);
	
	public abstract String[] getChoices();

}
