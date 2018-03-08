package PathwayPanel;

import java.awt.Color;

import YeastVGL.YeastVGL;

public class UneditableTile extends DrawingPanelTile {
	
	public UneditableTile(YeastVGL yeastVGL) {
		super(yeastVGL);
		BACKGROUND_COLOR = Color.WHITE;
		setBackground(BACKGROUND_COLOR);
		setOpaque(true);
	}

}
