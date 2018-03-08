package PathwayPanel;

import java.awt.Color;

import javax.swing.JLabel;

import YeastVGL.YeastVGL;

public class PrecursorTile extends DrawingPanelTile {
	
	public PrecursorTile(YeastVGL yeastVGL, int row, int col) {
		super(yeastVGL, row, col);
		BLANK_BACKGROUND_COLOR = new Color(150, 150, 255);
		setBackground(BLANK_BACKGROUND_COLOR);
		setOpaque(true);
		add(new JLabel("P"));
	}
}
