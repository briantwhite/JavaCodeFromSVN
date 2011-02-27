package Grader;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ProgressMonitor;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Caret;

import GeneticModels.GeneticModel;
import ModelBuilder.ModelBuilderUI;

public class Grader extends JFrame {
	
	private File workingDir;
	private GeneticModel geneticModel;
	private ModelBuilderUI modelBuilder;
	
	private Timer fileLoadingTimer;
	private WorkFileLoader workFileLoader;
	private ProgressMonitor fileLoadingProgressMonitor;

	private JList workFileList;
	private DefaultListModel workFileNames;
	
	public JEditorPane correctAnswer;
	public JScrollPane correctAnswerScroller;
	public Caret topOfCorrectAnswer;

	public JEditorPane theirAnswer;
	public JScrollPane theirAnswerScroller;
	public Caret topOfTheirAnswer;

	private TreeMap<String, GradedResult> filenamesAndResults;
	
	public Grader(File workingDir, GeneticModel geneticModel, ModelBuilderUI modelBuilder) {
		this.workingDir = workingDir;
		this.geneticModel = geneticModel;
		this.modelBuilder = modelBuilder;
		fileLoadingTimer = new Timer(100, new FileLoadingTimerListener());
		setupUI();
		pack();
		setVisible(true);
	}
	
	private void setupUI() {
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
//					getModelByName(workFileName);
				}
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
		theirAnswerScroller = new JScrollPane(correctAnswer);
		theirAnswerScroller.setPreferredSize(new Dimension(300,80));
		topOfTheirAnswer = correctAnswer.getCaret();
		theirAnswerPanel.add(theirAnswerScroller);
		
		mainPanel.add(theirAnswerPanel);
	

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(mainPanel, BorderLayout.CENTER);
	}
	
	public void openDirectoryAndLoadFiles() {
		String[] files = workingDir.list();
		for (int i = 0; i < files.length; i++) {
			if (files[i].endsWith(".wr2")) {
				workFileNames.addElement(files[i]);
			}
		}
		workFileLoader = new WorkFileLoader(
				workingDir, 
				workFileNames, 
				filenamesAndResults);
		Thread t = new Thread(workFileLoader);
		t.start();
		fileLoadingTimer.start();
		fileLoadingProgressMonitor = new ProgressMonitor(
				Grader.this,
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
				Grader.this.setCursor(Cursor.DEFAULT_CURSOR);
				fileLoadingProgressMonitor.close();
				workFileList.setEnabled(true);
			} else {
				Grader.this.setCursor(Cursor.WAIT_CURSOR);
				fileLoadingProgressMonitor.setProgress(workFileLoader.getProgress());
				workFileList.setEnabled(false);
			}
		}		
	}

}
