package protex.client;

import protex.client.java.awt.Color;


public class FoldedPolypeptide {
	private boolean ssBondsOn;
	private String aaSeq;
	private Grid fullSizeGrid;
	private String toolTipTextString;
	
	public FoldedPolypeptide(boolean ssBondsOn, String seq, Grid big) {
		this.ssBondsOn = ssBondsOn;
		aaSeq = seq;
		fullSizeGrid = big;
		toolTipTextString = seq;
	}
	
	public boolean getssBondsOn() {
		return ssBondsOn;
	}

	public String getAaSeq() {
		return aaSeq;
	}

	public Grid getFullSizeGrid() {
		return fullSizeGrid;
	}
	
	public String getToolTipTextString() {
		return toolTipTextString;
	}
	
	public void setToolTipTextString(String text) {
		toolTipTextString = text;
	}

}
