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
		cc = new ShadingColorCoder(table.getContrastScaler()); 
		int x = 0;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				AminoAcid a = list[x];

				//don't show the 'dummy' amino acid used for target shapes
				if (a.getAbName().equals("X")) {
					break;
				}

				paintAminoAcid(g2d, a, j * cellDiameter, i * cellDiameter);

				x++;
			}
		}
	}

	public void paintAminoAcid(Graphics g, AminoAcid a, int x, int y) {

		Graphics2D g2d = (Graphics2D)g;
		
		boolean smallSize = false;
		if (cellRadius < 20) {
			smallSize = true;
		}

		int offset = getStringIndentationConstant(a.getName(), cellRadius);

		if (a.getName().equals("X")) {
			g2d.setColor(Color.BLUE);
		} else {
			g2d.setColor(cc.getCellColor(a.getNormalizedHydrophobicIndex()));
		}
		g2d.fillOval(x, y, cellDiameter, cellDiameter);
		
		//default label color is white
		g2d.setColor(Color.WHITE);

		//if philic - then add stuff
		if (a.getName().equals("Arg") ||
				a.getName().equals("Lys") ||
				a.getName().equals("His")) {
			g2d.setColor(Color.blue);
			if (!smallSize) {
				g2d.drawString("+", x + cellRadius - 15, y + cellRadius);
				g2d.setColor(Color.BLACK);
			} 
		}

		if (a.getName().equals("Asp") ||
				a.getName().equals("Glu")) {
			g2d.setColor(Color.red);
			if (!smallSize) {
				g2d.drawString("-", x + cellRadius - 15, y + cellRadius);
				g2d.setColor(Color.BLACK);
			} 
		}

		if (a.getName().equals("Asn") ||
				a.getName().equals("Gln") ||
				a.getName().equals("Ser") ||
				a.getName().equals("Thr") ||
				a.getName().equals("Tyr")) {
			g2d.setColor(Color.green);
			if (!smallSize) {
				g2d.drawString("*", x + cellRadius - 15, y + cellRadius);
				g2d.setColor(Color.BLACK);
			} 
		}

		if (smallSize) {
			Rectangle2D labelBounds = 
				g2d.getFont().getStringBounds(a.getAbName(), g2d.getFontRenderContext());
			g2d.drawString(a.getAbName(), 
					x + cellRadius - (int)labelBounds.getWidth()/2, 
					y + cellRadius + (int)labelBounds.getHeight()/3);
		} else {
			g2d.drawString(a.getName(), x + cellRadius - offset, y + cellRadius);
			g2d.drawString(a.getAbName(), x + cellRadius , y + cellRadius + AB_Y_OFFSET);
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
