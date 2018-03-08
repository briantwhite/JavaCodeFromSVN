package PathwayPanel;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import YeastVGL.YeastVGL;

public abstract class DrawingPanelTile extends JPanel {
	
	public final static int TILE_WIDTH = 50;
	public final static int TILE_HEIGHT = 50;
	
	public Color BLANK_BACKGROUND_COLOR;
	public Color ACTIVE_BACKGROUND_COLOR;
	
	public final int row, col;
	
	private YeastVGL yeastVGL;
	
	public JPopupMenu popupMenu;
	
	public DrawingPanelTile(YeastVGL yeastVGL, int row, int col) {
		this.row = row;
		this.col = col;
		this.yeastVGL = yeastVGL;
		this.setPreferredSize(new Dimension(TILE_WIDTH, TILE_HEIGHT));
//		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		popupMenu = new JPopupMenu();
	}
}
