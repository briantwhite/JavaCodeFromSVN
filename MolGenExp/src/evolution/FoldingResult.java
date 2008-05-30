package evolution;

import java.awt.Color;

public class FoldingResult {
	
	private String aaSeq;
	private String proteinString;
	private Color color;
	
	public FoldingResult(String aaSeq, String proteinString, Color color) {
		this.aaSeq = aaSeq;
		this.proteinString = proteinString;
		this.color = color;
	}

	public String getAaSeq() {
		return aaSeq;
	}

	public String getProteinString() {
		return proteinString;
	}

	public Color getColor() {
		return color;
	}

}
