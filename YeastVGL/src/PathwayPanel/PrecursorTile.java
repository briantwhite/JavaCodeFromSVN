package PathwayPanel;

import java.awt.Color;

import javax.swing.JLabel;

import YeastVGL.YeastVGL;

public class PrecursorTile extends DrawingPanelTile {
	
	public PrecursorTile(YeastVGL yeastVGL) {
		super(yeastVGL);
		BACKGROUND_COLOR = new Color(255, 255, 240);
		setBackground(BACKGROUND_COLOR);
		setOpaque(true);
		add(new JLabel("P"));
	}


}
