package YeastVGL;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;

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
				
			}
		});
		saveWorkItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(ctp.getJsonString());
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
