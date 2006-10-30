package molGenExp;

import genetics.GeneticsWorkshop;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import molBiol.Genex;
import biochem.Protex;


public class MolGenExp extends JFrame implements ListSelectionListener {

	private final static String version = "1.0";

	private JPanel mainPanel;

	JMenuBar menuBar;
	JMenu fileMenu;
	JMenuItem quitMenuItem;

	JMenu greenhouseMenu;
	JMenuItem loadGreenhouseMenuItem;
	JMenuItem saveGreenhouseMenuItem;
	JMenuItem saveAsGreenhouseMenuItem;
	JMenuItem deleteSelectedOrganismMenuItem;

	private JPanel innerPanel;

	private Greenhouse greenhouse;

	JTabbedPane explorerPane;
	
	private GeneticsWorkshop gw;
	//for genetics only; the two selected organisms
	private Organism firstSelectedOrganism;
	private Organism secondSelectedOrganism;
	
	private Protex protex;
	private Genex genex;

	private File greenhouseDirectory;
	
	private ColorModel colorModel;

	public MolGenExp() {
		super("Molecular Genetics Explorer " + version);
		addWindowListener(new ApplicationCloser());
		colorModel = new RYBColorModel();
		setupUI();
	}

	public static void main(String[] args) {
		MolGenExp mge = new MolGenExp();
		mge.pack();
		mge.setVisible(true);
	}

	class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

	private void setupUI() {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		this.setSize(new Dimension(screenSize.width, screenSize.height * 9/10));

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		menuBar = new JMenuBar();
		menuBar.setBorder(new BevelBorder(BevelBorder.RAISED));

		fileMenu = new JMenu("File");
		quitMenuItem = new JMenuItem("Quit");
		fileMenu.add(quitMenuItem);
		menuBar.add(fileMenu);

		menuBar.add(Box.createHorizontalGlue());

		greenhouseMenu = new JMenu("Greenhouse");
		loadGreenhouseMenuItem = new JMenuItem("Load Greenhouse...");
		greenhouseMenu.add(loadGreenhouseMenuItem);
		saveGreenhouseMenuItem = new JMenuItem("Save Greenhouse");
		greenhouseMenu.add(saveGreenhouseMenuItem);
		saveAsGreenhouseMenuItem = new JMenuItem("Save Greenhouse As...");
		greenhouseMenu.add(saveAsGreenhouseMenuItem);
		greenhouseMenu.addSeparator();
		deleteSelectedOrganismMenuItem = 
			new JMenuItem("Delete Selected Organism");
		greenhouseMenu.add(deleteSelectedOrganismMenuItem);
		menuBar.add(greenhouseMenu);
		mainPanel.add(menuBar, BorderLayout.NORTH);

		innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));

		explorerPane = new JTabbedPane();
//		explorerPane.setSize(new Dimension(screenSize.width * 8/10,
//		screenSize.height * 8/10));
		
		gw = new GeneticsWorkshop(this);
		explorerPane.addTab("Genetics", gw);
		firstSelectedOrganism = null;
		secondSelectedOrganism = null;


		protex = new Protex(this);
		explorerPane.addTab("Biochemistry", protex);

		genex = new Genex(this);
		explorerPane.addTab("Molecular Biology", genex);

		innerPanel.add(explorerPane);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(Box.createRigidArea(new Dimension(150,1)));
		greenhouse = new Greenhouse(new DefaultListModel(), this);
		rightPanel.setMaximumSize(new Dimension(150,1000));
		greenhouse.setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		rightPanel.setBorder(BorderFactory.createTitledBorder("Greenhouse"));
		JScrollPane greenhouseScrollPane = new JScrollPane(greenhouse);
		rightPanel.add(greenhouseScrollPane);
		
		innerPanel.add(rightPanel);

		mainPanel.add(innerPanel, BorderLayout.CENTER);

		getContentPane().add(mainPanel);
		
		explorerPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int currentPane = explorerPane.getSelectedIndex();
				switch (currentPane) {
					case 0:			//genetics
						greenhouse.setSelectionMode(
								ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
						break;
					case 1:			//biochemistry
						greenhouse.setSelectionMode(
								ListSelectionModel.SINGLE_SELECTION);
						break;
					case 2:			//molecular biology
						greenhouse.setSelectionMode(
								ListSelectionModel.SINGLE_SELECTION);
						break;
				}
			}
			
		});

		//make a greenhouse directory if one doesn't exist
		//  if one exists, load contents
		greenhouseDirectory = new File("Greenhouse");
		if(!greenhouseDirectory.exists() 
				|| !greenhouseDirectory.isDirectory()) {
			boolean success = greenhouseDirectory.mkdir();
			if (!success) {
				JOptionPane.showMessageDialog(
						this, 
						"Cannot create Greenhouse Folder",
						"File System Error", 
						JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			}
		} else {
			loadGreenhouse(greenhouseDirectory);
		}

		quitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		loadGreenhouseMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadGreenhouseFromChosenFolder();
			}
		});

		saveGreenhouseMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] all = greenhouse.getAll();
				if (all.length == 0) {
					JOptionPane.showMessageDialog(
							null, 
							"No Organisms in Greenhouse to Save",
							"Empty Greenhouse", 
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				//save it
				if (greenhouseDirectory != null) {
					saveToFolder(all);
				} else {
					saveToChosenFolder(all);
				}
			}
		});

		saveAsGreenhouseMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] all = greenhouse.getAll();
				if (all.length == 0) {
					JOptionPane.showMessageDialog(
							null, 
							"No Organisms in Greenhouse to Save",
							"Empty Greenhouse", 
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				saveToChosenFolder(all)	;
			}
		});

		deleteSelectedOrganismMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				greenhouse.deleteSelected();
			}
		});

	}

	public ColorModel getOverallColorModel() {
		return colorModel;
	}
	
	public Greenhouse getGreenhouse() {
		return greenhouse;
	}

	public void saveToGreenhouse(Organism o) {
		greenhouse.add(o);
	}

	public void loadOrganismIntoActivePanel(Organism o) {
		String selectedPane = 
			explorerPane.getSelectedComponent().getClass().toString();

		if (selectedPane.equals("class molBiol.Genex")) {
			genex.loadOrganism(o);
		}

		if (selectedPane.equals("class biochem.Protex")) {
			protex.loadOrganism(o);
		}
	}
	
	public void loadGreenhouseFromChosenFolder() {
		JFileChooser inFolderChooser = new JFileChooser();
		inFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = inFolderChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			greenhouseDirectory = inFolderChooser.getSelectedFile();
			loadGreenhouse(greenhouseDirectory);
		}
	}
	
	public void saveToChosenFolder(Object[] all) {
		JFileChooser outFolderChooser = new JFileChooser();
		outFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = outFolderChooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			greenhouseDirectory = outFolderChooser.getSelectedFile();
			saveToFolder(all);
		}
	}

	public void saveToFolder(Object[] all) {
		for (int i = 0; i < all.length; i++) {
			Organism o = (Organism)all[i];
			String name = o.getName();
			String fileName = greenhouseDirectory.toString() 
				+ System.getProperty("file.separator") 
				+ name
				+ ".organism";
			try {
				FileOutputStream f =
					new FileOutputStream(fileName);
				ObjectOutput s = new ObjectOutputStream(f);
				s.writeObject(o);
				s.flush();
				s.close();
				f.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void loadGreenhouse(File greenhouseDir) {
		greenhouse.clearList();
		String[] files = greenhouseDir.list();
		for (int i = 0; i < files.length; i++){
			String fileString = files[i];
			if (fileString.endsWith(".organism")) {
				String organismName = 
					fileString.replaceAll(".organism", "");
				String orgFileName = greenhouseDirectory.toString() 
					+ System.getProperty("file.separator") 
					+ fileString;
				try {
					FileInputStream in = new FileInputStream(orgFileName);
					ObjectInputStream s = new ObjectInputStream(in);
					Organism o = (Organism) s.readObject();
					o.setName(organismName);
					greenhouse.add(o);
					s.close();
					in.close();
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		greenhouse.revalidate();
		greenhouse.repaint();
	}

	//handler for selections of creatures in Genetics mode
	//  max of two at a time.
	public void valueChanged(ListSelectionEvent e) {
		if (explorerPane.getSelectedIndex() != 0) {
			return;
		}
		
		Object sourceObject = e.getSource();
		Organism selectedOrganism = 
			(Organism)((JList)sourceObject).getSelectedValue();
		System.out.println(selectedOrganism.getName());
	}


	
//	public Organism getSelectedOrganism() {
//		return greenhouse.getS
//	}

}
