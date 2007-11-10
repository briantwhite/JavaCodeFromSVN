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

	private static int cellRadius;

	private static int cellDiameter;

	private static int AB_Y_OFFSET;

	private int row, column;

	private AminoAcid[] list;

	private ColorCoder cc;

	/**
	 * Constructor
	 * 
	 * @param width
	 *            Width of canvas
	 * @param height
	 *            Height of canvas
	 */
	public AminoAcidPalette(int row, int column) {
		super();
		this.row = row;
		this.column = column;
		cellRadius = FoldingServer.aaRadius;
		cellDiameter = 2 * cellRadius;
		AB_Y_OFFSET = 13;
		super.setPreferredSize(
				new Dimension(column * cellDiameter, row * cellDiameter));
		super.setBackground(FoldingServer.SS_BONDS_OFF_BACKGROUND);
	}

	/**
	 * Draw the palette
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension d = getSize();
		Point p = this.getLocation();
		StandardTable table = new StandardTable();
		list = table.getAllAcids();
		cc = new ShadingColorCoder(table.getContrastScaler()); 
		int x = 0;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				AminoAcid a = list[x];

				//don't show the 'dummy' amino acid used for target shapes
				if (a.getAbName().equals("X")) {
					break;
				}

				paintAminoAcid(g, a, j * cellDiameter, i * cellDiameter);

				x++;
			}
		}
	}

	public void paintAminoAcid(Graphics g, AminoAcid a, int x, int y) {

		boolean smallSize = false;
		if (cellRadius < 20) {
			smallSize = true;
		}

		int offset = getStringIndentationConstant(a.getName(), cellRadius);

		if (a.getName().equals("X")) {
			g.setColor(Color.BLUE);
		} else {
			g.setColor(cc.getCellColor(a.getNormalizedHydrophobicIndex()));
		}
		g.fillOval(x, y, cellDiameter, cellDiameter);
		
		//default label color is white
		g.setColor(Color.WHITE);

		//if philic - then add stuff
		if (a.getName().equals("Arg") ||
				a.getName().equals("Lys") ||
				a.getName().equals("His")) {
			g.setColor(Color.blue);
			if (!smallSize) {
				g.drawString("+", x + cellRadius - 15, y + cellRadius);
				g.setColor(Color.BLACK);
			} 
		}

		if (a.getName().equals("Asp") ||
				a.getName().equals("Glu")) {
			g.setColor(Color.red);
			if (!smallSize) {
				g.drawString("-", x + cellRadius - 15, y + cellRadius);
				g.setColor(Color.BLACK);
			} 
		}

		if (a.getName().equals("Asn") ||
				a.getName().equals("Gln") ||
				a.getName().equals("Ser") ||
				a.getName().equals("Thr") ||
				a.getName().equals("Tyr")) {
			g.setColor(Color.green);
			if (!smallSize) {
				g.drawString("*", x + cellRadius - 15, y + cellRadius);
				g.setColor(Color.BLACK);
			} 
		}

		if (smallSize) {
			int fontSize = g.getFont().getSize();
			g.drawString(a.getAbName(), x + cellRadius - fontSize/2, y + cellRadius + fontSize/2);
		} else {
			g.drawString(a.getName(), x + cellRadius - offset, y + cellRadius);
			g.drawString(a.getAbName(), x + cellRadius , y + cellRadius + AB_Y_OFFSET);
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
