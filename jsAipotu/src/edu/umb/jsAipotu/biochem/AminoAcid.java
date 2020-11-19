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

package edu.umb.jsAipotu.biochem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import preferences.GlobalDefaults;




import molGenExp.MolGenExp;

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

	public void paint(Graphics g, ColorCoder cc, int x, int y) {

		final int AB_Y_OFFSET = 13;

		int aminoAcidDiameter = GlobalDefaults.aaRadius * 2;

		Graphics2D g2d = (Graphics2D)g;

		int offset = getStringIndentationConstant(name, GlobalDefaults.aaRadius);

		g2d.setColor(cc.getCellColor(normalizedHydrophobicIndex));

		g2d.fillOval(x, y, aminoAcidDiameter, aminoAcidDiameter);

		//default label color is white
		g2d.setColor(Color.WHITE);

		//if philic - then add stuff
		if (name.equals("Arg") ||
				name.equals("Lys") ||
				name.equals("His")) {
			g2d.setColor(Color.blue);
			g2d.drawString("+", 
					x + GlobalDefaults.aaRadius - 15, 
					y + GlobalDefaults.aaRadius);
			g2d.setColor(Color.BLACK);
		}

		if (name.equals("Asp") ||
				name.equals("Glu")) {
			g2d.setColor(Color.red);
			g2d.drawString("-", 
					x + GlobalDefaults.aaRadius - 15, 
					y + GlobalDefaults.aaRadius);
			g2d.setColor(Color.BLACK);
		}

		if (name.equals("Asn") ||
				name.equals("Gln") ||
				name.equals("Ser") ||
				name.equals("Thr") ||
				name.equals("Tyr")) {
			g2d.setColor(Color.green);
			g2d.drawString("*", 
					x + GlobalDefaults.aaRadius - 15, 
					y + GlobalDefaults.aaRadius);
			g2d.setColor(Color.BLACK);
		}
		
		g2d.drawString(name, 
				x + GlobalDefaults.aaRadius - offset, 
				y + GlobalDefaults.aaRadius);
		g2d.drawString(abName, 
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
