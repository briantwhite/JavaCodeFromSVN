// AminoAcid.java
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
// Modified by Namita, Ruchi (NR) on 10/17/2004

package protex;

import java.io.Serializable;

/**
 * Model an AminoAcid
 */
public class AminoAcid implements Serializable {
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
//	
//	public String getFullName(){
//		return name + "(" + abName + ")";
//	}
}
