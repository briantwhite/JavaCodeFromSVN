package utilities;

import java.awt.Dimension;

import javax.swing.ImageIcon;

import molGenExp.ProteinImageFactory;
import molGenExp.ProteinImageSet;
import biochem.FoldedPolypeptide;
import biochem.FoldingException;
import biochem.FoldingManager;
import biochem.OutputPalette;

public class ProteinFolder {

	public static FoldedPolypeptide foldProtein(String aaSeq) throws FoldingException {
		FoldingManager manager = new FoldingManager();
		OutputPalette op = new OutputPalette();
		ProteinImageSet images = null;
		FoldedPolypeptide fp = null;

		//fold it
		manager.fold(aaSeq);

		//make an icon for display in a dialog
		// and get the color
		manager.createCanvas(op);
		Dimension requiredCanvasSize = 
			op.getDrawingPane().getRequiredCanvasSize();

		images = 
			ProteinImageFactory.generateImages(op, requiredCanvasSize);

		fp = new FoldedPolypeptide(
				aaSeq,
				op.getDrawingPane().getGrid(), 
				new ImageIcon(images.getThumbnailImage()), 
				op.getProteinColor());

		return fp;
	}

}
