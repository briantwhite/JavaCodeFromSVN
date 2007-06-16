package biochem;

import java.awt.Color;
import java.io.Serializable;

import javax.swing.ImageIcon;

import molGenExp.HistListItem;

public class FoldedPolypeptide extends HistListItem implements Serializable {
	private String aaSeq;
	private Grid fullSizeGrid;
	private ImageIcon thumbnailPic;
	private ImageIcon fullSizePic;
	private Color color;
	
	public FoldedPolypeptide(String seq, 	Grid big, 
			ImageIcon bigPic,
			ImageIcon thumb, Color color) {
		aaSeq = seq;
		fullSizeGrid = big;
		fullSizePic = bigPic;
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

	public ImageIcon getFullSizePic() {
		return fullSizePic;
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
