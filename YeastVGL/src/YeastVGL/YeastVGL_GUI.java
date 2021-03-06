package YeastVGL;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.gson.Gson;

import Biochemistry.MutantSet;
import Biochemistry.Pathway;

public class YeastVGL_GUI extends JFrame {

	YeastVGL yeastVGL;
	Pathway pathway;
	MutantSet mutantSet;
	int numEnzymes;
	int numMolecules;

	JTabbedPane innerPanel;

	File currentSaveWorkFile;

	JMenuItem saveWorkItem;
	private boolean haveSomethingToSave;

	public YeastVGL_GUI(YeastVGL yeastVGL) {
		super("Yeast VGL 1.0");
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new ApplicationCloser());
		this.yeastVGL = yeastVGL;
		this.pathway = yeastVGL.getPathway();
		this.numEnzymes = pathway.getNumberOfEnzymes();
		this.numMolecules = pathway.getNumberOfMolecules();
		this.mutantSet = yeastVGL.getMutantSet();
		currentSaveWorkFile = null;

		haveSomethingToSave = false;
		setupUI();
	}

	class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			quit();
		}
	}

	public void setupUI() {

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		JMenu fileMenu = new JMenu("File");
		JMenuItem newProblemItem = new JMenuItem("New Problem");
		newProblemItem.setAccelerator(KeyStroke.getKeyStroke('N', 
				Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		fileMenu.add(newProblemItem);
		JMenuItem openWorkItem = new JMenuItem("Open Work...");
		openWorkItem.setAccelerator(KeyStroke.getKeyStroke('O', 
				Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		fileMenu.add(openWorkItem);
		saveWorkItem = new JMenuItem("Save Work");
		saveWorkItem.setAccelerator(KeyStroke.getKeyStroke('S', 
				Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		fileMenu.add(saveWorkItem);
		JMenuItem saveWorkAsItem = new JMenuItem("Save Work As...");
		fileMenu.add(saveWorkAsItem);
		JMenuItem quitItem = new JMenuItem("Quit");
		fileMenu.add(quitItem);
		menuBar.add(fileMenu);
		mainPanel.add(menuBar, BorderLayout.NORTH);


		innerPanel = new JTabbedPane();
		
		WelcomePanel wp = new WelcomePanel(this);
		innerPanel.addTab("Welcome", wp);
		innerPanel.addTab("Complementation Test", yeastVGL.getComplementationTestPanel());
		innerPanel.addTab("Pathway Analysis", yeastVGL.getPathwayPanel());

		// start with other tabs disabled for now
		innerPanel.setEnabledAt(1, false);
		innerPanel.setEnabledAt(2, false);
		
		// update the working set choices when you go to the PathwayPanel
		innerPanel.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				yeastVGL.getPathwayPanel().updateWorkingSet(
						yeastVGL.getComplementationTestPanel().getWorkingSet());
				yeastVGL.getPathwayPanel().updateDisplay();
			}			
		});
		
		mainPanel.add(innerPanel, BorderLayout.CENTER);

		this.add(mainPanel);
		this.pack();

		// menu listeners
		newProblemItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newProblem();
			}
		});
		openWorkItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openWork();
			}
		});
		saveWorkItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveWork();
			}
		});
		saveWorkAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveWorkAs();
			}
		});
		quitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quit();
			}
		});
	}

	public void enableTabs() {
		innerPanel.setEnabledAt(1, true);
		innerPanel.setEnabledAt(2, true);
	}

	public void goToComplementationTestPane() {
		innerPanel.setSelectedIndex(1);
	}

	public void haveSomethingToSave() {
		haveSomethingToSave = true;
	}
	
	public void newProblem() {
		enableTabs();
		goToComplementationTestPane();
		haveSomethingToSave = true;
	}

	public void saveWorkAs() {
		final JFileChooser fc = new JFileChooser(
				System.getProperty("user.home") 
				+ System.getProperty("file.separator") 
				+ "Desktop" 
				+ System.getProperty("file.separator"));
		fc.setFileFilter(new FileNameExtensionFilter("Yeast VGL files", "yvgl"));
		int retVal = fc.showSaveDialog(this);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			if (!file.getName().endsWith(".yvgl")) {
				file = new File(file.getAbsolutePath() + ".yvgl");
			}
			currentSaveWorkFile = file;
			saveWork();
		}
	}

	public void saveWork() {
		if (currentSaveWorkFile == null) {
			saveWorkAs();
			return;
		}
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(currentSaveWorkFile));
			writer.write(yeastVGL.getJsonString());
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		haveSomethingToSave = false;
	}

	public void openWork() {
		final JFileChooser fc = new JFileChooser(
				System.getProperty("user.home") 
				+ System.getProperty("file.separator") 
				+ "Desktop" 
				+ System.getProperty("file.separator"));
		fc.setFileFilter(new FileNameExtensionFilter("Yeast VGL files", "yvgl"));
		int retVal = fc.showOpenDialog(this);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(fc.getSelectedFile()));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			currentSaveWorkFile = fc.getSelectedFile();
			StringBuffer buf = new StringBuffer();
			String line;
			try {
				line = reader.readLine();
				while (line != null) {
					buf.append(line);
					buf.append(System.lineSeparator());
					line = reader.readLine();
				}
				reader.close();
				Gson gson = new Gson();
				State state = gson.fromJson(buf.toString(), State.class);
				yeastVGL.restoreSavedState(state);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			enableTabs();
			goToComplementationTestPane();
			haveSomethingToSave = true;
		}
	}

	public void quit() {
		if (haveSomethingToSave) {
			Object[] options = {"Quit without saving", "Save your work", "Cancel"};
			int n = JOptionPane.showOptionDialog(
					this, 
					"You have unsaved work, do you want to save before quitting?", 
					"Unsaved work", 
					JOptionPane.YES_NO_CANCEL_OPTION, 
					JOptionPane.QUESTION_MESSAGE, 
					null, 
					options, 
					options[2]);
			if (n == 0) {
				System.exit(0);
			} else if (n == 1) {
				saveWork();
			} else {
				return;
			}
		} else {
			System.exit(0);
		}
	}

}
