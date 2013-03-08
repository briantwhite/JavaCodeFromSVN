package KeyMaker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;

import org.jdom.Document;
import org.jdom.Element;


public class KeyMaker extends JFrame {

	JSpinner dateSpinner;
	JTextField password;

	public KeyMaker() {
		super("Key Maker");
		addWindowListener(new ApplicationCloser());

	}

	class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		KeyMaker keyMaker = new KeyMaker();
		keyMaker.setupUI();
		keyMaker.pack();
		keyMaker.setVisible(true);
	}

	public void setupUI() {
		Calendar calendar = new GregorianCalendar();
		Date today = calendar.getTime();
		calendar.add(Calendar.YEAR, -1);
		Date startDate = calendar.getTime();
		calendar.add(Calendar.YEAR, 5);
		Date endDate = calendar.getTime();

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JPanel datePanel = new JPanel();
		datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.X_AXIS));
		SpinnerModel spinnerModel = new SpinnerDateModel(today, startDate, endDate, Calendar.YEAR);
		JLabel l1 = new JLabel("Expires on (day-month-year):");
		dateSpinner = new JSpinner(spinnerModel);
		dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy"));
		datePanel.add(l1);
		datePanel.add(dateSpinner);
		mainPanel.add(datePanel);

		JPanel pwdPanel = new JPanel();
		pwdPanel.setLayout(new BoxLayout(pwdPanel, BoxLayout.X_AXIS));
		JLabel l2 = new JLabel("Password:");
		password = new JTextField(15);
		pwdPanel.add(l2);
		pwdPanel.add(password);
		mainPanel.add(pwdPanel);

		JButton saveTokenButton = new JButton("Save Token");
		saveTokenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFiles();
			}
		});

		mainPanel.add(saveTokenButton);

		add(mainPanel);
	}

	private void saveFiles() {
		Element rootE = new Element("Token");

		Element dateE = new Element("ExpDate");
		dateE.addContent(((Date)dateSpinner.getValue()).toString());
		rootE.addContent(dateE);

		Element pwdE = new Element("Pswd");
		String pswd = password.getText();
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			md.update(pswd.getBytes());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, 
					"Unable to test your password due to system error",
					"Error",
					JOptionPane.WARNING_MESSAGE);
		}
		byte[] raw = md.digest();
		String hash = new String(Base64Coder.encode(raw));
		pwdE.addContent(hash);
		rootE.addContent(pwdE);

		Document doc = new Document(rootE);

		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setDialogTitle("Choose the DIRECTORY where the files will be saved");
		int retVal = fc.showSaveDialog(this);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			String dirName = fc.getCurrentDirectory().toString() 
			+ System.getProperty("file.separator"); 
			EncryptionTools.saveRSAEncrypted(doc, new File(dirName + "grader.key"));
			EncryptionTools.readRSAEncrypted(new File(dirName + "grader.key"));
			MakeRSAPair.saveStudentInstructorKeyPair(new File(dirName));
//			EncryptionTools.saveXOREncrypted(doc, new File(filename));
//			EncryptionTools.readXOREncrypted(new File(filename));
		}
	}

}
