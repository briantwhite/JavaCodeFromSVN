import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class WordTriples extends JFrame {

	JTabbedPane steps;
	
	JFileChooser inputFileChooser;
	File hypsFile;
	
	InfoLabel loadHypsInfoLabel;
	
	public WordTriples () {
		super("Word Triples Analyzer");
		
		inputFileChooser = new JFileChooser();
		hypsFile = null;
		
		loadHypsInfoLabel = new InfoLabel("No file selected!");
		
		addWindowListener(new ApplicationCloser());
		Container contentPane = getContentPane();

		steps = new JTabbedPane();
		steps.addTab("(0)Select Input File", 
				makeSelectInputFilePane());
		steps.addTab("(1)Hypotheses found in Input File",
				makeLoadHypsPane());
		
		contentPane.add(steps);
		
		steps.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				switch (steps.getSelectedIndex()) {
				case 0:
					break;
				
				case 1:
					if (inputFileChooser.getSelectedFile() != null) {
						hypsFile = inputFileChooser.getSelectedFile();
						loadHypsInfoLabel.setText("You selected: " 
								+ hypsFile.toString());
					}
					break;
				
				
				}
			}
		});
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WordTriples wordTriples = new WordTriples();
		wordTriples.pack();
		wordTriples.show();

	}

	JPanel makeSelectInputFilePane(){	
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(inputFileChooser, BorderLayout.CENTER);
		inputFileChooser.setControlButtonsAreShown(false);
		panel.add(new InfoLabel("Select Input File and then move to next pane."),
				BorderLayout.NORTH);
		return panel;
	}
	
	JPanel makeLoadHypsPane() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(loadHypsInfoLabel, BorderLayout.NORTH);
		
		return panel;
	}
		
	class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

}
