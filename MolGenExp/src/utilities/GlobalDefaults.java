package utilities;

import molGenExp.RYBColorModel;
import biochem.BiochemAttributes;
import biochem.StandardTable;

public class GlobalDefaults {
	
	public static final boolean foldingServerAllowed = false;
	
	//radius of aas as drawn in big images
	public final static int aaRadius = 20;

	public final static String version = "1.0";

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

	
	public GlobalDefaults() {
		
	}

}
