package edu.umb.jsAipotu.client.biochem;

import javax.swing.ImageIcon;

import com.google.gwt.canvas.dom.client.CssColor;

import edu.umb.jsAipotu.client.molGenExp.HistListItem;

public class FoldedProteinWithImages extends HistListItem {
	private String aaSeq;
	private ImageIcon thumbnailPic;
	private ImageIcon bigPic;
	private CssColor color;
	
	public FoldedProteinWithImages(String aaSeq, 
			ImageIcon bigPic,
			ImageIcon thumb, 
			CssColor color) {
		this.aaSeq = aaSeq;
		this.bigPic = bigPic;
		thumbnailPic = thumb;
		this.color = color;
		toolTipText = aaSeq;
	}

	public String getAaSeq() {
		return aaSeq;
	}

	public ImageIcon getFullSizePic() {
		return bigPic;
	}
	
	public ImageIcon getThumbnailPic() {
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
}
