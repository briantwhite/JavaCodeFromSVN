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
import javax.swing.SpringLayout;

import evolution.SpringUtilities;

public class EvolutionPicturesPane extends PreferencePane {

	private JCheckBox pixOnCheckBox;
	private JTextField saveToPath;
	private JButton browseButton;

	public EvolutionPicturesPane(PreferencesDialog parentDialog) {
		super(parentDialog);
	}

	protected JPanel setupCustomPanel() {
		JPanel customPanel = new JPanel();
		customPanel.setLayout(new SpringLayout());

		JLabel pixOnLabel = new JLabel("Save an image of each generation");
		pixOnCheckBox = new JCheckBox();
		pixOnCheckBox.setSelected(preferences.isGenerationPixOn());
		pixOnLabel.setLabelFor(pixOnCheckBox);
		customPanel.add(pixOnLabel);
		customPanel.add(pixOnCheckBox);

		JLabel pathLabel = new JLabel("Save to:");
		JLabel nullLabel = new JLabel("");
		nullLabel.setLabelFor(pathLabel);
		customPanel.add(pathLabel);
		customPanel.add(nullLabel);

		saveToPath = new JTextField(preferences.getSavePixToPath(), 40);
		browseButton = new JButton("Browse");
		customPanel.add(saveToPath);
		customPanel.add(browseButton);

		SpringUtilities.makeCompactGrid(customPanel,
				3, 2,
				6, 6,
				6, 6);

		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(saveToPath.getText());
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setDialogTitle("Choose a directory in which to save the image files");
				int returnVal = fc.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					if (fc.getSelectedFile().isDirectory()) {
						saveToPath.setText(fc.getSelectedFile().toString());
					} else {
						saveToPath.setText(fc.getSelectedFile().getParent().toString());						
					}
				}
			}
		});

		return customPanel;
	}

	protected void cancel() {
		parentDialog.setVisible(false);
	}

	protected void ok() {
		preferences.setGenerationPixOn(pixOnCheckBox.isSelected());
		preferences.setSavePixToPath(saveToPath.getText());
		parentDialog.setVisible(false);
	}

	protected void restoreDefaults() {
		preferences.setGenerationPixOn(preferences.DEFAULT_GENERATION_PIX_ON);
		preferences.setSavePixToPath(preferences.DEFAULT_SAVE_PIX_TO_PATH);
		parentDialog.setVisible(false);
	}


}
