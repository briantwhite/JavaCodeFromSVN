package PathwayPanel;

import java.awt.Color;
import java.awt.Graphics;

import YeastVGL.YeastVGL;

public class MoleculeTile extends DrawingPanelTile {
	
	
	public MoleculeTile(YeastVGL yeastVGL) {
		super(yeastVGL);
		BACKGROUND_COLOR = new Color(240, 240, 255);
		setBackground(BACKGROUND_COLOR);
		setOpaque(true);
	}
	

}
