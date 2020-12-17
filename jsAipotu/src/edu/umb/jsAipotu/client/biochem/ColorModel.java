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

package edu.umb.jsAipotu.client.biochem;

import java.util.HashMap;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.resources.client.ImageResource;


/**
 * Class representing a generic color model for coloring proteins by 
 * the amino acids present in the core.
 * 
 * @author Brian White
 * 

 */
public abstract class ColorModel {

	private CssColor[] numberToColorMap;
	
	private HashMap<CssColor, Integer> colorToNumberMap;

	/**
	 * Constructor
	 */
	public ColorModel() {
		
	}

	public abstract int getColorNumber(CssColor c);
	
	public abstract String getColorName(CssColor c);
	
	public abstract void categorizeAcids(Grid grid);

	/**
	 * compute the color of a protein folded on a grid
	 * @param grid
	 * @return
	 * @throws PaintedInACornerFoldingException 
	 */
	public abstract CssColor getProteinColor(Grid grid) throws PaintedInACornerFoldingException;

	/**
	 * Mix two colors and return the result
	 * @param a
	 * @param b
	 * @return
	 */
	public abstract CssColor mixTwoColors(CssColor a, CssColor b);
	
	// color the names of amino acids by their properties
	public abstract CssColor colorAaNameText(AminoAcid aa);
	
	public abstract CssColor getColorFromString(String c);
	
	public abstract ImageResource getImageResourceFromColor(CssColor c);
}
