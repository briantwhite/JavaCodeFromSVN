import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class WordTriples extends JFrame {

	JTabbedPane steps;
	
	SelectHypFileUI selectHypFileUI;
	File hypFile;
	
	ShowLoadedHypsUI showLoadedHypsUI;
			
	public WordTriples () {
		super("Word Triples Analyzer");
				
		addWindowListener(new ApplicationCloser());
		Container contentPane = getContentPane();
		
		selectHypFileUI = new SelectHypFileUI();
		hypFile = null;
		
		showLoadedHypsUI = new ShowLoadedHypsUI();

		steps = new JTabbedPane();
		steps.addTab("(0)Select Input File", selectHypFileUI);
		steps.addTab("(1)Hypotheses found in Input File", showLoadedHypsUI);
		
		contentPane.add(steps);
		
		steps.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				switch (steps.getSelectedIndex()) {
				case 0:
					break;
				
				case 1:
					if (selectHypFileUI.getSelectedHypFile() != null) {
						hypFile = selectHypFileUI.getSelectedHypFile();
						showLoadedHypsUI.setInfoLabel("You selected "
								+ hypFile.getName().toString() 
								+ " as the input file.");
					} else {
						showLoadedHypsUI.setInfoLabel(
								"No hypothesis file selected.");
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

	
	JPanel makeLoadHypsPane() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(new JLabel("hi there"), BorderLayout.NORTH);
		
		return panel;
	}
		
	class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

}
