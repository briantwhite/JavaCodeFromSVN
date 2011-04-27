package Grader;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.text.Caret;

import VGL.EncryptionTools;
import VGL.VGLII;

public class Grader extends JFrame {

	private File workingDir;
	private VGLII vglII;

	private Timer fileLoadingTimer;
	private WorkFileLoader workFileLoader;
	private JProgressBar fileLoadingProgressBar;
	private JLabel filenameLabel;
	private JProgressBar decryptionProgressBar;
	private JDialog progressDialog;
	private boolean loadingFiles;

	private JList workFileList;
	private DefaultListModel workFileNames;

	public JEditorPane correctAnswer;
	public JScrollPane correctAnswerScroller;
	public Caret topOfCorrectAnswer;

	public JEditorPane theirAnswer;
	public JScrollPane theirAnswerScroller;
	public Caret topOfTheirAnswer;

	private TreeMap<String, GradingResult> filenamesAndResults;

	public Grader(File workingDir, VGLII vglII) {
		this.workingDir = workingDir;
		this.vglII = vglII;
		fileLoadingTimer = new Timer(100, new FileLoadingTimerListener());
		filenamesAndResults = new TreeMap<String, GradingResult>();
		setupUI();
		pack();
		setVisible(true);
	}

	private void setupUI() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu helpMenu = new JMenu("Help");
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(helpMenu);
		JMenuItem helpItem = new JMenuItem("Help");
		helpMenu.add(helpItem);
		helpItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GraderHelp.showhelp();
			}
		});

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(Box.createRigidArea(new Dimension(1,400)));

		JPanel leftPanel = new JPanel();
		leftPanel.setBorder(BorderFactory.createTitledBorder("Work Files"));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(Box.createRigidArea(new Dimension(300,1)));

		workFileNames = new DefaultListModel();
		workFileList = new JList(workFileNames);
		workFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		workFileList.setLayoutOrientation(JList.VERTICAL);
		workFileList.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(workFileList);
		listScroller.setPreferredSize(new Dimension(300,80));
		leftPanel.add(listScroller);

		workFileList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList) evt.getSource();
				String workFileName =
					(workFileNames.get((list.locationToIndex(evt.getPoint())))).toString();
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
		String[] files = workingDir.list();
		for (int i = 0; i < files.length; i++) {
			if (files[i].endsWith(".gr2")) {
				workFileNames.addElement(files[i]);
			}
		}
		workFileLoader = new WorkFileLoader(
				workingDir, 
				workFileNames, 
				filenamesAndResults,
				vglII);
		Thread t = new Thread(workFileLoader);
		t.start();
		fileLoadingTimer.start();

		loadingFiles = true;

		progressDialog = new JDialog(vglII, true);
		progressDialog.setLocationRelativeTo(null);
		progressDialog.setTitle("Loading files for Grading...");
		progressDialog.setPreferredSize(new Dimension(300, 170));
		JPanel progressPanel = new JPanel();
		progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
		progressPanel.add(
				new JLabel("Reading in " + workFileNames.getSize() + " work files."));
		fileLoadingProgressBar = new JProgressBar(0, workFileNames.getSize());
		fileLoadingProgressBar.setValue(0);
		progressPanel.add(fileLoadingProgressBar);
		filenameLabel = new JLabel("Loading...");
		progressPanel.add(filenameLabel);
		decryptionProgressBar = new JProgressBar();
		progressPanel.add(decryptionProgressBar);
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
	}

	class FileLoadingTimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(!loadingFiles || 
					(workFileLoader.getProgress() == workFileLoader.getLengthOfTask())) {
				workFileLoader.stop();
				fileLoadingTimer.stop();
				vglII.setCursor(Cursor.DEFAULT_CURSOR);
				progressDialog.dispose();
				workFileList.setEnabled(true);
			} else {
				vglII.setCursor(Cursor.WAIT_CURSOR);
				fileLoadingProgressBar.setValue(workFileLoader.getProgress());
				filenameLabel.setText(workFileLoader.getCurrentFileName());
				decryptionProgressBar.setMinimum(0);
				decryptionProgressBar.setMaximum(
						EncryptionTools.getInstance().getNumberOfEncryptedBlocks());
				decryptionProgressBar.setValue(
						EncryptionTools.getInstance().getProgress());
				workFileList.setEnabled(false);
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
