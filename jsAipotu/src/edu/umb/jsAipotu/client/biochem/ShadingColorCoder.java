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

/**
 * just shade between different intensities of one color.
 */
public class ShadingColorCoder extends ColorCoder {

	//the starting max color - for the most 'phobic one
	private float maxRed = 255.0f;
	private float maxGreen = 255.0f;
	private float maxBlue = 255.0f;

	public ShadingColorCoder() {
	}

	public ShadingColorCoder(float f) {
		this();
	}

	protected String getCellColor(double hydrophobicIndex) {
		return getCellColor((float) hydrophobicIndex);
	}

	protected String getCellColor(float hydrophobicIndex) {
		float x = (0.99f) * (1 - (hydrophobicIndex * hydrophobicIndex * hydrophobicIndex)); 
		return "rgb(" + String.valueOf(Math.round(maxRed * x)) + "," 
		+ String.valueOf(Math.round(maxGreen * x)) + "," 
		+ String.valueOf(Math.round(maxBlue * x)) + ")";

	}

}

