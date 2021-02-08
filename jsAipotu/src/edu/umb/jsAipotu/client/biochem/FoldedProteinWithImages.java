package edu.umb.jsAipotu.client.biochem;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.CssColor;

import edu.umb.jsAipotu.client.molGenExp.HistListItem;

public class FoldedProteinWithImages extends HistListItem {
	private String aaSeq;
	private Canvas thumbnailPic;
	private Canvas bigPic;
	private CssColor color;
	private boolean isSelected;
	
	public FoldedProteinWithImages(String aaSeq, 
			Canvas bigPic,
			Canvas thumb, 
			CssColor color) {
		this.aaSeq = aaSeq;
		this.bigPic = bigPic;
		thumbnailPic = thumb;
		this.color = color;
		toolTipText = aaSeq;
		isSelected = false;
	}

	public String getAaSeq() {
		return aaSeq;
	}

	public Canvas getFullSizePic() {
		return bigPic;
	}
	
	public Canvas getThumbnailPic() {
		return thumbnailPic;
	}

	public CssColor getColor() {
		return color;
	}

	public String getToolTipText() {
		return toolTipText;
	}

	public void setToolTipText(String text) {
		toolTipText = text;
	}
	
	public void setSelected(boolean b) {
		isSelected = b;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
}
