package PathwayPanel;

import java.awt.Color;

import YeastVGL.YeastVGL;

public class ArrowTile extends DrawingPanelTile {

	public ArrowTile(YeastVGL yeastVGL) {
		super(yeastVGL);
		BACKGROUND_COLOR = new Color(255,255,240);
		setBackground(BACKGROUND_COLOR);
		setOpaque(true);
	}

}
