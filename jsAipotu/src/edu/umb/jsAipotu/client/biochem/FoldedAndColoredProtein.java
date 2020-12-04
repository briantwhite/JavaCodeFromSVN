package edu.umb.jsAipotu.client.biochem;


import com.google.gwt.canvas.dom.client.CssColor;

public class FoldedAndColoredProtein {
	
	private String proteinString;
	private CssColor color;
	
	public FoldedAndColoredProtein(String proteinString, CssColor color) {
		this.proteinString = proteinString;
		this.color = color;
	}

	public String getProteinString() {
		return proteinString;
	}

	public CssColor getColor() {
		return color;
	}

}
