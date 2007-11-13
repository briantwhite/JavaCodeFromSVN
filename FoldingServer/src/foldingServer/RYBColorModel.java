// RYBColor.java
//
//
// Copyright 2004-2005 MGX Team UMB.  All rights reserved.
/* 
 * License Information
 * 
 * This  program  is  free  software;  you can redistribute it and/or modify it 
 * under  the  terms  of  the GNU General Public License  as  published  by the 
 * Free  Software  Foundation; either version 2  of  the  License,  or (at your 
 * option) any later version.
 */

package foldingServer;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class representing RYBColor chart. Model the standard RYB color model.
 * 
 * @author Namita Singla
 * 
 * -- modified by TJ, makes RYBColorChart singleton
 * modified by BW - totally different method
 */
public class RYBColorModel extends ColorModel {

	private Color[] numberToColorMap = {
			// colors are odeled by bits in integer
			//    	1's = blue
			//		2's = yellow
			//		4's = red
			Color.WHITE,			 // 000 0
			Color.BLUE,			 // 001 1
			Color.YELLOW,	     // 010 2 
			Color.GREEN,			 // 011 3
			Color.RED,			 // 100 4
			new Color(255,0,255), // 101 5
			new Color(255,100,0),	 // 110 6 Orange
			Color.BLACK			 // 111 7
	};
	
	private HashMap colorToNumberMap;

	/**
	 * Constructor
	 */
	public RYBColorModel() { 
		super();
		colorToNumberMap = new HashMap();
		for (int i = 0; i < numberToColorMap.length; i++) {
			colorToNumberMap.put((Color)numberToColorMap[i], new Integer(i));
		}
	}

	/**
	 * Color by amino acids found in core
	 * 	accumulate color as amino acids found
	 */
	public Color colorByAminoAcid(Color c, AcidInChain a) {
		if (a.getName().equalsIgnoreCase("phe"))
			c = mixTwoColors(c, Color.red);
		if (a.getName().equalsIgnoreCase("tyr"))
			c = mixTwoColors(c, Color.blue);
		if (a.getName().equalsIgnoreCase("trp"))
			c = mixTwoColors(c, Color.yellow);
		return c;
	}


	/**
	 * Mix two colors and return the result
	 * @param a
	 * @param b
	 * @return
	 */
	public Color mixTwoColors(Color a, Color b) {
		int aNum = ((Integer)colorToNumberMap.get(a)).intValue();
		int bNum = ((Integer)colorToNumberMap.get(b)).intValue();
		return numberToColorMap[aNum | bNum];
	}

	public Color colorAaNameText(AminoAcid a) {
		if (a.getName().equals("Arg") ||
				a.getName().equals("Lys") ||
				a.getName().equals("His")) {
			return Color.BLUE;
		}
		if (a.getName().equals("Asp") ||
				a.getName().equals("Glu")) {
			return Color.RED;
		}
		return Color.black;
	}

}
