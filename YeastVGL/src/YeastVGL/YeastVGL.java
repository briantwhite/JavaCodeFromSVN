package YeastVGL;

public class YeastVGL {
	
	private Pathway pathway;
	private static YeastVGL yeastVGL;
	private MutantSet mutantSet;
	private static YeastVGL_GUI gui;
	
	public static final int NUM_MUTANTS = 15;

	public static void main(String[] args) {
		yeastVGL = new YeastVGL();
		gui = new YeastVGL_GUI(yeastVGL);
		gui.setVisible(true);
	}
	
	private YeastVGL() {
		pathway = new Pathway();
		mutantSet = new MutantSet(NUM_MUTANTS, pathway.getNumberOfEnzymes());
		
	}
	
	public Pathway getPathway() {
		return pathway;
	}
	
	public MutantSet getMutantSet() {
		return mutantSet;
	}
	
	public YeastVGL_GUI getGUI() {
		return gui;
	}

}
