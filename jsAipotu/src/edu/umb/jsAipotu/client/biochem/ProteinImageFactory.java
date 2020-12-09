package edu.umb.jsAipotu.client.biochem;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.user.client.Window;

import edu.umb.jsAipotu.client.preferences.GlobalDefaults;


public class ProteinImageFactory {
	// fix thumbnail size from WIDTH and HEIGHT of full-size image;
	//	these values (in pixels) are for scaling purposes

	public static ProteinImageSet generateImages(HexCanvas hexCanvas) {
		
		Canvas fullSizePic = Canvas.createIfSupported();
		if (fullSizePic == null) {
			Window.alert("Sorry, your browser doesn't support the HTML5 Canvas element that is needed for Aipotu; please try another");
			return null;
		}
		fullSizePic.setWidth(hexCanvas.getRequiredCanvasSize().width + "px");
		fullSizePic.setCoordinateSpaceWidth(hexCanvas.getRequiredCanvasSize().width);
		fullSizePic.setHeight(hexCanvas.getRequiredCanvasSize().height + "px");
		fullSizePic.setCoordinateSpaceHeight(hexCanvas.getRequiredCanvasSize().height);

		Context2d g = fullSizePic.getContext2d();
		hexCanvas.paint(g);
		
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
