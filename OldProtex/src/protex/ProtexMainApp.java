// ProtexMainApp.java
//
//
// Copyright 2004-2005 MGX Team UMB.  All rights reserved.
/*
 * License Information
 *
 * This  program  is  free  software;  you can redistribute it and/or modify it
 * under  the  terms  of  the GNU General Public License  as  published  by the
 * Free  Software  Foundation; either version 2  of  the  License,  or (at your
 * option) any later version.
 */
/*
 * The following organization of a public class is recommended by X. Jia [2004:
 * Object Oriented Software Development Using Java(TM). Addison Wesley, Boston,
 * 677 pp.]
 *
 * public class AClass { (public constants) (public constructors) (public
 * accessors) (public mutators) (nonpublic fields) (nonpublic auxiliary methods
 * or nested classes) }
 *
 * Jia also recommends the following design guidelines. *
 * 1. Avoid public fields. There should be no nonfinal public fields, except
 * when a class is final and the field is unconstrained. 2. Ensure completeness
 * of the public interface. The set of public methods defined in the class
 * should provide full and convenient access to the functionality of the class.
 * 3. Separate interface from implementation. When the functionality supported
 * by a class can be implemented in different ways, it is advisable to separate
 * the interface from the implementation.
 *
 * Created:  09 Feb 2005 (Namita Singla/MGX Team UMB)
 * Modified: 26 May 2005 (David Portman/MGX Team UMB)
 * Modified: 24 May 2005 (Namita Singla/MGX Team UMB)
 */

package protex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import java.net.URL;

import java.util.Iterator;
import java.util.Vector;

import javax.imageio.ImageIO;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * Driver for Protex Student GUI.
 *
 * @author Namita Singla/MGX Team UMB
 * @author David Portman/MGX Team UMB
 * @version 1.0 $Id: ProtexMainApp.java
 */
public class ProtexMainApp extends JFrame implements FoldingObserver {
	/////////////////////////////////////////////////////////////////
	// variable declaration here
	/////////////////////////////////////////////////////////////////

	// public fields

	public int SLIDER_INIT = 0;
	public static StringBuffer buffer = new StringBuffer();


        // non-public fields

        private static boolean blackColoring = false;
        private static FoldingManager manager;
        private static JFrame frame;

        // workspace directory and sub-directories
        protected static File rootDir = null;
        protected static File cacheDir = null;
//        protected static File problemsDir = null;
        protected static String slant = "/";

        // menu bar stuff
        private JMenuItem m_OpenItem;
//        private JMenuItem m_SaveItem;
//        private JMenuItem m_SaveAsItem;
//        private JMenuItem m_CloseItem;
        private JMenuItem m_ExitItem;
        private JMenuItem m_FoldItem;
        private JMenuItem m_DiffItem;
        private JMenuItem m_HelpItem;
        private JMenuItem m_AboutItem;

        // tool bar stuff
        private JToolBar m_ToolBar;
        private JButton m_OpenButton;
//        private JButton m_CloseButton;
        private JButton m_ExitButton;
//        private JButton m_SaveButton;
//        private JButton m_SaveAsButton;
        private JButton m_AboutButton;
        private JButton m_HelpButton;
        private JButton m_DiffButton;

        // IOPanel stuff
        private JButton editButton;
        private JButton clearButton;
        private JButton colorButton;
        private JPanel inputPanel, outputPanel;
        protected static IOPanel upperIOPanel;
        protected static IOPanel lowerIOPanel;
        protected static IOPanel activeIOPanel;

        // HistoryList stuff
        private static File imageFile;
        private static HistoryList histList;
        private BufferedImage dnaImage;
        private static FileOutputStream imageOutput;
        private static Polypeptide ppFromHistory;
        
        // added by TJ -- for closing window
        private CloseMe windowCloser;
        
//        // added by TJ -- for mixing 2 protein color
//        private RYBColorChart colorChart;
        protected static JLabel mixedColorLabel;


////////////////////////////////////////////////////////////////
// constructor definition here
// Construct the Protex-panel
/////////////////////////////////////////////////////////////////

        // public methods

	/**
	 * Constructor
	 */
    public ProtexMainApp() {
    	setTitle("Protex Explore");
    	setBackground(Color.white);
    	init();
		
//    	done by TJ -- program window closing
    	setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    	windowCloser = new CloseMe();
    	this.addWindowListener( windowCloser );

		// TODO force JFrame to fill screen
		// For right now, just center the frame
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screenSize.width,
				(int)(screenSize.height * 0.95));

		pack();
//		this.setVisible(true);
	}

/////////////////////////////////////////////////////////////////
// method name: init()
//        type: public
//   called by: constructor
//    comments: sets up left-panel (history + amino acide panel)
//              Sets up right-panel
//              creates menu bar
//              creates tool bar
/////////////////////////////////////////////////////////////////
	/**
	 * Initialize components
	 *
	 */
	public void init() {
//		// added by TJ -- for mixing 2 colors
//		colorChart = new RYBColorChart();
		
		getContentPane().setLayout(new BorderLayout());
		manager = FoldingManager.getInstance();
		manager.setBlackColoring(blackColoring);
		manager.attach(this);

		// create the menu and tool bars
		createMenuBar();
		createToolBar();

		// add the viewing components
		getContentPane().add(m_ToolBar, BorderLayout.NORTH);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				createLeftPanel(), createRightPanel());
		splitPane.setOneTouchExpandable(false);
		getContentPane().add(splitPane, BorderLayout.CENTER);

		// set defaults
		activeIOPanel = upperIOPanel;
		setActiveIOPanelBorder();

		// create workspace on disk
		createWorkspace();
	}

/////////////////////////////////////////////////////////////////
// method name: foldChain
//        type: public
//   called by: EventHandler.java
//              IOPanel.java
//    comments: Fold the chain of AminoAcid objects currently
//              displayed in the InputPalette
/////////////////////////////////////////////////////////////////
	/*
	 * Fold the chain of AminoAcid objects currently displayed in the
	 * InputPalette
	 *
	 * @param pp Polypeptide.
	 */
	public static void foldChain(Polypeptide pp) {

		// clear OutputPalette of activeIOPanel
		activeIOPanel.clearOutputPalette();

		// access the InputPalette of activeIOPanel
		String bs = " ";
		JPanel inputPanel = activeIOPanel.getInputPanel();
		Vector list = ((InputPalette) inputPanel).getAminoAcids();

		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			AminoAcid acid = (AminoAcid) itr.next();
			bs = bs + acid.getName() + ":";
		}

		// instantiate the Attributes of this Polypeptide
		String ppId = null;
		Polypeptide ppFH = null;
		if (pp != null) {
			ppId = pp.getId();
			if (pp.isInHistory())
				ppFH = pp;
		}
		Attributes attrib = new Attributes(bs, "straight", ppId, ppFH);

		// fold and display the Polypeptide chain (a protein)
		try {
			manager.fold(attrib);
		} catch (FoldingException ex) {
			ex.printStackTrace();
		}

		// paint folded Polypeptide in the activeIOPanel's OutputCanvasPanel
		OutputPalette outputPanel =
			(OutputPalette) activeIOPanel.getOutputCanvasPanel();
		manager.createCanvas((OutputPalette) outputPanel);
		outputPanel.repaint();

		// display in activeIOPanel's buttonPanel the color associated with
		//	the folded Polypeptide
		Color proteinColor = ((OutputPalette) outputPanel).getProteinColor();
		activeIOPanel.getColorButton().setBackground(proteinColor);
		
		// mix the color of the two IOPanel and display the color of the creature
		// -- added by TJ
		Color c = mixProteinColors();
		if(c == null){
			mixedColorLabel.setBackground(IOPanel.offColor);
		}
		else{
			mixedColorLabel.setBackground(c);
		}
		mixedColorLabel.repaint();

		// retrieve the newly folded Polypeptide;
		// 	store its proteinColor
		Polypeptide newPp = manager.getPolypeptide();
		if (pp == null) {
			newPp.setId(null);
			newPp.setInHistoryTo(false);
			newPp.setUHL(null, null, null);
		}
		else {
			newPp.setId(pp.getId());
			newPp.setInHistoryTo(pp.isInHistory());
			newPp.setUHL(pp.getUHL());
		}
		pp = newPp;
		pp.setColor(proteinColor);

		// if this Polypeptide is not yet in History, add it to History
		inputPanel = activeIOPanel.getInputPanel();
		if (ppFromHistory == null ||
				((InputPalette) inputPanel).getIsChanged()) {
			pp = addToHistory(pp);
		}

		// set a reference to this Polypeptide in the activeIOPanel
		activeIOPanel.setPolypeptide(pp);

		// XXX ERROR CHECK
		if (FoldingManager.getInstance().DEBUG) {
			System.out.println("\nProtexMainApp.foldChain()-- ");
			if (pp != null) {
				System.out.println(pp.toReport());
			} else {
				System.out.println("\n\tPolypeptide: Null Pointer Encountered.");
			}
		}
	}

	/**
	 * I'm done folding. Now report statistics.
	 *
	 */
	public void doneFolding(Attributes attrib) {
	    if (FoldingManager.getInstance().DEBUG)
	        System.out.println(manager.report());
	}

	/**
	 * Modifies the appearances of active and inactive IOPanel objects.
	 *
	 */
	public static void setActiveIOPanelBorder() {
		if (activeIOPanel.equals(upperIOPanel)) {
			upperIOPanel.setToActive();
			lowerIOPanel.setToInActive();
		}
		else if (activeIOPanel.equals(lowerIOPanel)) {
			lowerIOPanel.setToActive();
			upperIOPanel.setToInActive();
		}
		else {
			System.out.print("\nProtexMainApp.setActiveIOPanelBorder: ");
			System.out.println("ERROR -- You should never get here!");
		}
	}

	/**
	 * For debugging purposes (mostly).
	 *
	 * @return String name of this class (i.e., ProtexMainApp).
	 */
	public String toString() {
		return "ProtexMainApp";
	}
// Added by TJ -- for resetting active IOPanel
	public static void resetIOPanel(IOPanel activeIOPanel){
		ProtexMainApp.activeIOPanel = activeIOPanel;
		ProtexMainApp.setActiveIOPanelBorder();
		activeIOPanel.getKeyboardFocus(); // added by TJ -- shift focus to active inputPanel
	}
	
// Added by TJ -- for resetting the mixedColorLabel
	public static void resetMixedColorLabel(){
		mixedColorLabel.setBackground(IOPanel.offColor);
		mixedColorLabel.repaint();
	}
	
	
/////////////////////////////////////////////////////////////////
// method name: main
//        type: commented
//    comments: The protex in not a stand alone app now (mar2006).
//              It gets invoked via WRAPPER
//              By commenting main method we made sure that it
//              is not invoked
/////////////////////////////////////////////////////////////////

	/**
	 * Main entry point
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String nativeLF = UIManager.getSystemLookAndFeelClassName();
			UIManager.setLookAndFeel(nativeLF);
		} catch (Exception e) {
			e.printStackTrace();
		}
		frame = new ProtexMainApp();
		JFrame.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.updateComponentTreeUI(frame);
		frame.setVisible(true);
	}

/////////////////////////////////////////////////////////////////
// class  name: HListListener
//        type: private
//    comments: somebody has to take care of whatever we add to
//              protex history. This class is basically a
//              listener of change to history section and takes
//              appropiate action.
/////////////////////////////////////////////////////////////////
	/*
	 * A List selection listener for the elements that are present in
	 * DefaultListModel. HList uses DefaultListModel to add the elements.
	 *
	 * @author Pradeep Kadiyala
	 */
	private class HListListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent event) {
			if (!event.getValueIsAdjusting()) {
				PolypeptidePanel ppPanel = (PolypeptidePanel) histList
						.getSelectedValue();
			}
		}
	}

	// non-public methods

/////////////////////////////////////////////////////////////////
// method name: createMenuBar
//        type: commented
//    comments: protex in not a stand alone app now (mar2006).
//              It gets invoked via WRAPPER
//              The Wrapper-Frame has its own menu.
//              By becoming a part of Wrapper, Protex lost its
//              menu. But worry not because all the functionality
//              of menu-bar is now available as buttons.
/////////////////////////////////////////////////////////////////
	/*
	 * Create and load menu bar.
	 */
	private void createMenuBar() {
		JMenuBar mnuBar = new JMenuBar();

		// define image icons
		Class c = ProtexMainApp.class;
		URL openImageURL = c.getResource("images/open16.gif");
//		URL saveAsImageURL = c.getResource("images/saveas16.gif");
//		URL saveImageURL = c.getResource("images/save16.gif");
		URL aboutImageURL = c.getResource("images/about16.gif");
		URL helpImageURL = c.getResource("images/onlinehelp16.gif");
//		URL closeImageURL = c.getResource("images/closework16.gif");
		URL diffImageURL = c.getResource("images/diff.gif");  // by va

		ImageIcon openImage = new ImageIcon(openImageURL);
//		ImageIcon saveAsImage = new ImageIcon(saveAsImageURL);
//		ImageIcon saveImage = new ImageIcon(saveImageURL);
		ImageIcon aboutImage = new ImageIcon(aboutImageURL);
		ImageIcon helpImage = new ImageIcon(helpImageURL);
//		ImageIcon closeImage = new ImageIcon(closeImageURL);
		ImageIcon diffImage = new ImageIcon(diffImageURL);  // by va
		
		//  "File" options.
		JMenu mnuFile = new JMenu("File");
		m_OpenItem = createMenuItem("Open", "Open", openImage);
//		m_SaveItem = createMenuItem("Save", "Save", saveImage);
//		m_SaveAsItem = createMenuItem("Save As...", "SaveAs", saveAsImage);
//		m_CloseItem = createMenuItem("Close", "Close", closeImage);
		m_ExitItem = createMenuItem("Exit", "Exit", null);

		mnuFile.add(m_OpenItem);
		mnuFile.addSeparator();
//		mnuFile.add(m_SaveItem);
//		mnuFile.add(m_SaveAsItem);
//		mnuFile.addSeparator();
//		mnuFile.add(m_CloseItem);
//		mnuFile.addSeparator();
		mnuFile.add(m_ExitItem);
		mnuBar.add(mnuFile);

		//  "Utilities" options.
		JMenu mnuUtilities = new JMenu("Utilites");
//va :1----------------------
		m_DiffItem = createMenuItem("Difference", "Diff",diffImage);
//va :1----------------------
		mnuUtilities.add(m_DiffItem);
		mnuBar.add(mnuUtilities);

		//  "Help" options.
		JMenu mnuHelp = new JMenu("Help");
		m_HelpItem = createMenuItem("Help", "Help", helpImage);
		mnuHelp.add(m_HelpItem);
		m_AboutItem = createMenuItem("About Protex", "About", aboutImage);
		mnuHelp.add(m_AboutItem);
		mnuBar.add(mnuHelp);

		// enable/disable menu bar options as needed
		m_OpenItem.setEnabled(true);
//		m_SaveItem.setEnabled(false);
//		m_SaveAsItem.setEnabled(false);
//		m_CloseItem.setEnabled(false);
//va :4----------------------
		m_DiffItem.setEnabled(true);
//va :4----------------------
		m_HelpItem.setEnabled(true);
		m_AboutItem.setEnabled(true);

		setJMenuBar(mnuBar);
	}

	/*
	 * Create a MenuItem for a Menu of the MenuBar.
	 *
	 * @param string @param string2 @param openImage @return
	 */
	private JMenuItem createMenuItem(String label, String actionCommand,
			ImageIcon image) {
		JMenuItem result = new JMenuItem(label);
		result.setActionCommand(actionCommand);
		result.setIcon(image);
		result.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				dispatch(evt);
			}
		});
		return result;
	}


	/*
	 * Create and load toolbar
	 */
	private void createToolBar() {
		m_ToolBar = new JToolBar();

		// define image URLs
		Class c = ProtexMainApp.class;
		URL openImageURL = c.getResource("images/new.gif");
//		URL saveAsImageURL = c.getResource("images/saveas.gif");
//		URL saveImageURL = c.getResource("images/save.gif");
		URL aboutImageURL = c.getResource("images/about.gif");
		URL helpImageURL = c.getResource("images/onlinehelp.gif");
//		URL closeImageURL = c.getResource("images/closework.gif");
		URL diffImageURL = c.getResource("images/diff.gif");
		URL exitImageURL = c.getResource("images/exit.gif");

		// specify icon images
		ImageIcon openImage = new ImageIcon(openImageURL);
//		ImageIcon saveAsImage = new ImageIcon(saveAsImageURL);
//		ImageIcon saveImage = new ImageIcon(saveImageURL);
		ImageIcon aboutImage = new ImageIcon(aboutImageURL);
		ImageIcon helpImage = new ImageIcon(helpImageURL);
//		ImageIcon closeImage = new ImageIcon(closeImageURL);
		ImageIcon diffImage = new ImageIcon(diffImageURL);
		ImageIcon exitImage = new ImageIcon(exitImageURL);

		// define text messages for "balloon help"
		String openButtonText = "Open.";
//		String closeButtonText = "Close.";
		String exitButtonText = "Exit.";
//		String saveButtonText = "Save problem.";
//		String saveAsButtonText = "Save problem as a new file.";
		String diffButtonText = "See the differences between two polypeptide chains.";
		String aboutButtonText = "Display information about Protex.";
		String helpButtonText = "Display Protex Help.";

		// build toolbar buttons
		m_OpenButton = JButtonImageItem(openImage, "Open", openButtonText,
				KeyEvent.VK_O);
//		m_CloseButton = JButtonImageItem(closeImage, "Close", closeButtonText,
//				KeyEvent.VK_L);
		m_ExitButton = JButtonImageItem(exitImage, "Exit", exitButtonText,
				KeyEvent.VK_E);
//		m_SaveButton = JButtonImageItem(saveImage, "Save", saveButtonText,
//				KeyEvent.VK_S);
//		m_SaveAsButton = JButtonImageItem(saveAsImage, "Save As...",
//				saveAsButtonText, KeyEvent.VK_V);
		m_DiffButton = JButtonImageItem(diffImage, "Diff", diffButtonText,
				KeyEvent.VK_D);
		m_AboutButton = JButtonImageItem(aboutImage, "About", aboutButtonText,
				KeyEvent.VK_A);
		m_HelpButton = JButtonImageItem(helpImage, "Help", helpButtonText,
				KeyEvent.VK_H);

		// add buttons to toolbar
		m_ToolBar.add(m_OpenButton);
//		m_ToolBar.add(m_CloseButton);
		m_ToolBar.add(m_ExitButton);
//		m_ToolBar.add(m_SaveButton);
//		m_ToolBar.add(m_SaveAsButton);
		m_ToolBar.add(m_DiffButton);
		m_ToolBar.add(m_HelpButton);
		m_ToolBar.add(m_AboutButton);

		// enable/disable toolbar buttons as needed
		m_OpenButton.setEnabled(true);
//		m_CloseButton.setEnabled(false);
//		m_SaveButton.setEnabled(false);
//		m_SaveAsButton.setEnabled(false);
//va :5----------------------
//		m_DiffButton.setEnabled(false);
		m_DiffButton.setEnabled(true);
//va :5----------------------
		m_HelpButton.setEnabled(true);
		m_AboutButton.setEnabled(true);

		m_ToolBar.setRollover(true);
	}

	/*
	 * Build a toolbar JButton.
	 *
	 * @param buttonImage @param actionCommand @param toolTipText @param
	 * keyEvent @return
	 */
	private JButton JButtonImageItem(ImageIcon buttonImage,
			String actionCommand, String toolTipText, int keyEvent) {
		JButton result = new JButton(buttonImage);
		result.setActionCommand(actionCommand);
		result.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				dispatch(evt);
			}
		});
		if (toolTipText != null)
			result.setToolTipText(toolTipText);
		result.setMnemonic(keyEvent);
		return result;
	}

	/*
	 * Dispatch registered events to EventHandler class for action.
	 *
	 * @param evt ActionEvent.
	 */
	private void dispatch(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		update(getGraphics());
		if (cmd.equals("Open"))
			//EventHandler.openProblem();
			EventHandler.openDefault(); // temporary, for debugging purposes
//		else if (cmd.equals("Save"))
//			EventHandler.saveProblem();
//		else if (cmd.equals("SaveAs"))
//			EventHandler.saveAsProblem();
//		else if (cmd.equals("Close"))
//			EventHandler.closeProblem();
		else if (cmd.equals("Diff"))
			EventHandler.displayDiff();
		// modified by TJ -- use closeMe to close the application
		else if (cmd.equals("Exit"))
//			EventHandler.exitMainApp();
			windowCloser.actionPerformed(evt);
		else if (cmd.equals("About"))
			EventHandler.displayAbout();
		else if (cmd.equals("Help"))
		    EventHandler.displayHelp(this.getToolkit());
	}

	/*
	 * Create panel holding AminoAcids.
	 *
	 * @return JPanel
	 */
	private JPanel createPalettePanel() {
		JPanel palettePanel = new JPanel();
		palettePanel.setBorder(BorderFactory
				.createTitledBorder("AminoAcid Palette"));
		AminoAcidPalette palette = new AminoAcidPalette(225, 180, 4, 5, false);
		palettePanel.setBackground(IOPanel.offColor);
		palettePanel.add(palette);
		return palettePanel;
	}


	/*
	 *
	 * @return JPanel
	 */
	private JPanel createHistoryPanel() {
		JPanel historyPanel = new JPanel();
//va:B                historyPanel.setBorder(BorderFactory.createTitledBorder("History"));
                historyPanel.setBorder(BorderFactory.createTitledBorder("Protex-History"));
		historyPanel.setBackground(IOPanel.offColor);
		histList = new HistoryList(new DefaultListModel(),this.getToolkit());
		historyPanel.add(histList.getHistListScrollPane(), BorderLayout.CENTER);
		return historyPanel;
	}

	/*
	 * Create JPanel on left-hand side of JFrame.
	 *
	 * @return JPanel
	 */
	private JPanel createLeftPanel() {
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(createPalettePanel(), BorderLayout.NORTH);
		leftPanel.add(createHistoryPanel(), BorderLayout.CENTER);
		return leftPanel;
	}

	/*
	 * Create JPanel on right-hand side of JFrame.
	 *
	 * @return JPanel
	 * -- modified by TJ
	 */
	private JPanel createRightPanel() {
		JPanel rightPanel = new JPanel(new BorderLayout());
		JPanel topPanel = new JPanel(new BorderLayout());
		mixedColorLabel = new JLabel();
		mixedColorLabel.setBorder(BorderFactory.createTitledBorder("Combined Color"));
		mixedColorLabel.setOpaque(true);
		mixedColorLabel.setBackground(IOPanel.offColor);
		topPanel.add(mixedColorLabel, BorderLayout.CENTER);
		rightPanel.add(topPanel, BorderLayout.NORTH);
		JPanel IOPanel = new JPanel(new BorderLayout());
		upperIOPanel = new IOPanel("Upper Work Panel");
//		rightPanel.add(upperIOPanel, BorderLayout.NORTH);
		IOPanel.add(upperIOPanel, BorderLayout.NORTH);
		lowerIOPanel = new IOPanel("Lower Work Panel");
//		rightPanel.add(lowerIOPanel, BorderLayout.CENTER);
		IOPanel.add(lowerIOPanel, BorderLayout.CENTER);
		rightPanel.add(IOPanel, BorderLayout.CENTER);
		return rightPanel;
	}

	/*
	 * Build a a directory structure on disk, which will be used to hold images
	 * and other output of this application.
	 *
	 */
	private void createWorkspace() {

		rootDir = new File("Protex");
		if (!rootDir.exists()) {
			rootDir.mkdir();

			if (FoldingManager.getInstance().DEBUG) {
				System.out.println("\nProtexMainApp.createWorkspace(): "
						+ "Creating a workspace root directory "
						+ rootDir.toString());

				System.out.println("\nProtexMainApp.createWorkspace(): "
						+ "Path is " + rootDir.getPath().toString());
			}
		}

		if (FoldingManager.getInstance().DEBUG) {
			try {
				System.out.println("\nProtexMainApp.createWorkspace(): "
						+ "Operating system is "
						+ System.getProperty("os.name").toString());

				System.out.println("\nProtexMainApp.createWorkspace(): "
						+ "Absolute path is "
						+ rootDir.getAbsolutePath().toString());

				System.out.println("\nProtexMainApp.createWorkspace(): "
						+ "Canonical path is "
						+ rootDir.getCanonicalPath().toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// determine filesystem separator: is this Windows?
		if (System.getProperty("user.dir").indexOf('/') == -1)
			slant = "\\";

		// create workspace sub-directory: Cache
		cacheDir = new File(rootDir.getPath() + slant + "Cache");
		if (!cacheDir.exists()) {
			cacheDir.mkdir();

			if (FoldingManager.getInstance().DEBUG) {
				System.out.println("\nProtexMainApp.createWorkspace(): "
						+ "Creating a workspace cache directory "
						+ cacheDir.toString());

				System.out.println("\nProtexMainApp.createWorkspace(): "
						+ "Path is " + cacheDir.getPath().toString());
			}
		}

//		// create workspace sub-directory: Problems
//		problemsDir = new File(rootDir.getPath() + slant + "Problems");
//		if (!problemsDir.exists()) {
//			problemsDir.mkdir();
//
//			if (FoldingManager.getInstance().DEBUG) {
//				System.out.println("\nProtexMainApp.createWorkspace(): "
//						+ "Creating a workspace problems directory "
//						+ problemsDir.toString());
//
//				System.out.println("\nProtexMainApp.createWorkspace(): "
//						+ "Path is " + problemsDir.getPath().toString());
//			}
//		}
	}

	/*
	 * Create an image of the displayed outputPanel.
	 *
	 * @author Ruchi Dubey for GeneX @author Modified by David Portman for GeneX
	 * on 31 Mar 2005. @author Modified by David Portman for Protex on 03 Apr
	 * 2005.
	 *
	 * @param imgName String Name of this (full-size) image. @return
	 */
	private static BufferedImage saveAsImage(String imgName) {

		// create FileOutputStream for file holding full-size image
		try {
			imageFile = new File(imgName);
			if (imageFile.exists()) {
				imageFile.delete();
			}
			imageOutput = new FileOutputStream(imageFile);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		// define the full-size image as a Graphics2D object
		BufferedImage image =
			new BufferedImage(400, 230, BufferedImage.TYPE_INT_RGB);

		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		OutputPalette oP =
			(OutputPalette) activeIOPanel.getOutputCanvasPanel();
		oP.getDrawingPane().paint(g);
		g.dispose();

		// write the Graphics2D image on disk
		try {
			ImageIO.write(image, "jpeg", imageOutput);
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		return image;
	}

	/*
	 * Mixes the two colors associated with two Polypeptide chains.
	 *
	 * @return Color
	 * 
	 * modified by TJ --  bugs fixed
	 */
	private static Color mixProteinColors() {
//		OutputPalette canvas1 = (OutputPalette) upperIOPanel
//				.getOutputCanvasPanel();
//		OutputPalette canvas2 = (OutputPalette) lowerIOPanel
//				.getOutputCanvasPanel();
		Color upperIOPanelColor = upperIOPanel.getColorButton().getBackground();
		Color lowerIOPanelColor = lowerIOPanel.getColorButton().getBackground();
		Color c = null;
		// if upperIOPanel or lowerIOPanel has no color, don't mix
		if(upperIOPanelColor.equals(IOPanel.offColor) ||
				lowerIOPanelColor.equals(IOPanel.offColor)){
			return null;
		}
		try {
//			c = RYBColorChart.getRYBColorChart().mixTwoColors(canvas1.getProteinColor(),
//					canvas2.getProteinColor());
			c = RYBColorChart.getRYBColorChart().mixTwoColors(upperIOPanelColor, lowerIOPanelColor);
		} catch (Exception ex) {
//		    if (FoldingManager.getInstance().DEBUG)
			     System.out.println("One of the two proteins is missing: " + ex);
			     ex.printStackTrace();
//			     if(!RYBColorChart.isInitialized()){
//			     	System.out.println("color chart not initialized");
//			     }
		}

		// XXX ERROR CHECK
	    if (FoldingManager.getInstance().DEBUG)
			System.out.println("Color:" + c);

	    System.out.println("Color:" + c);
		return c;
	}

	/*
	 * Add a Polypeptide (Folded Protein) to History.
	 *
	 * @param pp Polypeptide.
	 * @return Polypepetide.
	 */
	private static Polypeptide addToHistory(Polypeptide pp) {
		// assign a String id to this Polypeptide
		int id = 1 + FoldingManager.getInstance().getLastPPId();
		pp.setId(FoldingManager.assignPPId(id));
		FoldingManager.getInstance().setLastPPId(id);

		// assign names of files holding
		//	- full-size image
		//	- thumbnail-size image
		String imgName = cacheDir + slant + "fullsize_" + id + ".jpeg";
		String thmbName = cacheDir + slant + "thmbnail_" + id + ".jpeg";

		// create the full-size image first
		BufferedImage fullsize = saveAsImage(imgName);

		// create the thumbnail-size image next
		ImageIcon thumbnail = Thumbnail.createThumbnail(fullsize, thmbName);

		// set flag for work-panel location of this Polypeptide
		if (activeIOPanel.equals(ProtexMainApp.upperIOPanel)) {
			pp.setInUpperIOPanelTo(true);
		}
		else if (activeIOPanel.equals(ProtexMainApp.lowerIOPanel)) {
			pp.setInLowerIOPanelTo(true);
		}
		else {
			System.out.println("\nProtexMainApp.doneFolding(): "
					+ "ERROR - You should never get here.");
		}

		// put full-size image, thumbnail and Polypeptide in
		//  PolypeptidePanel; add to the HistoryPanel
		PolypeptidePanel ppp = new PolypeptidePanel(fullsize, thumbnail, pp);
		pp = histList.add(ppp);

		// register the work panel locations of this Polypeptide
		registerReferences(pp);
		return pp;
	}

	/*
	 * Make sure that a Polypeptide (Folded Protein) is properly registered in
	 * in the History.
	 *
	 * @param pp Polypeptide.
	 */
	private static void registerReferences(Polypeptide pp) {
		PolypeptidePanel pppFromHistory =
			(PolypeptidePanel) histList.getHistListModel().lastElement();
		ppFromHistory = (Polypeptide) pppFromHistory.getPolypeptide();
		pp.setUHL(ppFromHistory);

		// set a reference to this Polypeptide in the activeIOPanel
		activeIOPanel.setPolypeptide(pp);
	}
	
	 /**
     * Program window closing.
     *
     * Exit from File menu and [x] from window should each
     * delete the contents in cache dir.
     * -- added by TJ
     */
	private class CloseMe extends WindowAdapter 
	implements ActionListener 
	{
		public	void windowClosing(WindowEvent e) 
		{
			this.actionPerformed(null);
		}

		public void actionPerformed( ActionEvent e )
		{
			int result = JOptionPane.showConfirmDialog(null,
					"Do you want to exit Protain Explorer?", "Exit?",
					JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION){
				// clear both IOPanels
				upperIOPanel.clearAll();
				lowerIOPanel.clearAll();
				// clear History list
				histList.clear();
				// remove all contents in cache
				if(cacheDir != null){
					File[] fileList = cacheDir.listFiles();
					if(fileList != null){
						for(int i = 0; i < fileList.length; i++){
							boolean isSuccessful = fileList[i].delete();
							if(!isSuccessful){
//								if(fileList[i].isFile()){
//									System.out.println(fileList[i] + " is a file");
//								}
//								if(fileList[i].exists()){
//									System.out.println(fileList[i] + " exists");
//								}
								System.out.println(fileList[i] + " cannot be deleted!");
							}
//							else{
//								System.out.println(fileList[i] + " is deleted!");
//							}
						}
					}
				}
				System.exit(1);
			}
		}
	}
}
