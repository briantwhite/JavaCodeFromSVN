// EventHandler.java
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
 *     public class AClass {
 *         (public constants)
 *         (public constructors)
 *         (public accessors)
 *         (public mutators)
 *         (nonpublic fields)
 *         (nonpublic auxiliary methods or nested classes)
 *     }
 *
 * Jia also recommends the following design guidelines.
 *
 *     1. Avoid public fields.  There should be no nonfinal public fields,
 *        except when a class is final and the field is unconstrained.
 *     2. Ensure completeness of the public interface.  The set of public
 *        methods defined in the class should provide full and convenient
 *        access to the functionality of the class.
 *     3. Separate interface from implementation.  When the functionality
 *        supported by a class can be implemented in different ways, it is
 *        advisable to separate the interface from the implementation.
 *
 * Created:  07 Apr 2005
 * Modified: 26 May 2005 (David Portman/MGX Team UMB)
 */

package protex;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

//va :3----------------------------
import java.util.Vector;
import java.util.ArrayList;
import java.net.URL;
//va :3----------------------------

/**
 * A class holding methods for handling various types of events.
 *
 * @author David Portman/MGX Team UMB
 */
public class EventHandler {

//	// public methods
//
//	/**
//	 * Opens a saved problem.
//	 * NOTE: THIS METHOD CURRENTLY NOT INVOKED. SEE METHOD openDefault().
//	 *
//	 */
//	public static void openProblem() {
//		Polypeptide pp;
//		activeIOPanel = ProtexMainApp.activeIOPanel;
//
//		// use default Attributes for testing purposes
//		Attributes attrib = new Attributes();
//
//		// reinitialize StringBuffer
//		ProtexMainApp.buffer.delete(0, ProtexMainApp.buffer.length());
//		ProtexMainApp.buffer.insert(0, attrib.getInputString());
//
//		// TODO straighten out ppId assignments, build a list of AminoAcids
//		// Steps:
//		//		read acid names from "buffer"
//		//			for each acid name in buffer
//		//			read through table (StandardTable.java)
//		//				if table entry matches, take hydrophobic index etc; exit loop
//		//          create new aminoacid (AminoAcid.java)
//		//			add acid to chain
//		try {
//			pp = PolypeptideFactory.getInstance().createPolypeptide(
//					ProtexMainApp.buffer.toString(), attrib.getIsFolded(),
//					attrib.getIsRandom(), attrib.getLength(), attrib.getSeed(),
//					attrib.getTable(), attrib.getPpId());
//
//			loadPP(pp);
//		} catch (FoldingException ex) {
//			ex.printStackTrace();
//		}
//
//		// InputPalette has been changed (will put in histList)
//		InputPalette iP = (InputPalette) activeIOPanel.getInputPanel();
//		iP.setIsChangedTo(true);
//
//		// clear OutputPalette (activeIOPanel only)
//		OutputPalette outputPanel =
//			(OutputPalette) activeIOPanel.getOutputCanvasPanel();
//		try {
//			outputPanel.getDrawingPane().getGrid().unsetAll();
//			outputPanel.repaint();
//		} catch (Exception ex) {
//			if (FoldingManager.getInstance().DEBUG) {
//				System.out.print("\nopenProblem(): " +
//                                	"\nNo output grid");
//			}
//		}
//	}

	/**
	 * For debugging purposes: Loads a default AminoAcid chain into the
	 * InputPalette of the activeIOPanel.
	 *
	 */
	public static void openDefault() {
		Polypeptide pp;
		activeIOPanel = ProtexMainApp.activeIOPanel;

		// use default Attributes for testing purposes
		Attributes attrib = new Attributes();

		// reinitialize StringBuffer
		ProtexMainApp.buffer.delete(0, ProtexMainApp.buffer.length());
		ProtexMainApp.buffer.insert(0, attrib.getInputString());

		// TODO straighten out ppId assignments, build a list of AminoAcids
		// Steps:
		//		read acid names from "buffer"
		//			for each acid name in buffer
		//			read through table (StandardTable.java)
		//				if table entry matches, take hydrophobic index etc; exit loop
		//          create new aminoacid (AminoAcid.java)
		//			add acid to chain
		try {
			pp = PolypeptideFactory.getInstance().createPolypeptide(
					ProtexMainApp.buffer.toString(), attrib.getIsFolded(),
					attrib.getIsRandom(), attrib.getLength(), attrib.getSeed(),
					attrib.getTable(), attrib.getPpId());

			loadPP(pp);
		} catch (FoldingException ex) {
			ex.printStackTrace();
		}

		// InputPalette has been changed
		InputPalette iP = (InputPalette) activeIOPanel.getInputPanel();
		iP.setIsChangedTo(true);

		// clear OutputPalette
		OutputPalette outputPanel =
			(OutputPalette) activeIOPanel.getOutputCanvasPanel();
			outputPanel.removeAll();
			outputPanel.repaint();
	}

	/**
	 * Load an existing AminoAcid chain into the InputPalette by
	 * 	- Clearing inputPalette.
	 *  - Displaying (painting) a new list of AminoAcids.
	 *
	 * @param pp
	 *            Polypeptide
	 */
	public static void loadPP(Polypeptide pp) {
		JPanel inputPanel = activeIOPanel.getInputPanel();
		((InputPalette) inputPanel).removeAll();

		Iterator itr = pp.iterator();
		while (itr.hasNext()) {
			AminoAcid acid = ((AcidInChain) itr.next()).getAminoAcid();
			((InputPalette) inputPanel).addAminoAcid(acid);
			((InputPalette) inputPanel).repaint();
		}
	}

//	/**
//	 * Save this Problem in its current state in the default file on disk.
//	 */
//	public static void saveProblem() {
//		JOptionPane.showMessageDialog(null,
//				"This feature is currently disabled.", "Save",
//				JOptionPane.INFORMATION_MESSAGE);
//	}
//
//	/**
//	 * Display this Problem in a new file on disk.
//	 */
//	public static void saveAsProblem() {
//		JOptionPane.showMessageDialog(null,
//				"This feature is currently disabled.", "Save As...",
//				JOptionPane.INFORMATION_MESSAGE);
//	}
//
//	/**
//	 * Close this Problem.
//	 */
//	public static void closeProblem() {
//		JOptionPane.showMessageDialog(null,
//				"This feature is currently disabled.", "Close",
//				JOptionPane.INFORMATION_MESSAGE);
//	}

///////////////////////////////////////////////////////////////////////////////////
//
// removed by TJ -- use closeMe defined in ProtexMainApp to close the application
//
///////////////////////////////////////////////////////////////////////////////////
//	/**
//	 * Display exit message with yes/no option.
//	 */
//	public static void exitMainApp() {
//		int result = JOptionPane.showConfirmDialog(null,
//				"Do you want to exit Protex Beta?", "Exit?",
//				JOptionPane.YES_NO_OPTION);
//		if (result == JOptionPane.YES_OPTION)
//			System.exit(1);
//	}

	/**
	 * Calculate and display "diff."
	 */
	public static void displayDiff() {
//va :2----------------------------
//		JOptionPane.showMessageDialog(null,
//				"This feature is currently disabled.", "Diff",
//				JOptionPane.INFORMATION_MESSAGE);

//--step1: get the polypeptide chains of each panels
//          Polypeptide ppOne = ProtexMainApp.upperIOPanel.getInputPanel().getPolypeptide();
//          Polypeptide ppTwo = ProtexMainApp.lowerIOPanel.getPolypeptide();
          Vector ppOne = ( (InputPalette)
                           (ProtexMainApp.upperIOPanel.getInputPanel())).
                           getAminoAcids();

          Vector ppTwo = ( (InputPalette)
                           (ProtexMainApp.lowerIOPanel.getInputPanel())).
                            getAminoAcids();

//--step2A: validation: both panels must have PP chains if we need a difference.
          if ((ppOne.size()==0) && (ppTwo.size()==0)){

            JOptionPane.showMessageDialog(null,
                                          "Error: Currently both panels are"+
                                          " empty.. Difference not possible.",
                                          "Diff",
                                          JOptionPane.INFORMATION_MESSAGE);
            return;
          }

//--step2B: validation: both panels must have PP chains if we need a difference.
          if (ppOne.size()==0) {

            JOptionPane.showMessageDialog(null,
                                          "Error: Currently top panel is"+
                                          " empty.. Difference not possible.",
                                          "Diff",
                                          JOptionPane.INFORMATION_MESSAGE);
            return;
          }

//--step2C: validation: both panels must have PP chains if we need a difference.
          if (ppTwo.size()==0) {

            JOptionPane.showMessageDialog(null,
                                          "Error: Currently lower panel is"+
                                          " empty.. Difference not possible.",
                                          "Diff",
                                          JOptionPane.INFORMATION_MESSAGE);
            return;
          }


                handleDiff(ppOne, ppTwo);
//va :2----------------------------
	}

	/**
	 * Display the online help facility.
	 */
	public static void displayHelp(Toolkit kit) {
		final JEditorPane helpPane = new JEditorPane();
		helpPane.setEditable(false);
		helpPane.setContentType("text/html");

		try {
			helpPane.setPage(ProtexMainApp.class.getResource("help/index1.html"));
//                       System.out.println("userdir: "+System.getProperty("user.dir"));
//                       System.out.println("file.separator: "+System.getProperty("file.separator"));
//			helpPane.setPage(new URL("file://"
//					        + System.getProperty("user.dir")
//							+ System.getProperty("file.separator")
//							+ "src/protex/help/index1.html"));
		}
		catch (Exception e) {
e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"File not found: protex/help/index.html",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		JScrollPane helpScrollPane = new JScrollPane(helpPane);
		JDialog helpDialog = new JDialog(new JFrame(""),"Protex Help");

		helpDialog.getContentPane().setLayout(new BorderLayout());
		helpDialog.getContentPane().add(helpScrollPane, BorderLayout.CENTER);

		Dimension screenSize = kit.getScreenSize();
		helpDialog.setBounds(
		        (screenSize.width/8), (screenSize.height/8),
				(screenSize.width*8/10), (screenSize.height*8/10));
		helpDialog.show();

		helpPane.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						helpPane.setPage(e.getURL());
					}
					catch (IOException ioe) {
						System.err.println(ioe.toString());
					}
				}
			}
		});
	}

	/**
	 * Display a message describing this version of Protex.
	 */
	public static void displayAbout() {
		JOptionPane.showMessageDialog(null,
		        "       This is Protex Beta 1." +
		        "\nFor information on the Web, visit" +
		        "\n\n    http://mgx2005.sourceforge.net" +
				"\n\nCopyright 2005-2006 MGX Team UMB." +
				"\n        All rights reserved.",
				"About Protex",
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Opens (loads) one HistoryList item into the active IOPanel.
	 *
	 * @see Class HistoryList.
	 * @param pp
	 *            Polypeptide.
	 */
	public static void openHLItem(PolypeptidePanel ppp) {
		Polypeptide pp = (Polypeptide) ppp.getPolypeptide();

		// check for Polypeptide already in active work panel
		activeIOPanel = ProtexMainApp.activeIOPanel;
		if (pp.isInUpperIOPanel()
				&& activeIOPanel.equals(ProtexMainApp.upperIOPanel) ||
			pp.isInLowerIOPanel()
				&& activeIOPanel.equals(ProtexMainApp.lowerIOPanel)				) {
			JOptionPane.showMessageDialog(null, pp.getId()
				+ " is already in " +
				activeIOPanel.getName() + ".",
				"Redundant Replacement", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// remove the Polypeptide currently occupying the activeIOPanel
		int result = 0;
		Polypeptide ppFromActiveIOPanel = activeIOPanel.getPolypeptide();
		if (ppFromActiveIOPanel != null) {
			result = JOptionPane.showConfirmDialog(null,
				ppFromActiveIOPanel.getId()
				+ " is in " + activeIOPanel.getName() + "."
				+ "\nReplace with " + pp.getId() + "?",
				"Confirm Replacement",
				JOptionPane.YES_NO_OPTION);

			if (result == JOptionPane.YES_OPTION) {

				// if ppFromActiveIOPanel is also in History, adjust all
				//	references to it
				if (ppFromActiveIOPanel.isInHistory()) {
					modifyReferencesTo(ppFromActiveIOPanel);
				}
				else {
					result = JOptionPane.showConfirmDialog(null,
						ppFromActiveIOPanel.getId() + " is no longer in History."
						+ "\nBy replacing it now, you will remove it completely."
						+ "\nReplace anyway?",
						activeIOPanel.getName(),
						JOptionPane.YES_NO_OPTION);

					if (result == JOptionPane.NO_OPTION)
						return;
				}
			}
			else {
				return;
			}
		}

		// load Polypeptide (in the activeIOPanel)
		loadPP(pp);

		// InputPalette has not yet been changed
		InputPalette inputPanel = (InputPalette) activeIOPanel.getInputPanel();
		inputPanel.setIsChangedTo(false);

		// fold the existing chain;
		//	NOTE: this method creates a new Polypeptide, but we just want to
		// 		reuse the existing Polypeptide, pp
		//  perhaps fold again, then transfer the information from this pp to
		//		the new pp; as long as each pp knows 1) that it already exists in
		//      History and 2) that it does (not) exist in the other work panel
		ProtexMainApp.foldChain(pp);

		// confirm that ppFromHistory is loaded in activeIOPanel
		pp = activeIOPanel.getPolypeptide();

		// XXX ERROR CHECK
		if (FoldingManager.getInstance().DEBUG) {
			System.out.println("\nEventHandler.openHLItem()-- ");
			if (pp != null) {
				System.out.println(pp.toReport());
			}
			else {
				System.out.println("\n\tPolypeptide: Null Pointer Encountered.");
			}
		}
	}

	/**
	 * Remove one cell from the HistoryList.
	 *
	 * @param index
	 *            int position.
	 */
	public static void removeHLItem(int index, DefaultListModel dlm,
			JScrollPane jsp, Polypeptide pp) {

		System.out.println("removing HL Item " + index);
		
		// prompt for confirmation
		int result = JOptionPane.showConfirmDialog(null,
			"Remove " + pp.getId() + " from History?",
			"Edit History", JOptionPane.YES_NO_OPTION);

		if (result == JOptionPane.YES_OPTION) {
			try {
				dlm.remove(index);
				jsp.revalidate();
				jsp.repaint();
			}
			catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			}

			// nullify references to this Polypeptide
			Polypeptide ppInUpperPanel = pp.getUHL()[0];
			Polypeptide ppInLowerPanel = pp.getUHL()[2];
			if (ppInUpperPanel != null) {
				ppInUpperPanel.setInHistoryTo(false);
				ppInUpperPanel.setUHL(ppInUpperPanel, null, ppInLowerPanel);
			}

			// XXX ERROR CHECK
			if (FoldingManager.getInstance().DEBUG) {
				System.out.println("\nEvenHandler.removeHLItem():");
				if (ppInUpperPanel != null) {
					System.out.println(ppInUpperPanel.toReport());
				} else {
					System.out
							.println("\n\tPolypeptide: Null Pointer Encountered.");
				}
			}

			pp.setInHistoryTo(false);
			pp.setUHL(ppInUpperPanel, null, ppInLowerPanel);

			// XXX ERROR CHECK
			if (FoldingManager.getInstance().DEBUG) {
				System.out.println("\nEvenHandler.removeHLItem():");
				if (pp != null) {
					System.out.println(pp.toReport());
				} else {
					System.out
							.println("\n\tPolypeptide: Null Pointer Encountered.");
				}
			}

			if (ppInLowerPanel != null) {
				ppInLowerPanel.setInHistoryTo(false);
				ppInLowerPanel.setUHL(ppInUpperPanel, null, ppInLowerPanel);
			}

			// XXX ERROR CHECK
			if (FoldingManager.getInstance().DEBUG) {
				System.out.println("\nEvenHandler.removeHLItem():");
				if (ppInLowerPanel != null) {
					System.out.println(ppInLowerPanel.toReport());
				} else {
					System.out
							.println("\n\tPolypeptide: Null Pointer Encountered.");
				}
			}
		}
	}

	// non-public field

	private static IOPanel activeIOPanel = null;

	// non-public method

	/*
	 * Modify (i.e, remove) all references in system to ppFromActiveIOPanel.
	 *
	 * @param ppFromActiveIOPanel Polypeptide.
	 */
	private static void modifyReferencesTo(Polypeptide ppFromActiveIOPanel) {
		Polypeptide ppFromHistory =
			ppFromActiveIOPanel.getUHL()[1];
		Polypeptide ppInUpperPanel =
			ppFromHistory.getUHL()[0];
		Polypeptide ppInLowerPanel =
			ppFromHistory.getUHL()[2];

		// remove references to ppFromActiveIOPanel
		if (activeIOPanel.equals(ProtexMainApp.upperIOPanel)) {
			ppFromHistory.setInUpperIOPanelTo(false);
			ppFromHistory.setUHL(null, ppFromHistory, ppInLowerPanel);

			// TODO remove referencts to ppFromActiveIOPanel from
			//	ppInLowerPanel

		}

		else if (activeIOPanel.equals(ProtexMainApp.lowerIOPanel)) {
			ppFromHistory.setInLowerIOPanelTo(false);
			ppFromHistory.setUHL(ppInUpperPanel, ppFromHistory, null);

			// TODO remove references to ppFromActiveIOPanel from
			//	ppInUpperPanel

		}
		else {
			System.out.println("\nEventHandler.openHLItem(): "
					+ "ERROR - You should never get here.");
		}

		// XXX ERROR CHECK
		if (FoldingManager.getInstance().DEBUG) {
			System.out.println("\nEventHandler.modifyReferencesTo()-- ");
			if (ppFromHistory != null) {
				System.out.println(ppFromHistory.toReport());
			}
			else {
				System.out.println("\n\tPolypeptide: Null Pointer Encountered.");
			}
		}
	}

////////////////////////////////////////////////////////////////////
//va :1
// handles the difference b/w 2 chains in top/bottom panels
//
// If any of the panels is empty then display appropiate message
//
// get the polypeptide chains of each panels and get indivisual
// blocks into an arraylist.
//
// feed the arraylists into LCS (longest common subsequence) algo
// and display the output of LCS in a message box.
////////////////////////////////////////////////////////////////////

 private static void handleDiff(Vector ppOne, Vector ppTwo){

    ArrayList ppOneList = new ArrayList();
    ArrayList ppTwoList = new ArrayList();

    for (int i = 0; i < ppOne.size(); i++) {
      ppOneList.add( (( (AminoAcid) ppOne.get(i)).getName()).toUpperCase());
    }

    for (int i = 0; i < ppTwo.size(); i++) {
      ppTwoList.add( (( (AminoAcid) ppTwo.get(i)).getName()).toUpperCase());
    }

   String diffString = LCS.LCSAlgorithm(ppOneList,ppTwoList);
   diffString.trim();

//  System.out.println("EventHandler: "+"\n"+diffString);
            JOptionPane.showMessageDialog(null,
                                          diffString,
                                          "Diff",
                                          JOptionPane.INFORMATION_MESSAGE);
            return;
}

}
