package utilities;

import molGenExp.RYBColorModel;
import biochem.BiochemAttributes;
import biochem.StandardTable;

public class GlobalDefaults {
	
	//radius of aas as drawn in big images
	public final static int aaRadius = 20;

	public final static String version = "1.2.3";

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

	public static final String paintedInACornerNotice = "<html>"
		+ "I'm sorry, I cannot fold that protein.<br>"
		+ "The folding algorithm gets stuck because "
		+ "it only works in two dimensions.<br>"
		+ "Please try another sequence.</html>";
	
	public GlobalDefaults() {
		
	}

}
