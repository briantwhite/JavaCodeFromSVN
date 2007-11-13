package foldingServer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class FoldingServer {

	public static final Color SS_BONDS_OFF_BACKGROUND = new Color((float) 0.7,
			(float) 0.7, (float) 1.0);

	public static final Color SS_BONDS_ON_BACKGROUND = new Color((float) 0.7,
			(float) 1.0, (float) 1.0);

	// 20 is standard size
	public static int aaRadius;

	public static ColorCoder colorCoder = 
		new ShadingColorCoder(new StandardTable().getContrastScaler());
	
	public static ColorModel colorModel = new RYBColorModel();

	public FoldingServer() {
	}

	public static void main(String[] args) {
		FoldingServer foldingServer = new FoldingServer();
		Date start = new Date();

		//palette mode
		// output a .png of the amino acid palette
		if (args[0].equals("-a")) {
			aaRadius = Integer.parseInt(args[1]);
			if (fileNameIsOK(args[2])) {
				foldingServer.makeAAPImage(args[2]);
			} else {
				System.err.println("ERROR: Bad filename: " + args[2]);
			}
		}

		//target mode
		// given a target shape string, output a .png of the target
		if (args[0].equals("-t")) {
			aaRadius = Integer.parseInt(args[1]);
			if (fileNameIsOK(args[3])) {
				foldingServer.makeTargetShapeImage(args[2], args[3]);
			} else {
				System.err.println("ERROR: Bad filename: " + args[3]);
			}
		}
		
		//picture mode
		//  given an aa seq, give the 2-d pic
		if (args[0].equalsIgnoreCase("-p")) {
			int mode = GridCanvas.MODE_SS_BONDS_ON;
			if (args[0].equals("-p")) {
				mode = GridCanvas.MODE_SS_BONDS_OFF;
			}
			aaRadius = Integer.parseInt(args[1]);
			if (fileNameIsOK(args[3])) {
				foldingServer.makeFoldedProteinImage(args[2], mode, args[3]);
			} else {
				System.err.println("ERROR: Bad filename: " + args[3]);
			}			
		}
		
		//color mode
		// given an aa seq, determine the color
		if (args[0].equalsIgnoreCase("-c")) {
			int mode = GridCanvas.MODE_SS_BONDS_ON;
			if (args[0].equals("-c")) {
				mode = GridCanvas.MODE_SS_BONDS_OFF;
			}
			foldingServer.foldProteinAndReportColor(args[1], mode);
		}
		
		//match mode
		// given an aa seq, a target shape string, and mode
		// give the 2-d pic and say if shapes match
		if (args[0].equalsIgnoreCase("-m")) {
			int mode = GridCanvas.MODE_SS_BONDS_ON;
			if (args[0].equals("-m")) {
				mode = GridCanvas.MODE_SS_BONDS_OFF;
			}
			
		}
		

		Date end = new Date();
		System.out.println("time=" + (end.getTime() - start.getTime()) + "ms");
	}

	private static boolean fileNameIsOK(String fileName) {
		if (fileName.contains("/") ||
				fileName.contains("\\") ||
				fileName.equals("")) {
			return false;
		} else {
			return true;
		}
	}

	private void makeAAPImage(String fileName) {
		AminoAcidPalette aap = new AminoAcidPalette(4,5);
		BufferedImage aapBI = new BufferedImage(
				aap.getSize().width, 
				aap.getSize().height, 
				BufferedImage.TYPE_INT_RGB);
		Graphics2D aapBIg2D = aapBI.createGraphics();
		aap.paint(aapBIg2D);
		File paletteFile = new File (fileName);
		try {
			ImageIO.write(aapBI, "png", paletteFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void makeTargetShapeImage(String targetShapeString, String fileName) {
		BufferedImage img = 
			ProteinImageGenerator.buildTargetProteinFromFoldingString(targetShapeString);
		File targetShapeFile = new File(fileName);
		try {
			ImageIO.write(img, "png", targetShapeFile);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private void makeFoldedProteinImage(String aaSeq, int mode, String fileName) {
		HexGrid grid = ProteinImageGenerator.foldOntoHexGrid(aaSeq, mode);
		HexCanvas canvas = new HexCanvas();
		canvas.setGrid(grid);
		BufferedImage img = ProteinImageGenerator.generateImage(canvas, mode);
		File proteinShapeFile = new File(fileName);
		try {
			ImageIO.write(img, "png", proteinShapeFile);
		} catch (IOException e) {
			e.printStackTrace();
		}				
	}
	
	private void foldProteinAndReportColor(String aaSeq, int mode) {
		HexGrid grid = ProteinImageGenerator.foldOntoHexGrid(aaSeq, mode);
		HexCanvas canvas = new HexCanvas();
		canvas.setGrid(grid);
		grid.categorizeAcids();
		System.out.println(grid.getProteinColor().toString());
	}

}
