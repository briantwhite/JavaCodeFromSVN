package molGenExp;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import biochem.OutputPalette;

public class ProteinImageFactory {
	// fix thumbnail size from WIDTH and HEIGHT of full-size image;
	//	these values (in pixels) are for scaling purposes
	private static final int thumbWidth = 130;
	private static final int thumbHeight = 70;

	public static ProteinImageSet generateImages(
			OutputPalette op,
			Dimension requiredFullSizeCanvasSize) {
		
		BufferedImage fullSizePic = 
			new BufferedImage(requiredFullSizeCanvasSize.width, 
					requiredFullSizeCanvasSize.height, 
					BufferedImage.TYPE_INT_RGB);
		Graphics2D g = fullSizePic.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		op.getDrawingPane().paint(g);
		g.dispose();
		
		int imageWidth = fullSizePic.getWidth(null);
		int imageHeight = fullSizePic.getHeight(null);
		double imageRatio = (double) imageWidth / (double) imageHeight;
		double thumbRatio = (double) thumbWidth / (double) thumbHeight;

		int actualThumbHeight = thumbHeight;
		int actualThumbWidth = thumbWidth;
		if (thumbRatio < imageRatio) {
			actualThumbHeight = (int) (thumbWidth / imageRatio);
		}
		else {
			actualThumbWidth = (int) (thumbHeight * imageRatio);
		}
		// draw original image to thumbnail image object;
		// 	scale it to the new size on-the-fly

		BufferedImage thumbImage =
		new BufferedImage(
		actualThumbWidth,
		actualThumbHeight,
		BufferedImage.TYPE_INT_RGB);
		Graphics2D smallG = thumbImage.createGraphics();
		smallG.setRenderingHint(
		RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		smallG.drawImage(fullSizePic, 0, 0, actualThumbWidth, actualThumbHeight, null);
		smallG.dispose();

		return new ProteinImageSet(fullSizePic, thumbImage);
	}

}
