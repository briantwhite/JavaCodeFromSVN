package phylogenySurvey;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SurveyUI {
	
	private Container masterContainer;
	private JPanel workPanel;
	
	private JLabel testLabel;
	
	public SurveyUI(Container masterContainer) {
		this.masterContainer = masterContainer;
	}
	
	public void setupUI() {
		workPanel = new JPanel();
		workPanel.setPreferredSize(new Dimension(1000,1000));
		workPanel.setLayout(null);
		
		testLabel = new JLabel("HI FRED");
		testLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		workPanel.add(testLabel);
		testLabel.setLocation(100, 100);
		
		masterContainer.add(workPanel);
	}

}
