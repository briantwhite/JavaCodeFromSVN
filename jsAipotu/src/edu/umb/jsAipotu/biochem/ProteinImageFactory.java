package edu.umb.jsAipotu.biochem;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import preferences.GlobalDefaults;


public class ProteinImageFactory {
	// fix thumbnail size from WIDTH and HEIGHT of full-size image;
	//	these values (in pixels) are for scaling purposes

	public static ProteinImageSet generateImages(HexCanvas hexCanvas) {
		
		BufferedImage fullSizePic = 
			new BufferedImage(hexCanvas.getRequiredCanvasSize().width, 
					hexCanvas.getRequiredCanvasSize().height, 
					BufferedImage.TYPE_INT_RGB);
		Graphics2D g = fullSizePic.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		hexCanvas.paint(g);
		g.dispose();
		
		int imageWidth = fullSizePic.getWidth(null);
		int imageHeight = fullSizePic.getHeight(null);
		double imageRatio = (double) imageWidth / (double) imageHeight;
		double thumbRatio = 
			(double) GlobalDefaults.thumbWidth / (double) GlobalDefaults.thumbHeight;

		int actualThumbHeight = GlobalDefaults.thumbHeight;
		int actualThumbWidth = GlobalDefaults.thumbWidth;
		if (thumbRatio < imageRatio) {
			actualThumbHeight = (int) (GlobalDefaults.thumbWidth / imageRatio);
		}
		else {
			actualThumbWidth = (int) (GlobalDefaults.thumbHeight * imageRatio);
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
