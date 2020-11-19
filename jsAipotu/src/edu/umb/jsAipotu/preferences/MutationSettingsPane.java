package edu.umb.jsAipotu.preferences;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import evolution.SpringUtilities;

public class MutationSettingsPane extends PreferencePane {

	private JCheckBox mutationsEnabledCheckBox;
	private JTextField pointMutations;
	private JTextField deletionMutations;
	private JTextField insertionMutations;

	public MutationSettingsPane(PreferencesDialog parentDialog) {
		super(parentDialog);
	}


	protected JPanel setupCustomPanel() {

		JPanel settingsPanel = new JPanel();
		settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
		
		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));
		upperPanel.add(new JLabel("Mutations Enabled:"));
		mutationsEnabledCheckBox = new JCheckBox();
		mutationsEnabledCheckBox.setSelected(MGEPreferences.DEFAULT_MUTATIONS_ENABLED);
		upperPanel.add(mutationsEnabledCheckBox);
		upperPanel.add(Box.createHorizontalGlue());
		settingsPanel.add(upperPanel);
		
		mutationsEnabledCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				preferences.setMutationsEnabled(mutationsEnabledCheckBox.isSelected());
				updateFrequencyEntryEnabledStatus();
			}
		});
		
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new SpringLayout());
		innerPanel.setBorder(BorderFactory.createTitledBorder("Frequency of..."));

		JLabel pointLabel = new JLabel("Point Mutations");
		pointMutations = new JTextField(
				Float.toString(MGEPreferences.DEFAULT_POINT_MUTATION_RATE), 6);
		pointLabel.setLabelFor(pointMutations);
		pointMutations.setEnabled(MGEPreferences.DEFAULT_MUTATIONS_ENABLED);
		innerPanel.add(pointLabel);
		innerPanel.add(pointMutations);

		JLabel deletionLabel = new JLabel("Deletion Mutations");
		deletionMutations = new JTextField(
				Float.toString(MGEPreferences.DEFAULT_DELETION_MUTATION_RATE), 6);
		deletionLabel.setLabelFor(deletionMutations);
		deletionMutations.setEnabled(MGEPreferences.DEFAULT_MUTATIONS_ENABLED);
		innerPanel.add(deletionLabel);
		innerPanel.add(deletionMutations);

		JLabel insertionLabel = new JLabel("Insertion Mutations");
		insertionMutations = new JTextField(
				Float.toString(MGEPreferences.DEFAULT_INSERTION_MUTATION_RATE), 6);
		insertionLabel.setLabelFor(insertionMutations);
		insertionMutations.setEnabled(MGEPreferences.DEFAULT_MUTATIONS_ENABLED);
		innerPanel.add(insertionLabel);
		innerPanel.add(insertionMutations);
		
		SpringUtilities.makeCompactGrid(innerPanel,
				3, 2,
				6, 6,
				6, 6);
		
		settingsPanel.add(innerPanel);

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
		mutationsEnabledCheckBox.setSelected(
				MGEPreferences.DEFAULT_MUTATIONS_ENABLED);
		pointMutations.setText(
				Float.toString(MGEPreferences.DEFAULT_POINT_MUTATION_RATE));
		deletionMutations.setText(
				Float.toString(MGEPreferences.DEFAULT_DELETION_MUTATION_RATE));
		insertionMutations.setText(
				Float.toString(MGEPreferences.DEFAULT_INSERTION_MUTATION_RATE));
		
		preferences.setMutationsEnabled(
				MGEPreferences.DEFAULT_MUTATIONS_ENABLED);
		preferences.setPointMutationRate(
				MGEPreferences.DEFAULT_POINT_MUTATION_RATE);
		preferences.setDeletionMutationRate(
				MGEPreferences.DEFAULT_DELETION_MUTATION_RATE);
		preferences.setInsertionMutationRate(
				MGEPreferences.DEFAULT_INSERTION_MUTATION_RATE);		
		updateFrequencyEntryEnabledStatus();
		parentDialog.setVisible(false);
	}
	
	private void updateFrequencyEntryEnabledStatus() {
		pointMutations.setEnabled(preferences.isMutationsEnabled());
		deletionMutations.setEnabled(preferences.isMutationsEnabled());
		insertionMutations.setEnabled(preferences.isMutationsEnabled());
	}

}
