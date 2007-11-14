package foldingServer;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ProteinImageGenerator {
		
	
	public static BufferedImage buildTargetProteinFromFoldingString(String foldingString) {
		
		Direction[] directionArray = directionStringToDirectionArray(foldingString);
		
		HexCanvas hexCanvas = new HexCanvas();
		
		hexCanvas.setGrid(layoutOntoHexGrid(directionArray));
				
		return generateImage(hexCanvas, GridCanvas.MODE_TARGET_SHAPE);
		
	}
	
	public static Direction[] directionStringToDirectionArray(String directionString) {
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
		
		StandardTable aaTable = new StandardTable();
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
	
	public static HexGrid foldOntoHexGrid(String aaSeq, int mode) {
		FoldingManager manager = FoldingManager.getInstance();
		String ssBondIndex = "0.0";
		if (mode == GridCanvas.MODE_SS_BONDS_ON) {
			ssBondIndex = "1.5";
		}
		Attributes attributes = new Attributes(aaSeq.trim(), 
				1, ssBondIndex, "straight", "test");
		try {
			manager.fold(attributes);
		} catch (FoldingException e) {
			e.printStackTrace();
		}
		return (HexGrid)manager.getGrid();
	}

	public static BufferedImage generateImage(GridCanvas gridCanvas, int mode) {
		
		gridCanvas.calculateRequiredCanvasSize();
		
		BufferedImage pic = 
			new BufferedImage(gridCanvas.getRequiredCanvasSize().width, 
					gridCanvas.getRequiredCanvasSize().height,
					BufferedImage.TYPE_INT_RGB);
		Graphics2D g = pic.createGraphics();
		FoldingServer.setAppropriateRenderingHints(g, FoldingServer.aaRadius);
		gridCanvas.paint(g, mode);
		g.dispose();
		
		return pic;
	}

}
