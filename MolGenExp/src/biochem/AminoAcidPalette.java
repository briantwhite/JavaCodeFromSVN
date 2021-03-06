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

package biochem;

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



/**
 * AminoAcidPalette displays all of the AminoAcid objects. 
 * 
 * @author Namita Singla/MGX Team UMB
 * @version v0.1
 */
public class AminoAcidPalette extends JPanel {

	// non-public fields

	private static int cellRadius = 20;

	private static int cellDiameter = 2 * cellRadius;
	
	private static int AB_Y_OFFSET = 13;

	private int row, column, columnWidth, rowHeight;

	private AminoAcid selectedAminoAcid;

	private AminoAcid[] list;

	private boolean admin;
	/**
	 * Constructor
	 * 
	 * @param width
	 *            Width of canvas
	 * @param height
	 *            Height of canvas
	 */
	public AminoAcidPalette(int width, int height, int row, int column) {
		super(new BorderLayout());
		super.setPreferredSize(new Dimension(width, height));
		super.setBackground(BiochemistryWorkbench.BACKGROUND_COLOR);

		this.row = row;
		this.column = column;
		this.admin = admin;
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
		ColorCoder cc = new ShadingColorCoder(table.getContrastScaler()); ///
		int x = 0;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				AminoAcid a = list[x];
				a.paint(g, cc, j * columnWidth, i * rowHeight);
				x++;
			}
		}
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

}
