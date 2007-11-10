package protex;

import java.awt.Color;
import java.io.Serializable;

import javax.swing.ImageIcon;

public class FoldedPolypeptide implements Serializable {
	private boolean ssBondsOn;
	private String aaSeq;
	private Grid fullSizeGrid;
	private ImageIcon thumbnailPic;
	private ImageIcon fullSizePic;
	private String toolTipTextString;
	
	public FoldedPolypeptide(boolean ssBondsOn,
			String seq, 
			Grid big, 
			ImageIcon bigPic,
			ImageIcon thumb) {
		this.ssBondsOn = ssBondsOn;
		aaSeq = seq;
		fullSizeGrid = big;
		fullSizePic = bigPic;
		thumbnailPic = thumb;
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

	public ImageIcon getFullSizePic() {
		return fullSizePic;
	}
	
	public ImageIcon getThumbnailPic() {
		return thumbnailPic;
	}
	
	public String getToolTipTextString() {
		return toolTipTextString;
	}
	
	public void setToolTipTextString(String text) {
		toolTipTextString = text;
	}

}
