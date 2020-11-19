package edu.umb.jsAipotu.biochem;

import java.awt.Color;

public class FoldedAndColoredProtein {
	
	private String proteinString;
	private Color color;
	
	public FoldedAndColoredProtein(String proteinString, Color color) {
		this.proteinString = proteinString;
		this.color = color;
	}

	public String getProteinString() {
		return proteinString;
	}

	public Color getColor() {
		return color;
	}

}
