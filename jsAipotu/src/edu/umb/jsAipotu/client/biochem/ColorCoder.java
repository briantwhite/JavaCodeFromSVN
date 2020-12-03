// ColorCoder.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota
/* 
 * License Information
 * 
 * This  program  is  free  software;  you can redistribute it and/or modify it 
 * under  the  terms  of  the GNU General Public License  as  published  by the 
 * Free  Software  Foundation; either version 2  of  the  License,  or (at your 
 * option) any later version.
 */

package edu.umb.jsAipotu.client.biochem;

import java.awt.Color;

/**
 * Interpolate between colors in HSB model.
 */
public abstract class ColorCoder {


	public ColorCoder() {
	}

	public ColorCoder(float k) {
	}

	protected Color getCellColor(double hydrophobicIndex) {
		return Color.BLACK;
	}

	protected Color getCellColor(float hydrophobicIndex) {
		return Color.BLACK;
	}

}

