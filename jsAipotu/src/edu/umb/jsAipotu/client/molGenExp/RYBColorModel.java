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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;

import edu.umb.jsAipotu.client.JsAipotu;
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
 */
public class RYBColorModel extends ColorModel {

	ArrayList<AcidInChain> hydrophobics;
	ArrayList<AcidInChain> hydrophilics;
	ArrayList<CssColor> coreColors;

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
	private HashMap<String, CssColor> nameToColorMap;

	/**
	 * Constructor
	 */
	public RYBColorModel() { 
		super();

		colorToNumberMap = new HashMap<CssColor, Integer>();
		for (int i = 0; i < numberToColorMap.length; i++) {
			colorToNumberMap.put((CssColor)numberToColorMap[i], new Integer(i));
		}

		nameToColorMap = new HashMap<String, CssColor>();
		for (int i = 0; i < numberToColorMap.length; i++) {
			nameToColorMap.put(numberToColorNameMap[i], numberToColorMap[i]);
		}

	}

	public CssColor getProteinColor(Grid grid) throws PaintedInACornerFoldingException {
		JsAipotu.consoleLog("RYBcm 109");
		CssColor color = CssColor.make(255, 255, 255);
		hydrophobics = new ArrayList<AcidInChain>();
		hydrophilics = new ArrayList<AcidInChain>();
		coreColors = new ArrayList<CssColor>();

		HexGrid realGrid = (HexGrid)grid;
		int numAcids = grid.getPP().getLength();
		Direction[] allDirections = grid.getAllDirections();
		if (numAcids < 13)
			return CssColor.make(255, 255, 255);
		categorizeAcids(grid);
		if (hydrophobics.size() < 7 || hydrophilics.size() < 6)
			return CssColor.make(255, 255, 255);
		CssColor c = CssColor.make(255, 255, 255);
		for (int i = 0; i < hydrophobics.size(); i++) {
			c = CssColor.make(255, 255, 255);
			AcidInChain a = (AcidInChain) hydrophobics.get(i);
			int d;
			for (d = 0; d < allDirections.length; d++) {
				AcidInChain ac = realGrid.get(
						realGrid.nextCell(allDirections[d], a.xyz));

				if (ac == null || !hydrophobics.contains(ac))
					break;
				else if (hydrophobics.contains(ac)) {
					c = colorByAminoAcid(c, ac);
					JsAipotu.consoleLog("RYBcm 135: colored by aa:" + c.value());
				}
			}
			if (d == allDirections.length) {
				c = colorByAminoAcid(c, a);
				JsAipotu.consoleLog("RYBcm 140: colored by aa:" + c.value());
				coreColors.add(c);
				JsAipotu.consoleLog("RYBCM 143: coreColors.toString()=" + coreColors.toString());
			}
		}
		JsAipotu.consoleLog("RYBcm 142: coreColors.size()=" + coreColors.size());
		JsAipotu.consoleLog("RYBcm 143: coreColors.get(0).value()=" + coreColors.get(0).value());
		if (coreColors.size() > 0) {
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
		CssColor color = (CssColor) coreColors.get(0);
		JsAipotu.consoleLog("RYBcolormodel 161: first color=" + coreColors.get(0).value());
		for (int i = 1; i < coreColors.size(); i++) {
			JsAipotu.consoleLog("RYBcolormodel 163: next color=" + coreColors.get(i).value());
			color = mixTwoColors(color, 
					(CssColor) coreColors.get(i));
		}
		return color;
	}


	/**
	 * Color by amino acids found in core
	 * 	accumulate color as amino acids found
	 */
	private CssColor colorByAminoAcid(CssColor c, AcidInChain a) {
		if (a.getName().equalsIgnoreCase("phe"))
			c = mixTwoColors(c, CssColor.make(255, 0, 0));
		if (a.getName().equalsIgnoreCase("tyr"))
			c = mixTwoColors(c, CssColor.make(0, 0, 255));
		if (a.getName().equalsIgnoreCase("trp"))
			c = mixTwoColors(c, CssColor.make(255, 255, 0));
		JsAipotu.consoleLog("RYBcm 186: made color:" + c.value());
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
		if (nameToColorMap.containsKey(c)) result = nameToColorMap.get(c);
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
