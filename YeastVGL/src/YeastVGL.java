import java.util.ArrayList;

public class YeastVGL {
	
	private static Pathway pathway;

	public static void main(String[] args) {
		pathway = new Pathway();
		YeastVGL_GUI gui = new YeastVGL_GUI(pathway);
		gui.setVisible(true);
	}

}
