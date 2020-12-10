//AminoAcid.java


//Copyright 2004, Ethan Bolker and Bogdan Calota
/* 
 * License Information
 * 
 * This  program  is  free  software;  you can redistribute it and/or modify it 
 * under  the  terms  of  the GNU General Public License  as  published  by the 
 * Free  Software  Foundation; either version 2  of  the  License,  or (at your 
 * option) any later version.
 */
//Modified by Namita, Ruchi (NR) on 10/17/2004

package edu.umb.jsAipotu.client.biochem;

import java.awt.Color;

import com.google.gwt.canvas.dom.client.Context2d;

import edu.umb.jsAipotu.client.preferences.GlobalDefaults;

/**
 * Model an AminoAcid
 */
public class AminoAcid {
	/**
	 * The name of this AminoAcid.
	 */
	private String name;

	// added by TJ -- abbreviate name of an Amino acid
	private String abName;

	/**
	 * The hydrophobic index tells the folding how much this AminoAcid wants to
	 * be on the inside.
	 */
	protected double hydrophobicIndex;

	protected int hydrogenbondIndex; //Added by NR

	protected int ionicIndex; //Added by NR

	/**
	 * The hydrophobic index normalized to have a value between +-1, so suitable
	 * for use in color display.
	 */
	private float normalizedHydrophobicIndex;

	public AminoAcid(String abName, String name, double hydrophobicIndex,
			int hydrogenbondIndex, int ionicIndex) {
		this.abName = abName;
		this.name = name;
		this.hydrophobicIndex = hydrophobicIndex;
		this.hydrogenbondIndex = hydrogenbondIndex;
		this.ionicIndex = ionicIndex;
		this.normalizedHydrophobicIndex = (float) hydrophobicIndex;
	}

	/**
	 * The default name in the hydroPhobicIndex, as a String.
	 */
	public AminoAcid(double hydrophobicIndex, int hydrogenbondIndex,
			int ionicIndex) {
		this("", "" + hydrophobicIndex, hydrophobicIndex, hydrogenbondIndex,
				ionicIndex);
	}

	public double getHydrophobicIndex() {
		return hydrophobicIndex;
	}

	public int gethydrogenbondIndex() {
		return hydrogenbondIndex;
	}

	public int getionicIndex() {
		return ionicIndex;
	}

	public float getNormalizedHydrophobicIndex() {
		return normalizedHydrophobicIndex;
	}

	public void setNormalizedHydrophobicIndex(double val) {
		normalizedHydrophobicIndex = (float) val;
	}

	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public String getAbName(){
		return abName;
	}

	public void paint(Context2d g2d, ColorCoder cc, int x, int y) {

		final int AB_Y_OFFSET = 13;

		int offset = getStringIndentationConstant(name, GlobalDefaults.aaRadius);

		
		g2d.beginPath();
		g2d.arc(x, y, GlobalDefaults.aaRadius, 0, 2 * Math.PI);
		g2d.setFillStyle(cc.getCellColor(normalizedHydrophobicIndex));
		g2d.fill();

		//default label color is white
		g2d.setFillStyle("white");

		//if philic - then add stuff
		if (name.equals("Arg") ||
				name.equals("Lys") ||
				name.equals("His")) {
			g2d.setFillStyle("blue");
			g2d.fillText("+", 
					x + GlobalDefaults.aaRadius - 15, 
					y + GlobalDefaults.aaRadius);
			g2d.setFillStyle("black");
		}

		if (name.equals("Asp") ||
				name.equals("Glu")) {
			g2d.setFillStyle("red");
			g2d.fillText("-", 
					x + GlobalDefaults.aaRadius - 15, 
					y + GlobalDefaults.aaRadius);
			g2d.setFillStyle("black");
		}

		if (name.equals("Asn") ||
				name.equals("Gln") ||
				name.equals("Ser") ||
				name.equals("Thr") ||
				name.equals("Tyr")) {
			g2d.setFillStyle("green");
			g2d.fillText("*", 
					x + GlobalDefaults.aaRadius - 15, 
					y + GlobalDefaults.aaRadius);
			g2d.setFillStyle("black");
		}
		
		g2d.fillText(name, 
				x + GlobalDefaults.aaRadius - offset, 
				y + GlobalDefaults.aaRadius);
		g2d.fillText(abName, 
				x + GlobalDefaults.aaRadius , 
				y + GlobalDefaults.aaRadius + AB_Y_OFFSET);
	}

	/**
	 * Returns constants used for center the name of the amino acid
	 */
	protected int getStringIndentationConstant(String name, int r) {
		// the values returned are hardcoded with values that
		//   look best when the canvas is drawn. Their value
		//   was establish through trials, and best was picked.

		int length = name.trim().length();
		if (length == 1) // 1
			return 0;
		else if (length == 2) // -1
			return 0;
		else if (length == 3) // 0.x
			return (int) (1 / 5f * r);
		else if (length == 4) // -0.x
			return (int) (1 / 2f * r);
		else if (length == 5) // -0.xx
			return (int) (2 / 3f * r);
		else
			// length == 6. can't be longer. -0.xxx
			return (int) (3 / 4f * r);
	}

}
