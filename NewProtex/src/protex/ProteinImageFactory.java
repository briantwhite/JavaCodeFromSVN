package protex;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ProteinImageFactory {
	
	// fix thumbnail size from WIDTH and HEIGHT of full-size image;
	//	these values (in pixels) are for scaling purposes
	private static final int thumbWidth = 130;
	private static final int thumbHeight = 70;
	
	public static ProteinImageSet generateImages(
			OutputPalette op) {
		return generateImages(op.getDrawingPane());
	}
	
	public static ProteinImageSet buildProtein(String foldingString, boolean strictMatchDisplayMode) {
		
		Direction[] directionArray = directionStringToDirectionArray(foldingString);
		
		HexCanvas hexCanvas = new HexCanvas();
		
		hexCanvas.setGrid(layoutOntoHexGrid(directionArray));
		
		hexCanvas.setStrictMatchDisplayMode(strictMatchDisplayMode);
		
		return generateImages(hexCanvas);
		
	}
	
	private static Direction[] directionStringToDirectionArray(String directionString) {
		//if there are colons, its aa:direction;aa:direction; etc
		// if not, it's just directions
		ArrayList directionList = new ArrayList();
		String[] parts = directionString.split(";");
		if (directionString.indexOf(":") != -1) {
			for (int i = 0; i < parts.length; i++) {
				String[] pieces = parts[i].split(":");
				directionList.add(Direction.getDirection(pieces[1]));
			}
		} else {
			for (int i = 0; i < parts.length; i++) {
				directionList.add(Direction.getDirection(parts[i]));
			}
		}
		Direction[] directionArray = new Direction[directionList.size()];
		for (int i = 0; i < directionList.size(); i++) {
			directionArray[i] = (Direction)directionList.get(i);
		}
		
		return directionArray;
	}
	
	public static HexGrid layoutOntoHexGrid(Direction[] directionArray) {
		int numAcids = directionArray.length;
		
		StandardTable aaTable = StandardTable.getInstance();
		AminoAcid acidX = aaTable.getFromAbName("X");
		
		//make protein of only X's (we want shape without sequence)
		AminoAcid[] aminoAcids = new AminoAcid[numAcids];
		for (int i = 0; i < numAcids; i++) {
			aminoAcids[i] = acidX;
		}
		
		Polypeptide polypeptide = new Polypeptide(aaTable, aminoAcids, "");
		polypeptide.setDirections(directionArray);
		polypeptide.setFolded();
		
		return new HexGrid(polypeptide);

	}

	public static ProteinImageSet generateImages(
			GridCanvas gridCanvas) {
		
		gridCanvas.calculateRequiredCanvasSize();
		
		BufferedImage fullSizePic = 
			new BufferedImage(gridCanvas.getRequiredCanvasSize().width, 
					gridCanvas.getRequiredCanvasSize().height,
					BufferedImage.TYPE_INT_RGB);
		Graphics2D g = fullSizePic.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		gridCanvas.paint(g);
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
