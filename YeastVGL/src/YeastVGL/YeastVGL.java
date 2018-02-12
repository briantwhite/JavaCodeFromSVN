package YeastVGL;

public class YeastVGL {
	
	private Pathway pathway;
	private static YeastVGL yeastVGL;

	public static void main(String[] args) {
		yeastVGL = new YeastVGL();
		YeastVGL_GUI gui = new YeastVGL_GUI(yeastVGL);
		gui.setVisible(true);
	}
	
	private YeastVGL() {
		pathway = new Pathway();
		
	}
	
	public Pathway getPathway() {
		return pathway;
	}
	

}
