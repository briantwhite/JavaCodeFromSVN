// HistoryList.java
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
 * Created:  06 Apr 2005
 * Modified: 10 May 2005 (David Portman/MGX Team UMB)
 */

package protex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * 
 * 
 * @author David Portman/MGX Team UMB
 */
public class HistoryList extends JList {

	/**
	 * Constructor.
	 */
	public HistoryList(ListModel dataModel, Toolkit kit) {
		super(dataModel);
		histListModel = (DefaultListModel) dataModel;
		super.setBackground(bgColor);

		// set the ListCellRenderer
		setCellRenderer(new HistoryCellRenderer());

		// define a MouseListener
		addMouseListener(new HLMDoubleClickListener());

		// define a ListSelectionListener
		addListSelectionListener(new HListListener());

		// Define a JScrollPane holding the JList
		histListScrollPane = new JScrollPane();
		histListScrollPane.getViewport().add(this);
		histListScrollPane.setPreferredSize(new Dimension(225, 360));

		// put a JPopupMenu in the JList; add a MouseListener so that menu comes
		// up
		JPopupMenu popup = createPopupMenu(kit);

		MouseListener popupListener = new HLPopupListener(popup);
		addMouseListener(popupListener);
	}

	/**
	 * 
	 * @return DefaultListModel.
	 */
	public DefaultListModel getHistListModel() {
		return histListModel;
	}

	/**
	 * 
	 * @return Return the histListScrollPane.
	 */
	public JScrollPane getHistListScrollPane() {
		return histListScrollPane;
	}

	/**
	 * Add a PolypeptidePanel. 
	 * 
	 * @param ppp
	 *            PolypeptidePanel.
	 * @return Polypeptide or null if input is null.
	 */
	public Polypeptide add(PolypeptidePanel ppp) {
		Polypeptide pp = ppp.getPolypeptide();

		if (pp == null ||
				pp.isInHistory()) {
			return null;
		}
		
		histListModel.addElement(ppp);
		histListScrollPane.revalidate();
		histListScrollPane.repaint();
		pp.setInHistoryTo(true);
		return pp;
	}

	/**
	 * clear the history list
	 * -- added by TJ, to clear cache
	 */
	public void clear(){
		int hlSize = histListModel.getSize();
		System.out.println("The size of hl is " + hlSize);
		while(histListModel.getSize() != 0){
			PolypeptidePanel ppp = (PolypeptidePanel) getModel().getElementAt(0);
			Polypeptide pp = (Polypeptide) ppp.getPolypeptide();
			removeFirstItem(pp);
		}
	}
	
	/**
	 * Remove first cell from the HistoryList.
	 * 
	 * -- added by TJ
	 */
	private void removeFirstItem(Polypeptide pp) {

		System.out.println("removing HL Item " + index);
//		
//		// prompt for confirmation
//		int result = JOptionPane.showConfirmDialog(null,
//			"Remove " + pp.getId() + " from History?",
//			"Edit History", JOptionPane.YES_NO_OPTION);

//		if (result == JOptionPane.YES_OPTION) {
		histListModel.remove(0);
		histListScrollPane.revalidate();
		histListScrollPane.repaint();
		
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

//		pp.setInHistoryTo(false);
//		pp.setUHL(ppInUpperPanel, null, ppInLowerPanel);

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
//		}
	}
	
	// non-public inner classes

	/*
	 * Holds fields and objects associated with one member (cell) in a
	 * HistoryList.
	 * 
	 * @author Pradeep Kadiyala/MGX Team UMB @author David Portman/MGX Team UMB
	 */
	private class HistoryCellRenderer extends JButton implements
			ListCellRenderer {

		/**
		 * Constructor.
		 *  
		 */
		public HistoryCellRenderer() {
			setOpaque(true);
			setLayout(new BorderLayout());
			this.setPreferredSize(new Dimension(100, 72));
		}

		// accessor method(s)

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			this.index = index;
			this.isSelected = isSelected;

			//setText(value.toString());
			//setForeground(isSelected ? onColor : offColor);
			setBackground(isSelected ? onColor : offColor);

			if (this.getComponentCount() > 0) {
				this.removeAll();
				this.repaint();
			}

			buildCells(value);
			setEnabled(list.isEnabled());

			return this;
		}

		// mutator method(s)

		// non-public fields

		private int index;

		private boolean isSelected;

		private Color onColor = //Color.PINK;
			new Color((float) 0.6, (float) 0.6, (float) 0.9);

		private Color offColor = Color.LIGHT_GRAY;
			//new Color((float) 0.95, (float) 0.95, (float) 0.85);

		// non-public methods

		/*
		 * Build a single cell in the JList.
		 * 
		 * @param value Object to be put in a Cell
		 */
		private void buildCells(Object value) {

			// a PolypetidePanel holds a single Polypeptide chain;
			//	access that Polypeptide chain
			PolypeptidePanel ppp = (PolypeptidePanel) value;
			Polypeptide pp = (Polypeptide) ppp.getPolypeptide();

			// XXX ERROR CHECK
			if (FoldingManager.getInstance().DEBUG) {
				System.out.println("\nHistoryList.HistoryCellRenderer.buildCells()--");
				if (pp != null) {
					System.out.println(pp.toReport());
				}
				else {
					System.out.println("\n\tPolypeptide: Null Pointer Encountered.");
				}
			}

			// access the thumbnail image associated with this PolypeptidePanel
			ImageIcon thumbnail = ppp.getThumbnail();

			// add id and thumbnail to this HistoryCellRenderer
			JButton textButton = new JButton(pp.getId(), thumbnail);
			textButton.setBorder(BorderFactory.createLineBorder(
					isSelected ? Color.RED : Color.BLACK, 2));
			textButton.setPreferredSize(new Dimension(10, 36));
			// added by TJ -- make the color more obvious
			textButton.setContentAreaFilled(false);
			textButton.setOpaque(true);
			textButton.setBackground(pp.getColor());
//			textButton.setBackground(isSelected ? onColor : pp.getColor());
			
			textButton.setEnabled(true);
			this.add(textButton);
		}
	}

	/*
	 * A List selection listener for the elements that are present in
	 * DefaultListModel. HList uses DefaultListModel to add the elements.
	 * 
	 * @author Pradeep Kadiyala/MGX Team UMB @author David Portman/MGX Team UMB
	 */
	private class HListListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent event) {
			if (!event.getValueIsAdjusting()) {
				PolypeptidePanel ppPanel = (PolypeptidePanel) getSelectedValue();
			}
		}
	}

	/*
	 * A MouseListener class for the HistoryList. Listens for a "double-click"
	 * on the mouse. The only method is mouseClicked().
	 * 
	 * @author Pradeep Kadiyala/MGX Team UMB @author David Portman/MGX Team UMB
	 */
	private class HLMDoubleClickListener extends MouseAdapter {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		public void mouseClicked(MouseEvent e) {

			if (e.getClickCount() == 2) {
				index = locationToIndex(e.getPoint());

				if (FoldingManager.getInstance().DEBUG) {
					System.out.println("\nHistoryList.HLMDoubleClickListener: "
							+ "Double clicked on JList item, index = " + index);
				}
				// bug fixed by TJ -- error checking added
				if(index == -1){
					return;
				}
				PolypeptidePanel ppp = (PolypeptidePanel) getModel()
						.getElementAt(index);
				//Polypeptide pp = ppp.getPolypeptide();
				EventHandler.openHLItem(ppp);
			}
		}
	}

	/*
	 * A MouseListener for the HistoryList JPopupMenu. Listens for
	 *  
	 */
	private class HLPopupListener extends MouseAdapter {

		/**
		 * Constructor.
		 * 
		 * @param popupMenu
		 */
		public HLPopupListener(JPopupMenu p) {
			popup = p;
		}

		/*
		 *  
		 */
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
			index = locationToIndex(e.getPoint());

			// TODO HL cell should light up when JPopupMenu appears
			if (FoldingManager.getInstance().DEBUG) {
				System.out.println("\nHistoryList.PopupListener: "
						+ "Single click on JList item, index = " + index);
			}
		}

		/*
		 *  
		 */
		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		// non-public field

		private JPopupMenu popup;

		/*
		 * Display the JPopupMenu.
		 * 
		 * @param e
		 */
		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	// non-public fields

	private int index;
	
	private Color bgColor =
		new Color((float) 0.95, (float) 0.95, (float) 0.85);

	private JMenuItem OpenItem;

//	private JMenuItem SaveAsItem;

//	private JMenuItem HelpItem;

	private JMenuItem RemoveItem;

	private DefaultListModel histListModel;

	private static JScrollPane histListScrollPane;

	/*
	 * Create a JPopupMenu.
	 */
	private JPopupMenu createPopupMenu(Toolkit kit) {

		// define image icons
		Class c = ProtexMainApp.class;
		URL openImageURL = c.getResource("images/open16.gif");
//		URL saveAsImageURL = c.getResource("images/saveas16.gif");
//		URL helpImageURL = c.getResource("images/onlinehelp16.gif");
		URL removeImageURL = c.getResource("images/closework16.gif");

		ImageIcon openImage = new ImageIcon(openImageURL);
//		ImageIcon saveAsImage = new ImageIcon(saveAsImageURL);
//		ImageIcon helpImage = new ImageIcon(helpImageURL);
		ImageIcon removeImage = new ImageIcon(removeImageURL);

		JPopupMenu popup = new JPopupMenu();
		OpenItem = createMenuItem("Open in Active Panel", "Open", openImage,kit);
//		SaveAsItem = createMenuItem("Save to File", "SaveAs", saveAsImage,kit);
//		HelpItem = createMenuItem("Help", "Help", helpImage,kit);
		RemoveItem = createMenuItem("Remove from History", "Remove",
				removeImage,kit);

		popup.add(OpenItem);
//		popup.addSeparator();
//		popup.add(SaveAsItem);
//		popup.add(HelpItem);
		popup.addSeparator();
		popup.add(RemoveItem);

		// enable/disable JMenuItems as needed.
		OpenItem.setEnabled(true);
//		SaveAsItem.setEnabled(false);
//		HelpItem.setEnabled(true);
		RemoveItem.setEnabled(true);

		return popup;
	}

	/*
	 * Create one JMenuItem for a drop-down JMenu.
	 * 
	 * @param string @param string2 @param openImage @return JMenuItem
	 */
	private JMenuItem createMenuItem(String label, String actionCommand,
			ImageIcon image, final Toolkit kit) {
		JMenuItem item = new JMenuItem(label);
		item.setActionCommand(actionCommand);
		item.setIcon(image);
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				if(index == -1){  // bug fixed -- error checking added by TJ
					// no HL Item in list, do nothing
					return;
				}
				PolypeptidePanel ppp = (PolypeptidePanel) getModel()
						.getElementAt(index);

				String cmd = evt.getActionCommand();
				if (cmd.equals("Open"))
					EventHandler.openHLItem(ppp);
//				else if (cmd.equals("SaveAs"))
//					EventHandler.saveAsProblem();
//				else if (cmd.equals("Help"))
//					EventHandler.displayHelp(kit);
				else if (cmd.equals("Remove")) {
					Polypeptide pp = (Polypeptide) ppp.getPolypeptide();

					// XXX ERROR CHECK
					if (FoldingManager.getInstance().DEBUG) {
						System.out.println("\nHistoryList.createJMenuItem()--");
						if (pp != null) {
							System.out.println(pp.toReport());
						} else {
							System.out
									.println("\n\tPolypeptide: Null Pointer Encountered.");
						}
					}

					EventHandler.removeHLItem(index, histListModel,
							histListScrollPane, pp);
				}
			}
		});
		return item;
	}

}
