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

package biochem;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Class representing a generic color model for coloring proteins by 
 * the amino acids present in the core.
 * 
 * @author Brian White
 * 

 */
public abstract class ColorModel implements Serializable {

	private Color[] numberToColorMap;
	
	private HashMap colorToNumberMap;

	/**
	 * Constructor
	 */
	public ColorModel() {
		
	}

	/**
	 * Color by amino acids found in core
	 * 	accumulate color as amino acids found
	 */
	public abstract Color colorByAminoAcid(Color c, AcidInChain a);



	/**
	 * Mix two colors and return the result
	 * @param a
	 * @param b
	 * @return
	 */
	public abstract Color mixTwoColors(Color a, Color b);
}
