package ModelBuilder;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public abstract class ModelDetailsPanel extends JPanel {
	String[] phenos;
	JComboBox t1Choices;
	JComboBox t2Choices;
	JComboBox t3Choices;
	JComboBox t4Choices;
	JComboBox t5Choices;
	JComboBox t6Choices;
	
	public abstract String[] getChoices();

}
