package jsVGLReader;


import java.awt.BorderLayout;
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
import javax.swing.JDialog;
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
import javax.swing.ListSelectionModel;
import javax.swing.text.Caret;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class jsVGLReader extends JFrame {

	private File workingDir;

	private JList<File> workFileList;
	private DefaultListModel<String> workFiles;

	public JEditorPane correctAnswer;
	public JScrollPane correctAnswerScroller;
	public Caret topOfCorrectAnswer;

	public JEditorPane theirAnswer;
	public JScrollPane theirAnswerScroller;
	public Caret topOfTheirAnswer;

	private TreeMap<String, ModelSet> filenamesAndModels;

	public jsVGLReader() {
		filenamesAndModels = new TreeMap<String, ModelSet>();
//		setupUI();
//		pack();
//		setVisible(true);
	}

	class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

	public static void main(String[] args) {		
		jsVGLReader reader = new jsVGLReader();

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
		reader.setupUI();
		reader.pack();
		reader.setVisible(true);
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

		workFiles = new DefaultListModel<String>();
		workFileList = new JList(workFiles);
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
					(workFiles.get((list.locationToIndex(evt.getPoint())))).toString();
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
				workFiles.addElement(files[i]);
			}
		}

		for (int i = 0; i < workFiles.getSize(); i++) {
			String currentWorkFileName = workFiles.get(i);
			ModelSet modelSet = getModelsFromFile(workingDir.toString() + System.getProperty("file.separator") + currentWorkFileName);
			filenamesAndModels.put(currentWorkFileName, modelSet);
		}
		setVisible(true);
	}

	private ModelSet getModelsFromFile(String fileName) {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(fileName));
			JSONObject jsonObj = (JSONObject)obj;
			JSONObject vglJSON = (JSONObject)jsonObj.get("VglII");
			JSONObject modelBuilderJSON = (JSONObject)vglJSON.get("ModelBuilderState");
			System.out.println(modelBuilderJSON.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void showWorkByName(String fileName) {

		ModelSet result = filenamesAndModels.get(fileName);

		correctAnswer.setText(result.getCorrectAnswerHTML());
		correctAnswer.setCaret(null);
		correctAnswer.setCaret(topOfCorrectAnswer);

		theirAnswer.setText(result.getStudentAnswerHTML());
		theirAnswer.setCaret(null);
		theirAnswer.setCaret(topOfTheirAnswer);

		this.toFront();
	}


}
