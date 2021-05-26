package edu.umb.jsAipotu.client.evolution;


import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.user.client.Random;

import edu.umb.jsAipotu.client.preferences.GlobalDefaults;

//class with just DNA and color - so it's smaller
//for use with evolution
//don't bother with pictures unless needed
public class ThinOrganism {
	private String dna1;
	private String dna2;
	private CssColor color1;
	private CssColor color2;
	private CssColor overallColor;

	protected ThinOrganism(String dna1, String dna2, 
			CssColor color1, CssColor color2, 
			CssColor overallColor) {

		this.dna1 = dna1;
		this.dna2 = dna2;
		this.color1 = color1;
		this.color2 = color2;
		this.overallColor = overallColor;
	}

	// empty constructor for starting screen and a dead ThinOrganism (one with non-foldable protein)
	protected ThinOrganism() {
		this.overallColor = GlobalDefaults.DEAD_COLOR;
	}

	public String getDNA1() {
		return dna1;
	}

	public String getDNA2() {
		return dna2;
	}

	public CssColor getColor1() {
		return color1;
	}

	public CssColor getColor2() {
		return color2;
	}

	public CssColor getOverallColor() {
		return overallColor;
	}

	public String getRandomDNASequence() {
		if (Random.nextInt(2) == 0) {
			return dna1;
		} else {
			return dna2;
		}
	}
}
