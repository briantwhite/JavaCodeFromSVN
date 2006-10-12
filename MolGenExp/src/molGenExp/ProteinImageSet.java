package molGenExp;

import java.awt.image.BufferedImage;

public class ProteinImageSet {
	BufferedImage fullScaleImage;
	BufferedImage thumbnailImage;
	
	public ProteinImageSet(BufferedImage fsi, BufferedImage ti) {
		fullScaleImage = fsi;
		thumbnailImage = ti;
	}

	public BufferedImage getFullScaleImage() {
		return fullScaleImage;
	}

	public BufferedImage getThumbnailImage() {
		return thumbnailImage;
	}

}
