package preferences;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EvolutionPicturesPane extends PreferencePane {

	JCheckBox pixOnCheckBox;
	JTextField saveToPath;
	JButton browseButton;

	public EvolutionPicturesPane(PreferencesDialog parentDialog) {
		super(parentDialog);
	}

	protected JPanel setupCustomPanel() {
		JPanel customPanel = new JPanel();
		customPanel.setLayout(new BoxLayout(customPanel, BoxLayout.Y_AXIS));

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		JLabel pixOnLabel = new JLabel("Save an image of each generation");
		pixOnCheckBox = new JCheckBox();
		pixOnCheckBox.setSelected(Defaults.DEFAULT_GENERATION_PIX);
		pixOnLabel.setLabelFor(pixOnCheckBox);
		topPanel.add(pixOnLabel);
		topPanel.add(pixOnCheckBox);
		customPanel.add(topPanel);

		JLabel pathLabel = new JLabel("Save to:");
		customPanel.add(pathLabel);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		saveToPath = new JTextField(Defaults.SAVE_PIX_PATH, 60);
		saveToPath.setMaximumSize(new Dimension(200,20));
		browseButton = new JButton("Browse");
		bottomPanel.add(saveToPath);
		bottomPanel.add(browseButton);
		customPanel.add(bottomPanel);

		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(saveToPath.getText());
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					saveToPath.setText(fc.getSelectedFile().toString());
				}
			}
		});
		
		return customPanel;


	}

	protected void cancel() {
		parentDialog.setVisible(false);
	}

	protected void ok() {
		parentDialog.generationPixPrefsChanged(pixOnCheckBox.isSelected());
		parentDialog.setVisible(false);
	}

	protected void restoreDefaults() {
		pixOnCheckBox.setSelected(Defaults.DEFAULT_GENERATION_PIX);
		parentDialog.generationPixPrefsChanged(pixOnCheckBox.isSelected());
		parentDialog.setVisible(false);
	}


}
