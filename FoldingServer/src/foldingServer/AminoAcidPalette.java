//AminoAcidPalette.java


//Copyright 2004-2005 MGX Team UMB. All rights reserved.
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

package foldingServer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

/**
 * AminoAcidPalette displays all of the AminoAcid objects. 
 * 
 * @author Namita Singla/MGX Team UMB
 * @version v0.1
 */
public class AminoAcidPalette extends JPanel {

	// non-public fields

	private static int cellRadius;

	private static int cellDiameter;

	private static int AB_Y_OFFSET;

	private int row, column;
	
	private Dimension size;

	private AminoAcid[] list;

	/**
	 * Constructor
	 * 
	 * @param width
	 *            Width of canvas
	 * @param height
	 *            Height of canvas
	 */
	public AminoAcidPalette(int row, int column) {
		this.row = row;
		this.column = column;
		cellRadius = FoldingServer.aaRadius;
		cellDiameter = 2 * cellRadius;
		AB_Y_OFFSET = 13;
		size = new Dimension(column * cellDiameter, row * cellDiameter);
	}
	
	public AminoAcidPalette() {
		this(0,0);
	}
	
	public Dimension getSize() {
		return size;
	}

	/**
	 * Draw the palette
	 */
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(FoldingServer.SS_BONDS_OFF_BACKGROUND);
		g2d.fillRect(0, 0, size.width, size.height);
		StandardTable table = new StandardTable();
		list = table.getAllAcids();
		int x = 0;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				AminoAcid a = list[x];

				//don't show the 'dummy' amino acid used for target shapes
				if (a.getAbName().equals("X")) {
					break;
				}
				a.paint(g, 
						cellRadius, 
						FoldingServer.colorCoder, 
						j * cellDiameter, 
						i * cellDiameter);
				x++;
			}
		}
	}
}
