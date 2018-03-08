package PathwayPanel;

import java.awt.Color;

import javax.swing.JLabel;

import YeastVGL.YeastVGL;

public class PrecursorTile extends DrawingPanelTile {
	
	public PrecursorTile(YeastVGL yeastVGL, int row, int col) {
		super(yeastVGL, row, col);
		BLANK_BACKGROUND_COLOR = new Color(255, 240, 240);
		BLANK_BACKGROUND_COLOR = new Color(255, 220, 220);
		setBackground(BLANK_BACKGROUND_COLOR);
		setOpaque(true);
		add(new JLabel("P:r" + row + " c" + col));
	}


}
