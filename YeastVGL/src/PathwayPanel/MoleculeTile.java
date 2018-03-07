package PathwayPanel;

import ComplementationTestPanel.DrawingPanelTile;
import YeastVGL.YeastVGL;

public class MoleculeTile extends DrawingPanelTile {
	
	public MoleculeTile(YeastVGL yeastVGL) {
		super(yeastVGL);
		type = DrawingPanelTile.MOLECULE_TILE;
	}

}
