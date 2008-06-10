package VGL;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;

import GeneticModels.Organism;

/**
 * Nikunj Koolar cs681-3 Fall 2002 - Spring 2003 Project VGL File:
 * Brian White Summer 2008
 * VGLII.java - the UI controller class. Its the heart of almost all UI
 * renditions and manipulations.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * @author Nikunj Koolar & Brian White
 * @version 1.0 $Id$
 */
public class VGLII extends JFrame {


	/**
	 * The program id for the application if none is provided
	 */
	private static final String m_DefaultId = "Virtual Genetics Lab Applet";

	/**
	 * The common file chooser instance for the application
	 */
	private JFileChooser m_FChooser;
	
	/**
	 * the server script for saving to server
	 */
	private static final String m_SaveToServerScript = "http://intro.bio.umb.edu/cgi-bin/saveWork.pl";
	
	/**
	 * script on server to get list of sections available for saving work
	 */
	private static final String m_GetSectionListScript = "http://intro.bio.umb.edu/cgi-bin/getSectionList.pl";

	/**
	 * The collection of Cage UIs associated with the current problem
	 */
	private ArrayList m_CageCollection;

	/**
	 * The id of the next cage that will be created
	 */
	private int m_NextCageId = 0;

	/**
	 * The mode in which to open/create an existing/new problem if the mode is
	 * "show underlying model" then its value is true, false otherwise
	 */
	private boolean m_IsBeginner = false;

	/**
	 * The singular instance that holds the current male-female selection for
	 * crossing
	 */
	private SelectionVial m_SelectionVial;

	/**
	 * This widget holds the buttons
	 */
	private JToolBar m_ToolBar = null;

	/**
	 * The label for the status panel to display information
	 */
	private JLabel m_StatusLabel = null;

	/**
	 * The Document renderer to allow printing
	 */
	private DocumentRenderer docRenderer;

	/**
	 * The filter type to display only Problem type files
	 */
	private static final String m_PrbFilterString = new String("prb");

	/**
	 * The filter type to display only Work files
	 */
	private static final String m_WrkFilterString = new String("wrk");

	/**
	 * The filter type to display Print files
	 */
	private static final String m_PrtFilterString = new String("html");

	/**
	 * Menu item to open a new problem type
	 */
	private JMenuItem m_NewProblemItem = null;

	/**
	 * Menu item to open a saved problem
	 */
	private JMenuItem m_OpenProblemItem = null;

	/**
	 * Menu item to save current work to a file
	 */
	private JMenuItem m_SaveProblemItem = null;

	/**
	 * Menu item to save current work to a different file than the current one
	 */
	private JMenuItem m_SaveProblemAsItem = null;
	
	/**
	 * Meu item to save current work to server
	 */
	private JMenuItem m_SaveToServerItem = null;

	/**
	 * Menu item to close the current work
	 */
	private JMenuItem m_CloseProblemItem = null;

	/**
	 * Menu item to close application
	 */
	private JMenuItem m_ExitItem = null;

	/**
	 * Menu item to cross two organisms
	 */
	private JMenuItem m_CrossTwoItem = null;

	/**
	 * Menu item to display "about VGL" box
	 */
	private JMenuItem m_AboutItem = null;

	/**
	 * Checkbox based menu item to set balloon help on/off
	 */
	private JCheckBoxMenuItem m_BalloonHelpItem = null;

	/**
	 * Menu item to invoke cage manager dialog
	 */
	private JMenuItem m_CageManagerItem = null;

	/**
	 * Menu item to print current work to file
	 */
	private JMenuItem m_PrintToFileItem = null;

	/**
	 * Menu item to print current work
	 */
	private JMenuItem m_PrintItem = null;

	/**
	 * Menu item to set up the printing page
	 */
	private JMenuItem m_PageSetupItem = null;

	/**
	 * Menu item to invoke help
	 */
	private JMenuItem m_OnlineHelpItem = null;

	/**
	 * Menu item to re-arrange cages
	 */
	private JMenuItem m_RearrangeCagesItem = null;

	/**
	 * Button to open a saved problem
	 */
	private JButton m_OpenButton = null;

	/**
	 * Button to open a new problem type
	 */
	private JButton m_NewButton = null;

	/**
	 * Button to close the current work
	 */
	private JButton m_CloseButton = null;

	/**
	 * Button to print the current work
	 */
	private JButton m_PrintButton = null;

	/**
	 * Button to exit application
	 */
	private JButton m_ExitButton = null;

	/**
	 * Button to save current work
	 */
	private JButton m_SaveButton = null;
	
	/**
	 * Button to save current work to server
	 */
	private JButton m_SaveToServerButton = null;

	/**
	 * Button to save to a different file than the current file
	 */
	private JButton m_SaveAsButton = null;

	/**
	 * Button to cross two organisms
	 */
	private JButton m_CrossTwoButton = null;

	/**
	 * Button to display "about VGL" box
	 */
	private JButton m_AboutButton = null;

	/**
	 * Button to print current work to file
	 */
	private JButton m_PrintToFileButton = null;

	/**
	 * Button to invoke help
	 */
	private JButton m_OnlineHelpButton = null;

	/**
	 * A reference to the frame of the application
	 */
	private JFrame m_DialogFrame = null;

	/**
	 * The current file to which work is being saved to
	 */
	private File m_CurrentSavedFile = null;

	/**
	 * The default path for the file dialogs to open in
	 */
	private File m_DefaultDirectory = new File(".");

	/**
	 * Boolean variable to keep track of whether balloon help is on or not
	 */
	private Boolean m_IsBalloonHelpActive = null;

	/**
	 * Stores the value of the next position on the screen where a cage should
	 * be displayed
	 */
	private Point m_NextCageScreenPosition;
	
	/**
	 * Signals that this is running as an applet with reduced 
	 * functionality
	 */
	private boolean m_isAnApplet;
	
	/**
	 * The constructor
	 * 
	 * @param frame
	 *            the reference frame for the application
	 */
	public VGLII(JFrame frame) {
		super();
		m_isAnApplet = false;
		m_ProgramId = m_DefaultId;
		m_FChooser = new JFileChooser();
		m_DialogFrame = frame;
	}
	
	/** 
	 * 
	 * constructor for applet
	 */
	public VGLII(Image image){
		super();
		m_isAnApplet = true;
		m_ProgramId = m_DefaultId;
		m_DialogFrame = new JFrame(m_ProgramId);
		m_Image = image;
	}
    
	/**
	 * The constructor for access via a parent application.
	 * 
	 * @param programId
	 *            the unique name for the application
	 * @param defaultDir
	 *            the path for file dialogs to open up
	 * @param img
	 *            the image associated with the application
	 * @param frame
	 *            the reference frame for the application
	 */
	public VGLII(String programId, File defaultDir, Image img, JFrame frame) {
		super();
		m_isAnApplet = false;
		m_ProgramId = programId;
		m_Image = img;
		m_DefaultDirectory = defaultDir;
		m_FChooser = new JFileChooser(defaultDir);
		m_DialogFrame = frame;
	}

	/**
	 * Initializes the application and sets up the various components
	 */
	public void init() {
		super.init(); //  Perform JApplet initialization.
		components(); //  Create and load all GUI components.
		m_DialogFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exitApplication();
			}
		});
	}

	/**
	 * Provides a JFrame from which dialog characteristics can be derived.
	 * 
	 * @param dialogFrame
	 *            the reference frame to be used
	 */
	public void dialogFrame(JFrame dialogFrame) {
		m_DialogFrame = dialogFrame;
	}

	/**
	 * Dispatches events resulting from pre-registered listeners.
	 * 
	 * @param evt
	 *            the action to be taken
	 */
	private void eventHandler(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		update(getGraphics());
		if (cmd.equals("NewProblem"))
			newProblem();
		else if (cmd.equals("OpenWork"))
			openProblem(null);
		else if (cmd.equals("SaveWork"))
			saveProblem();
		else if (cmd.equals("SaveAs"))
			saveAsProblem();
		else if (cmd.equals("PrintToFile"))
			printToFile();
		else if (cmd.equals("PageSetup"))
			pageSetup();
		else if (cmd.equals("PrintWork"))
			print();
		else if (cmd.equals("CloseWork"))
			closeProblem();
		else if (cmd.equals("CrossTwo"))
			crossTwo();
		else if (cmd.equals("Exit"))
			exitApplication();
		else if (cmd.equals("About"))
			aboutVGL();
		else if (cmd.equals("CageManager"))
			cageManager();
		else if (cmd.equals("BalloonHelp"))
			balloonHelp();
		else if (cmd.equals("OnlineHelp"))
			onlineHelp();
		else if (cmd.equals("RearrangeCages"))
			reArrangeCages();
	}

	/**
	 * Create menu item
	 * 
	 * @param label
	 *            the name for the menu item
	 * @param actionCommand
	 *            the command to execute when the menuitem is pressed
	 * @param image
	 *            the image to be used for the menu item
	 * @return the newly created menu item
	 */
	private JMenuItem menuItem(String label, String actionCommand,
			ImageIcon image) {
		JMenuItem result = new JMenuItem(label);
		result.setActionCommand(actionCommand);
		result.setIcon(image);
		result.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				eventHandler(evt);
			}
		});
		return result;
	}

	/**
	 * Create check box menu item.
	 * 
	 * @param label
	 *            the label for the check box menu item
	 * @param actionCommand
	 *            the command to execute when the menuitem is pressed
	 * @param image
	 *            the image for the checkbox menu item
	 * @return the newly created checkbox menu item
	 */
	private JCheckBoxMenuItem checkBoxMenuItem(String label,
			String actionCommand, ImageIcon image) {
		JCheckBoxMenuItem result = new JCheckBoxMenuItem(label);
		result.setIcon(image);
		result.setActionCommand(actionCommand);
		result.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				eventHandler(evt);
			}
		});
		return result;
	}

	/**
	 * Create a button from an image (typically for a toolbar)
	 * 
	 * @param buttonImage
	 *            an image icon to paint on the button
	 * @param actionCommand
	 *            the command to execute when this button is pressed
	 * @param toolTipText
	 *            if non-null, the tool tip for this button
	 * @param keyEvent
	 *            the ALT key to set as the shortcut for this button. (Set to
	 *            KeyEvent.VK_UNDEFINED if none wanted.)
	 * @return the newly created button
	 */
	private JButton JButtonImageItem(ImageIcon buttonImage,
			String actionCommand, String toolTipText, int keyEvent) {
		JButton result = new JButton(buttonImage);
		result.setActionCommand(actionCommand);
		result.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				eventHandler(evt);
			}
		});
		if (toolTipText != null)
			result.setToolTipText(toolTipText);
		result.setMnemonic(keyEvent);
		return result;
	}

	/**
	 * Create and load menu bar.
	 */
	private void menuBar() {
		JMenuBar mnuBar = new JMenuBar();
		URL openImageURL = VGLII.class.getResource("images/open16.gif");
		ImageIcon openImage = new ImageIcon(openImageURL);
		
		URL newImageURL = VGLII.class.getResource("images/new16.gif");
		ImageIcon newImage = new ImageIcon(newImageURL);
		
		URL saveAsImageURL = VGLII.class
		.getResource("images/saveas16.gif");
		ImageIcon saveAsImage = new ImageIcon(saveAsImageURL);
		
		URL saveToServerImageURL = VGLII.class
		.getResource("images/savetoserver16.gif");
		ImageIcon saveToServerImage = new ImageIcon(saveToServerImageURL);
		
		URL saveImageURL = VGLII.class.getResource("images/save16.gif");
		ImageIcon saveImage = new ImageIcon(saveImageURL);
		
		URL aboutImageURL = VGLII.class.getResource("images/about16.gif");
		ImageIcon aboutImage = new ImageIcon(aboutImageURL);
		
		URL printFileImageURL = 
			VGLII.class.getResource("images/printtofile16.gif");
		ImageIcon printFileImage = new ImageIcon(printFileImageURL);
		
		URL balloonHelpImageURL = VGLII.class
				.getResource("images/help16.gif");
		ImageIcon balloonHelpImage = new ImageIcon(balloonHelpImageURL);

		URL printImageURL = VGLII.class.getResource("images/print16.gif");
		ImageIcon printImage = new ImageIcon(printImageURL);

		URL pageSetupImageURL = VGLII.class
				.getResource("images/pagesetup16.gif");
		ImageIcon pageSetupImage = new ImageIcon(pageSetupImageURL);

		URL onlineHelpImageURL = VGLII.class
				.getResource("images/onlinehelp16.gif");
		ImageIcon onlineHelpImage = new ImageIcon(onlineHelpImageURL);

		URL closeImageURL = VGLII.class
				.getResource("images/closework16.gif");
		ImageIcon closeImage = new ImageIcon(closeImageURL);

		//  "File" options.
		JMenu mnuFile = new JMenu("File");		
		m_NewProblemItem = menuItem("New Problem", "NewProblem", newImage);
		m_OpenProblemItem = menuItem("Open Work", "OpenWork", openImage);
		m_SaveProblemItem = menuItem("Save Work", "SaveWork", saveImage);
		m_SaveProblemAsItem = menuItem("Save Work As..", "SaveAs", saveAsImage);
		m_SaveToServerItem = menuItem("Save To Server..", "SaveToServer", saveToServerImage);
		m_PageSetupItem = menuItem("Page Setup", "PageSetup", pageSetupImage);
		m_PrintItem = menuItem("Print Work", "PrintWork", printImage);
		m_PrintToFileItem = menuItem("Print Work To File", "PrintToFile",
				printFileImage);
		m_CloseProblemItem = menuItem("Close Work", "CloseWork", closeImage);
		m_ExitItem = menuItem("Exit", "Exit", null);

		if (!m_isAnApplet){
			mnuFile.add(m_NewProblemItem);
			mnuFile.add(m_OpenProblemItem);
			mnuFile.addSeparator();
			mnuFile.add(m_SaveProblemItem);
			mnuFile.add(m_SaveProblemAsItem);
			mnuFile.addSeparator();
		}
		
		if (m_SaveToServerEnabled) {
			mnuFile.add(m_SaveToServerItem);
		}
		mnuFile.add(m_PageSetupItem);
		mnuFile.add(m_PrintItem);
		
		if (!m_isAnApplet){
			mnuFile.add(m_PrintToFileItem);
			mnuFile.addSeparator();
			mnuFile.add(m_CloseProblemItem);
			mnuFile.addSeparator();
			mnuFile.add(m_ExitItem);
		}
		
		mnuBar.add(mnuFile);

		//  "Utilities" options.
		JMenu mnuUtilities = new JMenu("Utilites");
		m_CrossTwoItem = menuItem("Cross Two", "CrossTwo", null);
		mnuUtilities.add(m_CrossTwoItem);
		mnuBar.add(mnuUtilities);
		m_CageManagerItem = menuItem("Cages", "CageManager", null);
		mnuUtilities.add(m_CageManagerItem);
		m_RearrangeCagesItem = menuItem("Rearrange Cages", "RearrangeCages",
				null);
		mnuUtilities.add(m_RearrangeCagesItem);
		mnuBar.add(mnuUtilities);

		//  "Help" options.
		JMenu mnuHelp = new JMenu("Help");
		m_BalloonHelpItem = checkBoxMenuItem("Balloon Help", "BalloonHelp",
				balloonHelpImage);
		mnuHelp.add(m_BalloonHelpItem);
		m_OnlineHelpItem = menuItem("Help Page...", "OnlineHelp",
				onlineHelpImage);
		mnuHelp.add(m_OnlineHelpItem);
		mnuHelp.add(menuItem("About Virtual Genetics Lab...", "About",
				aboutImage));
		mnuBar.add(mnuHelp);
		setJMenuBar(mnuBar);
	}

	/**
	 * Create and load status panel
	 */
	private void statusPanel() {
		JPanel sPanel = new JPanel();
		sPanel.setLayout(new BorderLayout());

		m_StatusLabel = new JLabel();
		m_StatusLabel.setForeground(Color.black);
		m_StatusLabel.setBorder(new SoftBevelBorder(1));
		m_StatusLabel.setText(" ");
		sPanel.add(m_StatusLabel, BorderLayout.CENTER);
		getContentPane().add(sPanel, BorderLayout.SOUTH);
	}

	/**
	 * Create and load toolbar
	 */
	private void toolBar() {
		m_ToolBar = new JToolBar();
		URL openImageURL = VGLII.class.getResource("images/open.gif");
		ImageIcon openImage = new ImageIcon(openImageURL);

		URL newImageURL = VGLII.class.getResource("images/new.gif");
		ImageIcon newImage = new ImageIcon(newImageURL);

		URL saveAsImageURL = VGLII.class.getResource("images/saveas.gif");
		ImageIcon saveAsImage = new ImageIcon(saveAsImageURL);

		URL saveImageURL = VGLII.class.getResource("images/save.gif");
		ImageIcon saveImage = new ImageIcon(saveImageURL);
		
		URL saveToServerURL = VGLII.class.getResource("images/savetoserver.gif");
		ImageIcon saveToServerImage = new ImageIcon(saveToServerURL);

		URL aboutImageURL = VGLII.class.getResource("images/about.gif");
		ImageIcon aboutImage = new ImageIcon(aboutImageURL);

		URL printImageURL = VGLII.class.getResource("images/print.gif");
		ImageIcon printImage = new ImageIcon(printImageURL);

		URL printFileImageURL = VGLII.class
				.getResource("images/printtofile.gif");
		ImageIcon printFileImage = new ImageIcon(printFileImageURL);

		URL onlineHelpImageURL = VGLII.class
				.getResource("images/onlinehelp.gif");
		ImageIcon onlineHelpImage = new ImageIcon(onlineHelpImageURL);

		URL closeImageURL = VGLII.class
				.getResource("images/closework.gif");
		ImageIcon closeImage = new ImageIcon(closeImageURL);

		URL crossTwoImageURL = VGLII.class.getResource("images/cross.gif");
		ImageIcon crossTwoImage = new ImageIcon(crossTwoImageURL);

		URL exitImageURL = VGLII.class.getResource("images/exit.gif");
		ImageIcon exitImage = new ImageIcon(exitImageURL);

		m_NewButton = JButtonImageItem(newImage, "NewProblem",
				"New Problem...", KeyEvent.VK_N);
		m_OpenButton = JButtonImageItem(openImage, "OpenWork", "Open Work...",
				KeyEvent.VK_O);
		m_CloseButton = JButtonImageItem(closeImage, "CloseWork",
				"Close Work...", KeyEvent.VK_L);
		m_ExitButton = JButtonImageItem(exitImage, "Exit", "Exit...",
				KeyEvent.VK_E);
		m_SaveButton = JButtonImageItem(saveImage, "SaveWork", "Save Work...",
				KeyEvent.VK_S);
		m_SaveAsButton = JButtonImageItem(saveAsImage, "SaveAs", "Save as...",
				KeyEvent.VK_V);
		m_SaveToServerButton = JButtonImageItem(saveToServerImage, "SaveToServer", "Save To Server...",
				KeyEvent.VK_I);
		m_CrossTwoButton = JButtonImageItem(crossTwoImage, "CrossTwo",
				"Cross two organisms...", KeyEvent.VK_C);
		m_AboutButton = JButtonImageItem(aboutImage, "About",
				"About Virtual Genetics Lab...", KeyEvent.VK_A);

		m_PrintButton = JButtonImageItem(printImage, "PrintWork",
				"Print Work...", KeyEvent.VK_P);

		m_PrintToFileButton = JButtonImageItem(printFileImage, "PrintToFile",
				"Print Work To File...", KeyEvent.VK_F);
		m_OnlineHelpButton = JButtonImageItem(onlineHelpImage, "OnlineHelp",
				"Help Page", KeyEvent.VK_H);
		
		if(!m_isAnApplet){
			m_ToolBar.add(m_NewButton);
			m_ToolBar.add(m_OpenButton);
			m_ToolBar.add(m_CloseButton);
			m_ToolBar.add(m_ExitButton);
			m_ToolBar.add(m_SaveButton);
			m_ToolBar.add(m_SaveAsButton);
		}
		
		if (m_SaveToServerEnabled) {
			m_ToolBar.add(m_SaveToServerButton);
		}
		
		m_ToolBar.add(m_PrintButton);
		
		if(!m_isAnApplet){
			m_ToolBar.add(m_PrintToFileButton);
		}
		
		m_ToolBar.add(m_CrossTwoButton);
		m_ToolBar.add(m_OnlineHelpButton);
		m_ToolBar.add(m_AboutButton);
	}

	/**
	 * Create and load all GUI components
	 */
	private void components() {
		menuBar();
		statusPanel();
		toolBar();
		JPanel panePanel = new JPanel();
		panePanel.setLayout(new BorderLayout());
		panePanel.add(m_ToolBar, BorderLayout.NORTH);
		getContentPane().add(panePanel, BorderLayout.CENTER);
		cleanUp();
		docRenderer = new DocumentRenderer(); //setup for printing
	}

	/**
	 * Display about dialog
	 */
	private void aboutVGL() {
		JOptionPane.showMessageDialog(this, m_ProgramId + "\n"
				+ "Release Version 1.4.4\n" + "Copyright 2007\n" + "VGL Team.\n"
				+ "All Rights Reserved\n" + "GNU General Public License\n"
				+ "http://www.gnu.org/copyleft/gpl.html",
				"About Virtual Genetics Lab...",
				JOptionPane.INFORMATION_MESSAGE, new ImageIcon(m_Image));
	}

	/**
	 * Generic file chooser method
	 * 
	 * @param workingDir
	 *            the directory to open into
	 * @param dialogTitle
	 *            the title for the file dialog
	 * @param approveTip
	 *            tool tip text for approve button of file dialog
	 * @param useAllFilter
	 *            true if "show all files" mode is needed, false otherwise
	 * @param filefilter
	 *            filter for the file types to be displayed in the dialog
	 * @param filterTip
	 *            description information about filefilter
	 * @param dialogType
	 *            an int value to decide the type of dialog.
	 * @return
	 */
	private File selectFile(File workingDir, String dialogTitle,
			String approveTip, boolean useAllFilter, String filefilter,
			String filterTip, int dialogType) {
		File result = null;
		m_FChooser = null;
		m_FChooser = new JFileChooser(workingDir);
		m_FChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		javax.swing.filechooser.FileFilter ft = m_FChooser
				.getAcceptAllFileFilter();
		m_FChooser.removeChoosableFileFilter(ft);
		if (dialogType != -1)
			m_FChooser.setDialogType(dialogType);
		if (dialogTitle != null)
			m_FChooser.setDialogTitle(dialogTitle);
		if (approveTip != null)
			m_FChooser.setApproveButtonToolTipText(approveTip);

		if (filefilter != null) {
			CustomizedFileFilter filter = new CustomizedFileFilter(filefilter,
					filterTip);
			m_FChooser.addChoosableFileFilter(filter);
		}
		if (dialogType == JFileChooser.OPEN_DIALOG) {
			if (m_FChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
				result = m_FChooser.getSelectedFile();
		} else if (dialogType == JFileChooser.SAVE_DIALOG) {
			if (m_FChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
				result = m_FChooser.getSelectedFile();
		} else if (dialogType == -1) {
			if (m_FChooser.showDialog(this, "Print") == JFileChooser.APPROVE_OPTION)
				result = m_FChooser.getSelectedFile();
		}
		update(getGraphics());
	
		//need to kill the dialog so it won't re-appear on de-iconify
		Window[] windows = m_DialogFrame.getOwnedWindows();
		for (int i = 0; i < windows.length; i++) {
			if (windows[i].toString().matches("title=New Problem Type Selection")) {
				windows[i].dispose();
			}
		}
		return result;
	}

	/**
	 * Method to set up a new problem for the user
	 */
	private void newProblem() {
	}

	/**
	 * Opens up an existing saved problem, sets up the model, and opens up all
	 * the cages of that problem.
	 */
	public void openProblem(URL workFileURL) {
	}

	/**
	 * Saves the current work done by the user to a file.
	 */
	private boolean saveProblem() {
		return false;
	}

	/**
	 * Same as saveProblem, with current file set to null, so as to enable
	 * saving to new file
	 */
	private void saveAsProblem() {
		m_CurrentSavedFile = null;
		saveProblem();
	}

	
	/**
	 * Prints the current work done by the user to a .html file
	 */
	private void printToFile() {
	}

	/**
	 * Sets up the page for printing
	 */
	private void pageSetup() {
		docRenderer.pageDialog();
	}

	/**
	 * Prints the current work to the printer
	 */
	private void print() {
		docRenderer = new DocumentRenderer();
		docRenderer.setScaleWidthToFit(true);
		JTextPane printTextPane = new JTextPane();
		printTextPane.setContentType("text/html");
		printTextPane.setText(getWorkAsHTML());
		HTMLDocument htDoc = (HTMLDocument) printTextPane.getDocument();
		docRenderer.print(htDoc);
	}
	
	/**
	 * geneate an html representaiton of the current work
	 * used by print() and saveToServer()
	 *
	 */
	private String getWorkAsHTML() {
		StringBuffer htmlString = new StringBuffer();
		return htmlString.toString();
	}

	/**
	 * Closes the problem that the user has been working on so far and releases
	 * all related objects
	 */
	private void closeProblem() {
		if (m_CageCollection != null) {
			int ans1 = JOptionPane.showConfirmDialog(this,
					"You are about to close the current work.\n"
							+ "Do you wish to save before closing?",
					"Close Work", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (ans1 == JOptionPane.YES_OPTION)
				saveProblem();
			if (ans1 != JOptionPane.CANCEL_OPTION)
				cleanUp();
		}
	}

	/**
	 * Exits the application after doing the necessary cleanup
	 */
	private void exitApplication() {
		if (m_CageCollection != null) {
			int ans = JOptionPane.showConfirmDialog(this,
					"You are about to quit. \n"
							+ "Do you wish to save before quitting?",
					"Exit VGL", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (ans == JOptionPane.YES_OPTION) {
				if (saveProblem()) {
					cleanUp();
					System.exit(0);
				} else {
					return;
				}
			}
			if (ans != JOptionPane.CANCEL_OPTION) {
				cleanUp();
				System.exit(0);
			}
		} else
			System.exit(0);
	}

	/**
	 * Method to release temporary objects and re-initialize objects and
	 * variables before exiting the application or after closing a problem
	 */
	private void cleanUp() {
		if (m_CageCollection != null) {
			Iterator it = m_CageCollection.iterator();
			while (it.hasNext()) {
				CageUI c = (CageUI) it.next();
				it.remove();
				c.setVisible(false);
			}
		}
		m_CageCollection = null;
		m_Genetics = null;
		m_SelectionVial = null;
		m_CurrentSavedFile = null;
		m_NextCageId = 1;
		enableAll(false);
		m_NextCageScreenPosition = new Point(this.getX() + 200,
				this.getY() + 100);
		m_IsBalloonHelpActive = null;
		m_StatusLabel.setText("");
	}

	/**
	 * Method that actually sets up the cross between two organisms
	 */
	private void crossTwo() {
		OrganismUI organismUI1 = m_SelectionVial.getMaleParent();
		OrganismUI organismUI2 = m_SelectionVial.getFemaleParent();
		if (organismUI1 != null && organismUI2 != null) {
			Organism o1 = organismUI1.getOrganism();
			Organism o2 = organismUI2.getOrganism();
			Cage c = m_Genetics.crossTwo(m_NextCageId, o1, o2);
			CageUI cageUI = createCageUI(c);
			OrganismUI[] parentUIs = cageUI.getParentUIs();
			if (parentUIs[0].getOrganism().getSexType() == o1.getSexType()) {
				organismUI1.getReferencesList().add(parentUIs[0]);
				organismUI2.getReferencesList().add(parentUIs[1]);
				parentUIs[0].setCentralOrganismUI(organismUI1);
				parentUIs[1].setCentralOrganismUI(organismUI2);
			} else {
				organismUI1.getReferencesList().add(parentUIs[1]);
				organismUI2.getReferencesList().add(parentUIs[0]);
				parentUIs[1].setCentralOrganismUI(organismUI1);
				parentUIs[0].setCentralOrganismUI(organismUI2);
			}
		} else
			JOptionPane.showMessageDialog(this, m_ProgramId + "\n"
					+ "Cross Two cannot be carried out without two organisms\n"
					+ "Please select two organisms and try again\n",
					"Cross Two", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Depending on whether the m_IsBalloonHelpActive is true/false, this method
	 * allows/disallows users to obtain tooltip based information about screen
	 * objects
	 */
	private void balloonHelp() {
		if (m_IsBalloonHelpActive != null) {
			m_IsBalloonHelpActive = new Boolean(!m_IsBalloonHelpActive
					.booleanValue());
			m_BalloonHelpItem.setSelected(m_IsBalloonHelpActive.booleanValue());
		} else {
			m_IsBalloonHelpActive = new Boolean(true);
			m_BalloonHelpItem.setSelected(true);
		}
		if (m_CageCollection != null) {
			Iterator it = m_CageCollection.iterator();
			while (it.hasNext()) {
				CageUI cageUI = (CageUI) it.next();
				cageUI.setBalloonHelp(m_IsBalloonHelpActive.booleanValue());
			}
		}
	}

	/**
	 * This method invokes .html help into a JEditor pane
	 */
	private void onlineHelp() {
		final JEditorPane helpPane = new JEditorPane();
		helpPane.setEditable(false);
		helpPane.setContentType("text/html");

		try {
			helpPane.setPage(VGLII.class.getResource("Help/index.html"));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(m_DialogFrame,
					"Be sure the help folder is in the same folder as VGL.",
					"Can't find help file.", JOptionPane.ERROR_MESSAGE);
			return;
		}

		JScrollPane helpScrollPane = new JScrollPane(helpPane);
		JDialog helpDialog = new JDialog(m_DialogFrame, "VGL Help");
		JButton backButton = new JButton("Go Back to Top of Page");
		helpDialog.getContentPane().setLayout(new BorderLayout());
		helpDialog.getContentPane().add(backButton, BorderLayout.NORTH);
		helpDialog.getContentPane().add(helpScrollPane, BorderLayout.CENTER);
		Dimension screenSize = getToolkit().getScreenSize();
		helpDialog.setBounds((screenSize.width / 8), (screenSize.height / 8),
				(screenSize.width * 8 / 10), (screenSize.height * 8 / 10));
		helpDialog.setVisible(true);

		helpPane.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						helpPane.setPage(e.getURL());
					} catch (IOException ioe) {
						System.err.println(ioe.toString());
					}
				}
			}

		});

		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					helpPane.setPage(VGLII.class.getResource("Help/index.html"));
				} catch (Exception e) {
					System.err
							.println("Couldn't open help file" + e.toString());
				}
			}
		});
	}

	/**
	 * sets up the cage manager dialog and displays it
	 */
	private void cageManager() {
		JFrame frame = m_DialogFrame;
		CageManager dlg = new CageManager(frame, "Cages", m_CageCollection);
		dlg.setVisible(true);
		dlg = null;
	}

	/**
	 * This method acutally sets up the Cage's UI.
	 * 
	 * @param c
	 *            The cage object whose UI is to be created
	 * @return the newly created cageUI
	 */
	private CageUI createCageUI(Cage c) {
		JFrame frame = m_DialogFrame;
		HashMap children = c.getChildren();
		ArrayList parents = c.getParents();
		CageUI dlg = null;
		String details = null;
		details = m_Genetics.getModelInfo();
		dlg = new CageUI(frame, m_IsBeginner, c, m_SelectionVial, m_Trait,
				details);
		m_NextCageId++;
		if (dlg != null) {
			m_CageCollection.add(dlg);
			calculateCagePosition(dlg);
			dlg.setVisible(true);
			if (m_IsBalloonHelpActive != null)
				dlg.setBalloonHelp(m_IsBalloonHelpActive.booleanValue());
		}
		return dlg;
	}

	/**
	 * Method to toggle the enabled state of the various menu and button widgets
	 * 
	 * @param value
	 */
	private void enableAll(boolean value) {
		m_NewButton.setEnabled(!value);
		m_OpenButton.setEnabled(!value);
		m_NewProblemItem.setEnabled(!value);
		m_OpenProblemItem.setEnabled(!value);
		m_PrintItem.setEnabled(value);
		m_PrintButton.setEnabled(value);
		m_PrintToFileItem.setEnabled(value);
		m_PrintToFileButton.setEnabled(value);
		m_SaveButton.setEnabled(value);
		m_SaveAsButton.setEnabled(value);
		m_SaveToServerButton.setEnabled(value);
		m_CrossTwoButton.setEnabled(value);
		m_BalloonHelpItem.setEnabled(value);
		m_CageManagerItem.setEnabled(value);
		m_RearrangeCagesItem.setEnabled(value);
		m_SaveProblemItem.setEnabled(value);
		m_SaveProblemAsItem.setEnabled(value);
		m_SaveToServerItem.setEnabled(value);
		m_CloseProblemItem.setEnabled(value);
		m_CloseButton.setEnabled(value);
		m_CrossTwoItem.setEnabled(value);
	}

	/**
	 * This is a correction method to correct the file extensions if the user
	 * did not enter them correctly
	 * 
	 * @param thisFile
	 *            the file
	 * @param suffix
	 *            the extension to be given to the file
	 * @return
	 */
	private File convertTo(File thisFile, String suffix) {
		int endIndex = thisFile.getName().indexOf('.');
		String name = null;
		if (endIndex >= 0)
			name = thisFile.getPath().substring(0, endIndex);
		else
			name = thisFile.getPath();
		name = name + suffix;
		thisFile.delete();
		thisFile = new File(name);
		return thisFile;
	}

	/**
	 * This method iterates over the collection of cage objects and sets up the
	 * UI for each of the cages. This method is invoked when an saved problem is
	 * reopened for work
	 * 
	 * @param cages
	 *            the list of cages
	 * @throws Exception
	 *             in case any or all of the cages are not correct
	 */
//	private void reopenCages(ArrayList cages) throws Exception {
//		Iterator it = cages.iterator();
//		while (it.hasNext()) {
//			Cage c = (Cage) it.next();
//			CageUI cageUI = createCageUI(c);
//			if (c.getId() > 0) {
//				OrganismUI[] parentUIs = cageUI.getParentUIs();
//				if (parentUIs == null)
//					System.out.println("No parents found for Cage#: "
//							+ c.getId());
//				if (parentUIs[0] == null)
//					System.out.println("No parent0 found for Cage#: "
//							+ c.getId());
//				if (parentUIs[1] == null)
//					System.out.println("No parent1 found for Cage#: "
//							+ c.getId());
//				Organism o1 = parentUIs[0].getOrganism();
//				Organism o2 = parentUIs[1].getOrganism();
//				int o1_Id = o1.getId();
//				int o2_Id = o2.getId();
//				CageUI cage1 = (CageUI) m_CageCollection.get(o1.getCageId());
//				CageUI cage2 = (CageUI) m_CageCollection.get(o2.getCageId());
//				if (cage1 != null && cage2 != null) {
//					OrganismUI originalOUI1 = cage1.getOrganismUIFor(o1_Id);
//					OrganismUI originalOUI2 = cage2.getOrganismUIFor(o2_Id);
//					if (originalOUI1 != null && originalOUI2 != null) {
//						if (parentUIs[0].getOrganism().getSexType() == originalOUI1
//								.getOrganism().getSexType()) {
//							originalOUI1.getReferencesList().add(parentUIs[0]);
//							originalOUI2.getReferencesList().add(parentUIs[1]);
//							parentUIs[0].setCentralOrganismUI(originalOUI1);
//							parentUIs[1].setCentralOrganismUI(originalOUI2);
//						} else {
//							originalOUI1.getReferencesList().add(parentUIs[1]);
//							originalOUI2.getReferencesList().add(parentUIs[0]);
//							parentUIs[1].setCentralOrganismUI(originalOUI1);
//							parentUIs[0].setCentralOrganismUI(originalOUI2);
//						}
//					} else {
//						System.out
//								.println("For Original Organisms of Parents of Cage#: "
//										+ c.getId());
//						if (originalOUI1 == null)
//							System.out.println("Organism for: " + o1.getId()
//									+ " " + o1.getCageId() + " not found!");
//						if (originalOUI2 == null)
//							System.out.println("Organism for: " + o2.getId()
//									+ " " + o2.getCageId() + " not found!");
//					}
//				} else {
//					System.out.println("For Parents of Cage#: " + c.getId());
//					if (cage1 == null)
//						System.out.println("Cage for Organism: " + o1.getId()
//								+ " " + o1.getCageId() + " not found!");
//					if (cage2 == null)
//						System.out.println("Cage for Organism: " + o2.getId()
//								+ " " + o2.getCageId() + " not found!");
//				}
//			}
//		}
//	}

	/**
	 * Method to calculate the position of a cage on the screen
	 * 
	 * @param cageUI
	 *            the cage whose position needs to be calculated
	 */
	private void calculateCagePosition(CageUI cageUI) {
		Dimension cageSize = cageUI.getSize();
		Dimension screenSize = this.getSize();
		int positionX = (int) m_NextCageScreenPosition.getX();
		int positionY = (int) m_NextCageScreenPosition.getY();
		if ((positionX + cageSize.getWidth() > screenSize.getWidth())
				|| (positionY + cageSize.getHeight() > screenSize.getHeight())) {
			m_NextCageScreenPosition = new Point(this.getX() + 200,
					this.getY() + 100);
			positionX = (int) m_NextCageScreenPosition.getX();
			positionY = (int) m_NextCageScreenPosition.getY();
		}
		m_NextCageScreenPosition = new Point(positionX + 30, positionY + 30);
		cageUI.setLocation(positionX, positionY);
	}

	/**
	 * This method rearranges the current list of cages in a proper fashion
	 */
	private void reArrangeCages() {
		Dimension screenSize = this.getSize();
		Iterator it = m_CageCollection.iterator();
		m_NextCageScreenPosition = new Point(this.getX() + 200,
				this.getY() + 100);
		double positionX;
		double positionY;
		Dimension cageSize;
		while (it.hasNext()) {
			CageUI cageUI = (CageUI) it.next();
			positionX = m_NextCageScreenPosition.getX();
			positionY = m_NextCageScreenPosition.getY();
			cageSize = cageUI.getSize();
			if ((positionX + cageSize.getWidth() > screenSize.getWidth())
					|| (positionY + cageSize.getHeight() > screenSize
							.getHeight())) {
				m_NextCageScreenPosition = new Point(this.getX() + 200, this
						.getY() + 100);
			} else
				m_NextCageScreenPosition = new Point((int) positionX + 30,
						(int) positionY + 30);
			cageUI.setLocation((int) positionX, (int) positionY);
			if (cageUI.isVisible())
				cageUI.setVisible(true);
		}
		CageUI lastCageUI = (CageUI) m_CageCollection.get(m_CageCollection
				.size() - 1);
		m_NextCageScreenPosition = new Point(lastCageUI.getX(), lastCageUI
				.getY());
	}

	private void createHTMLFile(File printFile, ArrayList cages, String trait) {

	}
}

