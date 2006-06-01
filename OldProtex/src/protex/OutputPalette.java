// OutputPalette.java
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
 * Modified: 10 May 2005 (David Portman/MGX Team UMB)
 */

package protex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * OutputPalette displays folding results.
 * 
 * @author Namita Singla/MGX Team UMB
 * @version v0.1
 */
public class OutputPalette extends JPanel implements MouseListener {

	/**
	 * Constructor
	 *  
	 */
	public OutputPalette() {
		super(new BorderLayout());
		super.setBorder(BorderFactory.createTitledBorder(title));
		area = new Dimension(0, 0);

		//Set up the drawing area.
		drawingPane = new HexCanvas();
		drawingPane.setParentPanel(this);
		drawingPane.addMouseListener(this);

		//Put the drawing area in a scroll pane.
		JScrollPane scroller = new JScrollPane(drawingPane);
		scroller.setPreferredSize(new Dimension(175, 175));
		add(scroller, BorderLayout.CENTER);
	}

	// accessor methods

	/**
	 * Get the color of protein on this canvas
	 * 
	 * @return protein color
	 */
	public Color getProteinColor() {
		return drawingPane.grid.getProteinColor();
	}

	public Dimension getDimension() {
		return area;
	}

	public GridCanvas getDrawingPane() {
		return drawingPane;
	}

	// mutator methods
	
	/**
	 * Set the title and border.
	 */
	public void setTitle() {
		Polypeptide pp = iOP.getPolypeptide();
		if (pp == null || pp.getId() == null) {
			title = defaultTitle;
		}
		else {
			title = defaultTitle + " :: " + pp.getId();
		}
		setBorder(BorderFactory.createTitledBorder(title));
	}

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
	 * Clear graphic from this OutputPalette
	 *  
	 */
	public void removeAll() {
		if (drawingPane.getGrid() != null)
			this.drawingPane.getGrid().unsetAll();
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
	 * If mouse is clicked, make current IOPanel the active panel.
	 * 
	 * @param event
	 *            MouseEvent
	 */
	public void mouseClicked(MouseEvent event) {
// modified by TJ -- shift the focus of input panel
//		ProtexMainApp.activeIOPanel = iOP;
//		ProtexMainApp.setActiveIOPanelBorder();
		ProtexMainApp.resetIOPanel(iOP);
	}

	// non-public fields.

	private String defaultTitle = "Folded Protein";
	private String title = defaultTitle;
	
	private Dimension area; //indicates area taken up by graphics

	private GridCanvas drawingPane;

	private IOPanel iOP;
}
