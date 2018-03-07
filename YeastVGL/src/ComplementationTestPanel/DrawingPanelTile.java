package ComplementationTestPanel;

import javax.swing.JPanel;

import YeastVGL.YeastVGL;

public abstract class DrawingPanelTile extends JPanel {
	
	// types of panels
	public static final int MOLECULE_TILE = 0;
	public static final int ARROW_TILE = 1;
	public static final int ENZYME_TILE = 2;
	
	public int type;
	
	private YeastVGL yeastVGL;
	
	public DrawingPanelTile(YeastVGL yeastVGL) {
		this.yeastVGL = yeastVGL;
	}
}
