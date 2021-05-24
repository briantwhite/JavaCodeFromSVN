package edu.umb.jsAipotu.client.preferences;

public class MGEPreferences {

	private static MGEPreferences instance;

	//world info
	private int worldSize;
	protected static int DEFAULT_WORLD_SIZE = 10;
	private boolean showBothAllelesInWorld;
	protected static boolean DEFAULT_SHOW_BOTH_ALLELES = false;

	//mutation rates
	private float pointMutationRate;
	protected static float DEFAULT_POINT_MUTATION_RATE = 0.001f;
	private float deletionMutationRate;
	protected static float DEFAULT_DELETION_MUTATION_RATE = 0.001f;
	private float insertionMutationRate;
	protected static float DEFAULT_INSERTION_MUTATION_RATE = 0.001f;

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
		pointMutationRate = DEFAULT_POINT_MUTATION_RATE;
		deletionMutationRate = DEFAULT_DELETION_MUTATION_RATE;
		insertionMutationRate = DEFAULT_INSERTION_MUTATION_RATE;
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

}
