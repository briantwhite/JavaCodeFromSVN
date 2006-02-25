import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class CalculateWordDoublesUI extends JPanel {
	
	private InfoLabel infoLabel;
	private JLabel warningLabel;
	private JButton calculateButton;
	private JLabel progressLabel;
	private JProgressBar progressBar;
	private int[][] pairs;
	private TreeMap histogram;
	private int numCodes;

	
	public CalculateWordDoublesUI() {
		super();
		infoLabel = new InfoLabel("Hi there");
		this.setLayout(new BorderLayout());
		this.add(infoLabel, BorderLayout.NORTH);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		calculateButton = new JButton("Calculate Histogram");
		warningLabel = new JLabel("This can take a while!");
		progressLabel = new JLabel("No progress yet.");
		progressBar = new JProgressBar();
		buttonPanel.add(calculateButton);
		buttonPanel.add(warningLabel);
		buttonPanel.add(progressBar);
		buttonPanel.add(progressLabel);
		this.add(buttonPanel, BorderLayout.CENTER);
		this.pairs = pairs;
		
		calculateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				histogram = new TreeMap();
				for (int x = 0; x < numCodes; x++){
					for (int y = 0; y < numCodes; y++){
						Integer count = new Integer(pairs[x][y]);
						if (histogram.containsKey(count)) {
							Integer oldTally = (Integer)histogram.get(count);
							histogram.put(count, new Integer(oldTally.intValue() + 1));
						} else {
							histogram.put(count, new Integer(1));
						}
					}
					progressBar.setValue(x);
				}
			}
		});
	}
	
	public void setPairs(int[][] pairs) {
		this.pairs = pairs;
		numCodes = pairs.length;
		progressBar.setMaximum(numCodes);
		progressBar.setMinimum(0);
	}
	
	public TreeMap getHistogram() {
		return histogram;
	}

	public void setInfoLabelText(String text){
		infoLabel.setText(text);
	}
	

}
