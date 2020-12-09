package edu.umb.jsAipotu.client.biochem;

import com.google.gwt.canvas.client.Canvas;

public class ProteinImageSet {
	Canvas fullScaleImage;
	Canvas thumbnailImage;
	
	public ProteinImageSet(Canvas fsi, Canvas ti) {
		fullScaleImage = fsi;
		thumbnailImage = ti;
	}

	public Canvas getFullScaleImage() {
		return fullScaleImage;
	}

	public Canvas getThumbnailImage() {
		return thumbnailImage;
	}

}
