package YeastVGL;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;

import com.google.gson.Gson;

public class YeastVGL_GUI extends JFrame {

	YeastVGL yeastVGL;
	Pathway pathway;
	MutantSet mutantSet;
	int numEnzymes;
	int numMolecules;
		

	public YeastVGL_GUI(YeastVGL yeastVGL) {
		super("Yeast VGL 0.1");
		addWindowListener(new ApplicationCloser());
		this.yeastVGL = yeastVGL;
		this.pathway = yeastVGL.getPathway();
		this.numEnzymes = pathway.getNumberOfEnzymes();
		this.numMolecules = pathway.getNumberOfMolecules();
		this.mutantSet = yeastVGL.getMutantSet();
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

		
		JTabbedPane innerPanel = new JTabbedPane();
		ComplementationTestPanel ctp = new ComplementationTestPanel(yeastVGL);
		innerPanel.addTab("Complementation Test", ctp);
		PathwayPanel pp = new PathwayPanel(pathway);
		innerPanel.addTab("Pathway Analysis", pp);
		pp.updateDisplay();
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
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new FileReader(System.getProperty("user.home") + "/Desktop/text.txt"));
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
			}
		});
		saveWorkItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BufferedWriter writer;
				try {
					writer = new BufferedWriter(new FileWriter(System.getProperty("user.home") + "/Desktop/text.txt"));
					writer.write(ctp.getJsonString());
					writer.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		saveWorkAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		quitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}
	

}
