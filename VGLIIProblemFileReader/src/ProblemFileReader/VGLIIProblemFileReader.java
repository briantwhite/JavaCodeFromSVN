package ProblemFileReader;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.security.MessageDigest;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ProgressMonitor;
import javax.swing.Timer;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Caret;
import javax.swing.text.html.HTMLDocument;

import sun.misc.BASE64Encoder;
import GeneticModels.GeneticModel;
import VGL.DocumentRenderer;

public class VGLIIProblemFileReader extends JFrame {

	private static final String version = "1.0";
	/**
	 * key for encrypting work files
	 *   XORed with bytes of work file
	 */
	public final static byte[] KEY = 
		(new String("The Virtual Genetics Lab is Awesome!")).getBytes();
	
	private final static String CRYPT_PW = "l/uxlpFr/vHESJtxDaqaFK8Tx7k=";

	private boolean passwordEntered;
	
	private File currentDirectory;
	
	protected TreeMap<String, GeneticModel> namesAndModels;
	
	private Timer fileLoadingTimer;
	private WorkFileLoader workFileLoader;
	private ProgressMonitor fileLoadingProgressMonitor;

	private JMenu fileMenu;
	private JMenuItem openMenuItem;
	private JMenuItem printMenuItem;
	private JMenuItem quitMenuItem;

	private JFileChooser fileChooser;
	private JList workFileList;
	private DefaultListModel workFileNames;
	
	public JEditorPane correctAnswer;
	public JScrollPane answerScroller;
	public Caret topOfPage;
	
	private DocumentRenderer docRenderer;
	
	public VGLIIProblemFileReader() {
		super("VGLII Problem File Reader " + version);
		addWindowListener(new ApplicationCloser());
		passwordEntered = false;
		currentDirectory = new File(".");
		namesAndModels = new TreeMap<String, GeneticModel>();
		fileLoadingTimer = new Timer(100, new FileLoadingTimerListener());
		docRenderer = new DocumentRenderer();
		setupUI();
	}

	public static void main(String[] args) {
		VGLIIProblemFileReader vpfr = new VGLIIProblemFileReader();
		vpfr.pack();
		vpfr.setVisible(true);
	}

	class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	
	private void setupUI() {

		JMenuBar menuBar = new JMenuBar();

		fileMenu = new JMenu("File");

		openMenuItem = new JMenuItem("Open DIRECTORY containing work files...");
		openMenuItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.META_MASK));
		openMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFiles();
			}
		});
		fileMenu.add(openMenuItem);

		printMenuItem = new JMenuItem("Print selected...");
		printMenuItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.META_MASK));
		printMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printSelectedAnswers();
			}
		});
		fileMenu.add(printMenuItem);

		quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(quitMenuItem);

		menuBar.add(fileMenu);

		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setDialogTitle("Choose the DIRECTORY where the work files are stored");

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(Box.createRigidArea(new Dimension(1,400)));

		JPanel leftPanel = new JPanel();
		leftPanel.setBorder(BorderFactory.createTitledBorder("Work Files"));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(Box.createRigidArea(new Dimension(300,1)));
		
		workFileNames = new DefaultListModel();
		workFileList = new JList(workFileNames);
		workFileList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		workFileList.setLayoutOrientation(JList.VERTICAL);
		workFileList.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(workFileList);
		listScroller.setPreferredSize(new Dimension(300,80));
		leftPanel.add(listScroller);
		
		workFileList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(e.getValueIsAdjusting() == false) {
					String workFileName = 
						(workFileNames.get(workFileList.getSelectedIndex())).toString();
					getModelByName(workFileName);
				}
			}
		});

		mainPanel.add(leftPanel);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(BorderFactory.createTitledBorder("Correct Answer"));
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(Box.createRigidArea(new Dimension(300,1)));
		correctAnswer = new JEditorPane();
		correctAnswer.setContentType("text/html");
		answerScroller = new JScrollPane(correctAnswer);
		answerScroller.setPreferredSize(new Dimension(300,80));
		topOfPage = correctAnswer.getCaret();
		rightPanel.add(answerScroller);
		
		mainPanel.add(rightPanel);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(menuBar, BorderLayout.NORTH);
		getContentPane().add(mainPanel, BorderLayout.CENTER);

	}
	
	private void openFiles() {
		if (!passwordEntered) {
			final JPasswordField pwd = new JPasswordField(20);
			pwd.addAncestorListener(new AncestorListener() {
				public void ancestorAdded(AncestorEvent event) {
					pwd.requestFocusInWindow();
				}
				public void ancestorMoved(AncestorEvent event) {}
				public void ancestorRemoved(AncestorEvent event) {}
			});
			int action = JOptionPane.showConfirmDialog(
					VGLIIProblemFileReader.this,
					pwd,
					"Enter Password to decrypt the problem files",
					JOptionPane.OK_CANCEL_OPTION);
			if (action != JOptionPane.OK_OPTION) {
				return;
			}
			
			String password = new String(pwd.getPassword());
			MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("SHA");
				md.update(password.getBytes("UTF-8"));
			} catch (Exception e) {
				JOptionPane.showMessageDialog(
						VGLIIProblemFileReader.this, 
						"Unable to test your password due to system error",
						"Error",
						JOptionPane.WARNING_MESSAGE);
			}
			byte[] raw = md.digest();
			String hash = (new BASE64Encoder()).encode(raw);
			if (!hash.equals(CRYPT_PW)) {
				passwordEntered = false;
				JOptionPane.showMessageDialog(
						VGLIIProblemFileReader.this,
						"Password Incorrect, please try again.",
						"Password Error",
						JOptionPane.ERROR_MESSAGE);	
				return;
			} else {
				passwordEntered = true;
			}
		}
		int val = fileChooser.showOpenDialog(VGLIIProblemFileReader.this);
		if (val == JFileChooser.APPROVE_OPTION) {
			currentDirectory = fileChooser.getSelectedFile();
			openDirectoryAndLoadFiles();
		}
	}

	private void openDirectoryAndLoadFiles() {
		String[] files = currentDirectory.list();
		for (int i = 0; i < files.length; i++) {
			if (files[i].endsWith(".wr2")) {
				workFileNames.addElement(files[i]);
			}
		}
		workFileLoader = new WorkFileLoader(
				currentDirectory, 
				workFileNames, 
				namesAndModels);
		Thread t = new Thread(workFileLoader);
		t.start();
		fileLoadingTimer.start();
		fileLoadingProgressMonitor = new ProgressMonitor(
				VGLIIProblemFileReader.this,
				"Reading in " + workFileNames.getSize() + " work files.",
				"",
				0, 
				workFileLoader.getLengthOfTask());
	}
	
	class FileLoadingTimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(fileLoadingProgressMonitor.isCanceled() || 
					(workFileLoader.getProgress() == workFileLoader.getLengthOfTask())) {
				workFileLoader.stop();
				fileLoadingTimer.stop();
				VGLIIProblemFileReader.this.setCursor(Cursor.DEFAULT_CURSOR);
				fileLoadingProgressMonitor.close();
				workFileList.setEnabled(true);
				fileMenu.setEnabled(true);
			} else {
				VGLIIProblemFileReader.this.setCursor(Cursor.WAIT_CURSOR);
				fileLoadingProgressMonitor.setProgress(workFileLoader.getProgress());
				workFileList.setEnabled(false);
				fileMenu.setEnabled(false);
			}
		}		
	}
	
	private void getModelByName(String fileName) {
		GeneticModel model = namesAndModels.get(fileName);
		String answer = model.toString();
		answer = answer.replace("<body>", "<body><font color=red size=+2>" 
				+ fileName 
				+ " " + makeBeginnerModeString(model)
				+ "</font><hr>");
		correctAnswer.setText(answer);
		correctAnswer.setCaret(null);
		correctAnswer.setCaret(topOfPage);
	}
	
	private void printSelectedAnswers() {
		int[] selectedIndices = workFileList.getSelectedIndices();
		StringBuffer buffer = new StringBuffer();
		buffer.append("<html><body>");
		for (int i = 0; i < selectedIndices.length; i++) {
			String fileName = (workFileNames.get(selectedIndices[i])).toString();
			GeneticModel model = namesAndModels.get(fileName);
			String answer = model.toString();
			answer = answer.replace("<html><body>", 
					"<hr><font color=red size=+2>" + fileName 
					+ " " + makeBeginnerModeString(model) 
					+ "</font><hr>");
			answer = answer.replace("</body></html>", "");
			buffer.append(answer);
			buffer.append("<hr>");
		}
		buffer.append("</body></html>");
		
		docRenderer = new DocumentRenderer();
		docRenderer.setScaleWidthToFit(true);
		JTextPane printTextPane = new JTextPane();
		printTextPane.setContentType("text/html"); //$NON-NLS-1$
		printTextPane.setText(buffer.toString());
		HTMLDocument htDoc = (HTMLDocument) printTextPane.getDocument();
		docRenderer.print(htDoc);
	}
	
	private String makeBeginnerModeString(GeneticModel model) {
		StringBuffer buf = new StringBuffer();
		buf.append("Beginner Mode ");
		if(model.isBeginnerMode()) {
			buf.append("On");
		} else {
			buf.append("Off");
		}
		buf.append(" ");
		return buf.toString();
	}
}
