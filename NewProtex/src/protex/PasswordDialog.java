package protex;

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
	public static String[] getEmailAndPassword() {
		
		String[] result = new String[2];
		result[0] = null;
		result[1] = null;
		
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
		
		String[] options = new String[]{"OK", "Cancel"};
		int r = JOptionPane.showOptionDialog(
				null,
				pswdDialogPanel,
				"Login to EdX Server",
				JOptionPane.NO_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				null,
				options,
				options[1]);
		
		if ((r == 0) && (!emailField.getText().equals("")) && (!pswdField.getPassword().equals(""))) {
			result[0] = emailField.getText();
			result[1] = new String(pswdField.getPassword());
		}
		return result;
	}

}
