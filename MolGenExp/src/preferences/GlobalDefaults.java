package preferences;

import java.awt.Color;

import molBiol.MolBiolParams;
import molGenExp.RYBColorModel;
import biochem.BiochemAttributes;
import biochem.ColorModel;
import biochem.StandardTable;

public class GlobalDefaults {
	
	//radius of aas as drawn in big images
	public final static int aaRadius = 20;
	
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
	public static final Color DEAD_COLOR = Color.GRAY;
	public static final String DEAD_COLOR_NAME = "Gray";

	public static final String paintedInACornerNotice = "<html>"
		+ "I'm sorry, I cannot fold that protein.<br>"
		+ "The folding algorithm gets stuck because "
		+ "it only works in two dimensions.<br>"
		+ "Please try another sequence.</html>";
	
	public GlobalDefaults() {
		
	}

}
