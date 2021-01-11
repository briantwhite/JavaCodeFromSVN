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

package edu.umb.jsAipotu.client.biochem;

import java.awt.Dimension;
import java.awt.Point;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.user.client.Window;

import edu.umb.jsAipotu.client.preferences.GlobalDefaults;



/**
 * AminoAcidPalette displays all of the AminoAcid objects. 
 * 
 * @author Namita Singla/MGX Team UMB
 * @version v0.1
 */
public class AminoAcidPaletteBuilder {

	// non-public fields

	private static int cellRadius = 20;

	private static int cellDiameter = 2 * cellRadius;
	
	private static int columnWidth, rowHeight;

	private static AminoAcid[] list;

	public static Canvas build(int width, int height, int row, int column) {
		Canvas canvas = Canvas.createIfSupported();
		if (canvas == null) {
			Window.alert("This application is not supported by this browser; please try a different browser.");
			return null;
		}
		canvas.setWidth(width + "px");
		canvas.setCoordinateSpaceWidth(width);
		canvas.setHeight(height + "px");
		canvas.setCoordinateSpaceHeight(height);
		
		Context2d c2d = canvas.getContext2d();
		
		c2d.setFillStyle(GlobalDefaults.PROTEIN_BACKGROUND_COLOR.toString());
		c2d.fillRect(0, 0, width, height);

		columnWidth = cellDiameter + cellRadius / 4;
		rowHeight = columnWidth;
		StandardTable table = new StandardTable();
		list = table.getAllAcids();
		ColorCoder cc = new ShadingColorCoder(table.getContrastScaler()); ///
		int x = 0;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				AminoAcid a = list[x];
				a.paint(c2d, cc, j * columnWidth, i * rowHeight);
				x++;
			}
		}
		return canvas;
	}
}
