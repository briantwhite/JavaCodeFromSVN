package PathwayPanel;

import java.awt.Color;

import YeastVGL.YeastVGL;

public class EnzymeTile extends DrawingPanelTile {

	public EnzymeTile(YeastVGL yeastVGL) {
		super(yeastVGL);
		BACKGROUND_COLOR = new Color(240, 255, 240);
		setBackground(BACKGROUND_COLOR);
		setOpaque(true);
	}

}
