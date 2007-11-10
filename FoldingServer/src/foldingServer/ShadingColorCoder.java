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

package foldingServer;

import java.awt.Color;
import java.util.Iterator;

/**
 * just shade between different intensities of one color.
 */
public class ShadingColorCoder extends ColorCoder {

	//the starting max color - for the most 'phobic one
	private float maxRed = 1.0f;
	private float maxGreen = 1.0f;
	private float maxBlue = 1.0f;
		
	public ShadingColorCoder() {
	}

	public ShadingColorCoder(float f) {
		this();
	}

	protected Color getCellColor(double hydrophobicIndex) {
		return getCellColor((float) hydrophobicIndex);
	}

	protected Color getCellColor(float hydrophobicIndex) {
		float x = (0.99f) * (1 - (hydrophobicIndex * hydrophobicIndex * hydrophobicIndex)); 
		return new Color((maxRed * x), (maxGreen * x), (maxBlue * x));
	}


        /**
         * Method main for unit testing.
         *
	 */
	public static void main(String[] args) {
		try {
			ShadingColorCoder cc = new ShadingColorCoder();
			AminoAcidTable standard = AminoAcidTable.makeTable("standard");
			Iterator i = standard.getIterator();
			while (i.hasNext()) {
				AminoAcid a = standard.get((String) i.next());
				Color color = cc.getCellColor(a.getHydrophobicIndex());
				Color opp = new Color(-color.getRGB());
				System.out.println(a + " " + color + " " + opp + " "
						+ Integer.toHexString(color.getRGB()) + " "
						+ Integer.toHexString(-color.getRGB()));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

