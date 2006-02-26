import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

public class CalculateWordDoublesUI extends JPanel {
	
	private InfoLabel infoLabel;
	private JLabel warningLabel;
	private JButton calculateButton;
	private JLabel progressLabel;
	private JProgressBar progressBar;
	private int[][] pairs;
	private int numCodes;
	private PairHistogramCalculator phc;
	private Timer timer;

	
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
				phc.go();
				timer.start();
			}
		});
		
		timer = new Timer(500, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				progressBar.setValue(phc.getCurrent());
				progressLabel.setText(phc.getMessage());
				if (phc.done()){
					Toolkit.getDefaultToolkit().beep();
					timer.stop();
					progressBar.setValue(progressBar.getMaximum());
					progressLabel.setText("All done!");
				}
			}
		});
	}
	
	public void setPairs(int[][] pairs) {
		this.pairs = pairs;
		numCodes = pairs.length;
		progressBar.setMaximum(numCodes);
		progressBar.setMinimum(0);
		phc = new PairHistogramCalculator(pairs);
	}
	
	public TreeMap getHistogram() {
		return phc.getHistogram();
	}

	public void setInfoLabelText(String text){
		infoLabel.setText(text);
	}
	
	public boolean isDone() {
		return phc.done();
	}

}
