package utilities;

import molGenExp.RYBColorModel;
import biochem.BiochemAttributes;
import biochem.StandardTable;

public class GlobalDefaults {
	
	//radius of aas as drawn in big images
	public final static int aaRadius = 20;

	//world is a worldSize x worldSize array of orgs
	// needs to divide 500 evenly
	public final static int worldSize = 20;

	public final static String version = "1.3.4";

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

	
	public GlobalDefaults() {
		
	}

}
