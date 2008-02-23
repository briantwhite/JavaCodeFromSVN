package preferences;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import molGenExp.MolGenExp;
import molGenExp.MutationFreqList;
import evolution.SpringUtilities;

public class MutationSettingsPane extends PreferencePane {


	private JTextField pointMutations;
	private JTextField deletionMutations;
	private JTextField insertionMutations;
	
	public MutationSettingsPane(PreferencesDialog parentDialog) {
		super(parentDialog);
	}


	private void setupUI() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel settingsPanel = new JPanel();
		settingsPanel.setLayout(new SpringLayout());
		settingsPanel.setBorder(BorderFactory.createTitledBorder("Frequency of..."));

		JLabel pointLabel = new JLabel("Point Mutations");
		pointMutations = new JTextField("0.01");
		pointLabel.setLabelFor(pointMutations);
		settingsPanel.add(pointLabel);
		settingsPanel.add(pointMutations);

		JLabel deletionLabel = new JLabel("Deletion Mutations");
		deletionMutations = new JTextField("0.01");
		deletionLabel.setLabelFor(deletionMutations);
		settingsPanel.add(deletionLabel);
		settingsPanel.add(deletionMutations);

		JLabel insertionLabel = new JLabel("Insertion Mutations");
		insertionMutations = new JTextField("0.01");
		insertionLabel.setLabelFor(insertionMutations);
		settingsPanel.add(insertionLabel);
		settingsPanel.add(insertionMutations);

		SpringUtilities.makeCompactGrid(settingsPanel,
				3, 2,
				6, 6,
				6, 6);
	}
	
	protected JPanel setupCustomPanel() {
		JPanel settingsPanel = new JPanel();
		settingsPanel.setLayout(new SpringLayout());
		settingsPanel.setBorder(BorderFactory.createTitledBorder("Frequency of..."));

		JLabel pointLabel = new JLabel("Point Mutations");
		pointMutations = new JTextField("0.01");
		pointLabel.setLabelFor(pointMutations);
		settingsPanel.add(pointLabel);
		settingsPanel.add(pointMutations);

		JLabel deletionLabel = new JLabel("Deletion Mutations");
		deletionMutations = new JTextField("0.01");
		deletionLabel.setLabelFor(deletionMutations);
		settingsPanel.add(deletionLabel);
		settingsPanel.add(deletionMutations);

		JLabel insertionLabel = new JLabel("Insertion Mutations");
		insertionMutations = new JTextField("0.01");
		insertionLabel.setLabelFor(insertionMutations);
		settingsPanel.add(insertionLabel);
		settingsPanel.add(insertionMutations);

		SpringUtilities.makeCompactGrid(settingsPanel,
				3, 2,
				6, 6,
				6, 6);
		
		return settingsPanel;
	}

	private float parseText(JTextField textField, float currentValue) {
		float freq = -1f;
		try {
			freq = Float.parseFloat(textField.getText());
		} catch (NumberFormatException e) {
			badNumber(textField.getText());
			textField.setText(Float.toString(currentValue));
			return -1f;
		}
		if (freq < 0) {
			badNumber(textField.getText());
			textField.setText(Float.toString(currentValue));
			return -1f;
		}
		return freq;
	}

	private void badNumber(String s) {
		JOptionPane.showMessageDialog(this, 
				"<html><body>"
				+ "Mutation rates must be numbers between 0 and 1.<br>"
				+ "You entered:" + s + "<br>"
				+ "Substituting previous value.",
				"Incorrect entry",
				JOptionPane.WARNING_MESSAGE);
	}


	protected void cancel() {
		parentDialog.setVisible(false);
	}

	protected void ok() {
		MutationFreqList freqList = new MutationFreqList(
				parseText(pointMutations, 
						MGEPreferences.pointMutationRate),
				parseText(deletionMutations, 
						MGEPreferences.deletionMutationRate),
				parseText(insertionMutations,
						MGEPreferences.insertionMutationRate));
		
		if((freqList.getPointFreq() < 0) || 
				(freqList.getDeleteFreq() < 0) ||
				(freqList.getInsertFreq() < 0)) {
			return;
		}
		parentDialog.mutationPrefsChanged(freqList);
		parentDialog.setVisible(false);
	}

	protected void restoreDefaults() {
		pointMutations.setText(Defaults.DEFAULT_POINT);
		deletionMutations.setText(Defaults.DEFAULT_DELETE);
		insertionMutations.setText(Defaults.DEFAULT_INSERT);
		parentDialog.mutationPrefsChanged(new MutationFreqList(
				parseText(pointMutations, 
						MGEPreferences.pointMutationRate),
				parseText(deletionMutations, 
						MGEPreferences.deletionMutationRate),
				parseText(insertionMutations,
						MGEPreferences.insertionMutationRate)));
		parentDialog.setVisible(false);
	}

}
