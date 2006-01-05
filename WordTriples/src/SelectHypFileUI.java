import java.awt.BorderLayout;
import java.io.File;

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
}
