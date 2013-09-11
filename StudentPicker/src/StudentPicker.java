import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.prefs.Preferences;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class StudentPicker extends JFrame {

	private static final String VERSION = "2.6";

	private static final String SESSON_FILE_DIR_NAME = "SessionData";

	private static final int POINTS_FOR_ANSWER = 2;
	private static final int POINTS_FOR_PASS = 0;
	private static final int POINTS_FOR_NO_REPLY = -1;
	// score of null or "" = "never been called"

	private static final String HTML_HEADER = "<html><font size=+2 color=blue align=\'left\'>";
	private static final String HTML_FOOTER = "</font></html>";

	// strings for preferences
	private static final String WORKING_DIR_PREF_NAME = "iClickerDir";

	private String workingDirName;

	private JLabel nameLabel;
	private String name;

	private ArrayList<String> namesOfPresentStudents;
	private TreeMap<String, Integer>namesAndScores;

	private Random r;

	private Preferences prefs;

	private ScoreSaver scoreSaver;

	public StudentPicker() {
		super("Student Picker " + VERSION);
		namesOfPresentStudents = new ArrayList<String>();
		r = new Random();
		workingDirName = "";
		prefs = Preferences.userRoot().node(this.getClass().getName());
		workingDirName = prefs.get(WORKING_DIR_PREF_NAME, "");
	}


	public static void main(String[] args) {
		StudentPicker studentPicker = new StudentPicker();
		studentPicker.setupUI();
		studentPicker.start();
	}

	private void setupUI() {

		JPanel buttonPanel = new JPanel();

		JButton newNameButton = new JButton("New");
		buttonPanel.add(newNameButton);
		newNameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String newName = getRandomName();
				nameLabel.setText(HTML_HEADER + newName + HTML_FOOTER);
				name = newName;
			}			
		});

		JButton passedButton = new JButton("Passed");
		buttonPanel.add(passedButton);
		passedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				awardPoints(POINTS_FOR_PASS);
				clearNameField();
			}			
		});

		JButton answeredButton = new JButton("Answered");
		buttonPanel.add(answeredButton);
		answeredButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				awardPoints(POINTS_FOR_ANSWER);
				clearNameField();
			}			
		});

		JButton noReplyButton = new JButton("No Reply");
		buttonPanel.add(noReplyButton);
		noReplyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				awardPoints(POINTS_FOR_NO_REPLY);
				clearNameField();
			}			
		});

		JButton clearButton = new JButton("Clear");
		buttonPanel.add(clearButton);
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearNameField();
			}			
		});

		JButton resetSettingsButton = new JButton("Reset");
		buttonPanel.add(resetSettingsButton);
		resetSettingsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetWorkingDirName();
				namesOfPresentStudents = new ArrayList<String>();
				start();
				clearNameField();
			}			
		});

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setLayout(new BorderLayout());

		add(buttonPanel, BorderLayout.PAGE_START);

		nameLabel = new JLabel();
		add(nameLabel, BorderLayout.LINE_START);

		add(Box.createRigidArea(new Dimension(400,0)), BorderLayout.PAGE_END);

		clearNameField();

		pack();
		setVisible(true);
	}

	private void clearNameField() {
		nameLabel.setText(HTML_HEADER + "Ready" + HTML_FOOTER);	
		name = "";
	}

	private void start() {
		// if you can't find files or prefs, pop up file choosers
		if (workingDirName.equals("")) {
			resetWorkingDirName();
		}

		// first the do not call list
		File doNotCallFile = new File(workingDirName 
				+ System.getProperty("file.separator") 
				+ "do not call.txt");
		ArrayList<String> doNotCallList = null;
		if (doNotCallFile.exists()) {
			doNotCallList = FileLoader.getDoNotCallList(this, doNotCallFile);
		}

		/*
		 * next, the students & their ID #s
		 *     hash map is 
		 *     	key = name
		 *      value = student ID #
		 */				
		File studentFile = new File(workingDirName 
				+ System.getProperty("file.separator") 
				+ "Roster.txt");
		if (!studentFile.exists()) {
			studentFile = new File(workingDirName 
					+ System.getProperty("file.separator") 
					+ "roster.txt");
		}
		TreeMap<String, String> namesAndsStudentIDs = FileLoader.getNamesAndStudentIDs(this, studentFile);
		if (namesAndsStudentIDs == null) {
			resetWorkingDirName();
			start();
		}

		/*
		 * check to see if any names added to roster.txt since last run
		 * (students added to the class)
		 * - will need to add their name(s) to scores.txt
		 * 		with initial score = null (never called)
		 * 
		 */
		scoreSaver = new ScoreSaver(workingDirName);
		namesAndScores = scoreSaver.reconcileNamesAndScores(namesAndsStudentIDs.keySet());

		// delete names from list if on do not call list
		int numNamesOmitted = 0;
		if ((doNotCallList != null) && (doNotCallList.size() > 0)) {
			for (int i = 0; i < doNotCallList.size(); i++) {
				String name = doNotCallList.get(i);
				if (namesAndsStudentIDs.containsKey(name)) {
					namesAndsStudentIDs.remove(name);
					numNamesOmitted++;
				}
			}
		}

		/*
		 * now read in list of ID#s and iClicker #s
		 * hash map is:
		 * 		key = student id #
		 * 		value = iClicker ID
		 */
		File idFile = new File(workingDirName 
				+ System.getProperty("file.separator") 
				+ SESSON_FILE_DIR_NAME 
				+ System.getProperty("file.separator")
				+ "RemoteID.csv");
		HashMap<String, String> studentIDsAndClickerIDs = FileLoader.getStudentIDsAndClickerIDs(this, idFile);
		if (studentIDsAndClickerIDs == null) {
			resetWorkingDirName();
			start();
		}

		Iterator<String> it = studentIDsAndClickerIDs.keySet().iterator();

		/*
		 * then look for matches between the two maps
		 *  and make iClicker # to Name map
		 *   key = iClicker #
		 *    value = name
		 */
		HashMap<String, String> iClickerIDsAndNames = new HashMap<String, String>();
		Iterator<String> nameIt = namesAndsStudentIDs.keySet().iterator();
		while (nameIt.hasNext()) {
			String name = nameIt.next();
			String studentId = namesAndsStudentIDs.get(name);
			if (studentIDsAndClickerIDs.containsKey(studentId)) {
				String iClickerID = studentIDsAndClickerIDs.get(studentId);
				iClickerIDsAndNames.put(iClickerID, name);
			}
		}

		// now read in file of who's here
		// first, find newest file of the .raw files
		File iClickerDir = new File(workingDirName
				+ System.getProperty("file.separator")
				+ SESSON_FILE_DIR_NAME);
		String[] files = iClickerDir.list();
		ArrayList<String> rawFiles = new ArrayList<String>();
		if (files == null) {
			JOptionPane.showMessageDialog(this, 
					"No files in iClicker Log directory", 
					"No Files Found",
					JOptionPane.WARNING_MESSAGE);
			resetWorkingDirName();
		}

		for (int i = 0; i < files.length; i++) {
			if (files[i].endsWith(".raw")) rawFiles.add(files[i]);
		}
		if (rawFiles.size() == 0) {
			JOptionPane.showMessageDialog(this, 
					"No .raw files in iClicker Log directory", 
					"No .raw Files Found",
					JOptionPane.WARNING_MESSAGE);
			resetWorkingDirName();
		}
		Collections.sort(rawFiles);
		String newestFile = rawFiles.get(rawFiles.size() - 1);

		File newestLogFile = new File(workingDirName 
				+ System.getProperty("file.separator") 
				+ SESSON_FILE_DIR_NAME
				+ System.getProperty("file.separator")
				+ newestFile);
		nameLabel.setText(newestFile);
		namesOfPresentStudents = FileLoader.getNamesOfPresentStudents(this, newestLogFile, iClickerIDsAndNames);
		if (namesOfPresentStudents == null) {
			resetWorkingDirName();
			start();
		}

		//delete all those who have already gotten points
		// that is, names where score != null
		int numberAlreadyCalled = 0;
		for (String name : namesAndScores.keySet()) {
			if (namesAndScores.get(name) != null) {
				namesOfPresentStudents.remove(name);
				numberAlreadyCalled++;
			}
		}

		// pop up a dialog with the loading info
		StringBuffer b = new StringBuffer();
		b.append("<html><body>");
		b.append("Loaded " + namesAndsStudentIDs.size() + " name and student ID pairs.<br>");
		b.append("Excluded " + numNamesOmitted + " students on Do Not Call List.<br>");
		b.append("Loaded " + studentIDsAndClickerIDs.size() + " student ID and iClicker ID pairs.<br>");
		b.append("Found " + iClickerIDsAndNames.size() + " matches by student ID.<br>");
		b.append("Removed " + numberAlreadyCalled + " students who had already been called.<br>");
		b.append("There are " + namesOfPresentStudents.size() + " students in today's list.<br>");
		b.append("</body></html>");

		JOptionPane.showMessageDialog(this, b.toString(), "Loading Complete", JOptionPane.INFORMATION_MESSAGE);
	}

	private String getRandomName() {
		int i = r.nextInt(namesOfPresentStudents.size());
		return namesOfPresentStudents.get(i);
	}

	private void awardPoints(int points) {
		// must also open, save, and close points file
		if (!name.equals("")) {
			int oldScore = 0;
			if (namesAndScores.containsKey(name) && (namesAndScores.get(name) != null)) {
				oldScore = namesAndScores.get(name);
			}
			namesAndScores.put(name, new Integer(oldScore + points));
			scoreSaver.saveScores(namesAndScores);
			namesOfPresentStudents.remove(name);
		}
	}

	private void resetWorkingDirName() {
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.setDialogTitle("Select Directory where this course's iClicker files are");
		int retVal = fc.showOpenDialog(this);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			workingDirName = fc.getSelectedFile().getAbsolutePath();
			prefs.put(WORKING_DIR_PREF_NAME, workingDirName);
		} 
	}

	private void quit() {
		System.exit(0);
	}
}
