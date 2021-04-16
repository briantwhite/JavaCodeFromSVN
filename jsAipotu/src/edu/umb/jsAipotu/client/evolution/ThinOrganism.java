package edu.umb.jsAipotu.client.evolution;

import java.util.Random;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.user.client.ui.FocusPanel;

//class with just DNA and color - so it's smaller
//for use with evolution
//don't bother with pictures unless needed
public class ThinOrganism extends FocusPanel {
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
		this.setStyleName("thinOrganism");
		this.getElement().getStyle().setBackgroundColor(overallColor.toString());
	}
	
	// empty constructor for starting screen
	protected ThinOrganism() {
		this.setStyleName("thinOrganism");
		this.getElement().getStyle().setBackgroundColor("lightgray");
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
		Random r = new Random();
		if (r.nextDouble() > 0.5) {
			return dna1;
		} else {
			return dna2;
		}
	}
}