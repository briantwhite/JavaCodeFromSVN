package edu.umb.jsAipotu.client.biochem;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.user.client.Window;

import edu.umb.jsAipotu.client.preferences.GlobalDefaults;


public class ProteinImageFactory {
	// fix thumbnail size from WIDTH and HEIGHT of full-size image;
	//	these values (in pixels) are for scaling purposes

	public static ProteinImageSet generateImages(HexCanvas hexCanvas) {
		
		Canvas fullSizeCanvas = Canvas.createIfSupported();
		if (fullSizeCanvas == null) {
			Window.alert("Sorry, your browser doesn't support the HTML5 Canvas element that is needed for Aipotu; please try another");
			return null;
		}
		fullSizeCanvas.setWidth(hexCanvas.getRequiredCanvasSize().width + "px");
		fullSizeCanvas.setCoordinateSpaceWidth(hexCanvas.getRequiredCanvasSize().width);
		fullSizeCanvas.setHeight(hexCanvas.getRequiredCanvasSize().height + "px");
		fullSizeCanvas.setCoordinateSpaceHeight(hexCanvas.getRequiredCanvasSize().height);

		Context2d g = fullSizeCanvas.getContext2d();
		hexCanvas.paintProtein(g);
		
		int imageWidth = hexCanvas.getRequiredCanvasSize().width;
		int imageHeight = hexCanvas.getRequiredCanvasSize().height;
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
		Canvas thumbCanvas = Canvas.createIfSupported();
		Context2d tg = thumbCanvas.getContext2d();
		tg.drawImage(fullSizeCanvas.getCanvasElement(), 0, 0, (double) actualThumbWidth, (double) actualThumbHeight);
		
		return new ProteinImageSet(fullSizeCanvas, thumbCanvas);
	}

}
