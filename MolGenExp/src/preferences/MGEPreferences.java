package preferences;

import java.io.File;

public class MGEPreferences {
	
	private static MGEPreferences instance;
	
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
