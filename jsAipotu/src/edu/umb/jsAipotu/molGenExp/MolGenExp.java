package edu.umb.jsAipotu.molGenExp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.apple.eawt.Application;
import com.apple.eawt.QuitStrategy;

import edu.umb.jsAipotu.biochem.AminoAcid;
import edu.umb.jsAipotu.biochem.BiochemistryWorkbench;
import edu.umb.jsAipotu.biochem.BiochemistryWorkpanel;
import edu.umb.jsAipotu.biochem.FoldedProteinArchive;
import edu.umb.jsAipotu.biochem.FoldingException;
import edu.umb.jsAipotu.biochem.PaintedInACornerFoldingException;
import edu.umb.jsAipotu.biochem.StandardTable;
import edu.umb.jsAipotu.evolution.EvolutionWorkArea;
import edu.umb.jsAipotu.genetics.GeneticsWorkbench;
import edu.umb.jsAipotu.molBiol.MolBiolWorkbench;
import edu.umb.jsAipotu.molBiol.MolBiolWorkpanel;
import edu.umb.jsAipotu.preferences.GlobalDefaults;
import edu.umb.jsAipotu.preferences.MGEPreferences;
import edu.umb.jsAipotu.preferences.PreferencesDialog;


public class MolGenExp {

	//indices for tabbed panes
	private final static int GENETICS = 0;
	private final static int BIOCHEMISTRY = 1;
	private final static int MOLECULAR_BIOLOGY = 2;
	private final static int EVOLUTION = 3;

	private JPanel mainPanel;

	JMenuBar menuBar;

	JMenu fileMenu;
	JMenuItem prefsItem;
	JMenuItem dumpWorldItem;
	JMenuItem loadWorldItem;
	JMenuItem savePicOfUpperPanelItem;
	JMenuItem savePicOfLowerPanelItem;
	JMenuItem quitMenuItem;

	JMenu editMenu;
	JMenuItem copyUpperSequenceToClipboardItem;
	JMenuItem copyLowerSequenceToClipboardItem;	
	JMenuItem copyUpperImageToClipboardItem;
	JMenuItem copyLowerImageToClipboardItem;

	JMenu compareMenu;
	JMenuItem u_lMenuItem;
	JMenuItem u_sMenuItem;
	JMenuItem l_sMenuItem;
	JMenuItem u_cbMenuItem;
	JMenuItem l_cbMenuItem;

	private JPanel innerPanel;

	private GreenhouseLoader greenhouseLoader;
	private Timer greenhouseLoaderTimer;
	private Greenhouse greenhouse;
	private JButton addToGreenhouseButton;

	private JProgressBar progressBar;
	private JLabel statusLabel;
	private JLabel foldingStatsLabel;

	JTabbedPane explorerPane;

	private GeneticsWorkbench geneticsWorkbench;

	private OrganismFactory organismFactory;

	//for genetics only; the two selected organisms
	private OrganismAndLocation oal1;
	private OrganismAndLocation oal2;

	private BiochemistryWorkbench biochemistryWorkbench;
	private MolBiolWorkbench molBiolWorkbench;
	private EvolutionWorkArea evolutionWorkArea;

	private PreferencesDialog preferencesDialog;

	public MolGenExp() {
		setupUI();
	}

	private void setupUI() {

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		menuBar = new JMenuBar();
		menuBar.setBorder(new BevelBorder(BevelBorder.RAISED));

		fileMenu = new JMenu("File");
		prefsItem = new JMenuItem("Preferences...");
		dumpWorldItem = new JMenuItem("Save World to file...");
		loadWorldItem = new JMenuItem("Load World from file...");
		savePicOfUpperPanelItem = new JMenuItem("Save Image of Upper Panel...");
		savePicOfLowerPanelItem = new JMenuItem("Save Image of Lower Panel...");		
		quitMenuItem = new JMenuItem("Quit");
		fileMenu.add(savePicOfUpperPanelItem);
		fileMenu.add(savePicOfLowerPanelItem);
		fileMenu.addSeparator();
		fileMenu.add(prefsItem);
		fileMenu.add(dumpWorldItem);
		dumpWorldItem.setEnabled(false);
		fileMenu.add(loadWorldItem);
		loadWorldItem.setEnabled(false);
		fileMenu.add(quitMenuItem);
		menuBar.add(fileMenu);

		editMenu = new JMenu("Edit");
		copyUpperSequenceToClipboardItem = 
				new JMenuItem("Copy Upper Sequence to Clipboard");
		editMenu.add(copyUpperSequenceToClipboardItem);
		copyLowerSequenceToClipboardItem = 
				new JMenuItem("Copy Lower Sequence to Clipboard");
		editMenu.add(copyLowerSequenceToClipboardItem);
		editMenu.addSeparator();
		copyUpperImageToClipboardItem = 
				new JMenuItem("Copy Image of Upper Panel to Clipboard");
		editMenu.add(copyUpperImageToClipboardItem);
		copyLowerImageToClipboardItem = 
				new JMenuItem("Copy Image of Lower Panel to Clipboard");
		editMenu.add(copyLowerImageToClipboardItem);
		menuBar.add(editMenu);
		turnOffSequenceClipboardItems();

		compareMenu = new JMenu("Compare");
		u_lMenuItem = new JMenuItem("Upper vs. Lower");
		compareMenu.add(u_lMenuItem);
		compareMenu.addSeparator();
		u_sMenuItem = new JMenuItem("Upper vs. Sample");
		compareMenu.add(u_sMenuItem);
		l_sMenuItem = new JMenuItem("Lower vs. Sample");
		compareMenu.add(l_sMenuItem);
		compareMenu.addSeparator();
		u_cbMenuItem = new JMenuItem("Upper vs. Clipboard");
		compareMenu.add(u_cbMenuItem);
		l_cbMenuItem = new JMenuItem("Lower vs. Clipboard");
		compareMenu.add(l_cbMenuItem);
		menuBar.add(compareMenu);
		compareMenu.setEnabled(false);

		mainPanel.add(menuBar, BorderLayout.NORTH);

		innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));

		explorerPane = new JTabbedPane();

		addToGreenhouseButton = new JButton("Add...");

		geneticsWorkbench = new GeneticsWorkbench(this);
		explorerPane.addTab("Genetics", geneticsWorkbench);
		oal1 = null;
		oal2 = null;

		organismFactory = new OrganismFactory();

		biochemistryWorkbench = new BiochemistryWorkbench(this);
		explorerPane.addTab("Biochemistry", biochemistryWorkbench);

		molBiolWorkbench = new MolBiolWorkbench(this);
		explorerPane.addTab("Molecular Biology", molBiolWorkbench);

		evolutionWorkArea = new EvolutionWorkArea(this);
		explorerPane.addTab("Evolution", evolutionWorkArea);

		innerPanel.add(explorerPane);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setMaximumSize(new Dimension(120,1000));
		rightPanel.add(Box.createRigidArea(new Dimension(100,1)));
		addToGreenhouseButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		rightPanel.add(addToGreenhouseButton);
		addToGreenhouseButton.setEnabled(false);

		greenhouse = new Greenhouse(new DefaultListModel(), this);
		greenhouse.setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		rightPanel.setBorder(BorderFactory.createTitledBorder("Greenhouse"));
		JScrollPane greenhouseScrollPane = new JScrollPane(greenhouse);
		rightPanel.add(greenhouseScrollPane);

		innerPanel.add(rightPanel);

		mainPanel.add(innerPanel, BorderLayout.CENTER);

		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusPanel.add(Box.createHorizontalStrut(10));
		progressBar = new JProgressBar();
		progressBar.setMaximumSize(new Dimension(200,20));
		statusPanel.add(progressBar);
		statusPanel.add(Box.createHorizontalStrut(20));
		statusLabel = new JLabel("Welcome");
		statusPanel.add(statusLabel);
		statusPanel.add(Box.createHorizontalGlue());
		foldingStatsLabel = new JLabel("No sequences folded yet");
		statusPanel.add(foldingStatsLabel);
		foldingStatsLabel.setToolTipText("<html>Certain protein sequences cannot be folded properly<br>"
				+ "with the 2-dimensional algorithm Aipotu uses.<br>These sequences are "
				+ "are lethal to the organism that contains them.<br>"
				+ "Dead organisms are shown in gray.</html>");
		statusPanel.add(Box.createHorizontalStrut(20));

		mainPanel.add(statusPanel, BorderLayout.SOUTH);


		preferencesDialog = new PreferencesDialog(this);


		explorerPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int currentPane = explorerPane.getSelectedIndex();
				switch (currentPane) {
				case GENETICS:			
					clearSelectedOrganisms();
					compareMenu.setEnabled(false);
					turnOffSequenceClipboardItems();
					savePicOfLowerPanelItem.setEnabled(true);
					savePicOfUpperPanelItem.setText("Save Image of Upper Panel...");
					copyLowerImageToClipboardItem.setEnabled(true);
					copyUpperImageToClipboardItem.setText(
							"Copy Image of Upper Panel to Clipboard");
					addToGreenhouseButton.setEnabled(false);
					dumpWorldItem.setEnabled(false);
					loadWorldItem.setEnabled(false);
					setCustomSelectionSettings();
					break;
				case BIOCHEMISTRY:			
					clearSelectedOrganisms();
					compareMenu.setEnabled(true);
					turnOnSequenceClipboardItems();
					savePicOfLowerPanelItem.setEnabled(true);
					savePicOfUpperPanelItem.setText("Save Image of Upper Panel...");
					copyLowerImageToClipboardItem.setEnabled(true);
					copyUpperImageToClipboardItem.setText(
							"Copy Image of Upper Panel to Clipboard");
					addToGreenhouseButton.setEnabled(false);
					dumpWorldItem.setEnabled(false);
					loadWorldItem.setEnabled(false);
					setDefaultSelectionSettings();
					break;
				case MOLECULAR_BIOLOGY:			
					clearSelectedOrganisms();
					compareMenu.setEnabled(true);
					turnOnSequenceClipboardItems();
					savePicOfLowerPanelItem.setEnabled(true);
					savePicOfUpperPanelItem.setText("Save Image of Upper Panel...");
					copyLowerImageToClipboardItem.setEnabled(true);
					copyUpperImageToClipboardItem.setText(
							"Copy Image of Upper Panel to Clipboard");
					addToGreenhouseButton.setEnabled(true);
					dumpWorldItem.setEnabled(false);
					loadWorldItem.setEnabled(false);
					setDefaultSelectionSettings();
					break;
				case EVOLUTION:
					clearSelectedOrganisms();
					evolutionWorkArea.clearSelection();
					compareMenu.setEnabled(false);
					turnOffSequenceClipboardItems();
					savePicOfLowerPanelItem.setEnabled(false);
					savePicOfUpperPanelItem.setText("Save Image of Panel...");
					copyUpperImageToClipboardItem.setText(
							"Copy Image of Panel to Clipboard");
					copyLowerImageToClipboardItem.setEnabled(false);
					addToGreenhouseButton.setEnabled(true);
					dumpWorldItem.setEnabled(true);
					loadWorldItem.setEnabled(true);
					setEvolutionSelectionSettings();
					break;
				}
			}
		});

		savePicOfUpperPanelItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (explorerPane.getSelectedIndex() == EVOLUTION) {
					saveSnapshotToFile(evolutionWorkArea.takeSnapshot());
				} else {
					saveSnapshotToFile(((Workbench)explorerPane
							.getSelectedComponent())
							.getUpperPanel().takeSnapshot());
				}
			}
		});

		savePicOfLowerPanelItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveSnapshotToFile((
						((Workbench)explorerPane.getSelectedComponent())
						.getLowerPanel().takeSnapshot()));
			}			
		});

		quitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveToFolder(greenhouse.getAll());
				FoldedProteinArchive.getInstance().saveArchiveToZipFile();
				System.exit(0);
			}
		});

		prefsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				preferencesDialog.setVisible(true);
			}
		});

		dumpWorldItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				evolutionWorkArea.saveWorldToFile();
			}
		});

		loadWorldItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				evolutionWorkArea.loadWorldFromFile();
			}
		});

		copyUpperSequenceToClipboardItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String selectedPane = 
						explorerPane.getSelectedComponent().getClass().toString();
				if (selectedPane.equals("class molBiol.MolBiolWorkbench")) {
					Clipboard c = 
							Toolkit.getDefaultToolkit().getSystemClipboard();
					StringSelection s = 
							new StringSelection(((MolBiolWorkpanel)molBiolWorkbench.getUpperPanel()).getDNA());
					c.setContents(s, null);
					return;
				}
				if (selectedPane.equals("class biochem.BiochemistryWorkbench")) {
					Clipboard c = 
							Toolkit.getDefaultToolkit().getSystemClipboard();
					StringSelection s = 
							new StringSelection(
									((BiochemistryWorkpanel)(biochemistryWorkbench.getUpperPanel())).getAaSeq());
					c.setContents(s, null);
					return;
				}
			}
		});

		copyLowerSequenceToClipboardItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String selectedPane = 
						explorerPane.getSelectedComponent().getClass().toString();
				if (selectedPane.equals("class molBiol.MolBiolWorkbench")) {
					Clipboard c = 
							Toolkit.getDefaultToolkit().getSystemClipboard();
					StringSelection s = 
							new StringSelection(((MolBiolWorkpanel)molBiolWorkbench.getLowerPanel()).getDNA());
					c.setContents(s, null);
					return;
				}
				if (selectedPane.equals("class biochem.BiochemistryWorkbench")) {
					Clipboard c = 
							Toolkit.getDefaultToolkit().getSystemClipboard();
					StringSelection s = 
							new StringSelection(
									((BiochemistryWorkpanel)(biochemistryWorkbench
											.getLowerPanel())).getAaSeq());
					c.setContents(s, null);
					return;
				}
			}
		});

		copyUpperImageToClipboardItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageForClipboard ifc = null;
				if (explorerPane.getSelectedIndex() == EVOLUTION) {
					ifc = new ImageForClipboard(evolutionWorkArea.takeSnapshot());
				} else {
					ifc = new ImageForClipboard(
							((Workbench)explorerPane
									.getSelectedComponent())
							.getUpperPanel().takeSnapshot());
				}
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ifc, null);
			}
		});

		copyLowerImageToClipboardItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageForClipboard ifc = new ImageForClipboard((
						((Workbench)explorerPane.getSelectedComponent())
						.getLowerPanel().takeSnapshot()));
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ifc, null);
			}			
		});

		// the compare menu items
		u_lMenuItem.addActionListener(new SequenceComparatorMenuItemListener(
				this, 
				SequenceComparator.UPPER,
				SequenceComparator.LOWER));

		u_sMenuItem.addActionListener(new SequenceComparatorMenuItemListener(
				this, 
				SequenceComparator.UPPER,
				SequenceComparator.SAMPLE));

		l_sMenuItem.addActionListener(new SequenceComparatorMenuItemListener(
				this, 
				SequenceComparator.LOWER,
				SequenceComparator.SAMPLE));

		u_cbMenuItem.addActionListener(new SequenceComparatorMenuItemListener(
				this, 
				SequenceComparator.UPPER,
				SequenceComparator.CLIPBOARD));

		l_cbMenuItem.addActionListener(new SequenceComparatorMenuItemListener(
				this, 
				SequenceComparator.LOWER,
				SequenceComparator.CLIPBOARD));

		addToGreenhouseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					saveSelectedOrganismToGreenhouse();
					//save changes to disk
					Object[] all = greenhouse.getAll();
					if (all.length == 0) return;
					saveToFolder(all);
				} catch (FoldingException e) {
					JOptionPane.showMessageDialog(null, 
							GlobalDefaults.paintedInACornerNotice,
							"Folding Error", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

	}

	public DNASequenceComparator getDNASequences() {
		String clipSeq = "";
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = c.getContents(null);
		if ((contents != null) && 
				(contents.isDataFlavorSupported(DataFlavor.stringFlavor))) {
			try {
				clipSeq = (String)contents.getTransferData(DataFlavor.stringFlavor);
			} catch (Exception e) {
				clipSeq = "";
			}

			// if not a DNA sequence
			Pattern p = Pattern.compile("[^AGCT]+");
			if (p.matcher(clipSeq).find()) {
				clipSeq = "";
			}
		}
		return new DNASequenceComparator(((MolBiolWorkpanel)molBiolWorkbench.getUpperPanel()).getDNA(),
				((MolBiolWorkpanel)molBiolWorkbench.getLowerPanel()).getDNA(),
				GlobalDefaults.sampleDNA,
				clipSeq);
	}

	public ProteinSequenceComparator getProteinSequences() {
		String clipSeq = "";
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = c.getContents(null);
		if ((contents != null) && 
				(contents.isDataFlavorSupported(DataFlavor.stringFlavor))) {
			try {
				clipSeq = (String)contents.getTransferData(DataFlavor.stringFlavor);
			} catch (Exception e) {
				clipSeq = "";
			}

			//try to see if it's a 3-letter code sequence
			if (clipSeq.indexOf(" ") != -1) {
				clipSeq = convert3LetterTo1Letter(clipSeq);
				if (clipSeq == null) {
					clipSeq = "";
				}
			}
			//maybe it's a single-letter aa seq
			Pattern p = Pattern.compile("[^ARNDCQEGHILKMFPSTWYV]+");
			if (p.matcher(clipSeq).find()) {
				clipSeq ="";
			} 
		}
		return new ProteinSequenceComparator(
				convert3LetterTo1Letter(
						((BiochemistryWorkpanel)
								(biochemistryWorkbench.getUpperPanel())).getAaSeq()),
				convert3LetterTo1Letter(
						((BiochemistryWorkpanel)
								(biochemistryWorkbench.getLowerPanel())).getAaSeq()),
				GlobalDefaults.sampleProtein,
				clipSeq);

	}

	public String convert3LetterTo1Letter(String aaSeq) {
		StandardTable table = new StandardTable();
		StringBuffer abAASeq = new StringBuffer();
		StringTokenizer st = new StringTokenizer(aaSeq);
		while (st.hasMoreTokens()){
			AminoAcid aa = table.get(st.nextToken());
			//if it can't be parsed as an amino acid, abort
			if (aa == null) {
				return null;
			}
			abAASeq.append(aa.getAbName());
		}
		return abAASeq.toString();
	}

	public MolBiolWorkbench getMolBiolWorkbench() {
		return molBiolWorkbench;
	}

	public BiochemistryWorkbench getBiochemistryWorkbench() {
		return biochemistryWorkbench;
	}

	public GeneticsWorkbench getGeneticsWorkbench() {
		return geneticsWorkbench;
	}

	public Greenhouse getGreenhouse() {
		return greenhouse;
	}

	public String getCurrentWorkingPanel() {
		return explorerPane.getSelectedComponent().getClass().toString();
	}

	public void saveToGreenhouse(Organism o) {
		greenhouse.add(o);
	}

	public EvolutionWorkArea getEvolutionWorkArea() {
		return evolutionWorkArea;
	}

	public void loadOrganismIntoActivePanel(Organism o) 
			throws PaintedInACornerFoldingException {
		String selectedPane = 
				explorerPane.getSelectedComponent().getClass().toString();

		if (selectedPane.equals("class molBiol.MolBiolWorkbench")) {
			molBiolWorkbench.loadOrganism(o);
		}

		if (selectedPane.equals("class biochem.BiochemistryWorkbench")) {
			biochemistryWorkbench.loadOrganism(o);
		}
	}

	// save the greenhouse
	public void saveToFolder(Object[] all) {
		
	}

	public void loadGreenhouse() throws FoldingException {

	}

	public void saveSelectedOrganismToGreenhouse() throws FoldingException {
		switch (explorerPane.getSelectedIndex()) {
		case GENETICS:
			//should not happen - only works if one org selected
			if ((oal1 == null) || (oal2 != null)) {
				return;
			}
			saveOrganismToGreenhouse(oal1.getOrganism());
			break;

		case BIOCHEMISTRY:
			// should not happen - button should be disabled
			break;

		case MOLECULAR_BIOLOGY:
			//need to express and fold the proteins
			molBiolWorkbench.saveOrganismToGreenhouse();
			break;

		case EVOLUTION:
			//get from world
			Organism o = evolutionWorkArea.getWorld().getSelectedOrganism();
			if (o != null) {
				saveOrganismToGreenhouse(o);
				evolutionWorkArea.getWorld().clearSelectedOrganism();
			}
			break;
		}

	}

	public void saveOrganismToGreenhouse(Organism o) throws FoldingException {

		String name = "";
		String warning = "";
		Pattern p = Pattern.compile("[^ A-Za-z0-9\\_\\-]+");
		while (name.equals("") || 
				p.matcher(name).find() ||
				greenhouse.nameExistsAlready(name)){
			name = JOptionPane.showInputDialog(
					this,
					warning +
					"Give a unique name for your new organism.\n"
					+ "This can only include letters, numbers, -, <space> and "
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
		saveToGreenhouse(organismFactory.createOrganism(name,o));
		clearSelectedOrganisms();
	}

	public void saveSnapshotToFile(BufferedImage bi) {
		// try to save to desktop, if possible
		String savePixToPath = "";
		File desktopFile = new File(System.getProperty("user.home") 
				+  System.getProperty("file.separator") 
				+ "Desktop");
		if (desktopFile.canWrite()) {
			savePixToPath = desktopFile.getAbsolutePath();
		} 

		JFileChooser fc = new JFileChooser(savePixToPath);
		int retVal = fc.showSaveDialog(this);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			String fileName = fc.getSelectedFile().getAbsolutePath();
			if (!fileName.endsWith(".png")) {
				fileName = fileName.concat(".png");
			}

			try {
				ImageIO.write(bi, "png", new File(fileName));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//handlers for selections of creatures in Genetics mode
	//  max of two at a time.
	//these are called by the CustomListSelectionMode
	public void deselectOrganism(OrganismAndLocation oal) {

		// only do this in genetics
		if (explorerPane.getSelectedIndex() != GENETICS) {
			return;
		}

		//remove from list of selected organisms
		//if #1 is being deleted, delete it and move #2 up
		if ((oal.getOrganism()).equals(oal1.getOrganism())) {
			oal1 = oal2;
			oal2 = null;
			updateGeneticsButtonStatus();
			return;
		}

		//otherwise just delete #2
		if ((oal.getOrganism()).equals(oal2.getOrganism())) {
			oal2 = null;
			updateGeneticsButtonStatus();
			return;
		}

		//should not get to here
		updateGeneticsButtonStatus();
		return;
	}

	public void addSelectedOrganism(OrganismAndLocation oal) {

		// only do this in genetics
		if (explorerPane.getSelectedIndex() != GENETICS) {
			return;
		}

		//if none selected so far, put it in #1
		if ((oal1 == null) && (oal2 == null)) {
			oal1 = oal;
			updateGeneticsButtonStatus();
			return;
		}

		// if only one selected so far, it should be in #1
		// so move #1 to #2 and put this in #1
		if ((oal1 != null) && (oal2 == null)) {
			oal2 = oal1;
			oal1 = oal;
			updateGeneticsButtonStatus();
			return;
		}

		//it must be that there are 2 selected orgs
		// so you have to drop #2, move 1 to 2 and put the
		// new one in 1.
		if ((oal1 != null) && (oal2 != null)) {
			//drop #2
			oal2.getListLocation().removeSelectionIntervalDirectly(oal2);
			//move 1 to 2
			oal2 = oal1;
			//put new one in 1
			oal1 = oal;
			updateGeneticsButtonStatus();
		}

		//should not get to here
		return;
	}

	public void clearSelectedOrganisms() {
		if (oal1 != null) {
			oal1.getListLocation().removeSelectionIntervalDirectly(oal1);
		}

		if (oal2 != null) {
			oal2.getListLocation().removeSelectionIntervalDirectly(oal2);
		}

		oal1 = null;
		oal2 = null;
		updateGeneticsButtonStatus();
	}

	public Organism getOrg1() {
		return oal1.getOrganism();
	}

	public Organism getOrg2() {
		return oal2.getOrganism();
	}


	//if no orgs selected - no buttons active;
	// if only one - save to greenhouse, mutate, and self are active;
	// if two - cross only
	public void updateGeneticsButtonStatus() {
		int numSelectedOrgs = 0;

		if (oal1 != null) {
			numSelectedOrgs++;
		}

		if (oal2 != null) {
			numSelectedOrgs++;
		}

		switch (numSelectedOrgs) {
		case 0:
			addToGreenhouseButton.setEnabled(false);
			geneticsWorkbench.setCrossTwoButtonsEnabled(false);
			geneticsWorkbench.setSelfCrossButtonsEnabled(false);
			geneticsWorkbench.setMutateButtonsEnabled(false);
			break;

		case 1:
			addToGreenhouseButton.setEnabled(true);
			geneticsWorkbench.setCrossTwoButtonsEnabled(false);
			geneticsWorkbench.setSelfCrossButtonsEnabled(true);
			geneticsWorkbench.setMutateButtonsEnabled(true);
			break;

		case 2:
			addToGreenhouseButton.setEnabled(false);
			geneticsWorkbench.setCrossTwoButtonsEnabled(true);
			geneticsWorkbench.setSelfCrossButtonsEnabled(false);
			geneticsWorkbench.setMutateButtonsEnabled(false);
			break;

		}
	}

	public void setDefaultSelectionSettings() {
		greenhouse.setDefaultSelectionSettings();
		geneticsWorkbench.setDefaultSelectionSettings();
	}

	public void setCustomSelectionSettings() {
		greenhouse.setCustomSelectionSettings();
		geneticsWorkbench.setCustomSelectionSettings();
	}

	public void setEvolutionSelectionSettings() {
		greenhouse.setEvolutionSelectionSettings();
		geneticsWorkbench.setDefaultSelectionSettings();
	}

	public void setAddToGreenhouseButtonEnabled(boolean b) {
		addToGreenhouseButton.setEnabled(b);
	}

	private void turnOnSequenceClipboardItems() {
		copyUpperSequenceToClipboardItem.setEnabled(true);
		copyLowerSequenceToClipboardItem.setEnabled(true);
	}

	private void turnOffSequenceClipboardItems() {
		copyUpperSequenceToClipboardItem.setEnabled(false);
		copyLowerSequenceToClipboardItem.setEnabled(false);
	}

	public void setStatusLabelText(String text) {
		statusLabel.setText(text);
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public JLabel getFoldingStatsLabel() {
		return foldingStatsLabel;
	}

	public void loadSelectedIntoWorld() {
		Organism[] orgs = greenhouse.getSelectedOrganisms();
		if (orgs != null) {
			evolutionWorkArea.getWorld().initialize(orgs);
			evolutionWorkArea.setGeneration(0);
			evolutionWorkArea.repaint();
			evolutionWorkArea.setReadyToRun();
		}
	}

	public void setButtonStatusWhileEvolving() {
		addToGreenhouseButton.setEnabled(false);
		fileMenu.setEnabled(false);
		explorerPane.setEnabled(false);
	}

	public void restoreButtonStatusWhenDoneEvolving() {
		addToGreenhouseButton.setEnabled(true);
		fileMenu.setEnabled(true);
		explorerPane.setEnabled(true);		
	}

}