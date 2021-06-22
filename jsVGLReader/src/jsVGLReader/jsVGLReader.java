package jsVGLReader;


import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.text.Caret;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class jsVGLReader extends JFrame {

	private File workingDir;

	private JList<String> workFileNameDisplayList;
	private DefaultListModel<String> workFileNameListModel;
	
	private Timer fileLoadingTimer;
	private WorkFileLoader workFileLoader;
	private JProgressBar fileLoadingProgressBar;
	private JLabel filenameLabel;
	private JDialog progressDialog;
	private boolean loadingFiles;

	public JEditorPane correctAnswer;
	public JScrollPane correctAnswerScroller;
	public Caret topOfCorrectAnswer;

	public JEditorPane theirAnswer;
	public JScrollPane theirAnswerScroller;
	public Caret topOfTheirAnswer;

	private TreeMap<String, GradingResult> filenamesAndResults;

	public jsVGLReader() {
		filenamesAndResults = new TreeMap<String, GradingResult>();
		workFileNameListModel = new DefaultListModel<String>();
		fileLoadingTimer = new Timer(100, new FileLoadingTimerListener());
		setupUI();
		pack();
		setVisible(true);
	}

	class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

	public static void main(String[] args) {		
		// get password
		String password = "";
		JPasswordField pf = new JPasswordField();
		int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (okCxl == JOptionPane.OK_OPTION) {
			password = new String(pf.getPassword());
		}		
		if (!password.equals("jsVGL2020")) {
			System.exit(0);
		}
		jsVGLReader reader = new jsVGLReader();
	}

	private void setupUI() {
				
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu fileMenu = new JMenu("File");
		JMenuItem openItem = new JMenuItem("Open Directory with .jsvgl files...");
		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openDirectoryAndLoadFiles();
			}
		});
		fileMenu.add(openItem);
		JMenuItem quitItem = new JMenuItem("Quit");
		quitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		fileMenu.add(quitItem);
		menuBar.add(fileMenu);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(Box.createRigidArea(new Dimension(1,400)));

		JPanel leftPanel = new JPanel();
		leftPanel.setBorder(BorderFactory.createTitledBorder("Work Files"));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(Box.createRigidArea(new Dimension(300,1)));

		workFileNameDisplayList = new JList<String>(workFileNameListModel);
		workFileNameDisplayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		workFileNameDisplayList.setLayoutOrientation(JList.VERTICAL);
		JScrollPane listScroller = new JScrollPane(workFileNameDisplayList);
		listScroller.setPreferredSize(new Dimension(300,80));
		leftPanel.add(listScroller);

		workFileNameDisplayList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList<String> list = (JList<String>) evt.getSource();
				String workFileName =
					(workFileNameListModel.get((list.locationToIndex(evt.getPoint())))).toString();
				showWorkByName(workFileName);
			}

		});

		mainPanel.add(leftPanel);

		JPanel correctAnswerPanel = new JPanel();
		correctAnswerPanel.setBorder(BorderFactory.createTitledBorder("Correct Answer"));
		correctAnswerPanel.setLayout(new BoxLayout(correctAnswerPanel, BoxLayout.Y_AXIS));
		correctAnswerPanel.add(Box.createRigidArea(new Dimension(300,1)));
		correctAnswer = new JEditorPane();
		correctAnswer.setContentType("text/html");
		correctAnswerScroller = new JScrollPane(correctAnswer);
		correctAnswerScroller.setPreferredSize(new Dimension(300,80));
		topOfCorrectAnswer = correctAnswer.getCaret();
		correctAnswerPanel.add(correctAnswerScroller);

		mainPanel.add(correctAnswerPanel);

		JPanel theirAnswerPanel = new JPanel();
		theirAnswerPanel.setBorder(BorderFactory.createTitledBorder("Student's Answer"));
		theirAnswerPanel.setLayout(new BoxLayout(theirAnswerPanel, BoxLayout.Y_AXIS));
		theirAnswerPanel.add(Box.createRigidArea(new Dimension(300,1)));
		theirAnswer = new JEditorPane();
		theirAnswer.setContentType("text/html");
		theirAnswerScroller = new JScrollPane(theirAnswer);
		theirAnswerScroller.setPreferredSize(new Dimension(300,80));
		topOfTheirAnswer = theirAnswer.getCaret();
		theirAnswerPanel.add(theirAnswerScroller);

		mainPanel.add(theirAnswerPanel);


		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(mainPanel, BorderLayout.CENTER);
	}

	public void openDirectoryAndLoadFiles() {
		
		// get directory where files are
		JDialog dialog = new JDialog();
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setDialogTitle("Choose the DIRECTORY where the .jsvgl files are stored");
		int val = fileChooser.showOpenDialog(dialog);
		if (val != JFileChooser.APPROVE_OPTION) {
			System.exit(0);
		}
		workingDir = fileChooser.getSelectedFile();
		String[] files = workingDir.list();
		for (int i = 0; i < files.length; i++) {
			if (files[i].endsWith(".jsvgl")) {
				workFileNameListModel.addElement(files[i]);
			}
		}
		
		workFileLoader = new WorkFileLoader(
				workingDir, 
				workFileNameListModel, 
				filenamesAndResults);
		Thread t = new Thread(workFileLoader);
		t.start();
		fileLoadingTimer.start();

		loadingFiles = true;

		progressDialog = new JDialog(this, true);
		progressDialog.setLocationRelativeTo(null);
		progressDialog.setTitle("Loading files for Grading...");
		progressDialog.setPreferredSize(new Dimension(300, 170));
		JPanel progressPanel = new JPanel();
		progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
		progressPanel.add(
				new JLabel("Reading in " + workFileNameListModel.getSize() + " work files."));
		fileLoadingProgressBar = new JProgressBar(0, workFileNameListModel.getSize());
		fileLoadingProgressBar.setValue(0);
		progressPanel.add(fileLoadingProgressBar);
		filenameLabel = new JLabel("Loading...");
		progressPanel.add(filenameLabel);
		JButton cancelButton = new JButton("Cancel");
		progressPanel.add(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				progressDialog.dispose();
			}
		});
		
		JPanel wrapperPanel = new JPanel();
		wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.X_AXIS));
		wrapperPanel.add(Box.createRigidArea(new Dimension(25, 0)));
		wrapperPanel.add(progressPanel);
		wrapperPanel.add(Box.createRigidArea(new Dimension(25, 0)));
		
		JPanel outerPanel = new JPanel();
		outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));
		outerPanel.add(Box.createRigidArea(new Dimension(0, 25)));
		outerPanel.add(wrapperPanel);
		outerPanel.add(Box.createRigidArea(new Dimension(0, 25)));

		progressDialog.add(outerPanel);
		progressDialog.pack();
		progressDialog.setVisible(true);
		loadingFiles = false;
		
		setVisible(true);
	}
	
	class FileLoadingTimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(!loadingFiles || 
					(workFileLoader.getProgress() == workFileLoader.getLengthOfTask())) {
				workFileLoader.stop();
				fileLoadingTimer.stop();
				progressDialog.dispose();
			} else {
				fileLoadingProgressBar.setValue(workFileLoader.getProgress());
				filenameLabel.setText(workFileLoader.getCurrentFileName());
			}
		}		
	}


	private void showWorkByName(String fileName) {

		GradingResult result = filenamesAndResults.get(fileName);

		correctAnswer.setText(result.getCorrectAnswerHTML());
		correctAnswer.setCaret(null);
		correctAnswer.setCaret(topOfCorrectAnswer);

		theirAnswer.setText(result.getStudentAnswerHTML());
		theirAnswer.setCaret(null);
		theirAnswer.setCaret(topOfTheirAnswer);

		this.toFront();
	}


}
