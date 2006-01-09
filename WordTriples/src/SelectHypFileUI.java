import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class SelectHypFileUI extends JPanel {
	private JFileChooser inputFileChooser;

	public SelectHypFileUI() {
		super();
		inputFileChooser = new JFileChooser();
		this.setLayout(new BorderLayout());
		this.add(inputFileChooser, BorderLayout.CENTER);
		inputFileChooser.setControlButtonsAreShown(false);
		this.add(new InfoLabel("Select Input File and then move to next pane."),
				BorderLayout.NORTH);
	}

	public File getSelectedHypFile() {
		File file = null;
		if (inputFileChooser.getSelectedFile() != null) {
			file = inputFileChooser.getSelectedFile();
		}
		return file;
	}
	
	public ArrayList getLoadedHyps() {
		ArrayList hypotheses = new ArrayList();
		
		if (inputFileChooser.getSelectedFile() != null) {
			File hypFile = inputFileChooser.getSelectedFile();
			URL hypURL = null;
			
			try {
				hypURL = hypFile.toURL();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			StringBuffer hypListBuffer = new StringBuffer();
			InputStream hypInput = null;
			try {
				hypInput = hypURL.openStream();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			BufferedReader hypListStream = 
				new BufferedReader(new InputStreamReader(hypInput));
			String line = null;
			
			try {
				while ((line = hypListStream.readLine()) != null ){
					try {
						hypotheses.add(new Hypothesis(line));
					} catch (BadHypothesisStringException e) {
						
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return hypotheses;
	}
}
