package UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;

public class PSDE extends JFrame {

	private static String version = "0.8";

	//indices for tabbed panes
	private final static int MOLECULES = 0;
	private final static int REACTIONS = 1;
	private final static int ANSWERS = 2;

	private JPanel mainPanel;
	private JTabbedPane tabbedPanes;
	private MoleculesPane moleculesPane;
	private ReactionsPane reactionsPane;
	private AnswersPane answersPane;

	private JMenuBar menuBar;

	private JMenu fileMenu;
	private JMenuItem openFileItem;
	private JMenuItem saveFileItem;
	private JMenuItem saveFileAsItem;
	private JMenuItem quitItem;

	private JMenu toolsMenu;
	private JMenuItem startEMUItem;
	private Thread emuThread;

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
		openFileItem = new JMenuItem("Open OrgoGame File...");
		fileMenu.add(openFileItem);
		saveFileItem = new JMenuItem("Save OrgoGame File");
		fileMenu.add(saveFileItem);
		saveFileAsItem = new JMenuItem("Save OrgoGame File as...");
		fileMenu.add(saveFileAsItem);
		quitItem = new JMenuItem("Quit");
		fileMenu.add(quitItem);
		menuBar.add(fileMenu);

		toolsMenu = new JMenu("Tools");
		startEMUItem = new JMenuItem("Show Cell Phone Emulator");
		toolsMenu.add(startEMUItem);
		menuBar.add(toolsMenu);

		mainPanel.add(menuBar, BorderLayout.NORTH);

		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));

		tabbedPanes = new JTabbedPane();
		moleculesPane = new MoleculesPane();
		tabbedPanes.add("Enter and Edit Molecules", moleculesPane);
		reactionsPane = new ReactionsPane();
		tabbedPanes.add("Enter and Edit Reactions", reactionsPane);
		answersPane = new AnswersPane();
		tabbedPanes.add("Enter and Edit Answers", answersPane);
		mainPanel.add(tabbedPanes, BorderLayout.CENTER);


		setPreferredSize(new Dimension(1100,800));
		getContentPane().add(mainPanel);
		
		quitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		startEMUItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});


	}

}
