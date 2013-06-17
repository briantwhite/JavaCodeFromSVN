package protex.client;

import java.util.ArrayList;

public class ProteinImageFactory {
	
	/*public static ProteinImageSet generateImages(
			OutputPalette op) {
		return generateImages(op.getDrawingPane());
	}*/
	//ProteinImageSet
	public static GridCanvas buildProtein(String foldingString, boolean strictMatchDisplayMode) {
		
		Direction[] directionArray = directionStringToDirectionArray(foldingString);
		
		HexCanvas hexCanvas = new HexCanvas();
		hexCanvas.setGrid(layoutOntoHexGrid(directionArray));
		hexCanvas.setStrictMatchDisplayMode(strictMatchDisplayMode);
		hexCanvas.calculateRequiredCanvasSize();
		int width = hexCanvas.getRequiredCanvasSize().width;
		int height = hexCanvas.getRequiredCanvasSize().height;
		hexCanvas.setCanvas(width, height);
		
		return hexCanvas;
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

	/*public static Canvas generateImages(GridCanvas gridCanvas) {
		
		gridCanvas.calculateRequiredCanvasSize();
		
		Canvas canvas = Canvas.createIfSupported();
		canvas.setPixelSize(gridCanvas.getRequiredCanvasSize().width,
							gridCanvas.getRequiredCanvasSize().height);
		canvas.setCoordinateSpaceWidth(gridCanvas.getRequiredCanvasSize().width);
		canvas.setCoordinateSpaceHeight(gridCanvas.getRequiredCanvasSize().height);
		Context2d ctx = canvas.getContext2d();
		Graphics g = new Graphics(ctx);
		
		
		gridCanvas.paint(g);

		return new ProteinImageSet(fullSizePic, thumbImage);
	}*/

}
