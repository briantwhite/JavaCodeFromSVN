package biochem;

import java.awt.Color;
import java.io.Serializable;

import javax.swing.ImageIcon;

import molGenExp.HistListItem;

public class FoldedPolypeptide extends HistListItem {
	private String aaSeq;
	private Grid fullSizeGrid;
	private ImageIcon thumbnailPic;
	private Color color;
	
	public FoldedPolypeptide(String seq, 	
			Grid big, 
			ImageIcon thumb, 
			Color color) {
		aaSeq = seq;
		fullSizeGrid = big;
		thumbnailPic = thumb;
		this.color = color;
		toolTipText = seq;
	}

	public String getAaSeq() {
		return aaSeq;
	}

	public Grid getFullSizeGrid() {
		return fullSizeGrid;
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
