package preferences;

import java.io.File;

public class MGEPreferences {
	
	private static MGEPreferences instance;
	
	private File greenhouseDirectory = new File(GlobalDefaults.greenhouseDirName);
	
	// to deal with Mac OSX security
	//  this is null on a PC but has a value on a Mac
	//  see MGE's main() method and 5/2017 notes
	private String osXappRootDir = null;
	
	//world info
	private int worldSize;
	protected static int DEFAULT_WORLD_SIZE = 10;
	private boolean showBothAllelesInWorld;
	protected static boolean DEFAULT_SHOW_BOTH_ALLELES = false;
	
	//mutation rates
	private boolean mutationsEnabled;
	protected static boolean DEFAULT_MUTATIONS_ENABLED = true;
	private float pointMutationRate;
	protected static float DEFAULT_POINT_MUTATION_RATE = 0.001f;
	private float deletionMutationRate;
	protected static float DEFAULT_DELETION_MUTATION_RATE = 0.001f;
	private float insertionMutationRate;
	protected static float DEFAULT_INSERTION_MUTATION_RATE = 0.001f;
	
	//info for saving pix of each generation in evolve
	private boolean generationPixOn;
	protected static boolean DEFAULT_GENERATION_PIX_ON = false;
	private String savePixToPath;
	protected static String DEFAULT_SAVE_PIX_TO_PATH = "";
	
	//display options
	private boolean showColorNameText;
	protected static boolean DEFAULT_SHOW_COLOR_NAME_TEXT = true;

	
	public static MGEPreferences getInstance() {
		if (instance == null) {
			instance = new MGEPreferences();
		}
		return instance;
	}
	
	private MGEPreferences() {
		// set default values
		worldSize = DEFAULT_WORLD_SIZE;
		showBothAllelesInWorld = DEFAULT_SHOW_BOTH_ALLELES;
		
		mutationsEnabled = DEFAULT_MUTATIONS_ENABLED;
		pointMutationRate = DEFAULT_POINT_MUTATION_RATE;
		deletionMutationRate = DEFAULT_DELETION_MUTATION_RATE;
		insertionMutationRate = DEFAULT_INSERTION_MUTATION_RATE;
		
		generationPixOn = DEFAULT_GENERATION_PIX_ON;
		
		File desktopFile = new File(System.getProperty(
				"user.home") +  "/Desktop");
		if (desktopFile.canWrite()) {
			savePixToPath = desktopFile.getAbsolutePath();
		} 
				
		showColorNameText = DEFAULT_SHOW_COLOR_NAME_TEXT;
		
		/**
		 * Need to see if we're on a Mac (which has new security stuff as of 4/13/17)
		 *  if we are, we passed in a java command line param -D$APP_ROOT
		 *  if it's here, we need to use it to find the Greenouse/ 
		 *   which will be in the .app itself - not in a separate folder
		 *   $APP_ROOT is where the .jar is - it's in the Aipotu.app/Contents/Java/ folder
		 *   that's where we put Greenhouse/ in the single-file version
		 *   BUT - you can't write to the Greenhouse in the .app
		 *	
		 *	AND - what happens next depends on whether they've saved a greenhouse location in MacOS's preferences
		 *		if no - this is a first run and you must:
		 *			- gray out "Save greenhouse"
		 *			- use the greenhouse in the app until they "save greenhouse as"
		 *				then:
		 *					- update the greenhouseDirectory
		 *					- save the whole greenhouse there
		 *					- save the new directory to OSX's prefs
		 *		if yes - this is a re-run so you use the greenhouse dir from the OS X prefs.   
		 **/ 
//		// old code
//		if ((args.length == 1) && args[0].startsWith("-D")) {
//			// on mac
//			// see if they've saved a Greenhouse location in the OS X preferences
//			if (prefs.get(GlobalDefaults.GREENHOUSE_DIR_PREF_NAME, "").equals("")) {
//				// nothing saved, so this is a "first run"
//				String appRootDir = args[0].replace("-D", "");
//				MGEprefs.setGreenhouseDirectory(new File(appRootDir + "/Contents/Resources/" + GlobalDefaults.greenhouseDirName));
//				saveGreenhouseMenuItem.setEnabled(false);
//			} else {
//				// it was saved, so this is a re-run - use the saved directory in prefs
//				MGEprefs.setGreenhouseDirectory(new File(prefs.get(GlobalDefaults.GREENHOUSE_DIR_PREF_NAME, ".")));
//			}
//		} else {
//			// on PC - see if they've saved a different location
//			if (prefs.get(GlobalDefaults.GREENHOUSE_DIR_PREF_NAME, "").equals("")) {
//				MGEprefs.setGreenhouseDirectory(new File(GlobalDefaults.greenhouseDirName));
//			} else {
//				MGEprefs.setGreenhouseDirectory(new File(prefs.get(GlobalDefaults.GREENHOUSE_DIR_PREF_NAME, ".")));
//			}
//		}

		if (osXappRootDir == null) {
			// we're on PC
		} else {
			// we're on Mac
		}
	}
	
	public void setosXappRootDir(String x) {
		osXappRootDir = x;
	}
	
	public File getGreenhouseDirectory() {
		return greenhouseDirectory;
	}
	
	public void setGreenhouseDirectory(File f) {
		greenhouseDirectory = f;
	}
	
	public int getWorldSize() {
		return worldSize;
	}
	
	public void setWorldSize(int size) {
		worldSize = size;
	}
	
	public boolean isShowBothAllelesInWorld() {
		return showBothAllelesInWorld;
	}
	
	public void setShowBothAllelesInWorld(boolean b) {
		showBothAllelesInWorld = b;
	}
	
	public boolean isMutationsEnabled() {
		return mutationsEnabled;
	}
	
	public void setMutationsEnabled(boolean b) {
		mutationsEnabled = b;
	}

	public float getPointMutationRate() {
		return pointMutationRate;
	}

	public void setPointMutationRate(float pointMutationRate) {
		this.pointMutationRate = pointMutationRate;
	}

	public float getDeletionMutationRate() {
		return deletionMutationRate;
	}

	public void setDeletionMutationRate(float deletionMutationRate) {
		this.deletionMutationRate = deletionMutationRate;
	}

	public float getInsertionMutationRate() {
		return insertionMutationRate;
	}

	public void setInsertionMutationRate(float insertionMutationRate) {
		this.insertionMutationRate = insertionMutationRate;
	}

	public boolean isGenerationPixOn() {
		return generationPixOn;
	}

	public void setGenerationPixOn(boolean generationPixOn) {
		this.generationPixOn = generationPixOn;
	}

	public String getSavePixToPath() {
		return savePixToPath;
	}

	public void setSavePixToPath(String savePixToPath) {
		this.savePixToPath = savePixToPath;
	}

	public boolean isShowColorNameText() {
		return showColorNameText;
	}

	public void setShowColorNameText(boolean showColorNameText) {
		this.showColorNameText = showColorNameText;
	}
	

}
