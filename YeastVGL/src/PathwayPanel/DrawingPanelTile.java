package PathwayPanel;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import YeastVGL.YeastVGL;

public abstract class DrawingPanelTile extends JPanel {
	
	public final static int TILE_WIDTH = 50;
	public final static int TILE_HEIGHT = 50;
	
	public static Color BACKGROUND_COLOR = null;
	
	private YeastVGL yeastVGL;
	
	public DrawingPanelTile(YeastVGL yeastVGL) {
		this.yeastVGL = yeastVGL;
		this.setPreferredSize(new Dimension(TILE_WIDTH, TILE_HEIGHT));
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
}
