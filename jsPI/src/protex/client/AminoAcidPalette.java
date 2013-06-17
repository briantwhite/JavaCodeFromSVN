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

package protex.client;

import protex.client.java.awt.Color;
import protex.client.java.awt.Graphics;
import protex.client.java.awt.geom.Ellipse2D;
import protex.client.java.awt.geom.Rectangle2D;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;

/**
 * AminoAcidPalette displays all of the AminoAcid objects. 
 * 
 * @author Namita Singla/MGX Team UMB
 * @version v0.1
 */
public class AminoAcidPalette {

	private static int cellRadius = 20;
	private static int cellDiameter = 2 * cellRadius;
	private static int AB_Y_OFFSET = 13;
	private int row, column, columnWidth, rowHeight;
	private AminoAcid selectedAminoAcid;
	private AminoAcid[] list;
	
	public Canvas canvas;
	private Context2d ctx;
	private Graphics g;

	private boolean admin;
	
	public AminoAcidPalette(int width, int height, int row, int column, boolean admin) {

		canvas = Canvas.createIfSupported();
		canvas.addStyleName("protex-canvas");
		canvas.setPixelSize(width, height);
		canvas.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceHeight(height);
		ctx = canvas.getContext2d();
		g = new Graphics(ctx);
		
		//This sets the background. Use css instead?
		g.setFillColor(Protex.SS_BONDS_OFF_BACKGROUND);
		g.fill(new Rectangle2D.Double(0, 0, width, height));

		this.row = row;
		this.column = column;
		this.admin = admin;
		paint();
	}

	/**
	 * Draw the palette
	 */
	public void paint() {
		Color darkGreen = new Color(48, 200, 48); //Replaces original green that was too light
		columnWidth = cellDiameter + cellRadius / 4;
		rowHeight = columnWidth;
		StandardTable table = StandardTable.getInstance();
		list = table.getAllAcids();
		ColorCoder cc = new ShadingColorCoder(table.getContrastScaler());
		int x = 0;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				AminoAcid a = list[x];
				
				//don't show the 'dummy' amino acid used for target shapes
				if (a.getAbName().equals("X")) {
					break;
				}
				
				int offset = getStringIndentationConstant(a.getName(), cellRadius);
				int abOffset = getStringIndentationConstant(a.getAbName(), cellRadius);
				
				//g.setColor(cc.getCellColor(a.getNormalizedHydrophobicIndex()));
				//g.fillOval(j * columnWidth, i * rowHeight, cellDiameter, cellDiameter);
				g.setFillColor(cc.getCellColor(a.getNormalizedHydrophobicIndex()));
				g.fill(new Ellipse2D.Double(j * columnWidth, i * rowHeight,
											cellDiameter,
											cellDiameter));

				// default color for names is white
				//g.setColor(Color.white);
				Color aaLetterColor = Color.white;
				
				//if philic - then add stuff
				if (a.getName().equals("Arg") ||
						a.getName().equals("Lys") ||
						a.getName().equals("His")) {
					//g.setColor(Color.blue);
					//g.drawString("+", j * columnWidth + cellRadius - 15, i
						//* rowHeight + cellRadius);
					g.drawFilledString("+", j * columnWidth + cellRadius - 15, i
							* rowHeight + cellRadius,  "12pt sans-serif", Color.blue);
					//g.setColor(Color.BLACK);
					aaLetterColor = Color.black;
				}
				
				if (a.getName().equals("Asp") ||
						a.getName().equals("Glu")) {
					//g.setColor(Color.red);
					//g.drawString("-", j * columnWidth + cellRadius - 15, i
						//* rowHeight + cellRadius);
					g.drawFilledString("\u2212", j * columnWidth + cellRadius - 15, i
							* rowHeight + cellRadius, "12pt sans-serif", Color.red);
					//g.setColor(Color.BLACK);
					aaLetterColor = Color.black;
				}
				
				if (a.getName().equals("Asn") ||
						a.getName().equals("Gln") ||
						a.getName().equals("Ser") ||
						a.getName().equals("Thr") ||
						a.getName().equals("Tyr")) {
					//g.setColor(Color.green);
					//g.drawString("*", j * columnWidth + cellRadius - 15, i
						//* rowHeight + cellRadius);
					g.drawFilledString("\u25CF", j * columnWidth + cellRadius - 15, i
							* rowHeight + cellRadius, "12pt sans-serif", darkGreen);
					//g.setColor(Color.BLACK);
					aaLetterColor = Color.black;
				}
				g.drawFilledString(a.getName(), j * columnWidth + cellRadius - offset, i
						* rowHeight + cellRadius, "8pt sans-serif", aaLetterColor);
				//g.drawString(a.getName(), j * columnWidth + cellRadius - offset, i
						//* rowHeight + cellRadius);
				g.drawFilledString(a.getAbName(), j * columnWidth + cellRadius - abOffset, i 
						* rowHeight + cellRadius + AB_Y_OFFSET, "8pt sans-serif", aaLetterColor);
				//g.drawString(a.getAbName(), j * columnWidth + cellRadius - abOffset, i 
						//* rowHeight + cellRadius + AB_Y_OFFSET);
				g.setColor(Color.black);
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
