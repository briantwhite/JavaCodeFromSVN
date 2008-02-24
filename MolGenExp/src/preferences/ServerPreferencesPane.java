package preferences;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import molGenExp.ServerCommunicator;

import evolution.SpringUtilities;

public class ServerPreferencesPane extends PreferencePane {
	
	private JCheckBox useServerCheckBox;
	private JTextField serverURL;
	private JButton testServerButton;
	
	private ServerCommunicator communicator;


	public ServerPreferencesPane(PreferencesDialog parentDialog) {
		super(parentDialog);
		communicator = new ServerCommunicator(parentDialog.getMGE());
	}
	
	protected JPanel setupCustomPanel() {
		JPanel customPanel = new JPanel();
		customPanel.setLayout(new SpringLayout());

		JLabel useServerLabel = new JLabel("Use a remote server to fold proteins:");
		useServerCheckBox = new JCheckBox();
		useServerCheckBox.setSelected(preferences.isUseFoldingServer());
		useServerLabel.setLabelFor(useServerCheckBox);
		customPanel.add(useServerLabel);
		customPanel.add(useServerCheckBox);

		JLabel serverURLLabel = new JLabel("Server address:");
		JLabel nullLabel = new JLabel("");
		nullLabel.setLabelFor(serverURLLabel);
		customPanel.add(serverURLLabel);
		customPanel.add(nullLabel);

		serverURL = new JTextField(preferences.getFoldingServerURL(), 40);
		testServerButton = new JButton("Test Server Connection");
		customPanel.add(serverURL);
		customPanel.add(testServerButton);
		
		SpringUtilities.makeCompactGrid(customPanel,
				3, 2,
				6, 6,
				6, 6);

		testServerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String response = communicator.testServer();
				JOptionPane.showMessageDialog(null, "<html><body"
						+ "<font color=blue>The server responded:</font><br>"
						+ response
						+ "</body></html>");
			}
		});
		return customPanel;
	}

	protected void cancel() {
		parentDialog.setVisible(false);
	}

	protected void ok() {
		preferences.setUseFoldingServer(useServerCheckBox.isSelected());
		preferences.setFoldingServerURL(serverURL.getText());
		parentDialog.setVisible(false);
	}

	protected void restoreDefaults() {
		useServerCheckBox.setSelected(preferences.isUseFoldingServer());
		serverURL.setText(preferences.getFoldingServerURL());
		parentDialog.setVisible(false);
	}


}
