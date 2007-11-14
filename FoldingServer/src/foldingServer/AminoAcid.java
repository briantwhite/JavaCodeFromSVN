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

package foldingServer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;


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

	protected boolean ssBondIndex; // added by BW

	/**
	 * The hydrophobic index normalized to have a value between +-1, so suitable
	 * for use in color display.
	 */
	private float normalizedHydrophobicIndex;

	public AminoAcid(String abName, String name, double hydrophobicIndex,
			int hydrogenbondIndex, int ionicIndex, boolean ssBondIndex) {
		this.abName = abName;
		this.name = name;
		this.hydrophobicIndex = hydrophobicIndex;
		this.hydrogenbondIndex = hydrogenbondIndex;
		this.ionicIndex = ionicIndex;
		this.ssBondIndex = ssBondIndex;
		this.normalizedHydrophobicIndex = (float) hydrophobicIndex;
	}

	/**
	 * The default name in the hydroPhobicIndex, as a String.
	 */
	public AminoAcid(double hydrophobicIndex, int hydrogenbondIndex,
			int ionicIndex) {
		this("", "" + hydrophobicIndex, hydrophobicIndex, hydrogenbondIndex,
				ionicIndex, false);
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

	public boolean getssIndex() {
		return ssBondIndex;
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

	public void paint(Graphics g, int aminoAcidRadius, ColorCoder cc, int x, int y) {

		final int AB_Y_OFFSET = 13;

		int aminoAcidDiameter = aminoAcidRadius * 2;

		Graphics2D g2d = (Graphics2D)g;

		boolean smallSize = false;
		if (aminoAcidRadius < FoldingServer.SMALL_RADIUS_CUTOFF) {
			smallSize = true;
		}

		int offset = getStringIndentationConstant(name, aminoAcidRadius);

		if (name.equals("XXX")) {
			g2d.setColor(Color.BLUE);
		} else {
			g2d.setColor(cc.getCellColor(normalizedHydrophobicIndex));
		}
		g2d.fillOval(x, y, aminoAcidDiameter, aminoAcidDiameter);

		//default label color is white
		g2d.setColor(Color.WHITE);

		//if philic - then add stuff
		if (name.equals("Arg") ||
				name.equals("Lys") ||
				name.equals("His")) {
			g2d.setColor(Color.blue);
			if (!smallSize) {
				g2d.drawString("+", x + aminoAcidRadius - 15, y + aminoAcidRadius);
				g2d.setColor(Color.BLACK);
			} 
		}

		if (name.equals("Asp") ||
				name.equals("Glu")) {
			g2d.setColor(Color.red);
			if (!smallSize) {
				g2d.drawString("-", x + aminoAcidRadius - 15, y + aminoAcidRadius);
				g2d.setColor(Color.BLACK);
			} 
		}

		if (name.equals("Asn") ||
				name.equals("Gln") ||
				name.equals("Ser") ||
				name.equals("Thr") ||
				name.equals("Tyr")) {
			g2d.setColor(Color.green);
			if (!smallSize) {
				g2d.drawString("*", x + aminoAcidRadius - 15, y + aminoAcidRadius);
				g2d.setColor(Color.BLACK);
			} 
		}

		if (smallSize) {
			Rectangle2D labelBounds = 
				g2d.getFont().getStringBounds(abName, g2d.getFontRenderContext());
			if (!name.equals("XXX")) {
				g2d.drawString(abName, 
						x + aminoAcidRadius - (int)labelBounds.getWidth()/2, 
						y + aminoAcidRadius + (int)labelBounds.getHeight()/3);
			}
		} else {
			if (!name.equals("XXX")) {
				g2d.drawString(name, x + aminoAcidRadius - offset, y + aminoAcidRadius);
				g2d.drawString(abName, x + aminoAcidRadius , y + aminoAcidRadius + AB_Y_OFFSET);
			}
		}
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
