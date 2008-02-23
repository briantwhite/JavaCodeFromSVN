package preferences;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import molGenExp.MolGenExp;
import molGenExp.MutationFreqList;
import evolution.SpringUtilities;

public abstract class PreferencePane extends JPanel {
	
	protected PreferencesDialog parentDialog;

	private JButton okButton;
	private JButton restoreDefaultsButton;
	private JButton cancelButton;

	public PreferencePane(PreferencesDialog parentDialog) {
		super();
		this.parentDialog = parentDialog;
		setupUI(setupCustomPanel());
	}
	
	private void setupUI(JPanel customPanel) {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(customPanel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		restoreDefaultsButton = new JButton("Restore Defaults");
		buttonPanel.add(restoreDefaultsButton);
		cancelButton = new JButton("Cancel");
		buttonPanel.add(cancelButton);
		okButton = new JButton("OK");
		buttonPanel.add(okButton);
		
		add(buttonPanel);
		
		restoreDefaultsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restoreDefaults();
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ok();
			}
		});
	}

	protected abstract JPanel setupCustomPanel();
	
	protected abstract void restoreDefaults();
	protected abstract void cancel();
	protected abstract void ok();


}
