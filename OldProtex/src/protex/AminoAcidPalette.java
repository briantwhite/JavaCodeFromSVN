// AminoAcidPalette.java
//
//
// Copyright 2004-2005 MGX Team UMB. All rights reserved.
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
 * Created:  01 Nov 2004 (Namita Singla/MGX Team UMB)
 * Modified: 09 Mar 2005 (David Portman/MGX Team UMB)
 */

package protex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
//import javax.swing.event.MouseInputAdapter;

/**
 * AminoAcidPalette displays all of the AminoAcid objects. Students select from
 * these objects by pointing and clicking with a Mouse.
 * 
 * @author Namita Singla/MGX Team UMB
 * @version v0.1
 */
public class AminoAcidPalette extends JPanel {

	/**
     * This TransferHandler transfers an AminoAcid.
     * 
     * @author Namita Singla
     */
    public class DndTransferHandler extends TransferHandler {
    	public DndTransferHandler() {
    		super();
    	}
    
    	protected Transferable createTransferable(JComponent c) {
    		return new StringSelection(exportString(c));
    	}
    
    	protected String exportString(JComponent c) {
    	//	System.out.println("In drag");
    		return selectedAminoAcid.getName();
    	}
    
    	public int getSourceActions(JComponent c) {
    		return COPY;
    	}
    }
    /**
	 * Handles the press and click of mouse on an AminoAcid drag.
	 * 
	 * @author Namita Singla
	 */
	public class ClickMouseAdapter extends MouseAdapter
	{
		public ClickMouseAdapter() {
			super();
		}
		public void mouseClicked(MouseEvent event) {
			Point p = event.getPoint();
			try {
				selectedAminoAcid = aminoAcidAt(p);
				if (selectedAminoAcid != null) {
					if (admin) {
						((InputPalette) ProtexAdminGUI.inputPanel)
								.addAminoAcid(selectedAminoAcid);
						((InputPalette) ProtexAdminGUI.inputPanel)
								.repaint();
					} 
					else {
					    IOPanel iOP = ProtexMainApp.activeIOPanel;
						InputPalette inputPanel = 
						    (InputPalette) iOP.getInputPanel();
						inputPanel.addAminoAcid(selectedAminoAcid);
						inputPanel.repaint();
						if (!inputPanel.getIsChanged() &&
						        iOP.getPolypeptide() != null)
							inputPanel.promptIsChangedTo(true);
					}
				}
			} 
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

///////////////////////////////////////////////////////////////////////////////////////////
/**
 * done by TJ
 * remove the dnd function from protex
 */	
//	/**
//	 * Handles the start of a AminoAcid drag, but not drops to the InputPalette.
//	 * 
//	 * @author Namita Singla
//	 */
//	public class DndMouseAdapter extends MouseInputAdapter {
//
//		public DndMouseAdapter() {
//			super();
//		}
//
//		/**
//		 * Starts the drag of a AminoAcid. We save the selected aminoacid in the
//		 * field AminoAcidPalette.selectedAminoAcid. Then we invoke
//		 * handler.exportAsDrag to tell Swing that a drag is in progress But we
//		 * also want to stuff the selected AminoAcid inside a Transferable.
//		 *  
//		 */
//		public void mouseDragged(MouseEvent event) {
//			JComponent c = (JComponent) event.getSource();
//			AminoAcidPalette panel = (AminoAcidPalette) c;
//			Point p = event.getPoint();
//			try {
//				selectedAminoAcid = panel.aminoAcidAt(p);
//				if (selectedAminoAcid == null) {
//					return;
//				}
//				JPanel inputPanel = null;
//				if (admin) {
//					inputPanel = ProtexAdminGUI.inputPanel;
//				} else {
//					inputPanel = ProtexMainApp.activeIOPanel
//							.getInputPanel();
//				}
//				((InputPalette) inputPanel).setAminoAcid(selectedAminoAcid);
//				TransferHandler handler = c.getTransferHandler();
//				handler.exportAsDrag(c, event, TransferHandler.COPY);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Constructor
	 * 
	 * @param width
	 *            Width of canvas
	 * @param height
	 *            Height of canvas
	 */
	public AminoAcidPalette(int width, int height, int row, int column,
			boolean admin) {
		super(new BorderLayout());
		super.setPreferredSize(new Dimension(width, height));
		super.setBackground(Color.white);

		this.row = row;
		this.column = column;
		this.admin = admin;
//////////////////////////////////////////////////////////////////////////////////////////////////
// done by TJ -- remove DND
//		// Mouse Dragged event handler
//		addMouseMotionListener(new DndMouseAdapter());
/////////////////////////////////////////////////////////////////////////////////////////////////		
		
		// Mouse clicked event handler
		addMouseListener(new ClickMouseAdapter());
		setTransferHandler(new DndTransferHandler());
	}

	/**
	 * Draw the palette
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension d = getSize();
		Point p = this.getLocation();
		columnWidth = cellDiameter + cellRadius / 4;
		rowHeight = columnWidth;
		StandardTable table = new StandardTable();
		list = table.getAllAcids();
		ColorCoder cc = new ColorCoder(table.getContrastScaler()); ///
		int x = 0;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				AminoAcid a = list[x];
				g.setColor(cc.getCellColor(a.getNormalizedHydrophobicIndex()));
				g.fillOval(j * columnWidth, i * rowHeight, cellDiameter,
						cellDiameter);
				g.setColor(Color.black);
				int offset = getStringIndentationConstant(a.getName(), cellRadius);
				int abOffset = getStringIndentationConstant(a.getAbName(), cellRadius);
				g.drawString(a.getName(), j * columnWidth + cellRadius - offset, i
						* rowHeight + cellRadius);
				g.drawString(a.getAbName(), j * columnWidth + cellRadius - abOffset, i 
						* rowHeight + cellRadius + AB_Y_OFFSET);
				x++;
			}
		}
	}

	/**
	 * Return the first amino acod in which point p is contained, if none,
	 * return null
	 * 
	 * @param p
	 *            point clicked at
	 * @return AminoAcid
	 */
	public AminoAcid aminoAcidAt(Point p) {
		int x = 0;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				AminoAcid a = list[x];
				int d = (p.x - ((j * columnWidth) + cellRadius))
						* (p.x - ((j * columnWidth) + cellRadius))
						+ (p.y - ((i * rowHeight) + cellRadius))
						* (p.y - ((i * rowHeight) + cellRadius));
				if (d <= (cellRadius * cellRadius))
					return a;
				x++;
			}
		}
		return null;
	}

	/**
	 * Returns constants used for center the name of the polypeptide
	 */
	protected int getStringIndentationConstant(String name, int r) {
		// the values returned are hardcoded with values that
		//   look best when the canvas is drawn. Their value
		//   was establish through trials, and best was picked.

		int length = name.trim().length();
		if (length == 1) // 1
			return 0;
		else if (length == 2) // -1
			return 0;
		else if (length == 3) // 0.x
			return (int) (1 / 5f * r);
		else if (length == 4) // -0.x
			return (int) (1 / 2f * r);
		else if (length == 5) // -0.xx
			return (int) (2 / 3f * r);
		else
			// length == 6. can't be longer. -0.xxx
			return (int) (3 / 4f * r);
	}

	// non-public fields

	private static int cellRadius = 20;

	private static int cellDiameter = 2 * cellRadius;
	
	private static int AB_Y_OFFSET = 13;

	private int row, column, columnWidth, rowHeight;

	private AminoAcid selectedAminoAcid;

	private AminoAcid[] list;

	private boolean admin;
}
