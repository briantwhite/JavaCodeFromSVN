package edu.umb.jsAipotu.client.preferences;

import com.google.gwt.canvas.dom.client.CssColor;

import edu.umb.jsAipotu.client.biochem.BiochemAttributes;
import edu.umb.jsAipotu.client.biochem.ColorModel;
import edu.umb.jsAipotu.client.biochem.StandardTable;
import edu.umb.jsAipotu.client.molBiol.MolBiolParams;
import edu.umb.jsAipotu.client.molGenExp.RYBColorModel;

public class GlobalDefaults {
		
	//radius of aas as drawn in big images
	public final static int aaRadius = 20;
	
	// background color for protein images
	public static final CssColor PROTEIN_BACKGROUND_COLOR = CssColor.make(178, 178, 255); 
	
	// sizes for images
	public final static int thumbWidth = 130;
	public final static int thumbHeight = 70;

	public final static String version = "1.3.3";

	public final static String sampleDNA = 
		new String("CAGCTATAACCGAGATTGATGTCTAG"
				+ "TGCGATAAGCCCCAAAGATCGGCACATTTTGTGCGCTATA"
				+ "CAAAGGTTAGTGGTCTGTCGGCAGTAGTAGGGGGCGT");

	public final static String sampleProtein =
		new String("MSNRHILLVVCRQ");

	public static final String greenhouseDirName = "Greenhouse";

	public static final ColorModel colorModel = new RYBColorModel();

	public static final StandardTable aaTable = new StandardTable();

	public static final MolBiolParams molBiolParams = new MolBiolParams();
	
	public static final BiochemAttributes biochemAttributes = 
		new BiochemAttributes();

	public static String[] colorList = {"White", "Blue", "Yellow", "Green",
			"Red", "Purple", "Orange", "Black"};
	
	/**
	 * 'dead color' is the color of organisms that have folded in a corner proteins
	 * they're dead and shown as gray (or whatever the dead color is)
	 * - note that the folding routing returns a color of NULL if it's folded in a corner
	 */
	public static final CssColor DEAD_COLOR = CssColor.make("gray");
	public static final String DEAD_COLOR_NAME = "Gray";

	public static final String paintedInACornerNotice = "I'm sorry, I cannot fold that protein.\n"
		+ "The folding algorithm gets stuck because "
		+ "it only works in two dimensions.\n"
		+ "Please try another sequence.";
	
	public GlobalDefaults() {
		
	}

}
