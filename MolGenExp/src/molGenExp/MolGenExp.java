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
import java.util.regex.Pattern;

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


public class MolGenExp extends JFrame {

	//indices for tabbed panes
	private final static int GENETICS = 0;
	private final static int BIOCHEMISTRY = 1;
	private final static int MOLECULAR_BIOLOGY = 2;

	private final static String version = "1.0";

	public final static String sampleDNA = 
		new String("CAGCTATAACCGAGATTGATGTCTAG"
			+ "TGCGATAAGCCCCAAAGATCGGCACATTTTGTGCGCTATA"
			+ "CAAAGGTTAGTGGTCTGTCGGCAGTAGTAGGGGGCGT");
	
	public final static String sampleProtein =
		new String("MSNRHILLVVCRQ");
	
	
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
	private Organism org1;
	private Organism org2;

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
		org1 = null;
		org2 = null;


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
				case GENETICS:			
					greenhouse.setSelectionMode(
							ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
					clearSelectedOrganisms();
					break;
				case BIOCHEMISTRY:			
					greenhouse.setSelectionMode(
							ListSelectionModel.SINGLE_SELECTION);
					clearSelectedOrganisms();
					break;
				case MOLECULAR_BIOLOGY:			
					greenhouse.setSelectionMode(
							ListSelectionModel.SINGLE_SELECTION);
					clearSelectedOrganisms();
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
	
	public Genex getGenex() {
		return genex;
	}
	
	public Protex getProtex() {
		return protex;
	}
	
	public GeneticsWorkshop getGeneticsWorkshop() {
		return gw;
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
		//first, clear out all the old organims
		String[] oldOrganisms = greenhouseDirectory.list();
		for (int i = 0; i < oldOrganisms.length; i++) {
			String name = oldOrganisms[i];
			if (name.indexOf(".organism") != -1) {
				File f = new File(greenhouseDirectory, name);
				f.delete();
			}
		}
		
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
	
	public void saveSelectedOrganismToGreenhouse() {
		//should not happen - only works if one org selected
		if ((org1 == null) || (org2 != null)) {
			return;
		}
		saveOrganismToGreenhouse(org1);
	}
	
	public void saveOrganismToGreenhouse(Organism o) {
		
		String name = "";
		String warning = "";
		Pattern p = Pattern.compile("[^A-Za-z0-9\\_]+");
		while (name.equals("") || 
				p.matcher(name).find() ||
				greenhouse.nameExistsAlready(name)){
			name = JOptionPane.showInputDialog(
					this,
					warning +
					"Give a unique name for your new organism.\n"
					+ "This can only include letters, numbers, and "
					+ "_.",
					"Name your organism.",
					JOptionPane.PLAIN_MESSAGE);
			if (name == null) {
				return;
			}

			if(greenhouse.nameExistsAlready(name)) {
				warning = "<html><font color=red>"
					+ "The name you entered exists already,"
					+ " please cancel or try again.</font>\n";
			} else {
				warning = "<html><font color=red>"
					+ "The name you entered was not allowed," 
					+ " please cancel or try again.</font>\n";
			}
		}
		saveToGreenhouse(new Organism(
				Organism.GREENHOUSE,
				name,
				o));
	}


	//handler for selections of creatures in Genetics mode
	//  max of two at a time.
	public void updateSelectedOrganisms(Organism o) {

		// only do this in genetics
		if (explorerPane.getSelectedIndex() != GENETICS) {
			return;
		}

		if (org1 == null) {
			org1 = o;
			updateGeneticsButtonStatus();
			return;
		}

		//if you've clicked an already selected organism, clear it
		if (o.equals(org1)) {
			org1 = org2;
			org2 = null;
			updateSelectedOrganismDisplay();
			updateGeneticsButtonStatus();
			return;
		}

		if (o.equals(org2)) {
			org2 = null;
			updateSelectedOrganismDisplay();
			updateGeneticsButtonStatus();
			return;			
		}

		//otherwise, update the selected organims
		org2 = org1;
		org1 = o;
		updateSelectedOrganismDisplay();
		updateGeneticsButtonStatus();
	}

	public void clearSelectedOrganisms() {
		org1 = null;
		org2 = null;
		updateSelectedOrganismDisplay();
		updateGeneticsButtonStatus();
	}

	public Organism getOrg1() {
		return org1;
	}

	public Organism getOrg2() {
		return org2;
	}

	// make the display show the proper selected organisms
	public void updateSelectedOrganismDisplay() {

		greenhouse.clearSelection();
		gw.getLowerGeneticsWindow().getGeneticsWorkPanelList().clearSelection();
		gw.getUpperGeneticsWindow().getGeneticsWorkPanelList().clearSelection();

		updateListSelections(org1);
		updateListSelections(org2);

		greenhouse.revalidate();
		greenhouse.repaint();
		gw.getLowerGeneticsWindow().getGeneticsWorkPanelList().revalidate();
		gw.getLowerGeneticsWindow().getGeneticsWorkPanelList().repaint();
		gw.getUpperGeneticsWindow().getGeneticsWorkPanelList().revalidate();
		gw.getUpperGeneticsWindow().getGeneticsWorkPanelList().repaint();
	}

	void updateListSelections(Organism o) {
		JList list = null;

		if (o == null) {
			return;
		}
		
		switch (o.getLocation()) {
		case Organism.GREENHOUSE:
			list = greenhouse;
			break;
		case Organism.LOWER_GENETICS_WINDOW:
			list = gw.getLowerGeneticsWindow().getGeneticsWorkPanelList();
			break;
		case Organism.UPPER_GENETICS_WINDOW:
			list = gw.getUpperGeneticsWindow().getGeneticsWorkPanelList();
			break;
		}

		DefaultListModel listModel = 
			(DefaultListModel)list.getModel();

		list.addSelectionInterval(listModel.indexOf(o), 
				listModel.indexOf(o));

	}

	//if no orgs selected - no buttons active;
	// if only one - save to greenhouse, mutate, and self are active;
	// if two - cross only
	public void updateGeneticsButtonStatus() {
		int numSelectedOrgs = 0;

		if (org1 != null) {
			numSelectedOrgs++;
		}

		if (org2 != null) {
			numSelectedOrgs++;
		}

		switch (numSelectedOrgs) {
		case 0:
			gw.getGenMidButtonPanel().setSaveButtonEnabled(false);
			gw.setCrossTwoButtonsEnabled(false);
			gw.setSelfCrossButtonsEnabled(false);
			gw.setMutateButtonsEnabled(false);
			break;

		case 1:
			gw.getGenMidButtonPanel().setSaveButtonEnabled(true);
			gw.setCrossTwoButtonsEnabled(false);
			gw.setSelfCrossButtonsEnabled(true);
			gw.setMutateButtonsEnabled(true);
			break;

		case 2:
			gw.getGenMidButtonPanel().setSaveButtonEnabled(false);
			gw.setCrossTwoButtonsEnabled(true);
			gw.setSelfCrossButtonsEnabled(false);
			gw.setMutateButtonsEnabled(false);
			break;

		}
	}
}
