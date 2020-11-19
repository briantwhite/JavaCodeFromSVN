package edu.umb.jsAipotu.biochem;

import java.awt.Color;
import java.io.Serializable;

import javax.swing.ImageIcon;

import molGenExp.HistListItem;

public class FoldedProteinWithImages extends HistListItem {
	private String aaSeq;
	private ImageIcon thumbnailPic;
	private ImageIcon bigPic;
	private Color color;
	
	public FoldedProteinWithImages(String aaSeq, 
			ImageIcon bigPic,
			ImageIcon thumb, 
			Color color) {
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

	public Color getColor() {
		return color;
	}

	public String getToolTipText() {
		return toolTipText;
	}

	public void setToolTipText(String text) {
		toolTipText = text;
	}
}
