// BlackColorCoder.java
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

package protex;

import java.awt.Color;

/**
 * Interpolate between colors in HSB model.
 */
public class BlackColorCoder extends ColorCoder {
	private float k = (float) 0.5;

	// It's easy to solve for a, b and c in terms of k:

	private float a;

	private float b;

	private float c;

	public BlackColorCoder() {
		this((float) 0.5);
	}

	public BlackColorCoder(float k) {
		this.k = k;
		a = 4 - 4 * k;
		b = -6 + 6 * k;
		c = 3 - 2 * k;
	}

	protected Color getCellColor(double hydrophobicIndex) {
		return getCellColor((float) hydrophobicIndex);
	}

	protected Color getCellColor(float hydrophobicIndex) {
		// maps hydrophobic index from [-1, 1] into [0, 1]
		float x = (hydrophobicIndex + 1) / 2;

		// transforms linear into cubic interpolation
		float alpha = x * (c + x * (b + a * x));

		// transform the mapping so that [0,1] goes into [1,0]
		//   so that red( now black) is 1 and green( now white) is -1,
		//   where 1 and -1 represent the hydrophobic indices
		float colorIndex = 1.0f - alpha;

		// map the index between 0 and 150 to obtain the color shade
		//   map into [0, 150] and then into [100,250]
		float shade = 100 + colorIndex * 150;

		// create and return color
		return new Color((int) shade, (int) shade, (int) shade);

	}

}

