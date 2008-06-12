package VGL;

public class Preferences {
	
	private static Preferences instance;
	
	private int maxOffspring;
	private int minOffspring;
	
	private Preferences() {
		
	}
	
	public static Preferences getInstance() {
		if (instance == null) {
			instance = new Preferences();
		}
		return instance;
	}

	public int getMaxOffspring() {
		return maxOffspring;
	}

	public void setMaxOffspring(int maxOffspring) {
		this.maxOffspring = maxOffspring;
	}

	public int getMinOffspring() {
		return minOffspring;
	}

	public void setMinOffspring(int minOffspring) {
		this.minOffspring = minOffspring;
	}

}
