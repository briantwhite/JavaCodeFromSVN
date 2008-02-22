package molGenExp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import evolution.SpringUtilities;

public class MutationSettingsDialog extends JDialog {

	private final static String DEFAULT_POINT = "0.01";
	private final static String DEFAULT_DELETE = "0.01";
	private final static String DEFAULT_INSERT = "0.01";

	private MolGenExp mge;

	private JTextField pointMutations;
	private JTextField deletionMutations;
	private JTextField insertionMutations;

	private JButton okButton;
	private JButton restoreDefaultsButton;
	private JButton cancelButton;

	public MutationSettingsDialog(MolGenExp mge) {
		super(mge, "Set Mutation Rates", true);
		this.mge = mge;
		setupUI();
	}

	private void setupUI() {

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

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

		mainPanel.add(settingsPanel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		restoreDefaultsButton = new JButton("Restore Defaults");
		buttonPanel.add(restoreDefaultsButton);
		cancelButton = new JButton("Cancel");
		buttonPanel.add(cancelButton);
		okButton = new JButton("OK");
		buttonPanel.add(okButton);
		
		getRootPane().setDefaultButton(okButton);

		mainPanel.add(buttonPanel);

		add(mainPanel);
		pack();

		restoreDefaultsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pointMutations.setText(DEFAULT_POINT);
				deletionMutations.setText(DEFAULT_DELETE);
				insertionMutations.setText(DEFAULT_INSERT);
				setVisible(false);
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MutationFreqList freqList = new MutationFreqList(
						parseText(pointMutations, 
								MolGenExp.pointMutationRate),
						parseText(deletionMutations, 
								MolGenExp.deletionMutationRate),
						parseText(insertionMutations,
								MolGenExp.insertionMutationRate));
				
				if((freqList.getPointFreq() < 0) || 
						(freqList.getDeleteFreq() < 0) ||
						(freqList.getInsertFreq() < 0)) {
					return;
				}
				mge.mutationPrefsChanged(freqList);
				setVisible(false);
			}
		});
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

}
