package PathwayPanel;

import java.awt.Color;

import javax.swing.JLabel;

import YeastVGL.YeastVGL;

public class ArrowTile extends DrawingPanelTile {

	public ArrowTile(YeastVGL yeastVGL, int row, int col) {
		super(yeastVGL, row, col);
		BLANK_BACKGROUND_COLOR = new Color(255,255,240);
		ACTIVE_BACKGROUND_COLOR = new Color(255,255,220);
		setBackground(BLANK_BACKGROUND_COLOR);
		setOpaque(true);
		add(new JLabel("A:r" + row + " c" + col));
	}

}
