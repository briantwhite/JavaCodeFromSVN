package tbs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

public class ApplicationFrame extends JFrame {

	public static String TREE_FILE_EXTENSION = ".tree";
	public static String CONCATENATION_SEQUENCE = "<###>";  // used when putting multiple student trees together
	public static String CONCAT_REPLACEMENT_SEQ = "<##>";	// in admin mode
	

	public ApplicationFrame(final TBSApplet applet, final String VERSION, final String[] args) {
		super("Tree Builder " + VERSION);

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");

		JMenuItem saveAsItem = new JMenuItem("Save Work As...");
		fileMenu.add(saveAsItem);
		saveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveAsFile(applet);
			}			
		});

		JMenuItem quitItem = new JMenuItem("Quit");
		fileMenu.add(quitItem);
		quitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				quit(applet);
			}			
		});

		menuBar.add(fileMenu);
		setJMenuBar(menuBar);

		setSize(new Dimension(1000,600));
		add("North", Box.createRigidArea(new Dimension(1000,1)));
		add("East", Box.createRigidArea(new Dimension(1,600)));
		addWindowListener (new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				quit(applet);
			}
		});

		JPanel scorePanel = new JPanel();
		scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.X_AXIS));
		final JLabel scoreLabel = new JLabel("Welcome");
		JButton updateButton = new JButton("Update Score");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				scoreLabel.setText(applet.getScore());
			}
		});
		scorePanel.add(updateButton);
		scorePanel.add(scoreLabel);
		scorePanel.add(Box.createHorizontalGlue());
		add("South", scorePanel);

		add("Center", applet);
		applet.setStub(getStartingInfo(this, applet, args));
		setVisible(true);
		applet.init();
		applet.start();
		pack();

	}

	private MyAppletStub getStartingInfo(
			final ApplicationFrame frame, 
			final TBSApplet applet, 
			final String[] args) {
		/* if no args, give a choice
		 * if arg is filename - open file in student mode
		 * if arg is directory - open all files in admin mode
		 */
		MyAppletStub stub = null;
		if (args.length == 0) {
			String[] choices = {"Quit", 
					"Open Many Trees for Viewing", 
					"Open Many Trees for Scoring", 
					"Open Exiting Tree", 
			"Start New Tree"};

			String response = (String)JOptionPane.showInputDialog(
					frame,
					"How would you like to start?",
					"Welcome to Tree Builder " + TBSApplet.VERSION + "!",
					JOptionPane.PLAIN_MESSAGE,
					null,
					choices,
					choices[4]);

			if ((response == null) || (response.equals("Quit"))) {
				System.exit(0);

			} else if (response.equals("Open Many Trees for Viewing")) {
				stub = new MyAppletStub(applet, "true", "false", openManyTreeFiles());

			} else if (response.equals("Open Many Trees for Scoring")) {
				stub = new MyAppletStub(applet, "false", "true", "");

			} else if (response.equals("Open Exiting Tree")) {
				stub = new MyAppletStub(applet, "false", "false", openTreeFile());

			} else if (response.equals("Start New Tree")) {
				stub = new MyAppletStub(applet, "false", "false", "");

			} else {
				System.exit(0);

			}

		} else {
			String arg = args[0];
			if (arg.endsWith(".tree")) {
				stub = new MyAppletStub(applet, "false", "false", openTreeFile(new File(args[0])));
			} else {
				JOptionPane.showMessageDialog(this, 
						"<html>The file you chose was not a tree file."
						+ "<br>Exiting.", 
						"Not a Tree File", JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			}
		}
		return stub;
	}


	private String openTreeFile() {
		JFileChooser chooser = new JFileChooser(
				new File(
						System.getProperty("user.home") + 
						System.getProperty("file.separator") + 
				"Desktop"));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setFileFilter(new TreeFilter());
		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			return openTreeFile(chooser.getSelectedFile());
		}
		return "";
	}

	private class TreeFilter extends FileFilter {
		public boolean accept(File file) {
			String filename = file.getName();
			return filename.endsWith(TREE_FILE_EXTENSION);
		}

		public String getDescription() {
			return TREE_FILE_EXTENSION + " files";
		}

	}

	private String openTreeFile(File treeFile) {
		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(treeFile));
			String line = null;
			while (( line = reader.readLine()) != null){
				buffer.append(line);
				buffer.append(System.getProperty("line.separator"));
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	private String openManyTreeFiles() {
		StringBuffer resultBuffer = new StringBuffer();
		File treeDir = null;
		JFileChooser chooser = new JFileChooser(
				new File(
						System.getProperty("user.home") + 
						System.getProperty("file.separator") + 
				"Desktop"));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setDialogTitle("Choose a DIRECTORY where the .tree files can be found");
		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			treeDir = chooser.getSelectedFile();
		} 

		if (treeDir != null) {
			File[] files = treeDir.listFiles();
			for (File file : files) {
				if (file.getName().endsWith(ApplicationFrame.TREE_FILE_EXTENSION)) {
					String studentData = openTreeFile(file);
					// be sure the concatentation sequence isn't in the string
					resultBuffer.append(studentData.replaceAll(CONCATENATION_SEQUENCE, CONCAT_REPLACEMENT_SEQ));
					resultBuffer.append(CONCATENATION_SEQUENCE);
				}
			}
		}
		return resultBuffer.toString();
	}

	private static void saveAsFile(final TBSApplet applet) {
		JFileChooser fileChooser = new JFileChooser(
				new File(
						System.getProperty("user.home") + 
						System.getProperty("file.separator") + 
				"Desktop"));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int result = fileChooser.showSaveDialog(applet);
		if (result == JFileChooser.APPROVE_OPTION) {

			File outFile = fileChooser.getSelectedFile();
			String fileName = outFile.getName();

			fileName = fileName.replaceAll("[^a-zA-Z0-9 ]", "");
			if (!fileName.endsWith(TREE_FILE_EXTENSION)) {
				outFile = new File(
						outFile.getParent()
						+ System.getProperty("file.separator") 
						+ fileName + TREE_FILE_EXTENSION);
			}
			String newFileName = outFile.getName();
			String studentName = newFileName.replaceAll(TREE_FILE_EXTENSION, "");

			/*
			 * output string has each element separated by "+="
			 * 0 = student name
			 * 1 = date
			 * 2 = tree
			 * 3 = answer to question 1
			 * 4 = answer to question 2
			 * 5 = answer to question 3
			 * 6 = section # "section 02" as default
			 */
			String separator = "+=";
			StringBuffer buf = new StringBuffer();
			buf.append(studentName);
			buf.append(separator);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			buf.append(sdf.format(new Date()));
			buf.append(separator);
			buf.append(applet.getTree());
			buf.append(separator);
			buf.append(applet.getQ1());
			buf.append(separator);
			buf.append(applet.getQ2());
			buf.append(separator);
			//			buf.append(applet.getQ3());  // no Q3 in default mode
			buf.append(" ");
			buf.append(separator);
			buf.append("section 02");
			buf.append(separator);


			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
				out.write(buf.toString());
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	private static void quit(TBSApplet applet) {
		String[] choices = {"Save before Quitting", "Quit without saving"};
		int result = JOptionPane.showOptionDialog(
				applet, 
				"Do you want to save before Quitting?", 
				"Save before quitting", 
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE,
				null,
				choices,
				choices[0]);
		if (result == JOptionPane.YES_OPTION) {
			saveAsFile(applet);
		} 
		applet.stop();
		applet.destroy();
		System.exit(0);
	}

}
