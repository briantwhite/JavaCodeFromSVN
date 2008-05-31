package utilities;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import molGenExp.ProteinImageFactory;
import molGenExp.ProteinImageSet;
import biochem.FoldedPolypeptide;
import biochem.FoldingException;
import biochem.FoldingManager;
import biochem.OutputPalette;
import biochem.PolypeptideFactory;

public class ProteinUtilities {
	
	public static FoldedPolypeptide foldProtein(String aaSeq) {
		//fold it
		FoldingManager manager = FoldingManager.getInstance();
		try {
			manager.fold(aaSeq);
		} catch (FoldingException e) {
			e.printStackTrace();
		}
		
		//make an icon and display it in a dialog
		OutputPalette op = new OutputPalette();
		manager.createCanvas(op);
		Dimension requiredCanvasSize = 
			op.getDrawingPane().getRequiredCanvasSize();
		
		ProteinImageSet images = 
			ProteinImageFactory.generateImages(op, requiredCanvasSize);
		
		return new FoldedPolypeptide(
				aaSeq,
				op.getDrawingPane().getGrid(), 
				new ImageIcon(images.getThumbnailImage()), 
				op.getProteinColor());
		
	}
	
	public static String convertThreeLetterProteinStringToOneLetterProteinString(
			String threeLetter) {
		//insert spaces between amino acid codes
		StringBuffer psBuffer = new StringBuffer(threeLetter);
		for (int i = 3; i < psBuffer.length(); i = i + 4) {
			psBuffer = psBuffer.insert(i, " ");
		}
		threeLetter = psBuffer.toString();

		// parse input into strings, each representing an acid
		ArrayList acidStrings = 
			PolypeptideFactory.getInstance().getTokens(threeLetter);

		// parsing each acid string into AminoAcids using the AminoAcidTable.
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < acidStrings.size(); i++) {
			b.append((
					GlobalDefaults.aaTable.get(
							(String)acidStrings.get(i))).getAbName());
		}
		return b.toString();
	}


}
