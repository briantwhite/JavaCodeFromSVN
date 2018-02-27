package YeastVGL;
import java.awt.BorderLayout;
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
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.gson.Gson;

public class YeastVGL_GUI extends JFrame {

	YeastVGL yeastVGL;
	Pathway pathway;
	MutantSet mutantSet;
	int numEnzymes;
	int numMolecules;

	JTabbedPane innerPanel;
	ComplementationTestPanel ctp;
	
	File currentSaveWorkFile;


	public YeastVGL_GUI(YeastVGL yeastVGL) {
		super("Yeast VGL 0.1");
		addWindowListener(new ApplicationCloser());
		this.yeastVGL = yeastVGL;
		this.pathway = yeastVGL.getPathway();
		this.numEnzymes = pathway.getNumberOfEnzymes();
		this.numMolecules = pathway.getNumberOfMolecules();
		this.mutantSet = yeastVGL.getMutantSet();
		currentSaveWorkFile = null;
		setupUI();
	}

	class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

	public void setupUI() {

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorder(new BevelBorder(BevelBorder.RAISED));
		JMenu fileMenu = new JMenu("File");
		JMenuItem newProblemItem = new JMenuItem("New Problem");
		fileMenu.add(newProblemItem);
		JMenuItem openWorkItem = new JMenuItem("Open Work...");
		fileMenu.add(openWorkItem);
		JMenuItem saveWorkItem = new JMenuItem("Save Work");
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
		ctp = new ComplementationTestPanel(yeastVGL);
		innerPanel.addTab("Complementation Test", ctp);
		PathwayPanel pp = new PathwayPanel(pathway);
		innerPanel.addTab("Pathway Analysis", pp);
		pp.updateDisplay();
		// start with other tabs disabled for now
		innerPanel.setEnabledAt(1, false);
		innerPanel.setEnabledAt(2, false);
		mainPanel.add(innerPanel, BorderLayout.CENTER);

		this.add(mainPanel);
		this.pack();

		// menu listeners
		newProblemItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
				System.exit(0);
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
			writer.write(ctp.getJsonString());
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
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
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
				ctp.updateState(state);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			enableTabs();
			goToComplementationTestPane();
		}
	}

}
