package LogAnalyzer;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ProgressMonitor;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class VGLLogAnalyzer extends JFrame {

	public static final boolean debug = false;

	private static final String version = "1.0";

	/**
	 * key for encrypting work files
	 *   XORed with bytes of work file
	 */
	public final static byte[] KEY = 
		(new String("The Virtual Genetics Lab is Awesome!")).getBytes();

	/** 
	 * the integers for the different cross types
	 */
	public final static int FIELD_POP_CROSS = 0;
	public final static int FIELD_CROSS_OTHER = 1;
	public final static int SIBLING_CROSS = 2;
	public final static int OTHER_CROSS = 3;

	/**
	 * integers for cross modifiers
	 */
	public final static int SAME_PHENOTYPES = 0;
	public final static int DIFFERENT_PHENOTYPES = 1;

	public static String[] columnHeadings = {
		"FileName",  		// 0
		"VGL", 				// 1
		"ModelNumber",		// 2
		"CrossString",		// 3
		"CrossSequence", 	// 4
		"TotalCrosses", 	// 5
		"FieldOnly", 		// 6	
		"FieldxOther", 	// 7
		"Sibling", 			// 8
		"Other",			// 9
		"SamePheno",		// 10
		"DiffPheno",		// 11
		"NumberExtra",		// 12
		"NumberShowSexLink", //13
	"NumberShowDominance"};	// 14
	public static int[] columnsWithIntegers = {2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
	public static int[] columnsThatMustBeQuoted = {0, 3};
	public static int colorChipColumn = 4;


	private Object[][] data;

	private Timer fileLoadingTimer;
	private WorkFileLoader workFileLoader;
	private ProgressMonitor fileLoadingProgressMonitor;

	private JPanel mainPanel;

	private JFileChooser fileChooser;

	private JTable dataTable;

	private JMenu fileMenu;
	private JMenuItem openMenuItem;
	private JMenuItem quitMenuItem;

	private DocumentRenderer docRenderer;

	public VGLLogAnalyzer() {
		super("VGLII Problem File Reader " + version);
		addWindowListener(new ApplicationCloser());
		fileLoadingTimer = new Timer(100, new FileLoadingTimerListener());
		docRenderer = new DocumentRenderer();
		setupUI();
	}

	public static void main(String[] args) {
		VGLLogAnalyzer vla = new VGLLogAnalyzer();
		vla.pack();
		vla.setVisible(true);
	}

	class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

	private void setupUI() {

		JMenuBar menuBar = new JMenuBar();

		fileMenu = new JMenu("File");

		openMenuItem = new JMenuItem("Open directory with work files...");
		openMenuItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.META_MASK));
		openMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> workFileNames = openFiles();
				if (workFileNames == null) return;
				processFiles(workFileNames);
			}
		});
		fileMenu.add(openMenuItem);

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

		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(Box.createRigidArea(new Dimension(400,1)));
		JLabel label = new JLabel("Choose a directory where work files are stored.");
		label.setPreferredSize(new Dimension(400,400));
		mainPanel.add(label);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(menuBar, BorderLayout.NORTH);
		getContentPane().add(mainPanel, BorderLayout.CENTER);

	}

	private ArrayList<String> openFiles() {
		File currentDirectory = new File(".");
		int val = fileChooser.showOpenDialog(VGLLogAnalyzer.this);
		if (val == JFileChooser.APPROVE_OPTION) {
			currentDirectory = fileChooser.getSelectedFile();
			return openDirectoryAndAddFilesToList(currentDirectory);
		} else {
			return null;
		}
	}

	//recursively open directories
	private ArrayList<String> openDirectoryAndAddFilesToList(File currentDirectory) {
		ArrayList<String> workFileNames = new ArrayList<String>();
		String[] files = currentDirectory.list();
		for (int i = 0; i < files.length; i++) {
			File file = new File(
					currentDirectory.getAbsolutePath() 
					+ System.getProperty("file.separator") 
					+ files[i]);
			if (files[i].endsWith(".wr2") || files[i].endsWith(".wrk")) {
				workFileNames.add(file.getAbsolutePath());
			} else {
				if (file.isDirectory()) {
					workFileNames.addAll(openDirectoryAndAddFilesToList(new File(currentDirectory.getAbsolutePath() 
							+ System.getProperty("file.separator") 
							+ files[i])));
				}
			}
		}
		return workFileNames;
	}

	private void processFiles(ArrayList<String> workFileNames) {
		workFileLoader = new WorkFileLoader(workFileNames);
		Thread t = new Thread(workFileLoader);
		t.start();
		fileLoadingTimer.start();
		fileLoadingProgressMonitor = new ProgressMonitor(
				VGLLogAnalyzer.this,
				"Reading in " + workFileNames.size() + " work files.",
				"",
				0, 
				workFileLoader.getLengthOfTask());
	}

	class FileLoadingTimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(fileLoadingProgressMonitor.isCanceled() || 
					(workFileLoader.getProgress() == workFileLoader.getLengthOfTask())) {
				data = workFileLoader.getResult();
				workFileLoader.stop();
				fileLoadingTimer.stop();
				VGLLogAnalyzer.this.setCursor(Cursor.DEFAULT_CURSOR);
				fileLoadingProgressMonitor.close();
				fileMenu.setEnabled(true);
				dataTable = new JTable(new DefaultTableModel(data, columnHeadings));
				dataTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				dataTable.getColumnModel().getColumn(colorChipColumn).setCellRenderer(
						new ImageRenderer());
				dataTable.setAutoCreateRowSorter(true);
				TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) dataTable.getRowSorter();
				for (int i = 0; i < columnsWithIntegers.length; i++) {
					sorter.setComparator(columnsWithIntegers[i], new Comparator<Integer>() {
						public int compare(Integer o1, Integer o2) {
							if (o1.intValue() > o2.intValue()) return 1;
							if (o1.intValue() < o2.intValue()) return -1;
							return 0;
						}				
					});					
				}
				JOptionPane.showMessageDialog(null, 
						buildResultPanel(new JScrollPane(dataTable)),
						"Results",
						JOptionPane.PLAIN_MESSAGE);
			} else {
				VGLLogAnalyzer.this.setCursor(Cursor.WAIT_CURSOR);
				fileLoadingProgressMonitor.setProgress(workFileLoader.getProgress());
				fileMenu.setEnabled(false);
			}
		}		
	}

	private JPanel buildResultPanel(JScrollPane table) {
		JPanel resultPanel = new JPanel();
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
		resultPanel.add(Box.createRigidArea(new Dimension(1000,1)));

		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");

		JMenuItem saveMenuItem = new JMenuItem("Save results to file...");
		saveMenuItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.META_MASK));
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportDataToFile();
			}
		});
		fileMenu.add(saveMenuItem);

		JMenuItem printMenuItem = new JMenuItem("Print selected...");
		printMenuItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.META_MASK));
		printMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					MessageFormat headerFormat = new MessageFormat("Page {0}");
					MessageFormat footerFormat = new MessageFormat("- {0} -");
					dataTable.print(JTable.PrintMode.FIT_WIDTH, headerFormat, footerFormat);
				} catch (PrinterException pe) {
					System.err.println("Error printing: " + pe.getMessage());
				}
			}
		});
		fileMenu.add(printMenuItem);
		menuBar.add(fileMenu);

		JMenu editMenu = new JMenu("Edit");
		JMenuItem deleteSelectedMenuItem = new JMenuItem("Delete Selected Row(s)");
		deleteSelectedMenuItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_X,Event.CTRL_MASK));
		deleteSelectedMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] rowsToDelete = dataTable.getSelectedRows();
				DefaultTableModel tableModel = (DefaultTableModel)dataTable.getModel();
				for (int i = (rowsToDelete.length - 1); i >= 0; i--) {
					int rowToDelete = dataTable.convertRowIndexToModel(rowsToDelete[i]);
					System.out.println("Deleting: " + tableModel.getValueAt(rowToDelete, 0));
					tableModel.removeRow(rowToDelete);
				}
			}		
		});
		editMenu.add(deleteSelectedMenuItem);
		menuBar.add(editMenu);

		menuPanel.add(menuBar);
		menuPanel.add(Box.createHorizontalGlue());
		resultPanel.add(menuPanel);

		resultPanel.add(table);
		return resultPanel;
	}

	private void exportDataToFile() {
		File outFile = null;
		JFileChooser chooser = new JFileChooser(".");
		int val = chooser.showSaveDialog(VGLLogAnalyzer.this);
		if (val == JFileChooser.APPROVE_OPTION) {
			outFile = chooser.getSelectedFile();
		} 
		try {
			FileWriter outWriter = new FileWriter(outFile);
			PrintWriter out = new PrintWriter(outWriter);

			// make header line
			StringBuffer b = new StringBuffer();
			for (int i = 0; i < columnHeadings.length; i++) {
				if (i != colorChipColumn) {
					b.append(columnHeadings[i] + ",");
				}
			}
			b.deleteCharAt(b.length() - 1);
			out.println(b.toString());

			DefaultTableModel dataModel = (DefaultTableModel)dataTable.getModel();
			Object[][] currentData = 
				new Object[dataModel.getRowCount()][dataModel.getColumnCount()];
			for (int i = 0; i < dataModel.getRowCount(); i++) {
				for (int j = 0; j < dataModel.getColumnCount(); j++) {
					currentData[i][j] = 
						dataModel.getValueAt(dataTable.convertRowIndexToModel(i), j);
				}
			}
			for (int i = 0; i < currentData.length; i++) {
				StringBuffer buf = new StringBuffer();
				for (int j = 0; j < VGLLogAnalyzer.columnHeadings.length; j++) {
					if (j != colorChipColumn) {

						boolean needsQuotes = false;
						for (int k = 0; k < columnsThatMustBeQuoted.length; k++) {
							if (j == columnsThatMustBeQuoted[k]) needsQuotes = true;
						}

						if (needsQuotes) buf.append("\"");
						buf.append(currentData[i][j]);
						if (needsQuotes) buf.append("\"");

						buf.append(",");
					}
				}
				buf.deleteCharAt(buf.length() - 1);
				out.println(buf.toString());
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
