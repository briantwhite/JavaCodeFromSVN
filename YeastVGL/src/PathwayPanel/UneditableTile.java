package PathwayPanel;

import java.awt.Color;

import javax.swing.JLabel;

import YeastVGL.YeastVGL;

public class UneditableTile extends DrawingPanelTile {
	
	public UneditableTile(YeastVGL yeastVGL, int row, int col) {
		super(yeastVGL, row, col);
		BLANK_BACKGROUND_COLOR = Color.WHITE;
		ACTIVE_BACKGROUND_COLOR = Color.WHITE;
		setBackground(BLANK_BACKGROUND_COLOR);
		setOpaque(true);
	}

}
