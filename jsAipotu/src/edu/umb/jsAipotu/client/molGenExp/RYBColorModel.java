//RYBColor.java


//Copyright 2004-2005 MGX Team UMB.  All rights reserved.
/* 
 * License Information
 * 
 * This  program  is  free  software;  you can redistribute it and/or modify it 
 * under  the  terms  of  the GNU General Public License  as  published  by the 
 * Free  Software  Foundation; either version 2  of  the  License,  or (at your 
 * option) any later version.
 */

package edu.umb.jsAipotu.client.molGenExp;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.resources.client.ImageResource;

import edu.umb.jsAipotu.client.biochem.AcidInChain;
import edu.umb.jsAipotu.client.biochem.AminoAcid;
import edu.umb.jsAipotu.client.biochem.ColorModel;
import edu.umb.jsAipotu.client.biochem.Direction;
import edu.umb.jsAipotu.client.biochem.Grid;
import edu.umb.jsAipotu.client.biochem.HexGrid;
import edu.umb.jsAipotu.client.biochem.PaintedInACornerFoldingException;
import edu.umb.jsAipotu.client.preferences.GlobalDefaults;
import edu.umb.jsAipotu.client.resources.Resources;

/**
 * Class representing RYBColor chart. Model the standard RYB color model.
 * 
 * @author Namita Singla
 * 
 * -- modified by TJ, makes RYBColorChart singleton
 * modified by BW - totally different method
 * 
 * 1/15/21 notes for js version
 * 	can't take a CssColor out of an ArrayList (or, probably, any other complex data structure)
 *   because of class cast issues https://stackoverflow.com/questions/19748832/how-to-keep-a-list-of-csscolor-instances
 *  so, when you need to pull out a CssColor from an ArrayList, etc, don't save them as colors but
 *    save them as CssColor.toString which looks like "rgb[255,0,0]"
 *    and use CssColor.make(string) to re-construct the color
 */
public class RYBColorModel extends ColorModel {

	ArrayList<AcidInChain> hydrophobics;
	ArrayList<AcidInChain> hydrophilics;
	ArrayList<String> coreColorStrings;

	private CssColor[] numberToColorMap = {
			// colors are modeled by bits in integer
			//    	1's = blue
			//		2's = yellow
			//		4's = red
			// use RGB values to make the lookup work
			CssColor.make(255, 255, 255),	 // White   000 0
			CssColor.make(  0,   0, 255),	 // Blue    001 1
			CssColor.make(255, 255,   0),	 // Yellow  010 2 
			CssColor.make(  0, 255,   0),	 // Green   011 3
			CssColor.make(255,   0,   0),	 // Red     100 4
			CssColor.make(138,  43, 226),    // Purple  101 5 
			CssColor.make(255, 140,   0),	 // Orange  110 6  
			CssColor.make(  0,   0,   0)     // Black   111 7
	};

	private String[] numberToColorNameMap = {
			"White",
			"Blue",
			"Yellow",
			"Green",
			"Red",
			"Purple",
			"Orange",
			"Black"
	};

	private ImageResource[] numberToImageResourceMap = {
			Resources.INSTANCE.whiteFlowerImage(),
			Resources.INSTANCE.blueFlowerImage(),
			Resources.INSTANCE.yellowFlowerImage(),
			Resources.INSTANCE.greenFlowerImage(),
			Resources.INSTANCE.redFlowerImage(),
			Resources.INSTANCE.purpleFlowerImage(),
			Resources.INSTANCE.orangeFlowerImage(),
			Resources.INSTANCE.blackFlowerImage()
	};

	private HashMap<CssColor, Integer> colorToNumberMap;
	private HashMap<String, String> nameToColorStringMap;

	/**
	 * Constructor
	 */
	public RYBColorModel() { 
		super();

		colorToNumberMap = new HashMap<CssColor, Integer>();
		for (int i = 0; i < numberToColorMap.length; i++) {
			colorToNumberMap.put(numberToColorMap[i], new Integer(i));
		}

		nameToColorStringMap = new HashMap<String, String>();
		for (int i = 0; i < numberToColorMap.length; i++) {
			nameToColorStringMap.put(numberToColorNameMap[i], numberToColorMap[i].toString());
		}

	}

	public CssColor getProteinColor(Grid grid) throws PaintedInACornerFoldingException {
		CssColor color = getColorFromString("White");
		hydrophobics = new ArrayList<AcidInChain>();
		hydrophilics = new ArrayList<AcidInChain>();
		coreColorStrings = new ArrayList<String>();

		HexGrid realGrid = (HexGrid)grid;
		int numAcids = grid.getPP().getLength();
		Direction[] allDirections = grid.getAllDirections();
		if (numAcids < 13)
			return getColorFromString("White");
		categorizeAcids(grid);
		if (hydrophobics.size() < 7 || hydrophilics.size() < 6)
			return getColorFromString("White");
		CssColor c = getColorFromString("White");
		for (int i = 0; i < hydrophobics.size(); i++) {
			c = getColorFromString("White");
			AcidInChain a = (AcidInChain) hydrophobics.get(i);
			int d;
			for (d = 0; d < allDirections.length; d++) {
				AcidInChain ac = realGrid.get(
						realGrid.nextCell(allDirections[d], a.xyz));

				if (ac == null || !hydrophobics.contains(ac))
					break;
				else if (hydrophobics.contains(ac)) {
					c = colorByAminoAcid(c, ac);
				}
			}
			if (d == allDirections.length) {
				c = colorByAminoAcid(c, a);
				coreColorStrings.add(c.toString());
			}
		}
		if (coreColorStrings.size() > 0) {
			color = mixHexagonalCores();
		}
		return color;
	}

	public void categorizeAcids(Grid grid) {
		int numAcids = grid.getPP().getLength();
		AcidInChain[] acids = grid.getPP().getAcidArray();
		for (int i = 0; i < numAcids; i++) {
			AcidInChain a = acids[i];
			if (a.getHydrophobicIndex() >= 0
					|| a.getName().equalsIgnoreCase("tyr"))
				hydrophobics.add(a);
			else
				hydrophilics.add(a);
		}
	}

	private CssColor mixHexagonalCores() {
		CssColor color = CssColor.make(coreColorStrings.get(0));
		for (int i = 1; i < coreColorStrings.size(); i++) {
			color = mixTwoColors(color, 
					CssColor.make(coreColorStrings.get(i)));
		}
		return color;
	}


	/**
	 * Color by amino acids found in core
	 * 	accumulate color as amino acids found
	 */
	private CssColor colorByAminoAcid(CssColor c, AcidInChain a) {
		if (a.getName().equalsIgnoreCase("phe"))
			c = mixTwoColors(c, getColorFromString("Red"));
		if (a.getName().equalsIgnoreCase("tyr"))
			c = mixTwoColors(c, getColorFromString("Blue"));
		if (a.getName().equalsIgnoreCase("trp"))
			c = mixTwoColors(c, getColorFromString("Yellow"));
		return c;
	}


	/**
	 * Mix two colors and return the result
	 * @param a
	 * @param b
	 * @return
	 */
	public CssColor mixTwoColors(CssColor a, CssColor b) {
		// null colors mean that it's a dead organism because
		//  one or both proteins is folded in a corner
		if ((a == null) || (b == null)) {
			return GlobalDefaults.DEAD_COLOR;
		}
		int aNum = ((Integer)colorToNumberMap.get(a)).intValue();
		int bNum = ((Integer)colorToNumberMap.get(b)).intValue();
		return numberToColorMap[aNum | bNum];
	}

	public int getColorNumber(CssColor c) {
		if (colorToNumberMap.get(c) == null) return -1;			// if not on the list, it's the
		//  dead color
		return ((Integer)colorToNumberMap.get(c)).intValue();
	}

	public CssColor colorAaNameText(AminoAcid a) {
		if (a.getName().equals("Arg") ||
				a.getName().equals("Lys") ||
				a.getName().equals("His")) {
			return CssColor.make("blue");
		}
		if (a.getName().equals("Asp") ||
				a.getName().equals("Glu")) {
			return CssColor.make("red");
		}
		return CssColor.make("black");
	}

	public String getColorName(CssColor c) {
		int colorNumber = getColorNumber(c);
		if (colorNumber == -1) return null;
		return numberToColorNameMap[getColorNumber(c)];
	}

	public CssColor getColorFromString(String c) {
		CssColor result = null;
		if (nameToColorStringMap.containsKey(c)) result = CssColor.make(nameToColorStringMap.get(c));
		return result;
	}


	public ImageResource getImageResourceFromColor(CssColor c) {
		int n = getColorNumber(c);
		if (n == -1) {
			return Resources.INSTANCE.blankFlowerImage();
		}
		return numberToImageResourceMap[n];
	}

}
