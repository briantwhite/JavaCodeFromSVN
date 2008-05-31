package preferences;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import evolution.SpringUtilities;

public class MutationSettingsPane extends PreferencePane {


	private JTextField pointMutations;
	private JTextField deletionMutations;
	private JTextField insertionMutations;

	public MutationSettingsPane(PreferencesDialog parentDialog) {
		super(parentDialog);
	}


	protected JPanel setupCustomPanel() {

		JPanel settingsPanel = new JPanel();
		settingsPanel.setLayout(new SpringLayout());
		settingsPanel.setBorder(BorderFactory.createTitledBorder("Frequency of..."));

		JLabel pointLabel = new JLabel("Point Mutations");
		pointMutations = new JTextField(
				Float.toString(MGEPreferences.DEFAULT_POINT_MUTATION_RATE), 6);
		pointLabel.setLabelFor(pointMutations);
		settingsPanel.add(pointLabel);
		settingsPanel.add(pointMutations);

		JLabel deletionLabel = new JLabel("Deletion Mutations");
		deletionMutations = new JTextField(
				Float.toString(MGEPreferences.DEFAULT_DELETION_MUTATION_RATE), 6);
		deletionLabel.setLabelFor(deletionMutations);
		settingsPanel.add(deletionLabel);
		settingsPanel.add(deletionMutations);

		JLabel insertionLabel = new JLabel("Insertion Mutations");
		insertionMutations = new JTextField(
				Float.toString(MGEPreferences.DEFAULT_INSERTION_MUTATION_RATE), 6);
		insertionLabel.setLabelFor(insertionMutations);
		settingsPanel.add(insertionLabel);
		settingsPanel.add(insertionMutations);
		
		SpringUtilities.makeCompactGrid(settingsPanel,
				3, 2,
				6, 6,
				6, 6);

		return settingsPanel;
	}


	protected void cancel() {
		parentDialog.setVisible(false);
	}

	protected void ok() {
		float pmRate = parseTextToFloat(pointMutations.getText());
		float dmRate = parseTextToFloat(deletionMutations.getText());
		float imRate = parseTextToFloat(insertionMutations.getText());
		if ((pmRate < 0) || (dmRate < 0) || (imRate < 0)) {
			JOptionPane.showMessageDialog(this, 
					"Mutation rates must be numbers between 0 and 1.",
					"Incorrect entry",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		preferences.setPointMutationRate(pmRate);
		preferences.setDeletionMutationRate(dmRate);
		preferences.setInsertionMutationRate(imRate);
		parentDialog.setVisible(false);
	}

	//try to parse the string as a float
	// return -1 if not possible
	private float parseTextToFloat(String s) {
		float freq = -1f;
		try {
			freq = Float.parseFloat(s);
		} catch (NumberFormatException e) {
			freq = -1;
		}
		return freq;
	}



	protected void restoreDefaults() {
		pointMutations.setText(
				Float.toString(MGEPreferences.DEFAULT_POINT_MUTATION_RATE));
		deletionMutations.setText(
				Float.toString(MGEPreferences.DEFAULT_DELETION_MUTATION_RATE));
		insertionMutations.setText(
				Float.toString(MGEPreferences.DEFAULT_INSERTION_MUTATION_RATE));
		preferences.setPointMutationRate(
				MGEPreferences.DEFAULT_POINT_MUTATION_RATE);
		preferences.setDeletionMutationRate(
				MGEPreferences.DEFAULT_DELETION_MUTATION_RATE);
		preferences.setInsertionMutationRate(
				MGEPreferences.DEFAULT_INSERTION_MUTATION_RATE);
		parentDialog.setVisible(false);
	}

}
