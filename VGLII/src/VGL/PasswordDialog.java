package VGL;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class PasswordDialog {

	/*
	 * [0] = email
	 * [1] = password
	 */
	public static EmailAndPassword getEmailAndPassword(VGLII vglII, boolean isRetry) {

		EmailAndPassword result = new EmailAndPassword();
		
		JPanel outerPanel = new JPanel();
		outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));
		if (isRetry) {
			outerPanel.add(new JLabel("Incorrect e-mail and/or password."));
		}

		JPanel pswdDialogPanel = new JPanel(new SpringLayout());

		JLabel emailLabel = new JLabel("E-mail address:");
		JTextField emailField = new JTextField(20);
		emailLabel.setLabelFor(emailField);

		JLabel pswdLabel = new JLabel("Password:");
		JPasswordField pswdField = new JPasswordField(20);
		pswdLabel.setLabelFor(pswdField);

		pswdDialogPanel.add(emailLabel);
		pswdDialogPanel.add(emailField);
		pswdDialogPanel.add(pswdLabel);
		pswdDialogPanel.add(pswdField);

		SpringUtilities.makeCompactGrid(pswdDialogPanel, 2, 2, 6, 6, 6, 6);
		
		outerPanel.add(pswdDialogPanel);

		String[] options = new String[]{"OK", "Cancel"};
		int r = JOptionPane.showOptionDialog(
				vglII,
				outerPanel,
				"Login to EdX Server",
				JOptionPane.NO_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				null,
				options,
				options[0]);

		// be sure they said OK and had a password and e-mail before returning
		if ((r == 0) && (!emailField.getText().equals("")) && (!pswdField.getPassword().equals(""))) {
			result.eMail = emailField.getText();
			result.password = new String(pswdField.getPassword());
			return result;
		} else {
			return null;
		}
	}
}
