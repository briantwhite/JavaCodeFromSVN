package UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class PSDE extends JFrame {
	
	private static String version = "0.8";
	
	//indices for tabbed panes
	private final static int TEST_RUN = 0;
	private final static int MOLECULES = 1;
	private final static int REACTIONS = 2;
	private final static int ANSWERS = 3;
	
	private JPanel mainPanel;
	
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem openFileItem;
	private JMenuItem saveFileItem;
	private JMenuItem saveFileAsItem;
	private JMenuItem quitItem;
	
	public PSDE() {
		super("Organic Chemistry Game Problem Set Development Environment " + version);
		addWindowListener(new ApplicationCloser());
		setupUI();
	}
	
	class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	
	/**
	 * main method
	 */
	public static void main(String[] args) {
		PSDE psde = new PSDE();
		psde.pack();
		psde.setVisible(true);
	}

	private void setupUI() {
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		menuBar = new JMenuBar();
		menuBar.setBorder(new BevelBorder(BevelBorder.RAISED));

		fileMenu = new JMenu("File");
		openFileItem = new JMenuItem("Open Problem Set File...");
		fileMenu.add(openFileItem);
		saveFileItem = new JMenuItem("Save Problem Set File");
		fileMenu.add(saveFileItem);
		saveFileAsItem = new JMenuItem("Save Problem Set File as...");
		fileMenu.add(saveFileAsItem);
		quitItem = new JMenuItem("Quit");
		fileMenu.add(quitItem);
		menuBar.add(fileMenu);
		mainPanel.add(menuBar, BorderLayout.NORTH);
		
		
		setPreferredSize(new Dimension(1100,800));
		getContentPane().add(mainPanel);


	}

}
