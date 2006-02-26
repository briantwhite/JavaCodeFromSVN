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

import cern.colt.matrix.impl.SparseDoubleMatrix3D;

public class CalculateWordTriplesUI extends JPanel {
	
	private InfoLabel infoLabel;
	private JLabel warningLabel;
	private JButton calculateButton;
	private JLabel progressLabel;
	private JProgressBar progressBar;
	private SparseDoubleMatrix3D triples;
	private int numCodes;
	private TripleHistogramCalculator thc;
	private Timer timer;

	
	public CalculateWordTriplesUI() {
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
		this.triples = triples;
		
		calculateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				thc.go();
				timer.start();
			}
		});
		
		timer = new Timer(500, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				progressBar.setValue(thc.getCurrent());
				progressLabel.setText(thc.getMessage());
				if (thc.done()){
					Toolkit.getDefaultToolkit().beep();
					timer.stop();
					progressBar.setValue(progressBar.getMaximum());
					progressLabel.setText("All done!");
				}
			}
		});
	}
	
	public void setTriples(SparseDoubleMatrix3D triples, int numCodes) {
		this.triples = triples;
		this.numCodes = numCodes;
		progressBar.setMaximum(numCodes);
		progressBar.setMinimum(0);
		thc = new TripleHistogramCalculator(triples, numCodes);
	}
	
	public TreeMap getHistogram() {
		return thc.getHistogram();
	}

	public void setInfoLabelText(String text){
		infoLabel.setText(text);
	}
	
	public boolean isDone() {
		return thc.done();
	}

}
