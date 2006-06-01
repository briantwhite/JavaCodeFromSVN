// InputPalette.java
//
//
// Copyright 2224-2005 MGX Team UMB. All rights reserved.
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
 * Modified: 10 May 2005 (David Portman/MGX Team UMB)
 */

package protex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
//import java.awt.datatransfer.DataFlavor;
//import java.awt.datatransfer.Transferable;
//import java.awt.datatransfer.UnsupportedFlavorException;
//import java.awt.dnd.DnDConstants;
//import java.awt.dnd.DropTarget;
//import java.awt.dnd.DropTargetAdapter;
//import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//import java.io.IOException;
import java.util.Vector;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * InputPalette displays a Polypetide chain.
 * 
 * @author Namita Singla/MGX Team UMB
 * @version v0.1
 */
public class InputPalette extends JPanel implements MouseListener, KeyListener {

// done by TJ -- remove DND
//	/**
//     * Handles drops of AminoAcid to InputPalette.
//     * 
//     * @author Namita Singla
//     */
//    public class DndDropTargetListener extends DropTargetAdapter {
//    
//    	public DndDropTargetListener() {
//    		super();
//    	}
//    
//    	/**
//    	 * Handles drops of AminoAcid.  DROP ONLY IN AN ACTIVE IOPanel.
//    	 */
//    	public void drop(DropTargetDropEvent evt) {
//    	    if (iOP.equals(ProtexMainApp.activeIOPanel)) {
//        		Point p = evt.getLocation();
//        		Point p1 = new Point(p.x , p.y - cellRadius);
//        		int index = aminoAcidAt(p1);
//
//        		// XXX ERROR CHECK
//        		//System.out.println("(" + p1.getX()+ ","+ p1.getY()+ ")");
//        		//System.out.println(index);
//        		
//        		Transferable t = evt.getTransferable();
//        		if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
//            		//	System.out.println("In drop");
//        			String acid = null;
//        			try {
//        				acid = (String) t.getTransferData(DataFlavor.stringFlavor);
//        			} 
//        			catch (UnsupportedFlavorException e1) {
//        			    e1.printStackTrace();
//        			} 
//        			catch (IOException e2) {
//        			    e2.printStackTrace();
//        			}
//
//        			// XXX ERROR CHECK
//        			//String[] pars = acid.split(",");
//        			//for (int i = 0; i < pars.length; i++)
//        			//	System.out.println(pars[i]);
//        			//	addAminoAcid(pars[0], Double.parseDouble(pars[1]), Double
//        			//		.parseDouble(pars[2]), Double.parseDouble(pars[3]));
//        			
//        			addAminoAcid(selectedAcid, index);
//        			repaint();
//        			if (!isChanged && 
//        					iOP.getPolypeptide() != null)
//        				promptIsChangedTo(true);
//        		}
//        	}
//    	    else {
//    			JOptionPane.showMessageDialog(null,
//    					"You must activate the Work Panel" +
//    					"\nbefore you drag and drop. Click" +
//    					"\non the Work Panel to activate it.",
//    					"Message",
//    					JOptionPane.INFORMATION_MESSAGE);
//    	    }
//       	}
//    }
	

    /**
	 * The component inside the scroll pane.
	 */
	public class DrawingPane extends JPanel {
		protected void paintComponent(Graphics g) {
			super.setBackground(bgColor);
			super.paintComponent(g);
//			StandardTable table = new StandardTable();  // removed by TJ
			ColorCoder cc = new ColorCoder(table.getContrastScaler()); 
			
			if (admin)
				ProtexAdminGUI.buffer = new StringBuffer();
			else
				ProtexMainApp.buffer = new StringBuffer();
			boolean changed = false;
			
			try {
				// added by TJ -- cursor at left end, first draw it
				int cIndex = cursor.getCIndex();
				if(cIndex == -1){
					g.setColor(cursor.getColor());
					g.drawLine(0, 0, 0, cellDiameter);
				}
				//System.out.println(list.size());
				for (int i = 0; i < list.size(); i++) {
					AminoAcid a = (AminoAcid) list.get(i);
					g.setColor(cc.getCellColor(a
							.getNormalizedHydrophobicIndex()));
					g.fillOval(i * cellDiameter, 0, cellDiameter, cellDiameter);
					//System.out.println("drawing amino acid at: ("+ i * cellDiameter + "," + "0" + "," + cellDiameter+","+ cellDiameter+")");
					g.setColor(Color.black);
					int offset = getStringIndentationConstant(a.getName(),
							cellRadius);
					g.drawString(a.getName(),
							i * cellDiameter + cellRadius - offset, cellRadius);
					// added by TJ -- draw cursor
					if(i == cIndex){
						g.setColor(cursor.getColor());
						g.drawLine((i+1) * cellDiameter, 0, (i+1) * cellDiameter, cellDiameter);
						//System.out.println(cIndex);
					}
					if (admin)
						ProtexAdminGUI.buffer.append(a.getName() + ":");
					else
						ProtexMainApp.buffer.append(a.getName() + ":");
					int this_width = i * cellDiameter + cellDiameter;
					if (this_width > area.width) {
						area.width = this_width;
						changed = true;
					}
					if (changed) {
						//Update client's preferred size because
						//the area taken up by the graphics has
						//gotten larger or smaller (if cleared).
						drawingPane.setPreferredSize(area);

						//Let the scroll pane know to update itself
						//and its scrollbars.
						drawingPane.revalidate();
					}
				}
			} catch (Exception ex) {
			//	ex.printStackTrace();
			}
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param admin
	 *            boolean - true if Protex is running in administrator mode.
	 */
	public InputPalette(boolean admin) {

		super(new BorderLayout());
		super.setBorder(BorderFactory.createTitledBorder("Polypeptide Chain"));
		this.admin = admin;
		isChanged = false;
		area = new Dimension(0, 0);
		list = new Vector();
		cursor = new Cursor();
		table = new StandardTable();

		//Set up the drawing area.
		drawingPane = new DrawingPane();
		drawingPane.setBackground(Color.white);
		drawingPane.addMouseListener(this);
		drawingPane.addKeyListener(this);  // added by TJ -- for cursor movement

		//Put the drawing area in a scroll pane.
		JScrollPane scroller = new JScrollPane(drawingPane);
		scroller.setPreferredSize(new Dimension(200, 55));
		add(scroller, BorderLayout.CENTER);
// done by TJ -- remove DND
//		DndDropTargetListener dropTargetListener = new DndDropTargetListener();
//		DropTarget dropTarget = new DropTarget(this, DnDConstants.ACTION_COPY,
//				dropTargetListener, true);
	}

	// accessor methods

	/**
	 * 
	 * @return boolean true if this InputPalette's AminoAcid chain is modified.
	 */
	public boolean getIsChanged() {
		return isChanged;
	}

	/**
	 * 
	 * @return Vector
	 */
	public Vector getAminoAcids() {
		return list;
	}

	/**
	 * Convert AminoAcid chain to String. [DAP]
	 * 
	 * @return String
	 */
	public String acidsToString() {
		String s = "";
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			s.concat(((AminoAcid) itr.next()).toString());
		}
		return s;
	}

	// mutator methods

	/**
	 * Set the IOPanel associated with this InputPalette.
	 * 
	 * @param i
	 *            IOPanel
	 */
	public void setIOPanel(IOPanel i) {
		this.iOP = i;
	}

	/**
	 * Notifies the InputPalette that the AminoAcid chain is modified, but does
	 * so with prompt.  See also method setIsChangedTo().
	 * 
	 * @param b boolean
	 */
	public void promptIsChangedTo(boolean b) {
		if (b) {
			int result = JOptionPane.showConfirmDialog(null, 
				"Polypeptide Chain has been modified." + 
				"\n        Clear Folded Protein?", 
				iOP.getName(), JOptionPane.YES_NO_OPTION);
			
			if (result == JOptionPane.YES_OPTION) {
				iOP.clearOutputPalette();
			}
		}
		setIsChangedTo(b);
	}

	/**
	 * Notifies the InputPalette that the AminoAcid chain is modified.
	 * 
	 * @param b boolean
	 */
	public void setIsChangedTo(boolean b) {
		isChanged = b;
	}
	
//	/**
//	 * Add amino acid to polypeptide chain
//	 * 
//	 * @param aminoAcid
//	 *            AminoAcid
//	 */
//	public void addAminoAcid(AminoAcid aminoAcid, int index) {
//		if (index == -1)
//			addAminoAcid(aminoAcid);
//		else
//			list.add(index, aminoAcid);
//	}

	public void addAminoAcid(AminoAcid aminoAcid) {
		cursor.incrementCIndex();
		list.add(cursor.getCIndex(), aminoAcid);
	}

	/**
	 * added by TJ
	 * remove the amino acid that's on the left of the cursor
	 * do nothing is there no amino acid to the left of the cursor
	 */
	public void removeAminoAcid(){
		int cIndex = cursor.getCIndex();
		if(cIndex == -1){
			return;
		}
		list.remove(cIndex);
		cursor.decrementCIndex();
	}
	
	/**
	 * Add amino acid to polypeptide chain
	 * 
	 * @param aminoAcid AminoAcid.
	 */
	public void setAminoAcid(AminoAcid aminoAcid) {
		selectedAcid = aminoAcid;
	}

// removed by TJ -- unused method
//	public void addAminoAcid(String aminoAcid, double hydrophobicIndex,
//			double hydrogenbondIndex, double ionicIndex) {
//		AminoAcid acid = new AminoAcid(aminoAcid, hydrophobicIndex,
//				hydrogenbondIndex, ionicIndex);
//		list.add(aminoAcid);
//	}

	/**
	 * Remove amino acid from polypeptide chain.
	 * 
	 * @param aminoAcid
	 *            int - AminoAcid index value.
	 */
	public void removeAminoAcid(int aminoAcidIndex) {
		if (FoldingManager.getInstance().DEBUG) {
			System.out.print("\nInputPalette.removeAminoAcid(): ");
			System.out.println("aminoAcidIndex = " + aminoAcidIndex);
		}
		list.remove(aminoAcidIndex);
		if(aminoAcidIndex <= cursor.getCIndex()){
			cursor.decrementCIndex();
		}
	}

	/**
	 * Remove all AminoAcids in InputPalette.
	 */
	public void removeAll() {
		list.clear();
		cursor.reset();
	}

	// implementation of inherited methods

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * If mouse is clicked, make current IOPanel active; remove AminoAcid from
	 * chain.
	 * 
	 * @param event
	 *            MouseEvent
	 */
	public void mouseClicked(MouseEvent event) {

		// activate this IOPanel
//		ProtexMainApp.activeIOPanel = iOP;
//		ProtexMainApp.setActiveIOPanelBorder();
//		this.getKeyboardFocus();
		ProtexMainApp.resetIOPanel(iOP);

		// remove AminoAcid from chain?
		Point p = event.getPoint();
		try {
			aminoAcidIndex = aminoAcidAt(p);
			if (aminoAcidIndex >= 0) {
				removeAminoAcid(aminoAcidIndex);
				drawingPane.repaint();
				if (!isChanged && 
				        iOP.getPolypeptide() != null)
					promptIsChangedTo(true);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// added by TJ -- Implement KeyListener methods to support cursor movements
	public void getKeyboardFocus(){
		drawingPane.requestFocusInWindow();
	}
	
	public void keyPressed(KeyEvent event){
//		System.out.println("I am here");
		int keyPressed = event.getKeyCode();
		int cIndex = cursor.getCIndex();
		if(keyPressed == KeyEvent.VK_LEFT){
			// if cursor at left end, do nothing
			if(cIndex == -1){
				System.out.println("At left end, can't move left anymore!");
				return;
			}
			cursor.decrementCIndex();
			drawingPane.repaint();
		}
		else if(keyPressed == KeyEvent.VK_RIGHT){
			if(cIndex == list.size()-1){
				System.out.println("At right end, can't move right anymore!");
				return;
			}
			cursor.incrementCIndex();
			drawingPane.repaint();
		}
		else if(keyPressed == KeyEvent.VK_BACK_SPACE){
			if(cIndex == -1){
				System.out.println("At left end, can't do delete!");
				return;
			}
			removeAminoAcid(cIndex);
			drawingPane.repaint();
			if (!isChanged && 
			        iOP.getPolypeptide() != null)
				promptIsChangedTo(true);
		}
		else{ // could be adding new amino acid to string
			AminoAcid aAcid = table.getFromAbName("" + event.getKeyChar());
			if(aAcid == null){
				System.out.println("has no such amino acid, can't add!");
				return;
			}
			this.addAminoAcid(aAcid);
			drawingPane.repaint();
			if (!isChanged && 
			        iOP.getPolypeptide() != null)
				promptIsChangedTo(true);
		}
	}
	public void keyReleased(KeyEvent event){
	}
	public void keyTyped(KeyEvent event){
	}
	
	/**
	 * Return index value of the first AminoAcid containing point p. Return
	 * "null" if there is no such AminoAcid.
	 * 
	 * @param p
	 *            Point - point clicked on in Cartesian (x, y) space.
	 * @return int AminoAcid index.
	 */

	public int aminoAcidAt(Point p) {
	//	System.out.println("(" + p.getX()+ ","+ p.getY()+ ")");
	    
		for (int i = 0; i < list.size(); i++) {
			AminoAcid a = (AminoAcid) list.get(i);
		//	System.out.println("Looking at "+ a.getName());
			int d = (p.x - ((i * cellDiameter) + cellRadius))
					* (p.x - ((i * cellDiameter) + cellRadius))
					+ (p.y - cellRadius) * (p.y - cellRadius);
			if (d <= (cellRadius * cellRadius))
				return i;
		}
		return -1;
	}

    private int aminoAcidIndex;

	private static int cellRadius = 20;

	private static int cellDiameter = 2 * cellRadius;

	private boolean admin, isChanged;
	
	private Color bgColor = 
		new Color((float) 0.95, (float) 0.95, (float) 0.85);

	private Dimension area; //area taken up by graphics

	private JPanel drawingPane;

	private Vector list;
	
	private Cursor cursor; // added by TJ -- cursor implementation
	private StandardTable table;

	private IOPanel iOP;

	private AminoAcid selectedAcid;

	/**
     * Returns constants used for centering the name of the polypeptide
     * 
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
}
