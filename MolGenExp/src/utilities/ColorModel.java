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

package utilities;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashMap;

import biochem.AminoAcid;
import biochem.Grid;
import biochem.PaintedInACornerFoldingException;

/**
 * Class representing a generic color model for coloring proteins by 
 * the amino acids present in the core.
 * 
 * @author Brian White
 * 

 */
public abstract class ColorModel {

	private Color[] numberToColorMap;
	
	private HashMap colorToNumberMap;

	/**
	 * Constructor
	 */
	public ColorModel() {
		
	}

	public abstract int getColorNumber(Color c);
	
	public abstract String getColorName(Color c);
	
	public abstract void categorizeAcids(Grid grid);

	/**
	 * compute the color of a protein folded on a grid
	 * @param grid
	 * @return
	 * @throws PaintedInACornerFoldingException 
	 */
	public abstract Color getProteinColor(Grid grid) throws PaintedInACornerFoldingException;

	/**
	 * Mix two colors and return the result
	 * @param a
	 * @param b
	 * @return
	 */
	public abstract Color mixTwoColors(Color a, Color b);
	
	// color the names of amino acids by their properties
	public abstract Color colorAaNameText(AminoAcid aa);
}
