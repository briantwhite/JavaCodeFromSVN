package PathwayPanel;

import java.awt.Color;

import javax.swing.JLabel;

import YeastVGL.YeastVGL;

public class EnzymeTile extends DrawingPanelTile {

	public EnzymeTile(YeastVGL yeastVGL, int row, int col) {
		super(yeastVGL, row, col);
		BLANK_BACKGROUND_COLOR = new Color(240, 255, 240);
		ACTIVE_BACKGROUND_COLOR = new Color(220, 255, 220);
		setBackground(BLANK_BACKGROUND_COLOR);
		setOpaque(true);
		add(new JLabel("E:r" + row + " c" + col));
	}

}
