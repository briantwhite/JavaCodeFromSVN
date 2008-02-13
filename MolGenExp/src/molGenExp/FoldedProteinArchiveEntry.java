package molGenExp;

import java.awt.Color;

public class FoldedProteinArchiveEntry {
	
	private String proteinString;
	private Color color;
	
	public FoldedProteinArchiveEntry(String proteinString, Color color) {
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
