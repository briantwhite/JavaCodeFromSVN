package edu.umb.jsVGL.client.VGL;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class CustomDialogs {

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
	
	public static final int PMODE_CANCEL = 0;
	public static final int PMODE_REGULAR = 1;
	public static final int PMODE_PRACTICE = 2;
	
	public static int getPracticeModeChoice(VGLII vglII) {
		
		JPanel choicePanel = new JPanel();
		choicePanel.setLayout(new BoxLayout(choicePanel, BoxLayout.Y_AXIS));
		choicePanel.add(new JLabel("Choose the mode for this problem:"));
		JRadioButton pModeButton = new JRadioButton("Practice Mode");
		choicePanel.add(pModeButton);
		JRadioButton rModeButton = new JRadioButton("For Credit Mode");
		choicePanel.add(rModeButton);
		ButtonGroup group = new ButtonGroup();
		group.add(pModeButton);
		group.add(rModeButton);
		
		String[] options = new String[]{"OK", "Cancel"};
		int r = JOptionPane.showOptionDialog(
				vglII,
				choicePanel,
				"Mode Selection",
				JOptionPane.NO_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				null,
				options,
				options[0]);
		if (r == 0) {
			if (pModeButton.isSelected()) {
				return PMODE_PRACTICE;
			} else if (rModeButton.isSelected()) {
				return PMODE_REGULAR;
			} else {
				return PMODE_CANCEL;
			}
		} else {
			return PMODE_CANCEL;
		}
	}
}
