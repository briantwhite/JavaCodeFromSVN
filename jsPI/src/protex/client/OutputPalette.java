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

package protex.client;


import protex.client.java.awt.Color;
import protex.client.java.awt.Dimension;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

/**
 * OutputPalette displays folding results.
 * 
 * @author Namita Singla/MGX Team UMB
 * @version v0.1
 */
public class OutputPalette extends CaptionPanel {
	ScrollPanel scrollPanel;
	private Dimension area; //indicates area taken up by graphics
	private GridCanvas drawingPane;
		
	private boolean ssBondsOn; //for background coloring
	
	public OutputPalette(String title) {
		super(title);
	    
		//Set up the drawing area.
		drawingPane = new HexCanvas(200, 200);
		
		ScrollPanel scrollPane = new ScrollPanel();
		//scrollPane.add(drawingPane);
	    //scrollPane.setSize("390px", "390px");
		
		area = new Dimension(0, 0);
		ssBondsOn = false;
	}

	public Dimension getDimension() {
		return area;
	}

	public GridCanvas getDrawingPane() {
		return drawingPane;
	}
	
	public boolean getssBondsOn() {
		return ssBondsOn;
	}
	
	public void setssBondsOn(boolean b) {
		ssBondsOn = b;
	}

	/**
	 * Clear graphic from this OutputPalette
	 *  
	 */
	public void removeAll() {
		if (drawingPane.getGrid() != null)
			this.drawingPane.getGrid().unsetAll();
	}
}
